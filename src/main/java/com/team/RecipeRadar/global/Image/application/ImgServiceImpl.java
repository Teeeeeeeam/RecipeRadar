package com.team.RecipeRadar.global.Image.application;

import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import com.team.RecipeRadar.global.Image.dao.ImgRepository;
import com.team.RecipeRadar.global.Image.domain.UploadFile;
import com.team.RecipeRadar.global.Image.utils.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ImgServiceImpl implements ImageService {

    private final ImgRepository imgRepository;
    private final FileStore fileStore;

    @Override
    public void saveRecipeImg(Recipe recipe, UploadFile uploadFile) {
        uploadFile.setRecipe(recipe);
        imgRepository.save(uploadFile);
    }


    @Override
    public Optional<byte[]> getImageByRecipeId(Long recipeId) throws IOException {
        Optional<UploadFile> byRecipeId = imgRepository.findByRecipe_Id(recipeId);
        if (byRecipeId.isPresent()){
            UploadFile uploadFile = byRecipeId.get();
            Path path = Paths.get(fileStore.getFullPath(uploadFile.getStoreFileName()));
            if (Files.exists(path)) {
                return Optional.of(Files.readAllBytes(path));
            }
        }
        return Optional.empty();
    }
}
