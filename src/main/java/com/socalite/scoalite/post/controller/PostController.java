package com.socalite.scoalite.post.controller;

import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.utils.PageUtils;
import com.socalite.scoalite.post.AllPostEntry;
import com.socalite.scoalite.post.PostRequestDTO;
import com.socalite.scoalite.post.RegisterPostResponseDTO;
import com.socalite.scoalite.post.service.PostService;
import com.socalite.scoalite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<Multi<List<AllPostEntry>>> getPostsOfConnectedUser(@RequestHeader(value = "X_LOGIN") long userId,
                                                                             @RequestHeader(value = "X-PAGE-NUMBER", defaultValue = "0") int pageNumber,
                                                                             @RequestHeader(value = "X-PAGE-SIZE", defaultValue = "10") int pageSize,
                                                                             @RequestParam long connectedUserId) {
        boolean isConnected = userService.isConnected(userId, connectedUserId);
        if(!isConnected) {
            return ResponseEntity.noContent().build();
        }

        var pageRequest = PageUtils.build(pageNumber, pageSize);
        var posts = postService.getPosts(connectedUserId, pageRequest);
        return ResponseEntity.ok(posts);
    }

//    TODO: update post

    @PostMapping
    public ResponseEntity<RegisterPostResponseDTO> post(@RequestHeader(value = "X_LOGIN") long userId,
                                                        @RequestBody PostRequestDTO postRequestDTO) {
        var registerPostResponseDTO =
                postService.post(userId, postRequestDTO);
        return switch (registerPostResponseDTO.getCaptureType()) {
            case CAPTURED -> ResponseEntity.ok().body(registerPostResponseDTO);
            default -> ResponseEntity.badRequest().body(registerPostResponseDTO);
        };
    }
}
