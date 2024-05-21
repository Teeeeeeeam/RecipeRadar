package com.team.RecipeRadar.domain.admin.api;

import com.team.RecipeRadar.domain.admin.application.AdminService;
import com.team.RecipeRadar.global.exception.ErrorResponse;
import com.team.RecipeRadar.global.payload.ControllerApiResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@OpenAPIDefinition(tags = {
        @Tag(name = "어드민 카운트 컨트롤러", description = "어드민 관련 댓글 작업"),
        @Tag(name = "일반 사용자 댓글 컨트롤러", description = "일반 사용자 관련 댓글 작업")
})
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원수 조회 API", description = "현재 가입된 회원수를 조회하는 API", tags = {"어드민 카운트 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\":\"조회 성공\", \"data\":\"10\"}"))),
    })
    @GetMapping("/members/count")
    public ResponseEntity<?> getAllMembersCount(){
        long searchAllMembers = adminService.searchAllMembers();
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"조회 성공",searchAllMembers));
    }

    @Operation(summary = "게시글 조회 API", description = "작성된 게시글의 수를 조회하는 API", tags = {"어드민 카운트 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\":\"조회 성공\", \"data\":\"10\"}"))),
    })
    @GetMapping("/posts/count")
    public ResponseEntity<?> getAllPostsCount(){
        long searchAllMembers = adminService.searchAllPosts();
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"조회 성공",searchAllMembers));
    }

    @Operation(summary = "레시피 조회 API", description = "작성된 레시피의 수를 조회하는 API", tags = {"어드민 카운트 컨트롤러"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ControllerApiResponse.class),
                            examples = @ExampleObject(value = "{\"success\": true, \"message\":\"조회 성공\", \"data\":\"10\"}"))),
    })
    @GetMapping("/recipes/count")
    public ResponseEntity<?> getAllRecipesCount(){
        long searchAllMembers = adminService.searchAllRecipes();
        return ResponseEntity.ok(new ControllerApiResponse<>(true,"조회 성공",searchAllMembers));
    }
}
