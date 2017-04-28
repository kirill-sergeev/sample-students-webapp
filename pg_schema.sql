-- DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;
DROP TABLE IF EXISTS marks, lessons, courses, disciplines, groups, accounts, users CASCADE;
------------------------------------------------
------------------structure---------------------
------------------------------------------------

CREATE TABLE accounts (
  account_id SERIAL PRIMARY KEY,
  login      VARCHAR(60)  NOT NULL,
  password   VARCHAR(100) NOT NULL,
  token      VARCHAR(100) DEFAULT NULL
);

CREATE TABLE groups (
  group_id SERIAL PRIMARY KEY,
  title    VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE users (
  user_id    SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  account_id INTEGER REFERENCES accounts (account_id),
  group_id   INTEGER REFERENCES groups (group_id) DEFAULT NULL,
  role       VARCHAR(10)                          DEFAULT 'STUDENT'
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

CREATE TABLE lessons (
  lesson_id SERIAL PRIMARY KEY,
  course_id INTEGER REFERENCES courses (course_id)            NOT NULL,
  ordinal  SMALLINT                                          NOT NULL CHECK (ordinal BETWEEN 0 AND 10),
  date      DATE CHECK (date BETWEEN '2017-01-01' AND '2050-01-01'),
  type      VARCHAR(10)                                       NOT NULL
);

CREATE TABLE marks (
  mark_id   SERIAL PRIMARY KEY,
  lesson_id INTEGER REFERENCES lessons (lesson_id),
  user_id   INTEGER REFERENCES users (user_id) NOT NULL,
  mark      SMALLINT                           NOT NULL CHECK (mark BETWEEN 0 AND 100)
);

------------------------------------------------
------------------functions---------------------
------------------------------------------------

DROP FUNCTION IF EXISTS trigger_before_insert_on_marks();
CREATE OR REPLACE FUNCTION trigger_before_insert_on_marks()
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
                               WHERE date <= now())
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
CREATE OR REPLACE FUNCTION trigger_before_insert_on_courses()
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

DROP FUNCTION IF EXISTS student_avg_mark_by_discipline(INTEGER, INTEGER);
CREATE OR REPLACE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER)
  RETURNS NUMERIC
AS 'SELECT avg(marks.mark)
    FROM users, disciplines, courses, lessons, marks, groups
    WHERE users.group_id = groups.group_id AND groups.group_id = courses.group_id AND
          courses.discipline_id = disciplines.discipline_id AND courses.course_id = lessons.course_id AND
          lessons.lesson_id = marks.lesson_id AND marks.user_id = users.user_id AND users.user_id = $1 AND
          disciplines.discipline_id = $2
    GROUP BY users.user_id, disciplines.discipline_id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;


DROP FUNCTION IF EXISTS student_avg_mark_by_discipline(INTEGER, INTEGER);
CREATE OR REPLACE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER)
  RETURNS NUMERIC
AS 'SELECT avg(m.mark)
    FROM users u, disciplines d, courses c, lessons l, marks m, groups g
    WHERE u.group_id = g.id AND g.id = c.group_id AND
          c.discipline_id = d.id AND c.id = l.course_id AND
          l.id = m.lesson_id AND m.student_id = u.id AND u.id = $1 AND
          d.id = $2
    GROUP BY u.id, d.id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;
