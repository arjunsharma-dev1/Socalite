package com.socalite.scoalite;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpHeaders;
import org.flywaydb.core.Flyway;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TestContainer {

    @LocalServerPort
    private int port;

    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer<>("postgres:14");
//            .withPassword("12345")
//                .withUsername("testing")
//                .withDatabaseName("socialize");


    @Autowired
    private Flyway flyway;

    @DynamicPropertySource
    static void addProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        Startables.deepStart(postgresContainer).join();

        dynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "none");


        dynamicPropertyRegistry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.flyway.user", postgresContainer::getUsername);
        dynamicPropertyRegistry.add("spring.flyway.password", postgresContainer::getPassword);
        dynamicPropertyRegistry.add("spring.flyway.enabled", () -> true);
        dynamicPropertyRegistry.add("spring.flyway.cleanDisabled", () -> false);
    }

    protected RequestSpecification requestSpecification;

    @BeforeEach
    void setup() {

        flyway.migrate();

        requestSpecification = new RequestSpecBuilder()
                .setPort(port)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @AfterEach
    void tearDown() {
        flyway.clean();
    }
}
