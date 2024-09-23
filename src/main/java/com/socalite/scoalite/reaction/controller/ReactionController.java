package com.socalite.scoalite.reaction.controller;

import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.reaction.AllReactionDTO;
import com.socalite.scoalite.reaction.CaptureReactionDTO;
import com.socalite.scoalite.reaction.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("reactions")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public ResponseEntity<CaptureReactionDTO> react(@RequestHeader(value = "X_LOGIN") long userId,
                                                    @RequestBody ReactionDTO reactionDTO) {
        var captureReactionDTO = reactionService.react(userId, reactionDTO);
        return switch (captureReactionDTO.getCaptureReactionType()) {
            case CAPTURED, REMOVED, MODIFIED -> ResponseEntity.ok(captureReactionDTO);
            default -> ResponseEntity.badRequest().body(captureReactionDTO);
        };
    }

    @GetMapping("posts/{postId}/reactions")
    public ResponseEntity<Multi<List<AllReactionDTO>>> getReactionsOnAPost(@RequestHeader(value = "X_LOGIN") long loggedInUser,
                                                                           @RequestHeader(value = "X-PAGE-NUMBER", defaultValue = "0") int pageNumber,
                                                                           @RequestHeader(value = "X-PAGE-SIZE", defaultValue = "10") int pageSize,
                                                                           @PathVariable long postId) {
        var allReactions = reactionService.getAllReactionsOnAPost(loggedInUser, pageNumber, pageSize, postId);
        if (allReactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allReactions);
    }

//    TODO: change reaction
}
