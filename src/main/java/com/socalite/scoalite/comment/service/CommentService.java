package com.socalite.scoalite.comment.service;

import com.socalite.scoalite.CommentUpdateRequestDTO;
import com.socalite.scoalite.comment.CommentUpdateResponseDTO;
import com.socalite.scoalite.comment.RegisterCommentResponseDTO;
import com.socalite.scoalite.post.model.Post;
import com.socalite.scoalite.utils.Multi;
import com.socalite.scoalite.utils.PageUtils;
import com.socalite.scoalite.comment.AllCommentDTO;
import com.socalite.scoalite.comment.CommentRequestDTO;
import com.socalite.scoalite.comment.converter.CommentToAllComment;
import com.socalite.scoalite.comment.model.Comment;
import com.socalite.scoalite.comment.repo.CommentRepo;
import com.socalite.scoalite.post.service.PostService;
import com.socalite.scoalite.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepo commentRepo;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private ApplicationContext applicationContext;

    public Multi<List<AllCommentDTO>> getAllForPost(long postId, Pageable pageable) {
        var commentsPage = commentRepo.findAllByPostId(postId, pageable);

        var commentsToAllComment = applicationContext.getBean(CommentToAllComment.class);
        var allCommentsEntries = commentsPage.get().map(commentsToAllComment::convert).toList();
        return PageUtils.toMulti(commentsPage, allCommentsEntries);
    }

    public RegisterCommentResponseDTO postComment(long userId, CommentRequestDTO commentRequestDTO) {
        var postId = commentRequestDTO.getPostId();
        var commentContent = commentRequestDTO.getContent();

        var postRefOptional = postService.getPostRef(postId);
        if (postRefOptional.isEmpty()) {
            return RegisterCommentResponseDTO.failed("Post Not Present");
        }
        var postRef = postRefOptional.get();

        var userRefOptional = userService.getUserReference(userId);
        if (userRefOptional.isEmpty()) {
            return RegisterCommentResponseDTO.failed("User not present");
        }
        var userRef = userRefOptional.get();

        var postOwnerId = postRef.getOwner().getId();
        var isConnected = userService.isConnected(userId, postOwnerId);
        if (!isConnected) {
            return RegisterCommentResponseDTO.failed("User not following the post owner");
        }

        var comment = new Comment();
        comment.setOwner(userRef);
        comment.setContent(commentContent);
        comment.setCommentedAt(LocalDateTime.now());
        comment.setPost(postRef);

        var commentId = commentRepo.save(comment).getId();
        postService.incrementCommentCount(postRef);
        return RegisterCommentResponseDTO.success(commentId);
    }

    public void delete(long userId, long commentId) {
//        TODO: logic to decrementCommentCount
        var post = getPostFromCommentId(commentId);
        commentRepo.deleteByIdAndOwnerId(commentId, userId);
        postService.decrementCommentCount(post);
    }

    private Post getPostFromCommentId(long commentId) {
        var postOptional = commentRepo.findById(commentId)
                .map(Comment::getPost);
        if (postOptional.isEmpty()) {
            throw new RuntimeException("Post Not Found");
        }
        return postOptional.get();
    }

    public Multi<List<AllCommentDTO>> getAllCommentsOnAPost(long loggedInUser, int pageNumber, int pageSize, long postId) {
        var postOwnerDetailOptional = postService.getPostOwnerId(postId);
        if (postOwnerDetailOptional.isEmpty()) {
            return Multi.empty();
        }
        var postOwnerDetail = postOwnerDetailOptional.get();
        var isConnected = userService.isConnected(loggedInUser, postOwnerDetail.ownerId());
        if (!isConnected) {
            return Multi.empty();
        }
        var pageRequest = PageUtils.build(pageNumber, pageSize, Sort.by(Sort.Order.desc("commentedAt")));
        return getAllForPost(postId, pageRequest);
    }

    public Optional<CommentUpdateResponseDTO> update(CommentUpdateRequestDTO commentUpdateRequestDTO, long userId) {
        var commentId = commentUpdateRequestDTO.getCommentId();
        var commentContentUpdated = commentUpdateRequestDTO.getContent();

        if (StringUtils.isBlank(commentContentUpdated)) {
            return Optional.of(CommentUpdateResponseDTO.failed(commentId, "Comment cannot be empty"));
        }

        var commentOptional = commentRepo.findByIdAndOwnerId(commentId, userId);
        if (commentOptional.isEmpty()) {
            return Optional.of(CommentUpdateResponseDTO.failed(commentId, "No Comment Found"));
        }

        var comment = commentOptional.get();
        comment.setContent(commentContentUpdated);
        commentRepo.save(comment);

        return Optional.of(CommentUpdateResponseDTO.success(commentId));
    }
}
