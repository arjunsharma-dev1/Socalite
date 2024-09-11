package com.socalite.scoalite.comment.controller;

import com.socalite.scoalite.CommentUpdateRequestDTO;
import com.socalite.scoalite.comment.CommentUpdateResponseDTO;
import com.socalite.scoalite.comment.RegisterCommentResponseDTO;
import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.comment.AllCommentDTO;
import com.socalite.scoalite.comment.CommentRequestDTO;
import com.socalite.scoalite.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<RegisterCommentResponseDTO> postComment(@RequestHeader(value = "X_LOGIN") long userId,
                                                                  @RequestBody CommentRequestDTO commentRequestDTO) {
        var commentRegisterResponseDTO = commentService.postComment(userId, commentRequestDTO);
        return switch (commentRegisterResponseDTO.getCaptureType()) {
            case CAPTURED -> ResponseEntity.ok(commentRegisterResponseDTO);
            default -> ResponseEntity.badRequest().body(commentRegisterResponseDTO);
        };
    }

//    TODO: update comment

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@RequestHeader(value = "X_LOGIN") long userId,
                                                @PathVariable long commentId) {
        commentService.delete(userId, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Multi<List<AllCommentDTO>>> getUsersCommentsOnAPost(@RequestHeader(value = "X_LOGIN") long loggedInUser,
                                                                              @RequestHeader(value = "X-PAGE-NUMBER", defaultValue = "0") int pageNumber,
                                                                              @RequestHeader(value = "X-PAGE-SIZE", defaultValue = "10") int pageSize,
                                                                              @RequestParam long postId) {
        var allReactions = commentService.getAllCommentsOnAPost(loggedInUser, pageNumber, pageSize, postId);
        if (allReactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allReactions);
    }

    @PutMapping
    public ResponseEntity<CommentUpdateResponseDTO> updateComment(@RequestHeader(value = "X_LOGIN") long loggedInUser,
                                                                  @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO) {
        var commentUpdateResponseOptional =
                commentService.update(commentUpdateRequestDTO, loggedInUser);

        if (commentUpdateResponseOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var commentUpdateResponse =
                commentUpdateResponseOptional.get();

        return switch (commentUpdateResponse.getCaptureType()) {
            case MODIFIED -> ResponseEntity.ok(commentUpdateResponse);
            default -> ResponseEntity.badRequest().body(commentUpdateResponse);
        };
    }
}
