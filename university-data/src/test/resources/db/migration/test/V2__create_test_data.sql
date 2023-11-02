CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO public.courses (course_id, course_name, course_description)
VALUES (uuid_generate_v4(), 'Course1', 'This course covers various mathematical topics.'),
       (uuid_generate_v4(), 'Course2', 'Explore classic and modern works of English literature.'),
       (uuid_generate_v4(), 'Course3', 'Learn programming and computer science fundamentals.');


INSERT INTO public.groups (group_id, group_name)
VALUES (uuid_generate_v4(), 'AA-01'),
       (uuid_generate_v4(), 'AA-02'),
       (uuid_generate_v4(), 'AA-03');
