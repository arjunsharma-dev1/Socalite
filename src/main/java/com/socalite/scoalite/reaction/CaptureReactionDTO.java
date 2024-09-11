package com.socalite.scoalite.reaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socalite.scoalite.reaction.model.ReactionType;
import com.socalite.scoalite.utils.CaptureType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
public class CaptureReactionDTO {
    private ReactionType reactionType;
    private long postId;
    private String message;
    @JsonIgnore
    private CaptureType captureReactionType;

    public static CaptureReactionDTO modified(long postId, ReactionType type) {
        return new CaptureReactionDTO()
                .setReactionType(type)
                .setPostId(postId)
                .setMessage("Reaction Modified Successfully")
                .setCaptureReactionType(CaptureType.MODIFIED);
    }


    public static CaptureReactionDTO failure(long postId, String message) {
        if (StringUtils.isBlank(message)) {
            message = "Couldn't Capture Reaction";
        }
        return new CaptureReactionDTO()
                .setReactionType(null)
                .setPostId(postId)
                .setMessage(message)
                .setCaptureReactionType(CaptureType.FAILURE);
    }

    public static CaptureReactionDTO captured(long postId, ReactionType type) {
        Objects.requireNonNull(type);
        return new CaptureReactionDTO()
                .setReactionType(type)
                .setPostId(postId)
                .setMessage("Reaction Captured Successfully")
                .setCaptureReactionType(CaptureType.CAPTURED);
    }

    public static CaptureReactionDTO removed(long postId, ReactionType type) {
        Objects.requireNonNull(type);
        return new CaptureReactionDTO()
                .setReactionType(type)
                .setPostId(postId)
                .setMessage("Reaction Removed Successfully")
                .setCaptureReactionType(CaptureType.REMOVED);
    }
}
