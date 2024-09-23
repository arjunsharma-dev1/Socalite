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
import java.util.Map;
import java.util.StringJoiner;

import static com.socalite.scoalite.TestUtils.*;
import static org.hamcrest.Matchers.*;

public class ScoaliteApplicationTests extends TestContainer {

	@Test
	public void contextLoads() {
	}
}
