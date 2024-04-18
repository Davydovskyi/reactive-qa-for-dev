package edu.jcourse.qa.integration.rest;

import edu.jcourse.qa.config.PostgreTestcontainerConfig;
import edu.jcourse.qa.dto.DeveloperDto;
import edu.jcourse.qa.entity.Developer;
import edu.jcourse.qa.entity.Status;
import edu.jcourse.qa.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgreTestcontainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@RequiredArgsConstructor
class DeveloperRestControllerV1IT {

    private final WebTestClient webTestClient;
    private final DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        developerRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Test create developer when success functionality")
    void createWhenSuccess() {
        DeveloperDto developerDto = buildDeveloperDto(null, "email");

        webTestClient
                .post()
                .uri("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.email").isEqualTo("email");
    }

    @Test
    @DisplayName("Test update developer when success functionality")
    void updateWhenSuccess() {
        Developer developer = buildDeveloper();
        developerRepository.save(developer).block();
        DeveloperDto developerDto = buildDeveloperDto(developer.getId(), "email2");

        webTestClient
                .put()
                .uri("/api/v1/developers/{id}", developer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(developer.getId())
                .jsonPath("$.email").isEqualTo("email2");
    }

    @Test
    @DisplayName("Test update developer when failure functionality")
    void updateWhenFailure() {
        DeveloperDto developerDto = buildDeveloperDto(1L, "email");

        webTestClient
                .put()
                .uri("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.errors[0].status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.errors[0].message").isEqualTo("Developer not found");
    }

    @Test
    @DisplayName("Test find all developers when success functionality")
    void findAllWhenSuccess() {
        developerRepository.save(buildDeveloper()).block();

        webTestClient
                .get()
                .uri("/api/v1/developers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.size()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].email").isEqualTo("email");
    }

    @Test
    @DisplayName("Test hard delete developer when success functionality")
    void deleteHardWhenSuccess() {
        Developer developer = buildDeveloper();
        developerRepository.save(developer).block();

        webTestClient
                .delete()
                .uri("/api/v1/developers/{id}?force=true", developer.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("Test soft delete developer when success functionality")
    void deleteSoftWhenSuccess() {
        Developer developer = buildDeveloper();
        developerRepository.save(developer).block();

        webTestClient
                .delete()
                .uri("/api/v1/developers/{id}", developer.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

    @Test
    @DisplayName("Test soft delete developer when failure functionality")
    void deleteSoftWhenFailure() {
        webTestClient
                .delete()
                .uri("/api/v1/developers/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println);
    }

    private DeveloperDto buildDeveloperDto(Long id, String email) {
        return DeveloperDto.builder()
                .id(id)
                .email(email)
                .firstName("firstName")
                .lastName("lastName")
                .speciality("speciality")
                .status(Status.ACTIVE)
                .build();
    }

    private Developer buildDeveloper() {
        return Developer.builder()
                .id(null)
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .speciality("speciality")
                .status(Status.ACTIVE)
                .build();
    }
}