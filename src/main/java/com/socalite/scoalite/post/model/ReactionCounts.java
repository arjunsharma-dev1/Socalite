package com.socalite.scoalite.post.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class ReactionCounts {
    Set<ReactionCount> reactionCounts = new HashSet<>();
}
