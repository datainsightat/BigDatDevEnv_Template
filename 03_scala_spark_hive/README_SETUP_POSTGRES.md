# Setup Postgres

## Setup Server

[PGAdmin](http://localhost:5050/)

    General:
        Name: newserver
    Connection:
        Host_Name: pg_container
        Port: 5432
        Username: root
        Password: root

## Create DB Structure

    --Create Database 'newdb'
    CREATE DATABASE newdb
    WITH 
    OWNER = root
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

    --Create Schema 'newschema'
    CREATE SCHEMA IF NOT EXISTS newschema
    AUTHORIZATION root;

    --Create Table 'course_catalog'
    CREATE TABLE IF NOT EXISTS newschema.course_catalog
    (
        course_id integer NOT NULL,
        course_name character varying COLLATE pg_catalog."default" NOT NULL,
        author_name character varying COLLATE pg_catalog."default" NOT NULL,
        course_section json NOT NULL,
        creation_date date NOT NULL,
        CONSTRAINT futurex_course_catalog_pkey PRIMARY KEY (course_id)
    )

    TABLESPACE pg_default;

    ALTER TABLE IF EXISTS newschema.course_catalog
        OWNER to root;

    --Insert Data into Table 'course_catalog'
    INSERT INTO newschema.course_catalog(
        course_id, course_name, author_name, course_section, creation_date)
        VALUES (?, ?, ?, ?, ?);
