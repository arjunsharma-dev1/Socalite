package com.socalite.scoalite.post;

import com.socalite.scoalite.post.model.ReactionCount;
import com.socalite.scoalite.reaction.model.ReactionType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record AllPostEntry(Long id,
                           String content,
                           Map<ReactionType, Long> likesCount,
                           long commentsCount,
                           LocalDateTime postedAt,
                           Long ownerId,
                           String userName) {
}
