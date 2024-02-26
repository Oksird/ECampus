CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS groups
(
    group_id   uuid                                                NOT NULL,
    group_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
);

CREATE TABLE IF NOT EXISTS public.users
(
    user_id      uuid                                                NOT NULL,
    role         character varying(20) COLLATE pg_catalog."default"  NOT NULL,
    first_name   character varying(100) COLLATE pg_catalog."default" NOT NULL,
    last_name    character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password     character varying(255) COLLATE pg_catalog."default" NOT NULL,
    group_id     uuid,
    phone_number character varying(20) COLLATE pg_catalog."default",
    address      character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT unique_phone_number UNIQUE (phone_number),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_group_id_fkey FOREIGN KEY (group_id)
        REFERENCES public.groups (group_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS courses
(
    course_id          uuid                                                NOT NULL,
    course_name        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    course_description text COLLATE pg_catalog."default",
    teacher_id         uuid                                                NOT NULL,
    CONSTRAINT courses_pkey PRIMARY KEY (course_id),
    CONSTRAINT fk_teacher FOREIGN KEY (teacher_id)
        REFERENCES users (user_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS groups_courses
(
    group_id  uuid NOT NULL,
    course_id uuid NOT NULL,
    CONSTRAINT groups_courses_pkey PRIMARY KEY (group_id, course_id),
    CONSTRAINT groups_courses_course_id_fkey FOREIGN KEY (course_id)
        REFERENCES public.courses (course_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT groups_courses_group_id_fkey FOREIGN KEY (group_id)
        REFERENCES public.groups (group_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE lesson_times
(
    id            UUID PRIMARY KEY,
    lesson_number INT,
    start_time    TIME
);

CREATE TABLE lesson_types
(
    id          UUID PRIMARY KEY,
    lesson_type character varying(255)
);

CREATE TABLE study_days
(
    id          UUID PRIMARY KEY,
    day_of_week character varying(255)
);

CREATE TABLE study_weeks
(
    id          UUID PRIMARY KEY,
    week_number character varying(255)
);

CREATE TABLE lessons
(
    id              UUID PRIMARY KEY,
    lesson_type_id  UUID REFERENCES lesson_types (id),
    course_id       UUID REFERENCES courses (course_id),
    group_id        UUID REFERENCES groups (group_id),
    study_day_id    UUID REFERENCES study_days (id),
    study_week_id   UUID REFERENCES study_weeks (id),
    lesson_time_id  UUID REFERENCES lesson_times (id),
    additional_info TEXT
);

CREATE TABLE IF NOT EXISTS request_types
(
    id   UUID NOT NULL,
    type character varying(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS request_statuses
(
    id     UUID NOT NULL,
    status character varying(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_requests
(
    id      UUID NOT NULL,
    user_id UUID NOT NULL,
    type_id UUID NOT NULL,
    status_id  UUID NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (type_id) REFERENCES request_types (id),
    FOREIGN KEY (status_id) REFERENCES request_statuses(id)
);
