DROP TABLE IF EXISTS Students_Courses;
DROP TABLE IF EXISTS Teachers_Courses;
DROP TABLE IF EXISTS teachers_lessons;
DROP TABLE IF EXISTS groups_lessons;
DROP TABLE IF EXISTS Courses;
DROP TABLE IF EXISTS Groups;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS lessons;

CREATE TABLE Courses
(
    course_id          UUID PRIMARY KEY,
    course_name        VARCHAR(255) NOT NULL,
    course_description TEXT
);

CREATE TABLE Groups
(
    group_id   UUID PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE Users
(
    user_id    UUID PRIMARY KEY,
    role  VARCHAR(25) NOT NULL ,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    group_id   UUID
);

CREATE TABLE Teachers_Courses
(
    teacher_id UUID REFERENCES Users (user_id) ON DELETE CASCADE,
    course_id  UUID REFERENCES Courses (course_id) ON DELETE CASCADE,
    PRIMARY KEY (teacher_id, course_id)
);

CREATE TABLE Students_Courses
(
    student_id UUID REFERENCES Users (user_id) ON DELETE CASCADE,
    course_id  UUID REFERENCES Courses (course_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
);

-- Create the Lessons table
CREATE TABLE Lessons (
                         lesson_id UUID PRIMARY KEY,
                         course_id UUID REFERENCES Courses(course_id) ON DELETE CASCADE,
                         group_id UUID REFERENCES public.Groups(group_id) ON DELETE CASCADE,
                         teacher_id UUID REFERENCES Users(user_id) ON DELETE CASCADE,
                         date DATE,
                         start_time TIME,
                         end_time TIME
);
