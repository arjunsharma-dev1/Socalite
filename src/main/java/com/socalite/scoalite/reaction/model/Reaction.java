package com.socalite.scoalite.reaction.model;

import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.user.model.User;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(
        /*uniqueConstraints = {
                @UniqueConstraint(name = "unique_reaction", columnNames = {"post_id", "user_id"})
        }*/
)
public class Reaction {
    /*@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReactionSequenceGenerator")
    @SequenceGenerator(name = "ReactionSequenceGenerator", sequenceName = "reaction_seq", initialValue = 1, allocationSize = 1)
    private Long id;*/
    @EmbeddedId
    private ReactionId reactionId;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @Column
    private LocalDateTime reactedAt = LocalDateTime.now();

    @Nonnull
    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "reaction_post_fk"))
    private Post post;

    @Nonnull
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "reaction_user_fk"))
    private User owner;
}
