-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS roles
(
    id
    UUID
    PRIMARY
    KEY
    NOT
    NULL
    DEFAULT
    gen_random_uuid
(
),
    name VARCHAR
    );

CREATE TABLE IF NOT EXISTS users
(
    id
    UUID
    PRIMARY
    KEY
    NOT
    NULL
    DEFAULT
    gen_random_uuid
(
),
    email VARCHAR NOT NULL UNIQUE,
    password VARCHAR,
    role_id UUID NOT NULL,
    enabled bool NOT NULL,
    FOREIGN KEY
(
    role_id
) REFERENCES roles
(
    id
)
    );

CREATE TABLE IF NOT EXISTS sessions
(
    access_token
    VARCHAR
    NOT
    NULL
    PRIMARY
    KEY,
    refresh_token
    VARCHAR
    NOT
    NULL,
    user_id
    UUID
    NOT
    NULL,
    FOREIGN
    KEY
(
    user_id
) REFERENCES users
(
    id
)
    );