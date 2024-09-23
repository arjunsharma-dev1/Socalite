package com.socalite.scoalite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socalite.scoalite.comment.CommentRequestDTO;
import com.socalite.scoalite.reaction.controller.ReactionDTO;
import com.socalite.scoalite.user.dto.RegisterUserRequestDTO;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.net.URI;
import java.util.StringJoiner;

public interface TestUtils {

    static RegisterUserRequestDTO getSecondRegisterUserDTO() {
        return new RegisterUserRequestDTO()
                .setEmailAddress("sampleOne@gmail.com")
                .setUsername("sampleOne")
                .setFirstName("sample")
                .setLastName("last")
                .setMiddleName("jun")
                .setPhoneNumber("9834587280");
    }

    static RegisterUserRequestDTO getFirstRegisterUserDTO() {
        return new RegisterUserRequestDTO()
                .setEmailAddress("sample@gmail.com")
                .setUsername("sample")
                .setFirstName("sample")
                .setLastName("last")
                .setMiddleName("jun")
                .setPhoneNumber("9834587281");
    }

    static ValidatableResponse registerUser(RequestSpecification requestSpecification,
                                            RegisterUserRequestDTO registerUserRequestDTO) {
        return RestAssured.given(requestSpecification)
                .body(String.format("""
						{
							"firstName": "%s",
							"middleName": "%s",
							"lastName": "%s",
							"username": "%s",
							"emailAddress": "%s",
							"phoneNumber": "%s"
						}
						""",
                        registerUserRequestDTO.getFirstName(),
                        registerUserRequestDTO.getMiddleName(),
                        registerUserRequestDTO.getLastName(),
                        registerUserRequestDTO.getUsername(),
                        registerUserRequestDTO.getEmailAddress(),
                        registerUserRequestDTO.getPhoneNumber()))
                .when()
                    .post(URI.create("users"))
                .then();
    }

    static ValidatableResponse registerPost(RequestSpecification requestSpecification,
                                            long userId,
                                            String postContent) {
        return RestAssured.given(requestSpecification)
                .header("X_LOGIN", userId)
                .body(String.format("""
							{
								"content": "%s"
							}
							""", postContent))
                .when()
                .post(URI.create("posts"))
                .then();
    }

    static ValidatableResponse reactToPost(RequestSpecification requestSpecification,
                                           long userId,
                                           ReactionDTO reactionDTO) {
        return RestAssured.given(requestSpecification)
                .header("X_LOGIN", userId)
                .body(String.format("""
							{
								"postId": %d,
								"reactionType": "%s"
							}
							""", reactionDTO.getPostId(), reactionDTO.getReactionType().name()))
                .when()
                .post(URI.create("reactions"))
                .then();
    }

    static ValidatableResponse getReactions(RequestSpecification requestSpecification,
                                            long userId,
                                            long postId) {
        var reactionsURL = new StringJoiner("/")
                .add("reactions")
                .add("posts")
                .add(String.valueOf(postId))
                .add("reactions")
                .toString();


        return RestAssured.given(requestSpecification)
                .header("X_LOGIN", userId)
                .when()
                .get(URI.create(reactionsURL))
                .then();
    }

    static ValidatableResponse addComment(RequestSpecification requestSpecification,
                                          ObjectMapper objectMapper,
                                          long userId,
                                          CommentRequestDTO commentRequestDTO) throws JsonProcessingException {
        return RestAssured
                .given(requestSpecification)
                    .header("X_LOGIN", userId)
                    .body(objectMapper.writeValueAsString(commentRequestDTO))
                .when()
                    .post(URI.create("comments"))
                .then();
    }

    static ValidatableResponse getComments(RequestSpecification requestSpecification, long userId, long postId) {
        return RestAssured
                .given(requestSpecification)
                    .header("X_LOGIN", userId)
                    .queryParam("postId", postId)
                .when()
                    .get(URI.create("comments"))
                .then();
    }

    static ValidatableResponse deleteComment(RequestSpecification requestSpecification, long userId, int commentId) {
        return RestAssured
                .given(requestSpecification)
                    .header("X_LOGIN", userId)
                .when()
                    .delete(URI.create(String.format("comments/%s", commentId)))
                .then();
    }

    static ValidatableResponse updateComment(RequestSpecification rs,
                                             ObjectMapper objectMapper,
                                             CommentUpdateRequestDTO commentUpdateRequestDTO,
                                             int userId) throws JsonProcessingException {
        return RestAssured
                .given(rs)
                    .header("X_LOGIN", userId)
                    .body(objectMapper.writeValueAsString(commentUpdateRequestDTO))
                .when()
                    .put(URI.create("comments"))
                .then();

    }

    static ValidatableResponse getPosts(RequestSpecification requestSpecification, long userId) {
        return RestAssured
                .given(requestSpecification)
                    .header("X_LOGIN", userId)
                    .queryParam("connectedUserId", userId)
                .when()
                    .get(URI.create("posts"))
                .then();
    }
}
