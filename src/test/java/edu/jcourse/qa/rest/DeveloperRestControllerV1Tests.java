package edu.jcourse.qa.rest;

import edu.jcourse.qa.dto.DeveloperDto;
import edu.jcourse.qa.entity.Status;
import edu.jcourse.qa.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ComponentScan(basePackages = "edu.jcourse.qa.rest.handler")
@WebFluxTest(controllers = DeveloperRestControllerV1.class)
@RequiredArgsConstructor
class DeveloperRestControllerV1Tests {
    private final WebTestClient webTestClient;
    @MockBean
    private DeveloperService developerService;

    @Test
    @DisplayName("Test create developer when success functionality")
    void createWhenSuccess() {
        DeveloperDto developerDto = buildDeveloperDto(null, "email", Status.ACTIVE);
        DeveloperDto expectedResult = buildDeveloperDto(1L, "email", Status.ACTIVE);
        doReturn(Mono.just(expectedResult)).when(developerService).save(any());

        webTestClient
                .post()
                .uri("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.first_name").isEqualTo("firstName");

        verify(developerService).save(developerDto);
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test create developer when failure functionality")
    void createWhenFailure() {
        DeveloperDto developerDto = buildDeveloperDto(null, "email", Status.ACTIVE);
        doReturn(Mono.empty()).when(developerService).save(any());

        webTestClient
                .post()
                .uri("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.errors[0].status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath("$.errors[0].message").isEqualTo("Developer already exists");

        verify(developerService).save(developerDto);
        verifyNoMoreInteractions(developerService);
    }


    @Test
    @DisplayName("Test update developer when success functionality")
    void updateWhenSuccess() {
        DeveloperDto developerDto = buildDeveloperDto(1L, "email", Status.ACTIVE);
        DeveloperDto expectedResult = buildDeveloperDto(1L, "email2", Status.ACTIVE);
        doReturn(Mono.just(expectedResult)).when(developerService).update(any(), any());

        webTestClient
                .put()
                .uri("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(developerDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.email").isEqualTo("email2");

        verify(developerService).update(1L, developerDto);
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test update developer when failure functionality")
    void updateWhenFailure() {
        DeveloperDto developerDto = buildDeveloperDto(1L, "email", Status.ACTIVE);
        doReturn(Mono.empty()).when(developerService).update(any(), any());

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

        verify(developerService).update(1L, developerDto);
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test find all developers when success functionality")
    void findAllWhenSuccess() {
        DeveloperDto expectedResult = buildDeveloperDto(1L, "email", Status.ACTIVE);
        doReturn(Flux.just(expectedResult)).when(developerService).findAll();

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

        verify(developerService).findAll();
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test hard delete developer when success functionality")
    void deleteWhenSuccess() {
        doReturn(Mono.just(true)).when(developerService).hardDeleteById(any());

        webTestClient
                .delete()
                .uri("/api/v1/developers/1?force=true")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);

        verify(developerService).hardDeleteById(1L);
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test soft delete developer when success functionality")
    void deleteSoftWhenSuccess() {
        doReturn(Mono.just(true)).when(developerService).softDeleteById(any());

        webTestClient
                .delete()
                .uri("/api/v1/developers/1")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);

        verify(developerService).softDeleteById(1L);
        verifyNoMoreInteractions(developerService);
    }

    @Test
    @DisplayName("Test soft delete developer when failure functionality")
    void deleteSoftWhenFailure() {
        doReturn(Mono.just(false)).when(developerService).softDeleteById(any());

        webTestClient
                .delete()
                .uri("/api/v1/developers/1")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(System.out::println);

        verify(developerService).softDeleteById(1L);
        verifyNoMoreInteractions(developerService);
    }

    private DeveloperDto buildDeveloperDto(Long id, String email, Status status) {
        return DeveloperDto.builder()
                .id(id)
                .email(email)
                .firstName("firstName")
                .lastName("lastName")
                .speciality("speciality")
                .status(status)
                .build();
    }
}