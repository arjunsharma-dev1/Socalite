package com.socalite.scoalite.post.repo;

import com.socalite.scoalite.post.model.PostStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatsRepo extends JpaRepository<PostStats, Long> {
}
