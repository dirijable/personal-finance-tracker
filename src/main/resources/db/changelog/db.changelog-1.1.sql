--liquibase formatted sql

--changeset dirijable:1
ALTER TABLE account DROP CONSTRAINT account_name_key;

--changeset dirijable:2
ALTER TABLE account ADD CONSTRAINT uk_account_name_user UNIQUE (user_id, name);

