package com.team.RecipeRadar.domain.post.dao;

import com.team.RecipeRadar.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByPostContentContainingIgnoreCase(String postTitle);
    @Modifying
    @Query("delete from Post c where c.member.id=:member_id and c.id=:post_id")
    void deleteMemberId(@Param("member_id") Long member_id, @Param("post_id")Long post_id);

    Page<Post> findAllByPost_Id(Long post_id, Pageable pageable);
}
