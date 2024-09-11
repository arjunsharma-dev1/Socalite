package com.socalite.scoalite.post.model;

import com.socalite.scoalite.reaction.model.ReactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class PostStats {

    @Id
    @Column(name = "post_id")
    private Long id;

    @OneToMany(mappedBy = "postStats", cascade = CascadeType.ALL)
    private Set<ReactionCount> reactionCounts = new HashSet<>();

    @Column
    private long commentsCount = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "post_stats_reaction_post_stats_post_fk"))
    private Post post;

    public void incrementReactionCount(ReactionType reactionType) {
        reactionCounts.stream()
                .filter(reactionCount -> reactionCount.getId().getReactionType().equals(reactionType))
                .map(reactionCount -> reactionCount.setCount(reactionCount.getCount() + 1));
    }

    public void decrementReactionCount(ReactionType type) {
        reactionCounts.stream()
                .filter(reactionCount -> reactionCount.getId().getReactionType().equals(type))
                .map(reactionCount -> reactionCount.setCount(reactionCount.getCount() - 1));
    }
}

