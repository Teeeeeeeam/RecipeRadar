package com.team.RecipeRadar.global.Image.api;

import com.team.RecipeRadar.global.Image.application.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    /**
     * 이미지 조회하는 컨트럴로
     * @param recipeId     이미지가 조회될 recipe_id
     * @return             이미지를 바이트로 전달
     * @throws Exception
     */
    @GetMapping("/api/images/{recipe-id}")
    public ResponseEntity<byte[]> imageURL(@PathVariable("recipe-id")Long recipeId){
        try {
            Optional<byte[]> imageBytes = imageService.getImageByRecipeId(recipeId);
            if (imageBytes.isPresent()) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes.get(), httpHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
