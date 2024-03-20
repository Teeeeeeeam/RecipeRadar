package com.team.RecipeRadar.repository;

import com.team.RecipeRadar.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentContentContainingIgnoreCase(String commentTitle);
    @Modifying
    @Query("delete from Comment c where c.member.id=:member_id and c.id=:comment_id")
    void deleteMemberId(@Param("member_id") Long member_id, @Param("comment_id")Long comment_id);

    Page<Comment> findAllByPost_Id(Long post_id, Pageable pageable);
}
