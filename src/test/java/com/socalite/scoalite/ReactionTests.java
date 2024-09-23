package com.socalite.scoalite;

import com.fasterxml.jackson.databind.JsonNode;
import com.socalite.scoalite.reaction.controller.ReactionDTO;
import com.socalite.scoalite.reaction.model.ReactionType;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.socalite.scoalite.TestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@Log4j2
public class ReactionTests extends TestContainer {

    @Test
    void reactOnaPost() {
        var addUserResponse = registerUser(requestSpecification, getFirstRegisterUserDTO()).extract().as(JsonNode.class);

        var message = addUserResponse.path("message").asText();
        var userId = addUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1, userId, message);

        var addPostResponse = TestUtils.registerPost(requestSpecification, userId, "First Post through Automation Testing").extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asInt();
        Assertions.assertNotEquals(-1, postId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        reactToPost(requestSpecification, userId, reactionDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("reactionType", equalTo(reactionDTO.getReactionType().name()))
                .body("message", equalTo("Reaction Captured Successfully"))
                .body("postId", is(postId));
    }

    @Test
    void getReactionOnaPost() {
        var addUserResponse = registerUser(requestSpecification, getFirstRegisterUserDTO()).extract().as(JsonNode.class);

        var message = addUserResponse.path("message").asText();
        var userId = addUserResponse.path("userId").asInt();
        Assertions.assertNotEquals(-1, userId, message);

        var firstPostContent = "First Post through Automation Testing";

        var addPostResponse = TestUtils.registerPost(requestSpecification, userId, firstPostContent).extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asInt();
        Assertions.assertNotEquals(-1, postId, message);

        var addSecondUserResponse =
                registerUser(requestSpecification, getSecondRegisterUserDTO()).extract().as(JsonNode.class);

        var secondUserId = addSecondUserResponse.path("userId").asInt();
        Assertions.assertNotEquals(-1L, secondUserId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        var firstReactionResponse = reactToPost(requestSpecification, userId, reactionDTO).extract().as(JsonNode.class);
        var secondReactionResponse = reactToPost(requestSpecification, secondUserId, reactionDTO).extract().as(JsonNode.class);

        var firstReaction = firstReactionResponse.path("reactionType").asText();
        var secondReaction = secondReactionResponse.path("reactionType").asText();

        getReactions(requestSpecification, userId, postId)
                .statusCode(HttpStatus.SC_OK)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(2))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", is(2))
                .body("content[0].userId", equalTo(secondUserId))
                .body("content[0].postId", equalTo(postId))
                .body("content[0].reactionType", equalTo(secondReaction))
                .body("content[0].ownerId", equalTo(secondUserId))
                .body("content[0].ownerName", equalTo("samplejunlast"))
                .body("content[1].userId", equalTo(userId))
                .body("content[1].postId", equalTo(postId))
                .body("content[1].reactionType", equalTo(firstReaction))
                .body("content[1].ownerId", equalTo(userId))
                .body("content[1].ownerName", equalTo("samplejunlast"));

        var getAllPosts = getPosts(requestSpecification, userId);

        log.atInfo().log("All Posts: {}", getAllPosts.extract().asString());

        getAllPosts
                .statusCode(HttpStatus.SC_OK)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("empty", equalTo(false))
                .body("content.size()", equalTo(1)) // Ensure content array has exactly one item
                .body("content[0].id", equalTo(postId))
                .body("content[0].content", equalTo(firstPostContent))
                .body("content[0].likesCount", aMapWithSize(1))
                .body("content[0].likesCount.LIKE", equalTo(2))
                .body("content[0].commentsCount", equalTo(0))
                .body("content[0].ownerId", equalTo(userId))
                .body("content[0].userName", equalTo("samplejunlast"));
    }

    @Test
    void removeReactionOnaPost() {
        var addUserResponse = registerUser(requestSpecification, getFirstRegisterUserDTO()).extract().as(JsonNode.class);

        var message = addUserResponse.path("message").asText();
        var userId = addUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1, userId, message);

        var addPostResponse = registerPost(requestSpecification, userId, "First Post through Automation Testing").extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asInt();
        Assertions.assertNotEquals(-1, postId, message);

        var addSecondUserResponse =
                registerUser(requestSpecification, getSecondRegisterUserDTO()).extract().as(JsonNode.class);

        var secondUserId = addSecondUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1L, secondUserId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        var firstReactionResponse = reactToPost(requestSpecification, userId, reactionDTO).extract().as(JsonNode.class);
        var secondReactionResponse = reactToPost(requestSpecification, secondUserId, reactionDTO).extract().as(JsonNode.class);
        var firstReaction = firstReactionResponse.path("reactionType").asText();

        reactToPost(requestSpecification, userId, reactionDTO)
                .body("$", aMapWithSize(3))
                .body("reactionType", is(firstReaction))
                .body("message", is("Reaction Removed Successfully"))
                .body("postId", is(postId));


        var secondReaction = secondReactionResponse.path("reactionType").asText();

        var getAllReactionsResponse = getReactions(requestSpecification, userId, postId)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", is(1))
                .body("content[0].userId", equalTo(2))
                .body("content[0].postId", equalTo(1))
                .body("content[0].reactionType", equalTo(secondReaction))
                .body("content[0].ownerId", equalTo(2))
                .body("content[0].ownerName", equalTo("samplejunlast"));
    }

    @Test
    void changeReaction() {
        var addUserResponse = registerUser(requestSpecification, getFirstRegisterUserDTO()).extract().as(JsonNode.class);

        var message = addUserResponse.path("message").asText();
        var userId = addUserResponse.path("userId").asInt();
        Assertions.assertNotEquals(-1, userId, message);

        var postContent = "First Post through Automation Testing";
        var addPostResponse = registerPost(requestSpecification, userId, postContent).extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asInt();
        Assertions.assertNotEquals(-1, postId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        reactToPost(requestSpecification, userId, reactionDTO).extract().as(JsonNode.class);

        var newReactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.ANGRY);
        var newReaction = newReactionDTO.getReactionType().name();

        reactToPost(requestSpecification, userId, newReactionDTO)
                .statusCode(HttpStatus.SC_OK)
                .body("$", aMapWithSize(3))
                .body("reactionType", is(newReaction))
                .body("message", is("Reaction Modified Successfully"))
                .body("postId", is(postId));

        getReactions(requestSpecification, userId, postId)
                .statusCode(HttpStatus.SC_OK)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", is(1))
                .body("content[0].userId", equalTo(1))
                .body("content[0].postId", equalTo(1))
                .body("content[0].reactionType", equalTo(newReaction))
                .body("content[0].ownerId", equalTo(1))
                .body("content[0].ownerName", equalTo("samplejunlast"));

        getPosts(requestSpecification, userId)
                .statusCode(HttpStatus.SC_OK)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(1))
                .body("totalNumberOfPages", equalTo(1))
                .body("empty", equalTo(false))
                .body("content.size()", equalTo(1)) // Ensure content array has exactly one item
                .body("content[0].id", equalTo(postId))
                .body("content[0].content", equalTo(postContent))
                .body("content[0].likesCount", aMapWithSize(1))
                .body("content[0].likesCount.ANGRY", equalTo(1))
                .body("content[0].commentsCount", equalTo(0))
                .body("content[0].ownerId", equalTo(userId))
                .body("content[0].userName", equalTo("samplejunlast"));
    }
}
