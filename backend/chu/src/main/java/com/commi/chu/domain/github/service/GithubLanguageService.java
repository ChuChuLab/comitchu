package com.commi.chu.domain.github.service;

import com.commi.chu.domain.github.dto.graphql.GraphQlResponse;
import com.commi.chu.domain.github.dto.language.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GitHub GraphQL API를 통해 특정 사용자의 저장소별 언어 사용 통계를 조회하고 계산하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class GithubLanguageService {
    /**
     * 저장소별 언어 사용량을 가져오는 GraphQL 쿼리입니다.
     * <p>
     * - $login: GitHub 사용자명
     * - $after: 페이지네이션 커서(null이면 첫 페이지 조회)
     */
    private static final String LANG_QUERY = """
            query($login: String!, $after: String) {
              user(login: $login) {
                repositories(
                  first: 30,              # 한 번에 조회할 레포 수
                  after: $after,
                  isFork: false,          # 포크된 레포 제외
                  ownerAffiliations: [OWNER]  # 사용자가 소유한 레포만 조회
                ) {
                  pageInfo {
                    hasNextPage       # 추가 페이지가 있는지
                    endCursor         # 다음 페이지 요청 시 사용할 커서
                  }
                  nodes {              # 레포 목록
                    languages(
                      first: 10,        # 상위 10개 언어만 가져오기
                      orderBy: {
                        field: SIZE,    # 파일 크기 기준 정렬
                        direction: DESC # 내림차순
                      }
                    ) {
                      edges {           # 언어별 사용량 정보
                        size            # 해당 언어의 바이트 수
                        node {
                          name          # 언어 이름(ex. Java, Python)
                        }
                      }
                    }
                  }
                }
              }
            }
            """;


    private final RestClient restClient;

    /**
     * 특정 사용자의 저장소 전체에서 언어별 사용량을 집계하고,
     * 전체 대비 비율을 계산하여 DTO 목록으로 반환합니다.
     *
     * @param username GitHub 사용자 로그인 ID
     * @return 언어별 바이트 수와 비율 정보가 담긴 LanguageStatsDto 리스트(바이트 수 내림차순)
     */
    public LanguageStatListResponse fetchLanguagePercentages(String username) {
        // 언어별 누적 바이트 수를 저장하는 맵
        Map<String, Long> bytesByLang = new HashMap<>();
        String cursor = null;   // 페이지네이션 커서(null이면 첫 페이지)
        boolean hasNext = true; // 추가 페이지 조회 여부

        // 페이지가 남아있는 동안 반복 조회
        while (hasNext) {
            // GraphQL 변수 생성
            Map<String, Object> variables = new HashMap<>();
            variables.put("login", username);
            if (cursor != null) {
                // 이전 응답의 endCursor를 커서에 설정
                variables.put("after", cursor);
            }

            // 요청 본문에 쿼리와 변수 설정
            Map<String, Object> body = new HashMap<>();
            body.put("query", LANG_QUERY);
            body.put("variables", variables);

            // GraphQL API 호출 및 응답 수신
            GraphQlResponse<LanguageDataWrapper> resp = restClient.post()
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            // 응답에서 저장소 연결 정보 추출
            RepoConnection repoConn = resp.getData()
                    .getUser()
                    .getRepositories();

            // 각 저장소별 언어 사용량 집계
            for (RepoNode repo : repoConn.getNodes()) {
                for (LanguageEdge edge : repo.getLanguages().getEdges()) {
                    String lang = edge.getNode().getName();
                    long size = edge.getSize();
                    bytesByLang.merge(lang, size, Long::sum);
                }
            }

            // 페이지네이션 정보 갱신
            hasNext = repoConn.getPageInfo().isHasNextPage();
            cursor = repoConn.getPageInfo().getEndCursor();
        }

        // 전체 언어 사용량 합계 계산
        long totalBytes = bytesByLang.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        // DTO 목록으로 변환(바이트 수 내림차순 정렬)
        List<LanguageStatsDto> languageStatsList = bytesByLang.entrySet().stream()
                .map(entry -> LanguageStatsDto.of(entry.getKey(), entry.getValue(), totalBytes))
                .sorted(Comparator.comparingLong(LanguageStatsDto::getBytes).reversed())
                .toList();

        return LanguageStatListResponse.builder()
                .languageStats(languageStatsList)
                .build();
    }
}
