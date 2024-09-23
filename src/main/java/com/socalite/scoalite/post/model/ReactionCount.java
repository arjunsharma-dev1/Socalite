package com.socalite.scoalite.post.model;

import com.socalite.scoalite.reaction.model.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
public class ReactionCount {
    private ReactionType reactionType;
    private long count;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReactionCount rc)) {
            return false;
        }
        return Objects.equals(this.reactionType,rc.reactionType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.reactionType);
    }
}

