DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
-- DROP TABLE IF EXISTS marks, lessons, courses, disciplines, groups, accounts, users CASCADE;

------------------------------------------------
------------------structure---------------------
------------------------------------------------

CREATE TABLE groups (
  id    SERIAL PRIMARY KEY,
  title VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE users (
  id         SERIAL PRIMARY KEY,
  first_name VARCHAR(30) NOT NULL,
  last_name  VARCHAR(30) NOT NULL,
  group_id   INTEGER REFERENCES groups (id) DEFAULT NULL,
  role_id    INTEGER                        DEFAULT 1
);

CREATE TABLE accounts (
  id       INTEGER REFERENCES users (id) ON DELETE CASCADE PRIMARY KEY,
  login    VARCHAR(60)  NOT NULL,
  password VARCHAR(100) NOT NULL,
  token    VARCHAR(100) DEFAULT NULL
);

CREATE TABLE disciplines (
  id    SERIAL PRIMARY KEY,
  title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE courses (
  id            SERIAL PRIMARY KEY,
  discipline_id INTEGER REFERENCES disciplines (id)       NOT NULL,
  group_id      INTEGER REFERENCES groups (id)            NOT NULL,
  user_id       INTEGER REFERENCES users (id)             NOT NULL
);

CREATE TABLE lessons (
  id        SERIAL PRIMARY KEY,
  course_id INTEGER REFERENCES courses (id)            NOT NULL,
  type_id   INTEGER                                    NOT NULL,
  ordinal   SMALLINT                                   NOT NULL CHECK (ordinal BETWEEN 0 AND 10),
  date      DATE CHECK (date BETWEEN '2017-01-01' AND '2050-01-01')
);

CREATE TABLE marks (
  id        SERIAL PRIMARY KEY,
  lesson_id INTEGER REFERENCES lessons (id),
  user_id   INTEGER REFERENCES users (id) NOT NULL,
  mark      SMALLINT                      NOT NULL CHECK (mark BETWEEN 0 AND 100)
);

------------------------------------------------
------------------functions---------------------
------------------------------------------------

CREATE OR REPLACE FUNCTION trigger_before_insert_on_marks()
  RETURNS TRIGGER AS $marks_check$
BEGIN
  IF NEW.user_id NOT IN (SELECT id
                         FROM users u
                         WHERE u.group_id IN (SELECT group_id
                                              FROM courses c
                                              WHERE c.id IN (SELECT l.course_id
                                                             FROM lessons l
                                                             WHERE l.id =
                                                                   NEW.lesson_id))
  )
  THEN
    RAISE EXCEPTION 'This student is not a member of this course!';
  ELSEIF new.lesson_id NOT IN (SELECT id
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
  IF NEW.discipline_id IN (SELECT discipline_id
                           FROM courses
                           WHERE group_id = NEW.group_id)
  THEN
    RAISE EXCEPTION 'This group already have this discipline!';
  END IF;
  RETURN NEW;
END;
$courses_check$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS check_disciplines_on_groups
ON courses;
CREATE TRIGGER check_disciplines_on_groups
BEFORE INSERT ON courses
FOR EACH ROW
EXECUTE PROCEDURE trigger_before_insert_on_courses();

DROP FUNCTION IF EXISTS student_avg_mark_by_discipline( INTEGER, INTEGER, OUT NUMERIC );
CREATE OR REPLACE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER, OUT NUMERIC)
  RETURNS NUMERIC AS
'SELECT avg(mark)
 FROM users u, disciplines d, courses c, lessons l, marks m, groups g
 WHERE m.user_id = u.id
       AND u.group_id = g.id
       AND g.id = c.group_id
       AND c.id = l.course_id
       AND l.id = m.lesson_id
       AND c.discipline_id = d.id
       AND u.id = $1
       AND d.id = $2
 GROUP BY u.id, d.id;'
LANGUAGE SQL
IMMUTABLE
RETURNS NULL ON NULL INPUT;