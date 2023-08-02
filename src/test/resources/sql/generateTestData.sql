CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO public.courses (course_id, course_name, course_description)
VALUES (uuid_generate_v4(), 'Course1', 'This course covers various mathematical topics.'),
       (uuid_generate_v4(), 'Course2', 'Explore classic and modern works of English literature.'),
       (uuid_generate_v4(), 'Course3', 'Learn programming and computer science fundamentals.');


INSERT INTO public.groups (group_id, group_name)
VALUES (uuid_generate_v4(), 'AA-01'),
       (uuid_generate_v4(), 'AA-02'),
       (uuid_generate_v4(), 'AA-03');

INSERT INTO public.users (user_id, user_type, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'Admin', 'John', 'Smith', 'john.smith@example.com', 'admin1_password'),
       (uuid_generate_v4(), 'Admin', 'Emma', 'Johnson', 'emma.johnson@example.com', 'admin2_password'),
       (uuid_generate_v4(), 'Admin', 'Michael', 'Williams', 'michael.williams@example.com', 'admin3_password'),
       (uuid_generate_v4(), 'Admin', 'Olivia', 'Brown', 'olivia.brown@example.com', 'admin4_password'),
       (uuid_generate_v4(), 'Admin', 'William', 'Jones', 'william.jones@example.com', 'admin5_password');

INSERT INTO public.users (user_id, user_type, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'Teacher', 'John', 'Doe', 'et1', 'teacher123'),
       (uuid_generate_v4(), 'Teacher', 'Emily', 'Johnson', 'et2', 'password123'),
       (uuid_generate_v4(), 'Teacher', 'Michael', 'Smith', 'et3', 'hello123'),
       (uuid_generate_v4(), 'Teacher', 'Olivia', 'Williams', 'et4', 'teacher456'),
       (uuid_generate_v4(), 'Teacher', 'William', 'Brown', 'et5', 'password456'),
       (uuid_generate_v4(), 'Teacher', 'Sophia', 'Jones', 'et6', 'teacher789'),
       (uuid_generate_v4(), 'Teacher', 'James', 'Davis', 'et7', 'password789'),
       (uuid_generate_v4(), 'Teacher', 'Ava', 'Martinez', 'et8', 'teacher101'),
       (uuid_generate_v4(), 'Teacher', 'Benjamin', 'Anderson', 'et9', 'password101'),
       (uuid_generate_v4(), 'Teacher', 'Mia', 'Taylor', 'et10', 'teacher202');

INSERT INTO public.users (user_id, user_type, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'Student', 'Emma', 'Smith', 'es1', 'student123'),
       (uuid_generate_v4(), 'Student', 'Liam', 'Johnson', 'es2', 'password123'),
       (uuid_generate_v4(), 'Student', 'Oliver', 'Williams', 'es3', 'student456'),
       (uuid_generate_v4(), 'Student', 'Ava', 'Brown', 'es4', 'password456'),
       (uuid_generate_v4(), 'Student', 'Sophia', 'Jones', 'es5', 'student789'),
       (uuid_generate_v4(), 'Student', 'Noah', 'Davis', 'es6', 'password789'),
       (uuid_generate_v4(), 'Student', 'Isabella', 'Martinez', 'es7', 'student101'),
       (uuid_generate_v4(), 'Student', 'Ethan', 'Anderson', 'es8', 'password101'),
       (uuid_generate_v4(), 'Student', 'Mia', 'Taylor', 'es9', 'student202'),
       (uuid_generate_v4(), 'Student', 'Aiden', 'Miller', 'es10', 'password202');

UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'es1';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'es2';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'es3';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'es4';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'es5';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'es6';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'es7';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'es8';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'es9';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02'),
    course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
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

UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'et1';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'et2';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'et3';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'et4';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'et5';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'et6';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'et7';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course2')
WHERE email = 'et8';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course3')
WHERE email = 'et9';
UPDATE users
SET course_id=(SELECT course_id FROM courses WHERE course_name = 'Course1')
WHERE email = 'et10';

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
