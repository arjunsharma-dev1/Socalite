package com.socalite.scoalite;

import com.fasterxml.jackson.databind.JsonNode;
import com.socalite.scoalite.reaction.controller.ReactionDTO;
import com.socalite.scoalite.reaction.model.ReactionType;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
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
        var userId = addUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1, userId, message);

        var addPostResponse = TestUtils.registerPost(requestSpecification, userId, "First Post through Automation Testing").extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asLong();
        Assertions.assertNotEquals(-1, postId, message);

        var addSecondUserResponse =
                registerUser(requestSpecification, getSecondRegisterUserDTO()).extract().as(JsonNode.class);

        var secondUserId = addSecondUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1L, secondUserId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        var firstReactionResponse = reactToPost(requestSpecification, userId, reactionDTO).extract().as(JsonNode.class);
        var secondReactionResponse = reactToPost(requestSpecification, secondUserId, reactionDTO).extract().as(JsonNode.class);

        var firstReaction = firstReactionResponse.path("reactionType").asText();
        var secondReaction = secondReactionResponse.path("reactionType").asText();

        var getAllReactionsResponse = getReactions(requestSpecification, userId, postId)
                .body("pageNumber", equalTo(1))
                .body("pageSize", equalTo(10))
                .body("totalElementsCount", equalTo(2))
                .body("totalNumberOfPages", equalTo(1))
                .body("content.size()", is(2))
                .body("content[0].userId", equalTo(2))
                .body("content[0].postId", equalTo(1))
                .body("content[0].reactionType", equalTo(firstReaction))
                .body("content[0].ownerId", equalTo(2))
                .body("content[0].ownerName", equalTo("samplejunlast"))
                .body("content[1].userId", equalTo(1))
                .body("content[1].postId", equalTo(1))
                .body("content[1].reactionType", equalTo(secondReaction))
                .body("content[1].ownerId", equalTo(1))
                .body("content[1].ownerName", equalTo("samplejunlast"));
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
        var userId = addUserResponse.path("userId").asLong();
        Assertions.assertNotEquals(-1, userId, message);

        var addPostResponse = registerPost(requestSpecification, userId, "First Post through Automation Testing").extract().as(JsonNode.class);
        message = addPostResponse.path("message").asText();
        var postId = addPostResponse.path("postId").asInt();
        Assertions.assertNotEquals(-1, postId, message);

        var reactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.LIKE);
        reactToPost(requestSpecification, userId, reactionDTO).extract().as(JsonNode.class);

        var newReactionDTO = new ReactionDTO().setPostId(postId).setReactionType(ReactionType.ANGRY);
        var newReaction = newReactionDTO.getReactionType().name();

        reactToPost(requestSpecification, userId, newReactionDTO)
                .body("$", aMapWithSize(3))
                .body("reactionType", is(newReaction))
                .body("message", is("Reaction Modified Successfully"))
                .body("postId", is(postId));

        getReactions(requestSpecification, userId, postId)
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
    }
}
