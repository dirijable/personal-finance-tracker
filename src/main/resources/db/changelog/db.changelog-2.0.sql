--liquibase formatted sql

--changeset dirijable:1
CREATE TABLE refresh_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR,
    expiry_date TIMESTAMPTZ,
    user_id     BIGINT REFERENCES users (id)
);
--rollback DROP TABLE refresh_token;
