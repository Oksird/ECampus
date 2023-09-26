CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DELETE FROM users;

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


INSERT INTO university.public.users (user_id, user_type, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'Teacher', 'John', 'Doe', 'email1@gmail.com', 'TeacherS!123'),
       (uuid_generate_v4(), 'Teacher', 'Emily', 'Johnson', 'email2@gmail.com', 'pD!ssword123'),
       (uuid_generate_v4(), 'Teacher', 'Michael', 'Smith', 'email3@gmail.com', 'hellG!o123'),
       (uuid_generate_v4(), 'Teacher', 'Olivia', 'Williams', 'email4@gmail.com', 'teacAS!her456'),
       (uuid_generate_v4(), 'Teacher', 'William', 'Brown', 'email5@gmail.com', 'password4D1!56'),
       (uuid_generate_v4(), 'Teacher', 'Sophia', 'Jones', 'email6@gmail.com', 'teaG!cher789'),
       (uuid_generate_v4(), 'Teacher', 'James', 'Davis', 'email7@gmail.com', 'passwoG2rd789'),
       (uuid_generate_v4(), 'Teacher', 'Ava', 'Martinez', 'email8@gmail.com', 'teacherH31!01'),
       (uuid_generate_v4(), 'Teacher', 'Benjamin', 'Anderson', 'email9@gmail.com', 'pass!H3word101'),
       (uuid_generate_v4(), 'Teacher', 'Mia', 'Taylor', 'email10@gmail.com', 'teacher20!FA!2');


INSERT INTO public.users (user_id, user_type, first_name, last_name, email, password)
VALUES (uuid_generate_v4(), 'Student', 'Emma', 'Smith', 'es1@gmail.com', 'student123'),
       (uuid_generate_v4(), 'Student', 'Liam', 'Johnson', 'es2@gmail.com', 'password123'),
       (uuid_generate_v4(), 'Student', 'Oliver', 'Williams', 'es3@gmail.com', 'student456'),
       (uuid_generate_v4(), 'Student', 'Ava', 'Brown', 'es4@gmail.com', 'password456'),
       (uuid_generate_v4(), 'Student', 'Sophia', 'Jones', 'es5@gmail.com', 'student789'),
       (uuid_generate_v4(), 'Student', 'Noah', 'Davis', 'es6@gmail.com', 'password789'),
       (uuid_generate_v4(), 'Student', 'Isabella', 'Martinez', 'es7@gmail.com', 'student101'),
       (uuid_generate_v4(), 'Student', 'Ethan', 'Anderson', 'es8@gmail.com', 'password101'),
       (uuid_generate_v4(), 'Student', 'Mia', 'Taylor', 'es9@gmail.com', 'student202'),
       (uuid_generate_v4(), 'Student', 'Aiden', 'Miller', 'es10@gmail.com', 'password202');

UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es1@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es2@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03')
WHERE email = 'es3@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es4@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es5@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es6@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es7@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-01')
WHERE email = 'es8@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-03')
WHERE email = 'es9@gmail.com';
UPDATE users
SET group_id=(SELECT group_id FROM groups WHERE group_name = 'AA-02')
WHERE email = 'es10@gmail.com';

INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es1@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es2@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es3@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es4@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es5@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es6@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es7@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es8@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es9@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO students_courses VALUES ((SELECT user_id FROM users WHERE email = 'es10@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));

INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email1@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email2@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email3@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email4@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email5@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email6@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email7@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email8@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course2'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email9@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));
INSERT INTO teachers_courses VALUES ((SELECT user_id FROM users WHERE email = 'email10@gmail.com'), (SELECT course_id FROM courses WHERE course_name = 'Course1'));

INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'email1@gmail.com'), '2023-09-06', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'email2@gmail.com'), '2023-09-06', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT user_id FROM users WHERE email = 'email1@gmail.com'), '2023-09-06', '10:30:00', '11:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'email2@gmail.com'), '2023-09-07', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'email3@gmail.com'), '2023-09-07', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT user_id FROM users WHERE email = 'email1@gmail.com'), '2023-09-07', '10:30:00', '11:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'email4@gmail.com'), '2023-09-08', '08:30:00', '09:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'email1@gmail.com'), '2023-09-08', '09:30:00', '10:30:00');
INSERT INTO Lessons (lesson_id, course_id, group_id, teacher_id, date, start_time, end_time)
VALUES
    (uuid_generate_v4(), (SELECT course_id FROM courses WHERE course_name = 'Course3'), (SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT user_id FROM users WHERE email = 'email3@gmail.com'), '2023-09-08', '10:30:00', '11:30:00');
