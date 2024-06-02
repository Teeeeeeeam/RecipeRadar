package com.team.RecipeRadar.domain.answer.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeleteAnswerDto {

    @Schema(description = "사용자 id", example = "1")
    private Long memberId;

    @Schema(description = "응답 id", example = "1")
    private Long answerId;
}
