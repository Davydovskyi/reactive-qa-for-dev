package edu.jcourse.qa.rest;

import edu.jcourse.qa.dto.DeveloperDto;
import edu.jcourse.qa.exception.ApiException;
import edu.jcourse.qa.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperRestControllerV1 {
    private final DeveloperService developerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DeveloperDto> create(@RequestBody DeveloperDto developerDto) {
        return developerService.save(developerDto)
                .switchIfEmpty(Mono.error(new ApiException(HttpStatus.BAD_REQUEST, "Developer already exists")));
    }

    @PutMapping("/{id}")
    public Mono<DeveloperDto> update(@PathVariable Long id,
                                     @RequestBody DeveloperDto developerDto) {
        return developerService.update(id, developerDto)
                .switchIfEmpty(Mono.error(new ApiException(HttpStatus.NOT_FOUND, "Developer not found")));
    }

    @GetMapping("/{id}")
    public Mono<DeveloperDto> findById(@PathVariable Long id) {
        return developerService.findById(id)
                .switchIfEmpty(Mono.error(new ApiException(HttpStatus.NOT_FOUND, "Developer not found")));
    }

    @GetMapping
    public Flux<DeveloperDto> findAll() {
        return developerService.findAll();
    }

    @GetMapping("/specialty/{specialty}")
    public Flux<DeveloperDto> findAllActiveBySpecialty(@PathVariable String specialty) {
        return developerService.findAllActiveBySpecialty(specialty);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable Long id,
                                               @RequestParam(defaultValue = "false") boolean force) {
        Mono<Boolean> booleanMono = force ?
                developerService.hardDeleteById(id) :
                developerService.softDeleteById(id);
        return booleanMono
                .flatMap(deleted -> Mono.fromCallable(() -> deleted ?
                        ResponseEntity.noContent().build() :
                        ResponseEntity.notFound().build()));
    }
}