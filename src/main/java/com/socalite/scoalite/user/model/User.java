package com.socalite.scoalite.user.model;

import com.socalite.scoalite.comment.model.Comment;
import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.reaction.model.Reaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.Set;

@NamedEntityGraph(
        name = "User.Reactions.Comments",
        attributeNodes = {
                @NamedAttributeNode(value = "reactions"),
                @NamedAttributeNode(value = "comments")
        }
)
@Entity
@Table(
    name = "Users",
    uniqueConstraints = {
            @UniqueConstraint(name = "unique_username", columnNames = {"username"})
    }
)
@Getter
@Setter
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSequenceGenerator")
    @SequenceGenerator(name = "UserSequenceGenerator", sequenceName = "user_seq", initialValue = 1, allocationSize = 1)
    Long id;

    @Embedded
    Name name;

    @Column(unique = true, nullable = false)
    String username;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "user_contact_fk"))
    Contact contact;

    @OneToMany(mappedBy = "owner")
    Set<Reaction> reactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "owner")
    Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "owner")
    Set<Post> posts = new LinkedHashSet<>();

    private static final Log log = LogFactory.getLog(User.class);

    @PrePersist
    public void logNewUserEntryBeforeSaving() {
        log.info(String.format("User Before Persisting: %s", username));
    }

    @PostPersist
    public void logNewUserEntryAfterSaving() {
        log.info(String.format("User After Persisting: %s", username));
    }
}


