DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS groups;

CREATE TABLE IF NOT EXISTS groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS students
(
    student_id SERIAL PRIMARY KEY,
    group_id   integer REFERENCES groups (group_id),
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        varchar(50) UNIQUE NOT NULL,
    course_description varchar(500) NOT NULL
);
