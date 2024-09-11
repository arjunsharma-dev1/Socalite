package com.socalite.scoalite.reaction.service;

import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.utils.PageUtils;
import com.socalite.scoalite.post.service.PostService;
import com.socalite.scoalite.reaction.AllReactionDTO;
import com.socalite.scoalite.reaction.CaptureReactionDTO;
import com.socalite.scoalite.reaction.controller.ReactionDTO;
import com.socalite.scoalite.reaction.converter.ReactionToAllReaction;
import com.socalite.scoalite.reaction.model.Reaction;
import com.socalite.scoalite.reaction.model.ReactionId;
import com.socalite.scoalite.reaction.repo.ReactionRepo;
import com.socalite.scoalite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepo reactionRepo;

    @Autowired
    private PostService postService;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    public Multi<List<AllReactionDTO>> getAllForPost(long postId, Pageable pageable) {
        var reactionsPage = reactionRepo.findAllByPostId(postId, pageable);

        var reactionToAllReaction = applicationContext.getBean(ReactionToAllReaction.class);
        var allReactionsEntries = reactionsPage.get().map(reactionToAllReaction::convert).toList();
        return PageUtils.toMulti(reactionsPage, allReactionsEntries);
    }

    public CaptureReactionDTO react(long userId, ReactionDTO reactionDTO) {
        var postId = reactionDTO.getPostId();

        var postRefOptional = postService.getPostRef(postId);
        if (postRefOptional.isEmpty()) {
            return CaptureReactionDTO.failure(postId, "Post Doesn't Exists");
        }
        var postRef = postRefOptional.get();

        var userRefOptional = userService.getUserReference(userId);
        if (userRefOptional.isEmpty()) {
            return CaptureReactionDTO.failure(postId, "Post Doesn't Have Owner");
        }
        var reactionId = new ReactionId().setPostId(postId).setUserId(userId);
        var reactionOptional = reactionRepo.findById(reactionId);

        if (reactionOptional.isPresent()) {
            var reaction = reactionOptional.get();
            if (reaction.getType() != reactionDTO.getReactionType()) {
                reaction.setType(reactionDTO.getReactionType());
                reactionRepo.save(reaction);

                postService.decrementReactionCount(postRef, reaction.getType());
                postService.incrementReactionCount(postRef, reactionDTO.getReactionType());
                return CaptureReactionDTO.modified(postId, reaction.getType());
            } else {
                reactionRepo.deleteById(reactionId);

                postService.decrementReactionCount(postRef, reaction.getType());
                return CaptureReactionDTO.removed(postId, reaction.getType());
            }
        } else {
            var reaction = new Reaction()
                    .setType(reactionDTO.getReactionType())
                    .setPost(postRef)
                    .setOwner(userRefOptional.get())
                    .setReactionId(new ReactionId().setPostId(postId).setUserId(userId));

            var reactionType = reactionRepo.save(reaction).getType();
            postService.incrementReactionCount(postRef, reactionType);
            return CaptureReactionDTO.captured(postId, reactionType);
        }
    }

    public Multi<List<AllReactionDTO>> getAllReactionsOnAPost(long loggedInUser, int pageNumber, int pageSize, long postId) {
        var postOwnerDetailOptional = postService.getPostOwnerId(postId);
        if (postOwnerDetailOptional.isEmpty()) {
            return Multi.empty();
        }
        var postOwnerDetail = postOwnerDetailOptional.get();
        var isConnected = userService.isConnected(loggedInUser, postOwnerDetail.ownerId());
        if (!isConnected) {
            return Multi.empty();
        }
        var pageRequest = PageUtils.build(pageNumber, pageSize, Sort.by(Sort.Order.desc("reactedAt")));
        return getAllForPost(postId, pageRequest);
    }

//    private boolean hasAlreadyReacted(ReactionId reactionId) {
//        return reactionRepo.findById(reactionId);
//    }
}
