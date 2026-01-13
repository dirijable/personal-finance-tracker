--liquibase formatted sql

--changeset dirijable:1
ALTER TABLE users
RENAME COLUMN username TO name;

