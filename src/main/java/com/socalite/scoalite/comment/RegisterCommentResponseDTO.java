package com.socalite.scoalite.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socalite.scoalite.utils.CaptureType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RegisterCommentResponseDTO {
    private long commentId = -1L;
    private String message = "";
    @JsonIgnore
    private CaptureType captureType;

    public static RegisterCommentResponseDTO failed(String message) {
        return new RegisterCommentResponseDTO()
                .setCommentId(-1L)
                .setMessage(message)
                .setCaptureType(CaptureType.FAILURE);
    }

    public static RegisterCommentResponseDTO success(Long commentId) {
        return new RegisterCommentResponseDTO()
                .setCommentId(commentId)
                .setMessage("Comment Registered Successfully")
                .setCaptureType(CaptureType.CAPTURED);
    }
}
