--liquibase formatted sql

--changeset dirijable:1
CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(64) UNIQUE NOT NULL,
    password    TEXT               NOT NULL,
    username    VARCHAR(32)        NOT NULL,
    created_at  TIMESTAMPTZ,
    modified_at TIMESTAMPTZ
);
--rollback DROP TABLE users;

--changeset dirijable:2
CREATE TABLE account
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(64) UNIQUE                             NOT NULL,
    description VARCHAR(500),
    balance     NUMERIC(19, 2)                                 NOT NULL DEFAULT 0,
    currency    VARCHAR(3)                                     NOT NULL,
    user_id     BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    created_at  TIMESTAMPTZ,
    modified_at TIMESTAMPTZ
);
--rollback DROP TABLE account;

--changeset dirijable:3
CREATE TABLE category
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(500)                                   NOT NULL,
    description VARCHAR(500),
    user_id     BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    created_at  TIMESTAMPTZ,
    modified_at TIMESTAMPTZ,
    CONSTRAINT uk_category_name_user UNIQUE (name, user_id)
);
--rollback DROP TABLE category;

--changeset dirijable:4
CREATE TABLE transaction
(
    id               BIGSERIAL PRIMARY KEY,
    amount           NUMERIC(19, 2)                                   NOT NULL CHECK ( amount > 0 ),
    description      VARCHAR(500),
    transaction_date TIMESTAMPTZ                                      NOT NULL,
    transaction_type VARCHAR(32)                                      NOT NULL,
    account_id       BIGINT REFERENCES account (id) ON DELETE CASCADE NOT NULL,
    category_id      BIGINT                                           REFERENCES category (id) ON DELETE CASCADE NOT NULL,
    created_at       TIMESTAMPTZ,
    modified_at      TIMESTAMPTZ
);
--rollback DROP TABLE transaction;