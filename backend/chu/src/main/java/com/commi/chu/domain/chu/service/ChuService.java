package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.chu.dto.BackgroundRequestDto;
import com.commi.chu.domain.chu.entity.Background;
import com.commi.chu.domain.chu.dto.ChuSkinListResponseDto;
import com.commi.chu.domain.chu.dto.MainChuResponseDto;
import com.commi.chu.domain.chu.dto.UpdateBackgroundResponseDto;
import com.commi.chu.domain.chu.dto.UpdateMainChuResponseDto;
import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.entity.ChuStatus;
import com.commi.chu.domain.chu.entity.Language;
import com.commi.chu.domain.chu.entity.UserLang;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.chu.repository.LanguageRepository;
import com.commi.chu.domain.chu.repository.UserLangRepository;
import com.commi.chu.domain.github.entity.ActivitySnapshotLog;
import com.commi.chu.domain.github.repository.LogRepository;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChuService {

    private final UserRepository userRepository;
    private final ChuRepository chuRepository;
    private final BadgeGeneratorService badgeGeneratorService;
    private final LogRepository logRepository;
    private final UserLangRepository userLangRepository;
    private final LanguageRepository languageRepository;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH = "images/chu/";

    public String generateCommitchuLevelBadge(String githubUsername) {

        User user = userRepository.findByGithubUsername(githubUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "githubUsername", githubUsername));
        Chu chu = chuRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));

        String backgroundName = chu.getBackground();
        String lang = chu.getLang();
        String status = chu.getStatus().name();

        return badgeGeneratorService.generateSvgBadge(githubUsername, backgroundName, lang, status);
    }

    /***
     * chu의 상태를 업데이트 하는 메서드
     * @param user 사용자 정보
     * @param chu 사용자의 chu 정보
     */
    @Transactional
    public void updateChuStatus(User user, Chu chu) {
        List<ActivitySnapshotLog> recentLogs = logRepository.findTop3ByUserIdOrderByActivityDateDesc(user.getId());

        int totalCommits = recentLogs.stream()
            .mapToInt(ActivitySnapshotLog::getCommitCount).sum();

        ChuStatus chuStatus;
        if(totalCommits <= 0){
            chuStatus = ChuStatus.HUNGRY;
        } else if(totalCommits < 5){
            chuStatus = ChuStatus.NORMAL;
        } else{
            chuStatus = ChuStatus.HAPPY;
        }

        chu.updateStatus(chuStatus);
    }

    /***
     * 사용쟈의 대표 chu 정보를 반환하는 메서드
     *
     * @param userId 사용자 id
     * @return 사용자 대표 chu 정보를 반환
     * @throws CustomException USER_NOT_FOUND : 해당 Id의 사용자가 없을 때
     * @throws CustomException CHU_NOT_FOUND : 사용자의 chu 정보가 없을 때
     */
    public MainChuResponseDto getMainChu(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));

        Chu mainChu = chuRepository.findByUser(user)
            .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));

        return MainChuResponseDto.of(mainChu);
    }

    /***
     * 사용자가 보유한 Chu 스킨 목록을 조회합니다.
     *
     * @param userId 요청한 사용자 Id
     * @return 모든 스킨에 대해 잠금 해제 여부와 대표 스킨 여부를 포함
     * @throws CustomException USER_NOT_FOUND : 해당 Id의 사용자가 없을 때
     * @throws CustomException CHU_NOT_FOUND : 사용자의 chu 정보가 없을 때
     */
    public List<ChuSkinListResponseDto> getUserChuSkins(Integer userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));

        Chu chu = chuRepository.findByUser(user)
            .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));

        //전체 언어 목록 조회
        List<Language> allLanguages = languageRepository.findAll();

        //사용자가 해금한 언어 목록 조회
        List<UserLang> userLangs = userLangRepository.findByUserId(userId);

        //set의 contains 연산을 쓰기 위해 집합으로 변환
        Set<Language> owned = userLangs.stream()
            .map(UserLang::getLang)
            .collect(Collectors.toSet());

        return allLanguages.stream()
            .map(lang -> {
                boolean isUnlocked = owned.contains(lang);
                boolean isMain    = isUnlocked && chu.getLang().equals(lang.getLang());
                return ChuSkinListResponseDto.of(
                    lang.getId(),
                    isMain,
                    isUnlocked);
            })
            .collect(Collectors.toList());
    }


    @Transactional
    public UpdateMainChuResponseDto updateMainChu(Integer userId, Integer langId){

        //사용자 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));


        //해당 언어가 사용자가 실제로 해금한 언어인지 확인
        UserLang userLang = userLangRepository.findByUserIdAndLangId(userId, langId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_LANG_NOT_FOUND, "langId", langId));

        //chu 조회
        Chu chu = chuRepository.findByUser(user)
            .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));


        chu.updateLang(userLang.getLang().getLang());

        log.info("[updateMainChu] {}의 대표 chu 언어가 {}로 변경 됐습니다.", user.getGithubUsername(),chu.getLang());

        return UpdateMainChuResponseDto.of("대표 언어가 변경 되었습니다.");
    }

    /***
     * 배경화면을 바꾸는 비즈니스 로직입니다.
     *
     * @param userId 요천한 사용자 Id
     * @param backgroundName 변경할 배경화면 이름
     * @return 배경화면 변경에 성공했는지 응답
     */
    @Transactional
    public UpdateBackgroundResponseDto updateBackgroundImage(Integer userId, BackgroundRequestDto backgroundRequestDto) {

        //사용자 조회
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "userId", userId));

        //chu 조회
        Chu chu = chuRepository.findByUser(user)
            .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));


        //배경화면 이름 유효성 조회
        Background background;
        try{
            background = Background.valueOf(backgroundRequestDto.getBackgroundName());
        }catch (IllegalArgumentException e){
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "backgroundName", backgroundRequestDto.getBackgroundName());
        }

        //배경화면 변경
        chu.updateBackground(background.name());

        return UpdateBackgroundResponseDto.of("배경화면이 성공적으로 변경 됐습니다.");
    }
}
