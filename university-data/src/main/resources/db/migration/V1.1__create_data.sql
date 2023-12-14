ALTER TABLE lesson_times
    ALTER COLUMN lesson_number TYPE character varying(255);

INSERT INTO groups (group_id, group_name)
VALUES (uuid_generate_v4(), 'AB-35'),
       (uuid_generate_v4(), 'QA-01'),
       (uuid_generate_v4(), 'JS-52');

INSERT INTO courses (course_id, course_name, course_description, teacher_id)
VALUES (uuid_generate_v4(), 'Mathematics', 'Algebra and Geometry', 'df96df75-f2c9-485d-a01f-36baf0cfa07e'),
       (uuid_generate_v4(), 'Science', 'Physics and Chemistry', 'b0ff90c2-4db2-4aa8-8d75-4036197923eb');


INSERT INTO groups_courses (group_id, course_id)
VALUES ((SELECT group_id FROM groups WHERE group_name='AB-35'), (SELECT course_id FROM courses WHERE course_name='Mathematics')),
       ((SELECT group_id FROM groups WHERE group_name='QA-01'), (SELECT course_id FROM courses WHERE course_name='Science'));

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
       (uuid_generate_v4(), 'WEDNESDAY'),
       (uuid_generate_v4(), 'FRIDAY'),
       (uuid_generate_v4(), 'SATURDAY');


INSERT INTO study_weeks (id, week_number)
VALUES (uuid_generate_v4(), 'FIRST'),
       (uuid_generate_v4(), 'SECOND');