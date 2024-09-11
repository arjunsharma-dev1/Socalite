package com.socalite.scoalite.comment.repo;

import com.socalite.scoalite.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c FROM Comment c JOIN FETCH owner JOIN FETCH owner.contact WHERE c.post.id = :postId",
            countQuery = "SELECT count(c) FROM Comment c WHERE c.post.id = :postId")
    Page<Comment> findAllByPostId(long postId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Comment WHERE id = :commentId AND owner_id = :userId", nativeQuery = true)
    int deleteByIdAndOwnerId(long commentId, long userId);

    Optional<Comment> findByIdAndOwnerId(long commentId, long userId);
}
