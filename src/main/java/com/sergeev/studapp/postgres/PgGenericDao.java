package com.sergeev.studapp.postgres;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class PgGenericDao<T extends Identified> implements GenericDao<T>{

    private static final Logger LOG = LoggerFactory.getLogger(PgGenericDao.class);
    protected abstract String getSelectQuery();
    protected abstract String getSelectAllQuery();
    protected abstract String getCreateQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistentException;
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistentException;
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistentException;

    @Override
    public T save(T object) throws PersistentException {
        String sql = getCreateQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                object.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return object;
    }

    @Override
    public T getById(Integer id) throws PersistentException {
        String sql = getSelectQuery();
        List<T> list;
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record with PK = " + id + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public T update(T object) throws PersistentException {
        String sql = getUpdateQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On update modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return object;
    }

    @Override
    public void delete(Integer id) throws PersistentException {
        String sql = getDeleteQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setInt(1, id);
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On delete modify more then 1 record: " + count);
            }
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public List<T> getAll() throws PersistentException {
        List<T> list;
        String sql = getSelectAllQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            list = parseResultSet(rs);
        } catch (SQLException e) {
            throw new PersistentException(e);
        }
        return list;
    }

   /* static {
        String sql = "DROP TABLE IF EXISTS marks, lessons, courses, disciplines, groups, accounts, users CASCADE;\n" +
                "------------------------------------------------\n" +
                "------------------structure---------------------\n" +
                "------------------------------------------------\n" +
                "\n" +
                "CREATE TABLE accounts (\n" +
                "  account_id SERIAL PRIMARY KEY,\n" +
                "  login      VARCHAR(60)  NOT NULL,\n" +
                "  password   VARCHAR(100) NOT NULL,\n" +
                "  token      VARCHAR(100) DEFAULT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE groups (\n" +
                "  group_id SERIAL PRIMARY KEY,\n" +
                "  title    VARCHAR(30) NOT NULL UNIQUE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE users (\n" +
                "  user_id    SERIAL PRIMARY KEY,\n" +
                "  first_name VARCHAR(30) NOT NULL,\n" +
                "  last_name  VARCHAR(30) NOT NULL,\n" +
                "  account_id INTEGER REFERENCES accounts (account_id),\n" +
                "  group_id   INTEGER REFERENCES groups (group_id) DEFAULT NULL,\n" +
                "  role       VARCHAR(10)                          DEFAULT 'STUDENT'\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE disciplines (\n" +
                "  discipline_id SERIAL PRIMARY KEY,\n" +
                "  title         VARCHAR(50) NOT NULL UNIQUE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE courses (\n" +
                "  course_id     SERIAL PRIMARY KEY,\n" +
                "  discipline_id INTEGER REFERENCES disciplines (discipline_id) NOT NULL,\n" +
                "  group_id      INTEGER REFERENCES groups (group_id)           NOT NULL,\n" +
                "  user_id       INTEGER REFERENCES users (user_id)             NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE lessons (\n" +
                "  lesson_id SERIAL PRIMARY KEY,\n" +
                "  course_id INTEGER REFERENCES courses (course_id)            NOT NULL,\n" +
                "  ordinal  SMALLINT                                          NOT NULL CHECK (ordinal BETWEEN 0 AND 10),\n" +
                "  date      DATE CHECK (date BETWEEN '2017-01-01' AND '2050-01-01'),\n" +
                "  type      VARCHAR(10)                                       NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE marks (\n" +
                "  mark_id   SERIAL PRIMARY KEY,\n" +
                "  lesson_id INTEGER REFERENCES lessons (lesson_id),\n" +
                "  user_id   INTEGER REFERENCES users (user_id) NOT NULL,\n" +
                "  mark      SMALLINT                           NOT NULL CHECK (mark BETWEEN 0 AND 100)\n" +
                ");\n" +
                "\n" +
                "------------------------------------------------\n" +
                "------------------functions---------------------\n" +
                "------------------------------------------------\n" +
                "\n" +
                "DROP FUNCTION IF EXISTS trigger_before_insert_on_marks();\n" +
                "CREATE FUNCTION trigger_before_insert_on_marks()\n" +
                "  RETURNS TRIGGER AS $marks_check$\n" +
                "BEGIN\n" +
                "  IF NEW.user_id NOT IN (SELECT user_id\n" +
                "                         FROM users\n" +
                "                         WHERE users.group_id IN (SELECT courses.group_id\n" +
                "                                                  FROM courses\n" +
                "                                                  WHERE courses.course_id IN (SELECT lessons.course_id\n" +
                "                                                                              FROM lessons\n" +
                "                                                                              WHERE lessons.lesson_id =\n" +
                "                                                                                    new.lesson_id))\n" +
                "  )\n" +
                "  THEN\n" +
                "    RAISE EXCEPTION 'This student is not a member of this course!';\n" +
                "  ELSEIF new.lesson_id NOT IN (SELECT lesson_id\n" +
                "                               FROM lessons\n" +
                "                               WHERE date <= now())\n" +
                "    THEN\n" +
                "      RAISE EXCEPTION 'This lesson was not been yet!';\n" +
                "  END IF;\n" +
                "  RETURN NEW;\n" +
                "END;\n" +
                "$marks_check$ LANGUAGE plpgsql;\n" +
                "\n" +
                "DROP TRIGGER IF EXISTS check_student_on_course\n" +
                "ON marks;\n" +
                "CREATE TRIGGER check_student_on_course\n" +
                "BEFORE INSERT ON marks\n" +
                "FOR EACH ROW\n" +
                "EXECUTE PROCEDURE trigger_before_insert_on_marks();\n" +
                "\n" +
                "DROP FUNCTION IF EXISTS trigger_before_insert_on_courses();\n" +
                "CREATE FUNCTION trigger_before_insert_on_courses()\n" +
                "  RETURNS TRIGGER AS $courses_check$\n" +
                "BEGIN\n" +
                "  IF new.discipline_id IN (SELECT discipline_id\n" +
                "                           FROM courses\n" +
                "                           WHERE group_id = new.group_id)\n" +
                "  THEN\n" +
                "    RAISE EXCEPTION 'This group already have this discipline!';\n" +
                "  END IF;\n" +
                "  RETURN new;\n" +
                "END;\n" +
                "$courses_check$ LANGUAGE plpgsql;\n" +
                "\n" +
                "DROP TRIGGER IF EXISTS check_disciplines_on_groups\n" +
                "ON courses;\n" +
                "CREATE TRIGGER check_disciplines_on_groups\n" +
                "BEFORE INSERT ON courses\n" +
                "FOR EACH ROW\n" +
                "EXECUTE PROCEDURE trigger_before_insert_on_courses();\n" +
                "\n" +
                "DROP FUNCTION IF EXISTS student_avg_mark_by_discipline( INTEGER, INTEGER );\n" +
                "CREATE FUNCTION student_avg_mark_by_discipline(INTEGER, INTEGER)\n" +
                "  RETURNS NUMERIC\n" +
                "AS 'SELECT avg(marks.mark)\n" +
                "    FROM users, disciplines, courses, lessons, marks, groups\n" +
                "    WHERE users.group_id = groups.group_id AND groups.group_id = courses.group_id AND\n" +
                "          courses.discipline_id = disciplines.discipline_id AND courses.course_id = lessons.course_id AND\n" +
                "          lessons.lesson_id = marks.lesson_id AND marks.user_id = users.user_id AND users.user_id = $1 AND\n" +
                "          disciplines.discipline_id = $2\n" +
                "    GROUP BY users.user_id, disciplines.discipline_id;'\n" +
                "LANGUAGE SQL\n" +
                "IMMUTABLE\n" +
                "RETURNS NULL ON NULL INPUT;\n"+
                "INSERT INTO \"disciplines\" (title)\n" +
                "VALUES ('Algebra'), ('Biology'), ('Chemistry'), ('Economics'), ('History'), ('English'), ('Physics');\n" +
                "\n" +
                "INSERT INTO \"groups\" (title)\n" +
                "VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');\n" +
                "\n" +
                "INSERT INTO \"accounts\" (login, password)\n" +
                "VALUES ('kirill_sergeev', 111111), ('test_student', 123456), ('test_teacher', 123456), ('test_admin', 123456);\n" +
                "\n" +
                "INSERT INTO \"users\" (first_name, last_name, account_id, \"role\", group_id)\n" +
                "VALUES ('Kirill', 'Sergeev', 1, 'ADMIN', NULL),\n" +
                "  ('Test', 'Student', 2, 'STUDENT', 1),\n" +
                "  ('Test', 'Teacher', 3, 'TEACHER', NULL),\n" +
                "  ('Test', 'Admin', 4, 'ADMIN', NULL);\n";
        try (Connection connection = PgDaoFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
      }
    }*/
}
