package com.socalite.scoalite.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socalite.scoalite.utils.CaptureType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RegisterPostResponseDTO {
    private String message = "";
    private long postId = -1;
    @JsonIgnore
    private CaptureType captureType;

    public static RegisterPostResponseDTO success(Long postId) {
        return new RegisterPostResponseDTO()
                .setPostId(postId)
                .setMessage("Success")
                .setCaptureType(CaptureType.CAPTURED);
    }

    public static RegisterPostResponseDTO error() {
        return new RegisterPostResponseDTO()
                .setPostId(-1)
                .setMessage("User Not Present")
                .setCaptureType(CaptureType.FAILURE);
    }
}
