CREATE TABLE IF NOT EXISTS Courses
(
    course_id          UUID PRIMARY KEY,
    course_name        VARCHAR(255) NOT NULL,
    course_description TEXT
);

CREATE TABLE IF NOT EXISTS Groups
(
    group_id   UUID PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Users
(
    user_id    UUID PRIMARY KEY,
    user_type  VARCHAR(10)  NOT NULL CHECK (user_type IN ('Admin', 'Student', 'Teacher')),
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    course_id  UUID REFERENCES Courses (course_id),
    group_id   UUID REFERENCES Groups (group_id)
);

CREATE OR REPLACE FUNCTION is_teacher(teacher_id UUID) RETURNS BOOLEAN AS
$$
BEGIN
    RETURN EXISTS (SELECT 1 FROM Users WHERE user_id = teacher_id AND user_type = 'Teacher');
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION is_student(student_id UUID) RETURNS BOOLEAN AS
$$
BEGIN
    RETURN EXISTS (SELECT 1 FROM Users WHERE user_id = student_id AND user_type = 'Student');
END;
$$ LANGUAGE plpgsql;


CREATE TABLE IF NOT EXISTS Teachers_Courses
(
    teacher_id UUID REFERENCES Users (user_id) ON DELETE CASCADE,
    course_id  UUID REFERENCES Courses (course_id) ON DELETE CASCADE,
    PRIMARY KEY (teacher_id, course_id),
    CHECK (is_teacher(teacher_id))
);

CREATE TABLE IF NOT EXISTS Students_Courses
(
    student_id UUID REFERENCES Users (user_id) ON DELETE CASCADE,
    course_id  UUID REFERENCES Courses (course_id) ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id),
    CHECK (is_student(student_id))
);

CREATE TABLE IF NOT EXISTS Lessons
(
    lesson_id UUID PRIMARY KEY,
    course_id UUID REFERENCES Courses(course_id) ON DELETE CASCADE,
    group_id UUID REFERENCES public.Groups(group_id) ON DELETE CASCADE,
    teacher_id UUID REFERENCES Users(user_id) ON DELETE CASCADE,
    date DATE,
    start_time TIME,
    end_time TIME
);
