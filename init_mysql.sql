CREATE USER 'stackoverflow'@'%' IDENTIFIED BY 'stackoverflow';
CREATE DATABASE stackoverflow;

CREATE TABLE stackoverflow.Post (
id              INTEGER         PRIMARY KEY,
title           VARCHAR(256)    NOT NULL,
body            TEXT            NOT NULL,
accepted_answer INTEGER         NOT NULL,
creation_date   TIMESTAMP       NOT NULL
);

GRANT ALL ON stackoverflow.* TO 'stackoverflow'@'%' WITH GRANT OPTION;