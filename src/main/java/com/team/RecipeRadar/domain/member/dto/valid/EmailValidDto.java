package com.team.RecipeRadar.domain.member.dto.valid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class EmailValidDto {

    @Schema(description = "이메일 검증", example = "자신이메일@naver.com")
    private String email;
}
