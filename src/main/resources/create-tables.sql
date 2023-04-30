DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS student_courses CASCADE;

CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   integer REFERENCES groups (group_id),
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL
);

CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        varchar(50) UNIQUE NOT NULL,
    course_description varchar(500) NOT NULL
);

CREATE TABLE student_courses
(
    student_id integer REFERENCES students (student_id),
    course_id integer REFERENCES courses (course_id),
    PRIMARY KEY (student_id, course_id)
);