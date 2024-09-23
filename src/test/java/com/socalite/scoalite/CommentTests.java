package com.socalite.scoalite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socalite.scoalite.comment.CommentRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static com.socalite.scoalite.TestUtils.*;
import static org.hamcrest.Matchers.*;

@Log4j2
public class CommentTests extends TestContainer {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerComment() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asLong();
        var postId =
                registerPost(requestSpecification, userId, "Second Post For Comment Testing")
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asLong();

        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);
        addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo("Comment Registered Successfully"))
                .body("commentId", is(1));
    }

    @Test
    void getComments() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asInt();

        var postContent = "Second Post For Comment Testing";

        var postId =
                registerPost(requestSpecification, userId, postContent)
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asInt();

        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);

        var firstCommentId = addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        var secondCommentRequestDTO = new CommentRequestDTO().setContent("Second Comment for Testing").setPostId(postId);

        var secondCommentId = addComment(requestSpecification, objectMapper, userId, secondCommentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        TestUtils.getComments(requestSpecification, userId, postId)
                .body("$", Matchers.aMapWithSize(6))
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(2))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", equalTo(2))
                .body("content[0].commentId", equalTo(secondCommentId))
                .body("content[0].content", equalTo("Second Comment for Testing"))
                .body("content[0].ownerId", equalTo(1))
                .body("content[0].ownerName", equalTo("samplejunlast"))
                .body("content[1].commentId", equalTo(firstCommentId))
                .body("content[1].content", equalTo("First Comment for Testing"))
                .body("content[1].ownerId", equalTo(1))
                .body("content[1].ownerName", equalTo("samplejunlast"));

        TestUtils.getPosts(requestSpecification, userId)
                .statusCode(HttpStatus.SC_OK)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("empty", equalTo(false))
                .body("content.size()", equalTo(1)) // Ensure content array has exactly one item
                .body("content[0].id", equalTo(postId))
                .body("content[0].content", equalTo(postContent))
                .body("content[0].likesCount", anEmptyMap())
                .body("content[0].commentsCount", equalTo(2))
                .body("content[0].ownerId", equalTo(userId))
                .body("content[0].userName", equalTo("samplejunlast"));
    }

    @Test
    void deleteComment() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asInt();
        var postId =
                registerPost(requestSpecification, userId, "Second Post For Comment Testing")
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asInt();

        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);

        var firstCommentId = addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        var secondCommentRequestDTO = new CommentRequestDTO().setContent("Second Comment for Testing").setPostId(postId);

        var secondCommentId = addComment(requestSpecification, objectMapper, userId, secondCommentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        TestUtils.deleteComment(requestSpecification, userId, secondCommentId)
                .statusCode(HttpStatus.SC_OK);

        TestUtils.getComments(requestSpecification, userId, postId)
                .body("$", Matchers.aMapWithSize(6))
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", equalTo(1))
                .body("content[0].commentId", equalTo(firstCommentId))
                .body("content[0].content", equalTo("First Comment for Testing"))
                .body("content[0].ownerId", equalTo(userId))
                .body("content[0].ownerName", equalTo("samplejunlast"));
    }

    @Test
    void updateComment() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asInt();
        var postId =
                registerPost(requestSpecification, userId, "Second Post For Comment Testing")
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asInt();


        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);

        var commentId = addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        var commentRequestDTOSecond = new CommentRequestDTO().setContent("Second Comment for Testing").setPostId(postId);

        var commentSecondId = addComment(requestSpecification, objectMapper, userId, commentRequestDTOSecond)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();


        var firstCommentUpdateRequestDTO = new CommentUpdateRequestDTO().setCommentId(commentId).setContent("First Comment - Update");

        TestUtils.updateComment(requestSpecification, objectMapper, firstCommentUpdateRequestDTO, userId)
                .statusCode(HttpStatus.SC_OK)
                .body("message", is("Comment Updated Successfully"))
                .body("commentId", is(commentId));

        var allComments =
                TestUtils.getComments(requestSpecification, userId, postId)
                        .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(2))
                .body("totalNumberOfPages", equalTo(1))
                .body("empty", equalTo(false))
                .body("content.size()", equalTo(2)) // Ensure content array has exactly two items
                .body("content[0].commentId", equalTo(commentSecondId))
                .body("content[0].content", equalTo("Second Comment for Testing"))
                .body("content[0].ownerId", equalTo(userId))
                .body("content[0].ownerName", equalTo("samplejunlast"))
                .body("content[1].commentId", equalTo(commentId))
                .body("content[1].content", equalTo("First Comment - Update"))
                .body("content[1].ownerId", equalTo(userId))
                .body("content[1].ownerName", equalTo("samplejunlast"));

        log.atInfo().log(allComments);
    }

    @Test
    void updateNonExistingComment() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asInt();
        var postId =
                registerPost(requestSpecification, userId, "Second Post For Comment Testing")
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asInt();


        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);

        var commentId = addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();

        var commentRequestDTOSecond = new CommentRequestDTO().setContent("Second Comment for Testing").setPostId(postId);

        var commentSecondId = addComment(requestSpecification, objectMapper, userId, commentRequestDTOSecond)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();


        var nonExistingCommentId = 3;
        var firstCommentUpdateRequestDTO = new CommentUpdateRequestDTO().setCommentId(nonExistingCommentId).setContent("First Comment - Update");

        TestUtils.updateComment(requestSpecification, objectMapper, firstCommentUpdateRequestDTO, userId)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("No Comment Found"))
                .body("commentId", is(nonExistingCommentId));
    }

    @Test
    void updateEmptyCommentShouldFail() throws JsonProcessingException {
        var userId =
                registerUser(requestSpecification, getFirstRegisterUserDTO())
                        .extract()
                        .as(JsonNode.class)
                        .path("userId")
                        .asInt();
        var postId =
                registerPost(requestSpecification, userId, "Second Post For Comment Testing")
                        .extract()
                        .as(JsonNode.class)
                        .path("postId")
                        .asInt();


        var commentRequestDTO = new CommentRequestDTO().setContent("First Comment for Testing").setPostId(postId);

        var commentId = addComment(requestSpecification, objectMapper, userId, commentRequestDTO)
                .extract()
                .as(JsonNode.class)
                .path("commentId")
                .asInt();


        var nonExistingCommentId = 3;
        var firstCommentUpdateRequestDTO = new CommentUpdateRequestDTO().setCommentId(nonExistingCommentId).setContent("");

        TestUtils.updateComment(requestSpecification, objectMapper, firstCommentUpdateRequestDTO, userId)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Comment cannot be empty"))
                .body("commentId", is(nonExistingCommentId));
    }
}
