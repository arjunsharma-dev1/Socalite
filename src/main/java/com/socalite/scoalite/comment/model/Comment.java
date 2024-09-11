package com.socalite.scoalite.comment.model;

import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.user.model.User;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Objects;

@DynamicUpdate
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CommentSequenceGenerator")
    @SequenceGenerator(name = "CommentSequenceGenerator", sequenceName = "comment_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    @Nonnull
    @Column
    private String content;

    @Column
    private LocalDateTime commentedAt;


    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "comment_user_fk"))
    private User owner;

    @Nonnull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "comment_post_fk"))
    private Post post;

    public Comment() {
        commentedAt = LocalDateTime.now();
    }

    public Comment(Long id, @Nonnull String content, LocalDateTime commentedAt, User owner, @Nonnull Post post) {
        this.id = id;
        this.content = content;
        this.commentedAt = commentedAt;
        this.owner = owner;
        this.post = post;

        if (Objects.isNull(commentedAt)) {
            commentedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Nonnull
    public String getContent() {
        return content;
    }

    public void setContent(@Nonnull String content) {
        this.content = content;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Nonnull
    public Post getPost() {
        return post;
    }

    public void setPost(@Nonnull Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", commentedAt=" + commentedAt +
                '}';
    }
}
