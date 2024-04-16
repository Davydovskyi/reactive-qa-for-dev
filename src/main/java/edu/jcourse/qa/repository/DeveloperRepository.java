package edu.jcourse.qa.repository;

import edu.jcourse.qa.entity.Developer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeveloperRepository extends R2dbcRepository<Developer, Long> {

    Mono<Developer> findByEmail(String email);

    @Query("SELECT d FROM Developer d WHERE d.status = 'ACTIVE' AND d.speciality = :speciality")
    Flux<Developer> findAllActiveBySpeciality(String speciality);
}