package com.socalite.scoalite.reaction.converter;

import com.socalite.scoalite.utils.Converter;
import com.socalite.scoalite.utils.ToDTOConverter;
import com.socalite.scoalite.reaction.AllReactionDTO;
import com.socalite.scoalite.reaction.model.Reaction;
import com.socalite.scoalite.user.converter.NameToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class ReactionToAllReaction implements ToDTOConverter<Reaction, AllReactionDTO> {

    @Autowired
    private NameToStringConverter nameToStringConverter;

    @Override
    public AllReactionDTO convert(Reaction input) {
        var reactionId = input.getReactionId();
        return new AllReactionDTO(
                reactionId.getUserId(),
                reactionId.getPostId(),
                input.getType(),
                input.getReactedAt(),
                input.getOwner().getId(),
                nameToStringConverter.convert(input.getOwner().getName())
        );
    }
}
