package edu.jcourse.qa.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Service<T, K> {
    Mono<T> save(T object);

    Mono<T> update(K id, T object);

    Mono<T> findById(K id);

    Flux<T> findAll();

    Mono<Boolean> softDeleteById(K id);

    Mono<Boolean> hardDeleteById(K id);
}