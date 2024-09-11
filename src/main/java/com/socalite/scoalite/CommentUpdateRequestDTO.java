package com.socalite.scoalite;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class CommentUpdateRequestDTO {
    private long commentId;
    private String content;
}
