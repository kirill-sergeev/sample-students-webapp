DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE groups (
  group_id SERIAL PRIMARY KEY,
  title    VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE students (
  student_id SERIAL PRIMARY KEY,
  group_id   INTEGER REFERENCES groups (group_id),
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL
);

CREATE TABLE teachers (
  teacher_id SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL
);

CREATE TABLE lessons_types (
  lesson_type_id SERIAL PRIMARY KEY,
  title          VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE disciplines (
  discipline_id SERIAL PRIMARY KEY,
  title         VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE courses (
  course_id     SERIAL PRIMARY KEY,
  discipline_id INTEGER REFERENCES disciplines (discipline_id) NOT NULL,
  group_id      INTEGER REFERENCES groups (group_id)           NOT NULL,
  teacher_id    INTEGER REFERENCES teachers (teacher_id)       NOT NULL
);

CREATE TABLE lessons_order (
  order_id          SMALLSERIAL PRIMARY KEY,
  lesson_order      SMALLINT NOT NULL UNIQUE CHECK (lesson_order BETWEEN 1 AND 10),
  lesson_start_time TIME     NOT NULL UNIQUE,
  lesson_end_time   TIME     NOT NULL UNIQUE
);

CREATE TABLE lessons (
  lesson_id      SERIAL PRIMARY KEY,
  lesson_type_id INTEGER REFERENCES lessons_types (lesson_type_id) NOT NULL,
  course_id      INTEGER REFERENCES courses (course_id)           NOT NULL,
  lesson_date    DATE CHECK (lesson_date BETWEEN '2000-01-01' AND '2050-01-01'),
  lesson_order   SMALLINT REFERENCES lessons_order (order_id)
);

CREATE TABLE marks (
  mark_id    SERIAL PRIMARY KEY,
  lesson_id  INTEGER REFERENCES lessons (lesson_id),
  student_id INTEGER REFERENCES students (student_id) NOT NULL,
  mark       SMALLINT                                 NOT NULL CHECK (mark BETWEEN 0 AND 100)
);
DROP FUNCTION IF EXISTS trigger_before_insert_on_marks();
CREATE FUNCTION trigger_before_insert_on_marks()
  RETURNS TRIGGER AS $marks_check$
BEGIN
  IF NEW.student_id NOT IN (SELECT student_id
                            FROM students
                            WHERE students.group_id IN (SELECT courses.group_id
                                                        FROM courses
                                                        WHERE courses.course_id IN (SELECT lessons.course_id
                                                                                    FROM lessons
                                                                                    WHERE lessons.lesson_id =
                                                                                          new.lesson_id))
  )
  THEN
    RAISE EXCEPTION 'This student can not get an estimate on this course!';
  ELSEIF new.lesson_id NOT IN (SELECT lesson_id
                               FROM lessons
                               WHERE lesson_date <= now())
    THEN
      RAISE EXCEPTION 'This lesson has not been yet!';
  END IF;
  RETURN NEW;
END;
$marks_check$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS check_student_on_course
ON marks;
CREATE TRIGGER check_student_on_course
BEFORE INSERT ON marks
FOR EACH ROW
EXECUTE PROCEDURE trigger_before_insert_on_marks();

DROP FUNCTION IF EXISTS trigger_before_insert_on_courses();
CREATE FUNCTION trigger_before_insert_on_courses()
  RETURNS TRIGGER AS $courses_check$
BEGIN
  IF new.discipline_id IN (SELECT discipline_id
                           FROM courses
                           WHERE group_id = new.group_id)
  THEN
    RAISE EXCEPTION 'This group already has this discipline!';
  END IF;
  RETURN new;
END;
$courses_check$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS check_disciplines_on_groups
ON courses;
CREATE TRIGGER check_disciplines_on_groups
BEFORE INSERT ON courses
FOR EACH ROW
EXECUTE PROCEDURE trigger_before_insert_on_courses();








INSERT INTO "lessons_order" (lesson_order, lesson_start_time, lesson_end_time)
VALUES (1, '7:45:00', '9:20:00'), (2, '9:30:00', '11:05:00'), (3, '11:15:00', '12:50:00'), (4, '13:10:00', '14:45:00'),
  (5, '14:55:00', '16:30:00'), (6, '16:45:00', '18:15:00');
INSERT INTO "disciplines" (title)
VALUES ('algebra'), ('biology'), ('chemistry'), ('economics'), ('history'), ('english'), ('physics');
INSERT INTO "lessons_types" (title) VALUES ('lecture'), ('practice'), ('lab');
INSERT INTO "groups" (title) VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');
INSERT INTO "students" (group_id, first_name, last_name)
VALUES (3, 'Branden', 'Sanders'), (2, 'Brian', 'Sargent'), (3, 'Macon', 'Price'), (3, 'Charles', 'Jennings'),
  (2, 'Arden', 'Bullock'), (3, 'Emerson', 'Cochran'), (2, 'Tanek', 'Salazar'), (2, 'Quinlan', 'Huffman'),
  (2, 'Cody', 'Townsend'), (2, 'Lev', 'Anderson'), (3, 'Victor', 'Eaton'), (2, 'Victor', 'Berg'), (2, 'Isaac', 'Curry'),
  (3, 'Kasimir', 'Berry'), (1, 'Phillip', 'Fowler'), (2, 'Brian', 'Holmes'), (3, 'Harper', 'Kirk'),
  (1, 'Fuller', 'Conway'), (2, 'Hu', 'Cooley'), (3, 'Charles', 'Serrano'), (2, 'Jonah', 'Burke'),
  (3, 'Castor', 'Roberson'), (3, 'Micah', 'Crane'), (2, 'Colby', 'Rosa'), (3, 'Hiram', 'Potter'), (2, 'Brent', 'Hall'),
  (2, 'Ulric', 'Perkins'), (3, 'Sebastian', 'Barnett'), (1, 'Carlos', 'Gonzales'), (2, 'Xenos', 'Fuller');
INSERT INTO "teachers" (first_name, last_name)
VALUES ('Barrett', 'Clements'), ('Sean', 'Hopper'), ('Cedric', 'Serrano'), ('Cody', 'Riddle'), ('Aidan', 'King'),
  ('Neville', 'Harrison'), ('Ian', 'Cash'), ('Sean', 'Cleveland'), ('Stewart', 'Lamb'), ('Trevor', 'Frazier');
INSERT INTO "courses" (discipline_id, group_id, teacher_id)
VALUES (1, 3, 5), (6, 1, 9), (6, 2, 9), (3, 2, 5), (7, 2, 7), (3, 1, 2), (2, 1, 7), (3, 2, 8), (7, 2, 1), (1, 1, 1),
  (6, 2, 8), (2, 2, 9), (3, 1, 1), (6, 3, 10), (2, 2, 7), (1, 2, 5), (3, 1, 2), (1, 3, 10), (5, 1, 2), (6, 3, 5);
INSERT INTO "lessons" (lesson_type_id, course_id, lesson_date, lesson_order)
VALUES (1, 9, '2017-06-05', 3), (1, 2, '2017-05-11', 6), (2, 4, '2017-06-13', 3), (2, 1, '2017-06-03', 2),
  (1, 11, '2017-05-30', 3), (3, 2, '2017-03-14', 5), (2, 18, '2017-02-04', 3), (3, 8, '2017-05-05', 3),
  (1, 17, '2017-04-05', 6), (2, 15, '2017-04-30', 4), (2, 8, '2017-02-17', 4), (1, 12, '2017-03-22', 2),
  (1, 15, '2017-06-02', 3), (1, 5, '2017-05-03', 5), (3, 9, '2017-02-27', 3), (1, 2, '2017-05-22', 5),
  (1, 19, '2017-03-16', 2), (3, 16, '2017-05-23', 5), (1, 9, '2017-05-14', 2), (3, 12, '2017-02-27', 6),
  (3, 6, '2017-04-23', 3), (2, 5, '2017-05-05', 1), (1, 7, '2017-04-02', 3), (3, 14, '2017-06-06', 2),
  (1, 20, '2017-03-27', 6), (1, 8, '2017-02-18', 1), (2, 6, '2017-03-16', 4), (1, 16, '2017-05-23', 3),
  (1, 13, '2017-02-06', 4), (3, 5, '2017-04-02', 2), (1, 9, '2017-03-30', 1), (2, 17, '2017-05-31', 6),
  (2, 8, '2017-03-31', 3), (1, 6, '2017-06-02', 4), (2, 5, '2017-03-30', 1), (3, 6, '2017-03-11', 1),
  (3, 15, '2017-02-23', 3), (3, 1, '2017-02-21', 6), (3, 12, '2017-04-04', 5), (3, 15, '2017-06-01', 4),
  (3, 17, '2017-05-19', 6), (3, 4, '2017-05-07', 6), (1, 20, '2017-05-21', 4), (1, 2, '2017-05-07', 1),
  (2, 7, '2017-02-01', 4), (2, 16, '2017-05-06', 2), (2, 7, '2017-05-01', 2), (1, 5, '2017-04-02', 5),
  (2, 14, '2017-02-05', 2), (3, 8, '2017-02-22', 2), (1, 18, '2017-05-02', 5), (1, 1, '2017-02-17', 4),
  (2, 4, '2017-05-01', 1), (1, 1, '2017-02-01', 6), (1, 1, '2017-05-23', 1), (3, 17, '2017-06-04', 4),
  (2, 17, '2017-02-22', 6), (2, 13, '2017-03-16', 2), (2, 16, '2017-06-04', 5), (1, 17, '2017-03-06', 1),
  (3, 3, '2017-04-12', 2), (1, 16, '2017-04-28', 6), (3, 18, '2017-05-13', 6), (1, 10, '2017-02-24', 5),
  (2, 10, '2017-04-06', 6), (1, 7, '2017-05-27', 1), (2, 1, '2017-04-11', 2), (1, 13, '2017-03-31', 1),
  (1, 10, '2017-06-01', 2), (3, 18, '2017-05-02', 4), (3, 9, '2017-04-11', 2), (3, 15, '2017-05-25', 1),
  (2, 6, '2017-04-10', 4), (3, 19, '2017-06-14', 1), (2, 7, '2017-04-22', 5), (1, 8, '2017-04-20', 5),
  (3, 2, '2017-05-29', 6), (2, 16, '2017-05-08', 3), (1, 1, '2017-06-14', 3), (3, 16, '2017-03-31', 3),
  (1, 17, '2017-05-20', 3), (1, 2, '2017-05-21', 6), (2, 15, '2017-05-15', 3), (1, 2, '2017-05-20', 1),
  (2, 3, '2017-05-18', 3), (2, 12, '2017-03-16', 2), (3, 1, '2017-02-09', 2), (1, 15, '2017-03-12', 6),
  (2, 19, '2017-02-10', 6), (1, 12, '2017-05-21', 3), (2, 17, '2017-06-06', 3), (2, 19, '2017-04-03', 5),
  (3, 19, '2017-04-23', 5), (2, 16, '2017-02-26', 3), (3, 6, '2017-06-04', 4), (3, 3, '2017-03-11', 4),
  (3, 10, '2017-04-22', 5), (1, 7, '2017-05-02', 4), (3, 14, '2017-04-24', 1), (1, 15, '2017-05-03', 2);

INSERT INTO "marks" (lesson_id, student_id, mark) VALUES (54, 1, 100);

SELECT
  students.student_id,
  students.group_id,
  lessons.course_id,
  lessons.lesson_id
FROM students, groups, lessons, courses
WHERE (students.group_id = groups.group_id) AND (lessons.course_id = courses.course_id) AND
      (courses.group_id = groups.group_id) AND lesson_date <= now();

SELECT
  (students.first_name || ' ' || students.last_name) AS student,
  disciplines.title                                  AS discipline,
  lessons.lesson_date,
  marks.mark,
  (teachers.first_name || ' ' || teachers.last_name) AS teacher
FROM students, disciplines, courses, lessons, marks, groups, teachers
WHERE students.group_id = groups.group_id AND courses.group_id = groups.group_id AND
      courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
      marks.lesson_id = lessons.lesson_id AND students.student_id = marks.student_id AND
      courses.teacher_id = teachers.teacher_id;

SELECT
  (s.first_name || ' ' || s.last_name) AS student,
  d.title                              AS discipline,
  l.lesson_date,
  o.lesson_start_time,
  t.title                              AS lesson_type
FROM students s, lessons l, lessons_types t, lessons_order o, disciplines d, groups g, courses c
WHERE
  s.group_id = g.group_id AND c.group_id = g.group_id AND l.course_id = c.course_id AND
  l.lesson_type_id = t.lesson_type_id AND d.discipline_id = c.discipline_id AND l.lesson_order = o.order_id
ORDER BY s.student_id, l.lesson_date;

SELECT
  (students.first_name || ' ' || students.last_name) AS student,
  disciplines.title                                  AS discipline,
  avg(marks.mark),
  (teachers.first_name || ' ' || teachers.last_name) AS teacher
FROM students, disciplines, courses, lessons, marks, groups, teachers
WHERE students.group_id = groups.group_id AND courses.group_id = groups.group_id AND
      courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
      marks.lesson_id = lessons.lesson_id AND students.student_id = marks.student_id AND
      courses.teacher_id = teachers.teacher_id
GROUP BY student, discipline, teacher;

DROP FUNCTION IF EXISTS students_disciplines();
CREATE FUNCTION students_disciplines(INTEGER, INTEGER)
  RETURNS NUMERIC
AS 'SELECT avg(marks.mark)
    FROM students, disciplines, courses, lessons, marks, groups, teachers
    WHERE students.group_id = groups.group_id AND courses.group_id = groups.group_id AND
          courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
          marks.lesson_id = lessons.lesson_id AND students.student_id = marks.student_id AND
          courses.teacher_id = teachers.teacher_id AND students.student_id = $1 AND disciplines.discipline_id = $2
    GROUP BY students.student_id, disciplines.discipline_id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;

SELECT students_disciplines(1, 1);

DROP FUNCTION IF EXISTS students_disciplines_by_name();
CREATE FUNCTION students_disciplines_by_name(VARCHAR(30), VARCHAR(30), VARCHAR(50))
  RETURNS NUMERIC
AS 'SELECT avg(marks.mark)
    FROM students, disciplines, courses, lessons, marks, groups, teachers
    WHERE students.group_id = groups.group_id AND courses.group_id = groups.group_id AND
          courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
          marks.lesson_id = lessons.lesson_id AND students.student_id = marks.student_id AND
          courses.teacher_id = teachers.teacher_id AND students.first_name = $1 AND students.last_name = $2 AND
          disciplines.title = $3
    GROUP BY students.student_id, disciplines.discipline_id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;

SELECT students_disciplines_by_name('Branden', 'Sanders', 'algebra');


DROP FUNCTION IF EXISTS student_avg_mark_by_discipline();
CREATE FUNCTION student_avg_mark_by_discipline(INTEGER,INTEGER)
  RETURNS NUMERIC
AS 'SELECT avg(marks.mark)
    FROM students, disciplines, courses, lessons, marks, groups, teachers
    WHERE students.group_id = groups.group_id AND courses.group_id = groups.group_id AND
          courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
          marks.lesson_id = lessons.lesson_id AND students.student_id = marks.student_id AND
          courses.teacher_id = teachers.teacher_id AND students.student_id = $1  AND disciplines.discipline_id = $2
    GROUP BY students.student_id, disciplines.discipline_id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;

SELECT student_avg_mark_by_discipline(15,1);

INSERT INTO "marks" (lesson_id, student_id, mark) VALUES (64, 15, 15);

select course_id from courses where group_id=1;
select lesson_id from lessons where course_id=10 and lesson_date< now()

