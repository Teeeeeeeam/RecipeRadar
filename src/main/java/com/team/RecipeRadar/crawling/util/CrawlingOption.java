package com.team.RecipeRadar.crawling.util;

import com.beust.jcommander.Parameter;
import lombok.Getter;

import java.util.List;

@Getter
public class CrawlingOption {

    @Parameter(names = "-recipe", description = "스크랩 레시피", variableArity = true)
    public List<String> recipe;

    @Parameter(names= "-page", description = "스크랩 페이지 수")
    public Integer page = 10;


}
