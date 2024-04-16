package edu.jcourse.qa.service;

import edu.jcourse.qa.dto.DeveloperDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeveloperService extends Service<DeveloperDto, Long> {
    Mono<DeveloperDto> findByEmail(String email);

    Flux<DeveloperDto> findAllActiveBySpecialty(String specialty);
}