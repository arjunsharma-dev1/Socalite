package com.socalite.scoalite.post.repo;

import com.socalite.scoalite.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.owner.id = :ownerId")
    Page<Post> findAllByOwnerId(@Param(value = "ownerId") Long ownerId, Pageable pageable);
}
