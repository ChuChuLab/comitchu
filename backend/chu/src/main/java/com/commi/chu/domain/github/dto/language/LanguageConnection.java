package com.commi.chu.domain.github.dto.language;

import lombok.Data;

import java.util.List;

@Data
public class LanguageConnection {

    private List<LanguageEdge> edges;
}
