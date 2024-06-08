package com.team.RecipeRadar.domain.questions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAllResponse {

    private Boolean nextPage;
    private List<QuestionDto> questions;
}
