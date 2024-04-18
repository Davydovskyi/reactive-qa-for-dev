## Reactive QA for DEV

### Технологии

- Java 21
- Spring Boot 3
- Spring WebFlux
- Spring R2DBC
- Lombok
- JUnit
- Flyway
- Mockito
- PostgreSQL
- Testcontainers
- Mapstruct

#### STEP 1:

Базовое REST API с логикой работы с сущностью DeveloperEntity

#### STEP 2:

Добавлена логика обработки ошибок с использованием:

- DefaultErrorAttributes
- AbstractErrorWebExceptionHandler

#### STEP 3:

Добавлены юнит тесты для слоя контроллеров с использованием:

- Mockito
- Аннотации @WebFluxTest

#### STEP 4:

Добавлены интеграционные тесты для слоя контроллеров с использованием:

- Testcontainers
- Аннотации @SpringBootTest