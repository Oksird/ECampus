INSERT INTO courses(course_name, course_description)
VALUES
    ('Math', 'Something about numbers'),
    ('Literature', 'Words in books'),
    ('Science', 'It is for someone else');

INSERT INTO groups(group_name)
VALUES
    ('AB-01'),
    ('AB-02'),
    ('AB-03');

INSERT INTO students(group_id, first_name, last_name)
VALUES
    (1, 'John', 'Brown'),
    (1, 'Alex', 'Black'),
    (1, 'Mija', 'White'),
    (1, 'Lee', 'Simpson'),
    (2, 'Tim', 'Creep'),
    (2, 'Jorge', 'White'),
    (2, 'Melania', 'Watson'),
    (2, 'Max', 'Iakovos'),
    (3, 'David', 'Ivanko'),
    (3, 'Olena', 'Brown'),
    (3, 'Valeri', 'White'),
    (3, 'Joe', 'Smith');

INSERT INTO student_courses(student_id, course_id)
VALUES
    (1, 1),
    (2, 3),
    (3, 1),
    (4, 1),
    (5, 2),
    (6, 1),
    (7, 3),
    (8, 2),
    (9, 1),
    (10, 2),
    (11, 3),
    (12, 2);
