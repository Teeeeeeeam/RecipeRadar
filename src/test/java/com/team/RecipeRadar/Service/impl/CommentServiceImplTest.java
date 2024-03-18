package com.team.RecipeRadar.Service.impl;

import com.team.RecipeRadar.Entity.Article;
import com.team.RecipeRadar.Entity.Comment;
import com.team.RecipeRadar.Entity.Member;
import com.team.RecipeRadar.dto.ArticleDto;
import com.team.RecipeRadar.dto.CommentDto;
import com.team.RecipeRadar.dto.MemberDto;
import com.team.RecipeRadar.exception.ex.CommentException;
import com.team.RecipeRadar.repository.ArticleRepository;
import com.team.RecipeRadar.repository.CommentRepository;
import com.team.RecipeRadar.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CommentServiceImplTest {

    @Mock MemberRepository memberRepository;
    @Mock ArticleRepository articleRepository;
    @Mock CommentRepository commentRepository;

    @InjectMocks CommentServiceImpl commentService;

    @Mock
    EntityManager entityManager;

    @Test
    @DisplayName("Comment_save 저장테스트")
    public void testSave_ValidMemberAndArticle_ReturnsSavedComment() {
        // 범위
        MemberDto build = MemberDto.builder().id(1l).build();
        ArticleDto build1 = ArticleDto.builder().id(1l).build();

        LocalDateTime dateTime = LocalDateTime.of(2024,3,17,2,15);

        CommentDto commentDto = new CommentDto();
        commentDto.setComment_content("Test comment");
        commentDto.setMemberDto(build);
        commentDto.setArticleDto(build1);
        commentDto.setCreate_at(dateTime);

        Member member = new Member();
        member.setId(1L);
        Article article = new Article();
        article.setId(1L);

        // 목 리파지토리
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 실행
        Comment savedComment = commentService.save(commentDto);

        // 검증
        assertThat(savedComment).isNotNull();
        assertThat("Test comment").isEqualTo(savedComment.getComment_content());
        assertThat(savedComment.getUpdated_at()).isNull();
        assertThat(member).isEqualTo(savedComment.getMember());
        assertThat(article).isEqualTo(savedComment.getArticle());
    }

    @Test
    @DisplayName("NoSuchElementException 오류테스트")
    public void testSave_InvalidMemberOrArticle_ThrowsNoSuchElementException() {
        // 범위
        MemberDto build = MemberDto.builder().id(1l).build();
        ArticleDto build1 = ArticleDto.builder().id(1l).build();

        CommentDto commentDto = new CommentDto();
        commentDto.setComment_content("Test comment");
        commentDto.setMemberDto(build);
        commentDto.setArticleDto(build1);

        // 목 리파지토리
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // 검증 & 실행
        assertThatThrownBy(() -> commentService.save(commentDto))
                .isInstanceOf(NoSuchElementException.class);
    }
    @Test
    @DisplayName("댓글 삭제 검증")
    void delete_comment_Test() {
        // Mock 데이터 설정
        MemberDto memberDto = MemberDto.builder().id(1L).username("test").build(); //댓글 작성자

        CommentDto commentDto = CommentDto.builder().id(1L).comment_content("댓글 삭제 테스트코드").memberDto(memberDto).build();

        Member member = Member.builder().id(1L).username("test").build();

        Comment comment = Comment.builder().id(1L).comment_content("댓글 삭제 테스트코드").member(member).build();

        // Member 리포지토리 mock 설정
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // Comment 리포지토리 mock 설정
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // 테스트 수행
        commentService.delete_comment(commentDto);

        //deleteMemberId 메소드가 한번 실행이 됬는지 확인하는
        verify(commentRepository, times(1)).deleteMemberId(member.getId(), comment.getId());

    }
    @Test
    @DisplayName("비 작성자가 댓글을 삭제하려고 하는 경우")
    void delete_comment_by_non_author_Test() {
        // Mock 데이터 설정
        MemberDto nonAuthorDto = MemberDto.builder().id(2L).username("non_author").build(); // 비 작성자

        CommentDto commentDto = CommentDto.builder().id(3L).comment_content("댓글 삭제 테스트코드").memberDto(nonAuthorDto).build();

        Member member = Member.builder().id(1L).username("test").build();       //댓글 작성자
        Member member1 = Member.builder().id(2L).username("non_author").build();        //댓글 비작성자

        Comment comment = Comment.builder().id(3L).comment_content("댓글 삭제 테스트코드").member(member).build();       //member1이 작성한 댓글

        // Member 리포지토리 mock 설정
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member1));

        // Comment 리포지토리 mock 설정
        when(commentRepository.findById(3L)).thenReturn(Optional.of(comment));

        //CommentException에러가 나오는지 확인하는 메서드
        Assertions.assertThatThrownBy(() -> commentService.delete_comment(commentDto))
                .isInstanceOf(CommentException.class);

    }
}