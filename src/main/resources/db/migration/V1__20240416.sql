DROP TABLE IF EXISTS developer;

CREATE TABLE IF NOT EXISTS developer
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    speciality  VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    check (status = 'ACTIVE' OR status = 'DELETED')
);