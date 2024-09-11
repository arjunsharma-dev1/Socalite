package com.socalite.scoalite.post.model;

import com.socalite.scoalite.comment.model.Comment;
import com.socalite.scoalite.post.AllPostEntry;
import com.socalite.scoalite.reaction.model.Reaction;
import com.socalite.scoalite.user.model.User;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)

@DynamicUpdate
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PostSequenceGenerator")
    @SequenceGenerator(name = "PostSequenceGenerator", sequenceName = "post_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Nonnull
    @Column
    private String content;

    @Nonnull
    @Column
    private LocalDateTime postedAt = LocalDateTime.now();;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private PostStats stats = new PostStats();

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "post_user_fk"))
    private User owner;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactions = new HashSet<>();

    public Post(Long id, @Nonnull String content, @Nonnull LocalDateTime postedAt, @Nonnull User owner, Set<Comment> comments, Set<Reaction> reactions, PostStats postStats) {
        this.id = id;
        this.content = content;
        this.postedAt = postedAt;
        this.owner = owner;
        this.comments = comments;
        this.reactions = reactions;
        this.stats = postStats;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postedAt=" + postedAt +
                ", id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}