package com.socalite.scoalite.comment.converter;

import com.socalite.scoalite.utils.Converter;
import com.socalite.scoalite.utils.ToDTOConverter;
import com.socalite.scoalite.comment.AllCommentDTO;
import com.socalite.scoalite.comment.model.Comment;
import com.socalite.scoalite.user.converter.NameToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class CommentToAllComment implements ToDTOConverter<Comment, AllCommentDTO> {

    @Autowired
    private NameToStringConverter nameToStringConverter;

    @Override
    public AllCommentDTO convert(Comment input) {
        return new AllCommentDTO(
                input.getId(),
                input.getContent(),
                input.getCommentedAt(),
                input.getOwner().getId(),
                nameToStringConverter.convert(input.getOwner().getName())
        );
    }
}
