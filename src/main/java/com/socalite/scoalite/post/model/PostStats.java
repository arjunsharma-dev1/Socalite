package com.socalite.scoalite.post.model;

import com.socalite.scoalite.reaction.model.ReactionType;
import com.socalite.scoalite.utils.JpaConverterJson;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Type;

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


//    @Convert(converter = JpaConverterJson.class)
//    @Column(columnDefinition = "json")
//    @ColumnTransformer(write = "?::json")
    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "json")
    private ReactionCounts reactionCounts = new ReactionCounts();

    @Column
    private long commentsCount = 0;

    @OneToOne(mappedBy = "stats")
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    public void incrementReactionCount(ReactionType reactionType) {
//        reactionCounts.getReactionCounts().add(new ReactionCount())
        var reactionOptional = reactionCounts.getReactionCounts()
                .stream()
                .filter(reactionCount -> reactionCount.getReactionType().equals(reactionType))
                .findFirst();

        ReactionCount reactionCount =  null;
        if (reactionOptional.isEmpty()) {
            reactionCount = new ReactionCount()
                    .setCount(1)
                    .setReactionType(reactionType);
            reactionCounts.getReactionCounts().add(reactionCount);
            return;
        }

        reactionCount = reactionOptional.get();
        reactionCount.setCount(reactionCount.getCount()+1);
        reactionCount.setReactionType(reactionType);
    }

    public void decrementReactionCount(ReactionType reactionType) {
        var reactionOptional = reactionCounts.getReactionCounts()
                .stream()
                .filter(reactionCount -> reactionCount.getReactionType().equals(reactionType))
                .findFirst();

        if (reactionOptional.isEmpty()) {
            return;
        }
        var reactionCount = reactionOptional.get();

        if (reactionCount.getCount() - 1 == 0) {
            reactionCounts.getReactionCounts().remove(reactionCount);
            return;
        }
        reactionCount.setCount(reactionCount.getCount()-1);
        reactionCount.setReactionType(reactionType);
    }
}