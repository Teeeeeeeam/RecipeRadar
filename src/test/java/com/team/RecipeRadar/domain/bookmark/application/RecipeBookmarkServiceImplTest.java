package com.team.RecipeRadar.domain.bookmark.application;

import com.team.RecipeRadar.domain.bookmark.application.RecipeBookmarkServiceImpl;
import com.team.RecipeRadar.domain.bookmark.dto.response.UserInfoBookmarkResponse;
import com.team.RecipeRadar.domain.member.dao.MemberRepository;
import com.team.RecipeRadar.domain.member.domain.Member;
import com.team.RecipeRadar.domain.bookmark.dao.RecipeBookmarkRepository;
import com.team.RecipeRadar.domain.recipe.dao.recipe.RecipeRepository;
import com.team.RecipeRadar.domain.recipe.domain.Recipe;
import com.team.RecipeRadar.domain.recipe.dto.RecipeDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecipeBookmarkServiceImplTest {

    @Mock MemberRepository memberRepository;
    @Mock RecipeBookmarkRepository recipeBookmarkRepository;
    @Mock RecipeRepository recipeRepository;

    @InjectMocks
    RecipeBookmarkServiceImpl recipeBookmarkService;

    @Test
    @DisplayName("북마크 성공 테스트")
    void bookMark_Save_Success_Test(){
        Long recipe_id= 3l;
        Member member = Member.builder().id(1l).loginId("testId").username("testuserName").build();
        Recipe recipe = Recipe.builder().id(recipe_id).title("title").cookingLevel("1").likeCount(1).build();
        
        when(memberRepository.findById(eq(1l))).thenReturn(Optional.of(member));
        when(recipeRepository.findById(eq(3l))).thenReturn(Optional.of(recipe));
        when(recipeBookmarkRepository.existsByMember_IdAndRecipe_Id(member.getId(),recipe.getId())).thenReturn(false);

        recipeBookmarkService.saveBookmark(member.getId(),recipe.getId());

        Boolean aBoolean = recipeBookmarkService.saveBookmark(member.getId(), recipe.getId());
        assertThat(aBoolean).isFalse();

    }

    @Test
    @DisplayName("이미 북마크한 레피시가 존재할때 삭제 메서드가 실행되는지 테스트")
    void unBookMark_success_Test(){
        Long recipe_id= 3l;
        Member member = Member.builder().id(1l).loginId("testId").username("testuserName").build();
        Recipe recipe = Recipe.builder().id(recipe_id).title("title").cookingLevel("1").likeCount(1).build();

        when(memberRepository.findById(eq(1l))).thenReturn(Optional.of(member));
        when(recipeRepository.findById(eq(recipe_id))).thenReturn(Optional.of(recipe));
        when(recipeBookmarkRepository.existsByMember_IdAndRecipe_Id(member.getId(),recipe.getId())).thenReturn(true);

        Boolean aBoolean = recipeBookmarkService.saveBookmark(member.getId(), recipe.getId());

        assertThat(aBoolean).isTrue();
    }

    @Test
    @DisplayName("북마크 실패 예외 발생 테스트")
    void bookMark_Save_fail_Test(){
        Long recipe_id= 3l;
        Member member = Member.builder().id(1l).loginId("testId").username("testuserName").build();
        Recipe recipe = Recipe.builder().id(recipe_id).title("title").cookingLevel("1").likeCount(1).build();

        when(memberRepository.findById(eq(1l))).thenThrow(new NoSuchElementException("사용자및 레시피를 찾을수 없습니다."));

        assertThatThrownBy(() -> recipeBookmarkService.saveBookmark(member.getId(), recipe.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("사용자및 레시피를 찾을수 없습니다.");
    }

    @Test
    @DisplayName("북마크를 했는지 확인하는 테스트")
    void bookmarkCheck(){
        Long recipe_id= 3l;
        Long member_id = 2l;

        when(recipeBookmarkRepository.existsByMember_IdAndRecipe_Id(eq(member_id),eq(recipe_id))).thenReturn(true);

        Boolean checkBookmark = recipeBookmarkService.checkBookmark(member_id, recipe_id);
        assertThat(checkBookmark).isTrue();
    }

    @Test
    @DisplayName("사용자가 즐겨찾기한 레시피 제목 조회 테스트")
    void bookmark_page(){

        Long memberId = 1l;
        Member member = Member.builder().id(memberId).nickName("닉네임").build();

        when(memberRepository.findById(eq(memberId))).thenReturn(Optional.of(member));

        Pageable pageRequest = PageRequest.of(0, 10);

        List<RecipeDto> list = List.of(RecipeDto.builder().id(1l).title("레시피1").build(),RecipeDto.builder().id(2l).title("레시피2").build(),RecipeDto.builder().id(3l).title("레시피3").build());
        boolean hasNext =false;

        SliceImpl<RecipeDto> recipeDtoSlice = new SliceImpl<>(list, pageRequest, hasNext);

        when(recipeBookmarkRepository.userInfoBookmarks(eq(memberId),isNull(),eq(pageRequest))).thenReturn(recipeDtoSlice);

        UserInfoBookmarkResponse userInfoBookmarkResponse = recipeBookmarkService.userInfoBookmark(memberId, null, pageRequest);
        assertThat(userInfoBookmarkResponse.getBookmarkList()).hasSize(3);
        assertThat(userInfoBookmarkResponse.getNextPage()).isFalse();
    }


}