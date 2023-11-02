TRUNCATE students_courses;
TRUNCATE teachers_courses;
TRUNCATE users CASCADE;
TRUNCATE lessons;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO public.users (user_id, role, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'ROLE_ADMIN', 'John', 'Smith', 'john.smith@example.com', 'admin1_password'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'Emma', 'Johnson', 'emma.johnson@example.com', 'admin2_password'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'Michael', 'Williams', 'michael.williams@example.com', 'admin3_password'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'Olivia', 'Brown', 'olivia.brown@example.com', 'admin4_password'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'William', 'Jones', 'william.jones@example.com', 'admin5_password');

INSERT INTO public.users (user_id, role, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'ROLE_TEACHER', 'John', 'Doe', 'et1', 'teacher123'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Emily', 'Johnson', 'et2', 'password123'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Michael', 'Smith', 'et3', 'hello123'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Olivia', 'Williams', 'et4', 'teacher456'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'William', 'Brown', 'et5', 'password456'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Sophia', 'Jones', 'et6', 'teacher789'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'James', 'Davis', 'et7', 'password789'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Ava', 'Martinez', 'et8', 'teacher101'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Benjamin', 'Anderson', 'et9', 'password101'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Mia', 'Taylor', 'et10', 'teacher202');

INSERT INTO public.users (user_id, role, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'ROLE_STUDENT', 'Emma', 'Smith', 'es1', 'student123'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Liam', 'Johnson', 'es2', 'password123'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Oliver', 'Williams', 'es3', 'student456'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Ava', 'Brown', 'es4', 'password456'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Sophia', 'Jones', 'es5', 'student789'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Noah', 'Davis', 'es6', 'password789'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Isabella', 'Martinez', 'es7', 'student101'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Ethan', 'Anderson', 'es8', 'password101'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Mia', 'Taylor', 'es9', 'student202'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Aiden', 'Miller', 'es10', 'password202');

UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es1';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es2';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03')
WHERE email = 'es3';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es4';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es5';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es6';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es7';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es8';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03')
WHERE email = 'es9';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es10';

INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es1'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es2'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es3'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es4'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es5'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es6'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es7'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es8'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es9'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es10'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));

INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et1'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et2'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et3'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et4'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et5'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et6'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et7'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et8'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et9'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'et10'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));

INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'et1'), '2023-09-06', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'et1'), '2023-09-06', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'et2'), '2023-09-06', '10:30:00', '11:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'et2'), '2023-09-07', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'et3'), '2023-09-07', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'et3'), '2023-09-07', '10:30:00', '11:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'et4'), '2023-09-08', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'et4'), '2023-09-08', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'et5'), '2023-09-08', '10:30:00', '11:30:00');

INSERT INTO public.users (user_id, role, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'ROLE_STAFF', 'Max', 'Pop', 'esf1@gmail.com', 'student1223'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Leam', 'Johnson', 'esf2', 'password1323'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Opel', 'Williams', 'esf3', 'student4456'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Sim', 'Brown', 'esf4', 'password4556'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Sam', 'Jones', 'esf5', 'student7869'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Son', 'Davis', 'esf6', 'password7789'),
       (uuid_generate_v4(), 'ROLE_PENDING', 'Lee', 'Martinez', 'eps1@gmail.com', 'student101'),
       (uuid_generate_v4(), 'ROLE_PENDING', 'Admir', 'Anderson', 'ep2', 'password101'),
       (uuid_generate_v4(), 'ROLE_PENDING', 'Ihor', 'Taylor', 'ep3', 'student202'),
       (uuid_generate_v4(), 'ROLE_PENDING', 'Alex', 'Miller', 'ep4', 'password202');