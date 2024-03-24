package com.team.RecipeRadar.domain.like.postLike.application;


import com.team.RecipeRadar.domain.like.postLike.dao.PostLikeRepository;
import com.team.RecipeRadar.domain.like.postLike.domain.PostLike;
import com.team.RecipeRadar.domain.like.postLike.dto.PostLikeDto;
import com.team.RecipeRadar.domain.member.dao.MemberRepository;
import com.team.RecipeRadar.domain.member.domain.Member;
import com.team.RecipeRadar.domain.member.dto.MemberDto;
import com.team.RecipeRadar.domain.post.dao.PostRepository;
import com.team.RecipeRadar.domain.post.domain.Post;
import com.team.RecipeRadar.global.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class PostLikeServiceImplTest {


    @Mock PostLikeRepository postLikeRepository;
    @Mock MemberRepository memberRepository;
    @Mock PostRepository postRepository;
    @Mock
    JwtProvider jwtProvider;

    @InjectMocks PostLikeServiceImpl postLikeService;;

    @Test
    @DisplayName("게시물 좋아요")
    void addLike_NewLikeAdded() {
        // Given
        Member member = Member.builder().id(1l).loginId("testId").email("222").build();
        Post post = Post.builder()
                .id(2l)
                .postContent("컨텐트")
                .postTitle("타이틀")
                .postServing("ser")
                .postCookingTime("time")
                .postCookingLevel("level")
                .postLikeCount(0)
                .build();

        PostLikeDto postLikeDto = PostLikeDto.builder()
                .postId(2L)
                .memberId(1l)
                .build();

        when(memberRepository.findById(1l)).thenReturn(Optional.of(member));
        when(postRepository.findById(2l)).thenReturn(Optional.of(post));

        when(postLikeRepository.existsByMemberIdAndPostId(anyLong(), anyLong())).thenReturn(false);

        // When
        boolean result = postLikeService.addLike(postLikeDto);

        // Then
        assertFalse(result); // 좋아요 추가되었으므로 false를 반환해야 함을 확인
        verify(postLikeRepository, times(1)).save(any(PostLike.class)); // save() 메서드가 한 번 호출되었는지 확인
        verify(postLikeRepository, never()).deleteByMemberIdAndPostId(anyLong(), anyLong()); // deleteByMember_IdAndPostId() 메서드가 호출되지 않았는지 확인
    }

    @Test
    @DisplayName("게시물의 좋아요존재시 좋아요 삭제")
    void addLike_ExistingLikeRemoved() {
        // Given
        MemberDto memberDto = MemberDto.builder().id(1L).loginId("testId").email("222").build();
        Post addPostRequest = Post.builder()
                .id(2l)
                .postContent("컨텐트")
                .postTitle("타이틀")
                .postServing("ser")
                .postCookingTime("time")
                .postCookingLevel("level")
                .postLikeCount(0)
                .build();

        PostLikeDto postLikeDto = PostLikeDto.builder()
                .postId(2l)
                .memberId(1l)
                .build();


        when(postRepository.findById(2l)).thenReturn(Optional.of(addPostRequest));
        when(postLikeRepository.existsByMemberIdAndPostId(anyLong(), anyLong())).thenReturn(true);

        // When
        boolean result = postLikeService.addLike(postLikeDto);

        log.info("result={}",result);
        // Then
        assertTrue(result); // 좋아요가 이미 존재하므로 true를 반환해야 함을 확인
        verify(postLikeRepository, never()).save(any(PostLike.class)); // save() 메서드가 호출되지 않았는지 확인
        verify(postLikeRepository, times(1)).deleteByMemberIdAndPostId(anyLong(), anyLong()); // deleteByMember_IdAndPostId() 메서드가 한 번 호출되었는지 확인
    }

    @Test
    @DisplayName("jwt토큰을 이용한 좋아요 되어있는지 테스트")
    public void testCheckLike() {
        // 가짜 JWT 토큰 및 기타 필요한 데이터 설정
        String fakeJwtToken = "fakeToken";
        Long id = 1l;

        Member member = Member.builder().id(2l).loginId("fakeLoginId").build();
        // jwtProvider 메서드 호출에 대한 목 설정
        when(jwtProvider.validateAccessToken(fakeJwtToken)).thenReturn("fakeLoginId");

        // memberRepository 메서드 호출에 대한 목 설정
        when(memberRepository.findByLoginId("fakeLoginId")).thenReturn(member);

        // postLikeRepository 메서드 호출에 대한 목 설정
        when(postLikeRepository.existsByMemberIdAndPostId(anyLong(), anyLong())).thenReturn(true);

        // 메서드 호출 및 결과 확인
        assertTrue(postLikeService.checkLike(fakeJwtToken, id));
    }

    @Test
    @DisplayName("jwt토큰을 이용한 좋아요 되어있는지 테스트")
    public void test_Check_UnLike() {
        // 가짜 JWT 토큰 및 기타 필요한 데이터 설정
        String fakeJwtToken = "fakeToken";
        Long id = 1l;

        Member member = Member.builder().id(2l).loginId("fakeLoginId").build();
        // jwtProvider 메서드 호출에 대한 목 설정
        when(jwtProvider.validateAccessToken(fakeJwtToken)).thenReturn("fakeLoginId");

        // memberRepository 메서드 호출에 대한 목 설정
        when(memberRepository.findByLoginId("fakeLoginId")).thenReturn(member);

        // postLikeRepository 메서드 호출에 대한 목 설정
        when(postLikeRepository.existsByMemberIdAndPostId(anyLong(), anyLong())).thenReturn(false);

        // 메서드 호출 및 결과 확인
        assertFalse(postLikeService.checkLike(fakeJwtToken, id));
    }
}