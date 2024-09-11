package com.socalite.scoalite.post.converter;

import com.socalite.scoalite.reaction.model.ReactionType;
import com.socalite.scoalite.utils.Converter;
import com.socalite.scoalite.utils.ToDTOConverter;
import com.socalite.scoalite.post.AllPostEntry;
import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.user.converter.NameToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class PostToAllPostEntry implements ToDTOConverter<Post, AllPostEntry> {

    @Autowired
    private NameToStringConverter nameToStringConverter;

    @Override
    public AllPostEntry convert(Post input) {
        var owner = input.getOwner();

        var reactionCounts =
                input.getStats().getReactionCounts()
                        .stream()
                        .map(reactionCount -> new AbstractMap.SimpleImmutableEntry<>(
                                reactionCount.getId().getReactionType(),
                                reactionCount.getCount()
                        ))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (first, second) -> first));

        return new AllPostEntry(
                input.getId(),
                input.getContent(),
                reactionCounts,
                input.getStats().getCommentsCount(),
                input.getPostedAt(),
                owner.getId(),
                nameToStringConverter.convert(owner.getName())
        );
    }
}