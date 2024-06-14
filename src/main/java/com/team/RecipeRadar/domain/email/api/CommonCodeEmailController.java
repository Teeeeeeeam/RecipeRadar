package com.team.RecipeRadar.domain.email.api;

import com.team.RecipeRadar.domain.admin.domain.BlackListRepository;
import com.team.RecipeRadar.domain.email.application.MailService;
import com.team.RecipeRadar.global.exception.ErrorResponse;
import com.team.RecipeRadar.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Tag(name = "공통 - 이메일 전송 컨트롤러",description = "이메일 인증 번호 전송 및 검증")
@RequiredArgsConstructor
public class CommonCodeEmailController {

    @Qualifier("AccountEmail")
    private final MailService mailService;
    private final BlackListRepository blackListRepository;


    @Operation(summary = "인증 번호 메일 전송",description = "공통된 인증 번호가 사용되는 이메일 전송 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"메일 전송 성공\"}")))
    })
    @PostMapping("/api/code/email-confirmation/send")
    public ResponseEntity<?> mailConfirm(@Parameter(description ="이메일") @RequestParam("email") String email){
        boolean existsByEmail = blackListRepository.existsByEmail(email);
        if(!existsByEmail) {
            mailService.sensMailMessage(email);
            return ResponseEntity.ok(new ControllerApiResponse<>(true, "메일 전송 성공"));
        }else return ResponseEntity.badRequest().body(new ErrorResponse<>(false,"사용할수 없는 이메일입니다."));
    }

    @Operation(summary = "인증코드 검증",description = "공통된 인증번호가 일치하는지를 검증하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\": \"성공\" ,\"data\" : {\"isVerifyCode\": \"true\"}}"))),
            @ApiResponse(responseCode = "400",description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"success\": false, \"message\": \"[인증번호가 일치하지 않습니다. or 숫자만 입력해주세요.]\"}")))
    })
    @PostMapping("/api/search/email-confirmation/verify")
    public ResponseEntity<?> check(@RequestParam("email")String email, @RequestParam("code")String userCode){
            Map<String, Boolean> stringBooleanMap = mailService.verifyCode(email,Integer.parseInt(userCode));
            return ResponseEntity.ok(new ControllerApiResponse<>(true,"성공",stringBooleanMap));
    }
}