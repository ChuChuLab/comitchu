package com.commi.chu.domain.github.dto.graphql;

import lombok.Data;

import java.util.List;

/**
 * GitHub GraphQL 응답 최상위 DTO
 *
 * @param <T> data 페이로드 타입
 */
@Data
public class GraphQlResponse<T> {
    private T data;
    private List<GraphQlError> errors;

    @Data
    public static class GraphQlError {
        private String message;
        private List<Location> locations;
        private List<Object> path;
    }

    @Data
    public static class Location {
        private int line;
        private int column;
    }
}
