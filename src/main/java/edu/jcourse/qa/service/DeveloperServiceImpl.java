package edu.jcourse.qa.service;

import edu.jcourse.qa.dto.DeveloperDto;
import edu.jcourse.qa.entity.Status;
import edu.jcourse.qa.exception.ApiException;
import edu.jcourse.qa.mapper.DeveloperMapper;
import edu.jcourse.qa.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final DeveloperMapper mapper;

    @Override
    public Mono<DeveloperDto> findByEmail(String email) {
        return developerRepository.findByEmail(email)
                .map(mapper::toDto);
    }

    @Override
    public Flux<DeveloperDto> findAllActiveBySpecialty(String specialty) {
        return developerRepository.findAllActiveBySpeciality(specialty)
                .map(mapper::toDto);
    }

    @Override
    public Mono<DeveloperDto> save(DeveloperDto developerDto) {
        return developerRepository.save(mapper.toEntity(developerDto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<DeveloperDto> update(Long id, DeveloperDto developerDto) {
        return developerRepository.findById(id)
                .map(entity -> mapper.copyDtoToEntity(developerDto, entity))
                .flatMap(developerRepository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<DeveloperDto> findById(Long id) {
        return developerRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public Flux<DeveloperDto> findAll() {
        return developerRepository.findAll()
                .map(mapper::toDto);
    }

    @Override
    public Mono<Boolean> softDeleteById(Long id) {
        return developerRepository.findById(id)
                .flatMap(entity -> {
                    entity.setStatus(Status.DELETED);
                    return developerRepository.save(entity)
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> hardDeleteById(Long id) {
        return developerRepository.findById(id)
                .flatMap(entity ->
                        developerRepository.delete(entity)
                                .thenReturn(true))
                .defaultIfEmpty(false);
    }
}