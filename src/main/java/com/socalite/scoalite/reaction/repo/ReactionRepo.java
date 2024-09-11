package com.socalite.scoalite.reaction.repo;

import com.socalite.scoalite.reaction.model.Reaction;
import com.socalite.scoalite.reaction.model.ReactionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepo extends JpaRepository<Reaction, ReactionId> {
    @Query(value = "SELECT r FROM Reaction r JOIN FETCH owner JOIN FETCH owner.contact WHERE r.post.id = :postId",
            countQuery = "SELECT count(r) FROM Reaction r WHERE r.post.id = :postId")
    Page<Reaction> findAllByPostId(long postId, Pageable pageable);

}
