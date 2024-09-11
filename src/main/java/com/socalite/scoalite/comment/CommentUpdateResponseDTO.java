package com.socalite.scoalite.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socalite.scoalite.utils.CaptureType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CommentUpdateResponseDTO {
    private long commentId;
    private String message;
    @JsonIgnore
    private CaptureType captureType;

    public static CommentUpdateResponseDTO failed(long commentId, String message) {
        return new CommentUpdateResponseDTO()
                .setCommentId(commentId)
                .setMessage(message)
                .setCaptureType(CaptureType.FAILURE);
    }

    public static CommentUpdateResponseDTO success(Long commentId) {
        return new CommentUpdateResponseDTO()
                .setCommentId(commentId)
                .setMessage("Comment Updated Successfully")
                .setCaptureType(CaptureType.MODIFIED);
    }
}
