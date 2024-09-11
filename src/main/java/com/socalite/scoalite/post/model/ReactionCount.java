package com.socalite.scoalite.post.model;

import com.socalite.scoalite.reaction.model.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ReactionCount {
    @EmbeddedId
    private ReactionId id;
    @Column
    private long count;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "post_stats_reaction_reaction_count_fk"))
    private PostStats postStats;
}

