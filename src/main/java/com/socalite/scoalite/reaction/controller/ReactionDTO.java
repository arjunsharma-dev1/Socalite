package com.socalite.scoalite.reaction.controller;

import com.socalite.scoalite.reaction.model.ReactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ReactionDTO {
    private ReactionType reactionType;
    private long postId;
}
