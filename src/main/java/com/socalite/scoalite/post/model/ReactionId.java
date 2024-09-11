package com.socalite.scoalite.post.model;

import com.socalite.scoalite.reaction.model.ReactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Embeddable
public class ReactionId {
    @Column
    private long postId;
    @Column
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;
}