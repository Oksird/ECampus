CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE lesson_times
    ALTER COLUMN lesson_number TYPE character varying(255);

INSERT INTO groups (group_id, group_name)
VALUES (uuid_generate_v4(), 'AB-35'),
       (uuid_generate_v4(), 'QA-01'),
       (uuid_generate_v4(), 'JS-52');

INSERT INTO lesson_times (id, lesson_number, start_time)
VALUES (uuid_generate_v4(), 'FIRST', '08:30:00'),
       (uuid_generate_v4(), 'SECOND', '10:25:00'),
       (uuid_generate_v4(), 'THIRD', '12:20:00'),
       (uuid_generate_v4(), 'FOURTH', '14:15:00'),
       (uuid_generate_v4(), 'FIFTH', '16:10:00'),
       (uuid_generate_v4(), 'SIXTH', '18:30:00');


INSERT INTO lesson_types (id, lesson_type)
VALUES (uuid_generate_v4(), 'LECTURE'),
       (uuid_generate_v4(), 'LABORATORY_WORK'),
       (uuid_generate_v4(), 'PRACTICE'),
       (uuid_generate_v4(), 'SEMINAR');


INSERT INTO study_days (id, day_of_week)
VALUES (uuid_generate_v4(), 'MONDAY'),
       (uuid_generate_v4(), 'TUESDAY'),
       (uuid_generate_v4(), 'WEDNESDAY'),
       (uuid_generate_v4(), 'THURSDAY'),
       (uuid_generate_v4(), 'FRIDAY'),
       (uuid_generate_v4(), 'SATURDAY');


INSERT INTO study_weeks (id, week_number)
VALUES (uuid_generate_v4(), 'FIRST'),
       (uuid_generate_v4(), 'SECOND');

INSERT INTO request_types (id, type)
VALUES (uuid_generate_v4(), 'BECOME_STUDENT'),
       (uuid_generate_v4(), 'BECOME_TEACHER'),
       (uuid_generate_v4(), 'BECOME_STAFF');

INSERT INTO request_statuses (id, status)
VALUES (uuid_generate_v4(), 'PENDING'),
       (uuid_generate_v4(), 'APPROVED'),
       (uuid_generate_v4(), 'REJECTED');

INSERT INTO groups(group_id, group_name)
VALUES (uuid_generate_v4(), 'AA-01'),
       (uuid_generate_v4(), 'AA-02'),
       (uuid_generate_v4(), 'AA-03');

INSERT INTO users(user_id, role, first_name, last_name, email, password, group_id, phone_number, address)
VALUES (uuid_generate_v4(), 'ROLE_STUDENT', 'Maks', 'Muzych', 'maks.m@mail.com', 'password1', (SELECT group_id FROM groups WHERE group_name = 'AA-01'), '380528392715', 'Koswona 3'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'James', 'Oloven', 'james.m@mail.com', 'password2', (SELECT group_id FROM groups WHERE group_name = 'AA-01'), '380528694023', 'Pokash 4'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'John', 'White', 'temail@mail.com', 'password3', (SELECT group_id FROM groups WHERE group_name = 'AA-02'), '38055392715', 'Koswona 3'),
       (uuid_generate_v4(), 'ROLE_STUDENT', 'Alex', 'Brown', 'email@mail.com', 'password4', (SELECT group_id FROM groups WHERE group_name = 'AA-03'), '380786457755', 'Tasmowa 33'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Karok', 'Black', 'gemail@mail.com', 'password5', null, '380786450055', 'Tasmowa 33'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Bob', 'Olov', 'hemail@mail.com', 'password6', null, '380786007755', 'Kokova 23'),
       (uuid_generate_v4(), 'ROLE_TEACHER', 'Liza', 'Bored', 'jemail@mail.com', 'password7', null, '380786489755', 'Ikea 8'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'Jurich', 'Lomonov', 'kemail@mail.com', 'password8', null, '380316457755', 'Markosh 9'),
       (uuid_generate_v4(), 'ROLE_ADMIN', 'Patrik', 'Bobrent', 'cemail@mail.com', 'password9', null, '380786457546', 'Lesi 313'),
       (uuid_generate_v4(), 'ROLE_STAFF', 'Lim', 'Simos', 'zemail@mail.com', 'password10', null, '380786623455', 'Tasmowa 99'),
       (uuid_generate_v4(), 'ROLE_PENDING', 'Luca', 'Pekao', 'xemail@mail.com', 'password11', null, '380786682755', 'Tasmowa 23');

INSERT INTO courses(course_id, course_name, course_description, teacher_id)
VALUES (uuid_generate_v4(), 'Course1', 'Desc1', (SELECT users.user_id FROM users WHERE email = 'gemail@mail.com')),
       (uuid_generate_v4(), 'Course2', 'Desc2', (SELECT users.user_id FROM users WHERE email = 'hemail@mail.com')),
       (uuid_generate_v4(), 'Course3', 'Desc3', (SELECT users.user_id FROM users WHERE email = 'jemail@mail.com'));

INSERT INTO groups_courses(group_id, course_id)
VALUES ((SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT course_id FROM courses WHERE course_name = 'Course1')),
       ((SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT course_id FROM courses WHERE course_name = 'Course2')),
       ((SELECT group_id FROM groups WHERE group_name = 'AA-03'), (SELECT course_id FROM courses WHERE course_name = 'Course3'));

INSERT INTO lessons(id, lesson_type_id, course_id, group_id, study_day_id, study_week_id, lesson_time_id, additional_info)
VALUES (uuid_generate_v4(), (SELECT id FROM lesson_types WHERE lesson_type = 'LECTURE'), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT id FROM study_days WHERE day_of_week = 'MONDAY'), (SELECT id FROM study_weeks WHERE week_number = 'FIRST'), (SELECT id FROM lesson_times WHERE lesson_number = 'SECOND'), 'some info1'),
       (uuid_generate_v4(), (SELECT id FROM lesson_types WHERE lesson_type = 'PRACTICE'), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-01'), (SELECT id FROM study_days WHERE day_of_week = 'MONDAY'), (SELECT id FROM study_weeks WHERE week_number = 'FIRST'), (SELECT id FROM lesson_times WHERE lesson_number = 'THIRD'), 'some info2'),
       (uuid_generate_v4(), (SELECT id FROM lesson_types WHERE lesson_type = 'LECTURE'), (SELECT course_id FROM courses WHERE course_name = 'Course1'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT id FROM study_days WHERE day_of_week = 'TUESDAY'), (SELECT id FROM study_weeks WHERE week_number = 'SECOND'), (SELECT id FROM lesson_times WHERE lesson_number = 'FOURTH'), 'some info3'),
       (uuid_generate_v4(), (SELECT id FROM lesson_types WHERE lesson_type = 'PRACTICE'), (SELECT course_id FROM courses WHERE course_name = 'Course2'), (SELECT group_id FROM groups WHERE group_name = 'AA-02'), (SELECT id FROM study_days WHERE day_of_week = 'WEDNESDAY'), (SELECT id FROM study_weeks WHERE week_number = 'SECOND'), (SELECT id FROM lesson_times WHERE lesson_number = 'FIRST'), 'some info4');

INSERT INTO user_requests(id, user_id, type_id, status_id)
    VALUES (uuid_generate_v4(), (SELECT user_id FROM users WHERE email = 'maks.m@mail.com'), (SELECT id FROM request_types WHERE type = 'BECOME_TEACHER'), (SELECT id FROM request_statuses WHERE status = 'PENDING')),
           (uuid_generate_v4(), (SELECT user_id FROM users WHERE email = 'james.m@mail.com'), (SELECT id FROM request_types WHERE type = 'BECOME_TEACHER'), (SELECT id FROM request_statuses WHERE status = 'PENDING')),
           (uuid_generate_v4(), (SELECT user_id FROM users WHERE email = 'temail@mail.com'), (SELECT id FROM request_types WHERE type = 'BECOME_STAFF'), (SELECT id FROM request_statuses WHERE status = 'APPROVED'));

