package com.socalite.scoalite;

import com.fasterxml.jackson.databind.JsonNode;
import com.socalite.scoalite.reaction.controller.ReactionDTO;
import com.socalite.scoalite.reaction.model.ReactionType;
import com.socalite.scoalite.user.dto.RegisterUserRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.StringJoiner;

import static com.socalite.scoalite.TestUtils.*;
import static org.hamcrest.Matchers.*;

@Log4j2
public class ScoaliteApplicationTests extends TestContainer {

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPosts() {
		var userId = registerUser(requestSpecification, getFirstRegisterUserDTO())
				.extract()
				.as(JsonNode.class)
				.path("userId")
				.asInt();

		var postId = registerPost(requestSpecification, userId, "First Post through Automation Testing")
				.extract()
				.as(JsonNode.class)
				.path("postId")
				.asInt();

		var posts = RestAssured.given(requestSpecification)
					.header("X_LOGIN", userId)
					.queryParam("connectedUserId", userId)
				.when()
					.get(URI.create("posts"))
				.then()
					.statusCode(HttpStatus.SC_OK)
					.body("pageNumber", equalTo(1))
					.body("pageSize", equalTo(10))
					.body("totalElementsCount", equalTo(1))
					.body("totalNumberOfPages", equalTo(1))
					.body("empty", equalTo(false))
					.body("content.size()", equalTo(1)) // Ensure content array has exactly one item
					.body("content[0].id", equalTo(postId))
					.body("content[0].content", equalTo("First Post through Automation Testing"))
					.body("content[0].likesCount", empty())
					.body("content[0].commentsCount", equalTo(0))
					.body("content[0].ownerId", equalTo(userId))
					.body("content[0].userName", equalTo("samplejunlast"));;

		log.atInfo().log(posts);
	}

	@Test
	public void addPost() {
		var addUserResponse = registerUser(requestSpecification, getFirstRegisterUserDTO()).extract().as(JsonNode.class);

		var message = addUserResponse.path("message").asText();
		var userId = addUserResponse.path("userId").asLong();
		Assertions.assertNotEquals(-1, userId, message);
		/*addUserResponse
				.statusCode(HttpStatus.SC_OK)
				.body(Matchers.not(Matchers.blankString()))
				.body(Matchers.is(Matchers.matchesPattern("[0-9]+")));*/

		registerPost(requestSpecification, userId, "First Post through Automation Testing")
				.statusCode(HttpStatus.SC_OK)
				.body("message", equalTo("Success"))
				.body("postId", not(equalTo(-1)));
	}

	@Test
	public void userWithDuplicateUsername() {
		var user = getFirstRegisterUserDTO();

		registerUser(requestSpecification, user);
		registerUser(requestSpecification, user)
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.body("message", equalTo("Duplicate Username"))
				.body("userId", equalTo(-1));
	}

	@Test
	public void userWithDuplicateEmailAddress() {
		var user = getFirstRegisterUserDTO();

		registerUser(requestSpecification, user);

		user.setUsername(String.format("%s123", user.getUsername()));

		registerUser(requestSpecification, user)
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.body("message", equalTo("Duplicate Email"))
				.body("userId", equalTo(-1));
	}

	@Test
	public void userWithDuplicatePhoneNumber() {
		var user = getFirstRegisterUserDTO();

		registerUser(requestSpecification, user);

		user.setUsername(String.format("%s123", user.getUsername()));
		user.setEmailAddress(String.format("jam%s", user.getEmailAddress()));

		registerUser(requestSpecification, user)
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.body("message", equalTo("Duplicate Phone Number"))
				.body("userId", equalTo(-1));
	}

}
