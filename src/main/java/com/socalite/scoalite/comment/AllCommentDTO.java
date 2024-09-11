package com.socalite.scoalite.comment;

import com.socalite.scoalite.reaction.model.ReactionType;

import java.time.LocalDateTime;

public record AllCommentDTO(long commentId,
                           String content,
                           LocalDateTime commentedAt,
                           long ownerId,
                           String ownerName) {}
