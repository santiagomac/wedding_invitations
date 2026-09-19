-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS roles
(
    id   UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS users
(
    id       UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    email    VARCHAR          NOT NULL UNIQUE,
    password VARCHAR,
    role_id  UUID             NOT NULL,
    enabled  bool             NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS sessions
(
    access_token  VARCHAR NOT NULL PRIMARY KEY,
    refresh_token VARCHAR NOT NULL,
    user_id       UUID    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS invitations
(
    id               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    code             VARCHAR          NOT NULL UNIQUE,
    display_name     VARCHAR          NOT NULL,
    max_guests       INT              NOT NULL,
    status           VARCHAR          NOT NULL,
    confirmed_guests INT,
    responded_at     TIMESTAMP,
    created_at       TIMESTAMP        NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP        NOT NULL DEFAULT now()
);