DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

------------------------------------------------
------------------structure---------------------
------------------------------------------------

CREATE TABLE groups (
  group_id SERIAL PRIMARY KEY,
  title    VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE users (
  user_id    SERIAL PRIMARY KEY,
  login      VARCHAR(60)  NOT NULL,
  password   VARCHAR(100) NOT NULL,
  first_name VARCHAR(30)  NOT NULL,
  last_name  VARCHAR(30)  NOT NULL,
  group_id   INTEGER REFERENCES groups (group_id) DEFAULT NULL,
  type       SMALLINT                             DEFAULT 1 CHECK (type BETWEEN 1 AND 10)
);

CREATE TABLE disciplines (
  discipline_id SERIAL PRIMARY KEY,
  title         VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE courses (
  course_id     SERIAL PRIMARY KEY,
  discipline_id INTEGER REFERENCES disciplines (discipline_id) NOT NULL,
  group_id      INTEGER REFERENCES groups (group_id)           NOT NULL,
  user_id       INTEGER REFERENCES users (user_id)             NOT NULL
);

CREATE TABLE lessons_types (
  lesson_type_id SERIAL PRIMARY KEY,
  title          VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE lessons_order (
  lesson_order_id SERIAL PRIMARY KEY,
  number          SMALLINT NOT NULL UNIQUE CHECK (number BETWEEN 1 AND 10),
  start_time      TIME     NOT NULL UNIQUE,
  end_time        TIME     NOT NULL UNIQUE
);

CREATE TABLE lessons (
  lesson_id      SERIAL PRIMARY KEY,
  lesson_type_id INTEGER REFERENCES lessons_types (lesson_type_id) NOT NULL,
  course_id      INTEGER REFERENCES courses (course_id)            NOT NULL,
  lesson_date    DATE CHECK (lesson_date BETWEEN '2017-01-01' AND '2050-01-01'),
  lesson_order   SMALLINT REFERENCES lessons_order (number)
);

CREATE TABLE marks (
  mark_id   SERIAL PRIMARY KEY,
  lesson_id INTEGER REFERENCES lessons (lesson_id),
  user_id   INTEGER REFERENCES users (user_id) NOT NULL,
  mark      SMALLINT                              NOT NULL CHECK (mark BETWEEN 0 AND 100)
);

------------------------------------------------
------------------functions---------------------
------------------------------------------------

DROP FUNCTION IF EXISTS trigger_before_insert_on_marks();
CREATE FUNCTION trigger_before_insert_on_marks()
  RETURNS TRIGGER AS $marks_check$
BEGIN
  IF NEW.user_id NOT IN (SELECT user_id
                            FROM users
                            WHERE users.group_id IN (SELECT courses.group_id
                                                        FROM courses
                                                        WHERE courses.course_id IN (SELECT lessons.course_id
                                                                                    FROM lessons
                                                                                    WHERE lessons.lesson_id =
                                                                                          new.lesson_id))
  )
  THEN
    RAISE EXCEPTION 'This student is not a member of this course!';
  ELSEIF new.lesson_id NOT IN (SELECT lesson_id
                               FROM lessons
                               WHERE lesson_date <= now())
    THEN
      RAISE EXCEPTION 'This lesson was not been yet!';
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
    RAISE EXCEPTION 'This group already have this discipline!';
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

DROP FUNCTION IF EXISTS student_avg_mark_by_discipline();
CREATE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER)
  RETURNS NUMERIC
AS 'SELECT avg(marks.mark)
    FROM users, disciplines, courses, lessons, marks, groups
    WHERE users.group_id = groups.group_id AND courses.group_id = groups.group_id AND
          courses.discipline_id = disciplines.discipline_id AND lessons.course_id = courses.course_id AND
          marks.lesson_id = lessons.lesson_id AND users.user_id = marks.user_id AND
          courses.user_id = users.user_id AND users.user_id = $1 AND disciplines.discipline_id = $2
    GROUP BY users.user_id, disciplines.discipline_id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;

INSERT INTO "lessons_order" (number, start_time, end_time)
VALUES (1, '7:45:00', '9:20:00'), (2, '9:30:00', '11:05:00'), (3, '11:15:00', '12:50:00'), (4, '13:10:00', '14:45:00'),
  (5, '14:55:00', '16:30:00'), (6, '16:45:00', '18:15:00');

INSERT INTO "disciplines" (title)
VALUES ('Algebra'), ('Biology'), ('Chemistry'), ('Economics'), ('History'), ('English'), ('Physics');

INSERT INTO "lessons_types" (title)
VALUES ('LECTURE'), ('PRACTICE'), ('LAB');

INSERT INTO "groups" (title)
VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');

INSERT INTO "users" (login, password, first_name, last_name, type, group_id)
VALUES ('kirill_sergeev1', '123456', 'Kirill1', 'Sergeev1', 3, NULL );
------------------------------------------------
------------------queries-----------------------
------------------------------------------------

