package com.socalite.scoalite.reaction;

import com.socalite.scoalite.reaction.model.ReactionType;

import java.time.LocalDateTime;

public record AllReactionDTO(long userId,
                             long postId,
                             ReactionType reactionType,
                             LocalDateTime reactedAt,
                             long ownerId,
                             String ownerName){
}
