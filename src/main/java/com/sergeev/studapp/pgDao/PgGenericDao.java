package com.sergeev.studapp.pgDao;

import com.sergeev.studapp.dao.GenericDao;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Identified;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public abstract class PgGenericDao<T extends Identified> implements GenericDao<T>{

    public abstract String getSelectQuery();

    public abstract String getSelectAllQuery();

    public abstract String getCreateQuery();

    public abstract String getUpdateQuery();

    public abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistentException;

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws PersistentException;

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws PersistentException;

    @Override
    public T persist(T object) throws PersistentException {
        String sql = getCreateQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(statement, object);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                object.setId(resultSet.getString(1));
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return object;
    }

    @Override
    public T getById(String key) throws PersistentException {
        String sql = getSelectQuery();
        List<T> list;
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistentException("Record with PK = " + key + " not found.");
        }
        if (list.size() > 1) {
            throw new PersistentException("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public void update(T object) throws PersistentException {
        String sql = getUpdateQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void delete(T object) throws PersistentException {
        String sql = getDeleteQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setInt(1, Integer.parseInt(object.getId()));
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new PersistentException(e);
        }
    }

    @Override
    public void delete(String key) throws PersistentException {
        String sql = getDeleteQuery();
        try (Connection connection = PgDaoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try {
                statement.setInt(1, Integer.parseInt(key));
            } catch (Exception e) {
                throw new PersistentException(e);
            }
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new PersistentException("On delete modify more then 1 record: " + count);
            }
        } catch (Exception e) {
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
        } catch (Exception e) {
            throw new PersistentException(e);
        }
        return list;
    }

    public PgGenericDao() {
    }

//    static {
//        String sql = "DROP TABLE IF EXISTS marks, lessons, lessons_order, courses, disciplines, lessons_types, teachers, students, groups;" +
//                "CREATE TABLE groups (\n" +
//                "  group_id SERIAL PRIMARY KEY,\n" +
//                "  title    VARCHAR(30) NOT NULL UNIQUE\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE students (\n" +
//                "  student_id SERIAL PRIMARY KEY,\n" +
//                "  group_id   INTEGER REFERENCES groups (group_id),\n" +
//                "  first_name VARCHAR(30) NOT NULL,\n" +
//                "  last_name  VARCHAR(30) NOT NULL\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE teachers (\n" +
//                "  teacher_id SERIAL PRIMARY KEY,\n" +
//                "  first_name VARCHAR(30) NOT NULL,\n" +
//                "  last_name  VARCHAR(30) NOT NULL\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE lessons_types (\n" +
//                "  lesson_type_id SERIAL PRIMARY KEY,\n" +
//                "  title          VARCHAR(50) NOT NULL UNIQUE\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE disciplines (\n" +
//                "  discipline_id SERIAL PRIMARY KEY,\n" +
//                "  title         VARCHAR(50) NOT NULL UNIQUE\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE courses (\n" +
//                "  course_id     SERIAL PRIMARY KEY,\n" +
//                "  discipline_id INTEGER REFERENCES disciplines (discipline_id) NOT NULL,\n" +
//                "  group_id      INTEGER REFERENCES groups (group_id)           NOT NULL,\n" +
//                "  teacher_id    INTEGER REFERENCES teachers (teacher_id)       NOT NULL\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE lessons_order (\n" +
//                "  order_id          SERIAL PRIMARY KEY,\n" +
//                "  lesson_order      INTEGER NOT NULL UNIQUE CHECK (lesson_order BETWEEN 1 AND 10),\n" +
//                "  lesson_start_time TIME     NOT NULL UNIQUE,\n" +
//                "  lesson_end_time   TIME     NOT NULL UNIQUE\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE lessons (\n" +
//                "  lesson_id      SERIAL PRIMARY KEY,\n" +
//                "  lesson_type_id INTEGER REFERENCES lessons_types (lesson_type_id) NOT NULL,\n" +
//                "  course_id      INTEGER REFERENCES courses (course_id)           NOT NULL,\n" +
//                "  lesson_date    DATE CHECK (lesson_date BETWEEN '2000-01-01' AND '2050-01-01'),\n" +
//                "  lesson_order   INTEGER REFERENCES lessons_order (order_id)\n" +
//                ");\n" +
//                "\n" +
//                "CREATE TABLE marks (\n" +
//                "  mark_id    SERIAL PRIMARY KEY,\n" +
//                "  lesson_id  INTEGER REFERENCES lessons (lesson_id),\n" +
//                "  student_id INTEGER REFERENCES students (student_id) NOT NULL,\n" +
//                "  mark       INTEGER                                 NOT NULL CHECK (mark BETWEEN 0 AND 100)\n" +
//                ");" +
//                "\n" +
//                "INSERT INTO \"lessons_order\" (lesson_order, lesson_start_time, lesson_end_time)\n" +
//                "VALUES (1, '7:45:00', '9:20:00'), (2, '9:30:00', '11:05:00'), (3, '11:15:00', '12:50:00'), (4, '13:10:00', '14:45:00'),\n" +
//                "  (5, '14:55:00', '16:30:00'), (6, '16:45:00', '18:15:00');\n" +
//                "INSERT INTO \"disciplines\" (title)\n" +
//                "VALUES ('algebra'), ('biology'), ('chemistry'), ('economics'), ('history'), ('english'), ('physics');\n" +
//                "INSERT INTO \"lessons_types\" (title) VALUES ('lecture'), ('practice'), ('lab');\n" +
//                "INSERT INTO \"groups\" (title) VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');\n" +
//                "INSERT INTO \"students\" (group_id, first_name, last_name)\n" +
//                "VALUES (3, 'Branden', 'Sanders'), (2, 'Brian', 'Sargent'), (3, 'Macon', 'Price'), (3, 'Charles', 'Jennings'),\n" +
//                "  (2, 'Arden', 'Bullock'), (3, 'Emerson', 'Cochran'), (2, 'Tanek', 'Salazar'), (2, 'Quinlan', 'Huffman'),\n" +
//                "  (2, 'Cody', 'Townsend'), (2, 'Lev', 'Anderson'), (3, 'Victor', 'Eaton'), (2, 'Victor', 'Berg'), (2, 'Isaac', 'Curry'),\n" +
//                "  (3, 'Kasimir', 'Berry'), (1, 'Phillip', 'Fowler'), (2, 'Brian', 'Holmes'), (3, 'Harper', 'Kirk'),\n" +
//                "  (1, 'Fuller', 'Conway'), (2, 'Hu', 'Cooley'), (3, 'Charles', 'Serrano'), (2, 'Jonah', 'Burke'),\n" +
//                "  (3, 'Castor', 'Roberson'), (3, 'Micah', 'Crane'), (2, 'Colby', 'Rosa'), (3, 'Hiram', 'Potter'), (2, 'Brent', 'Hall'),\n" +
//                "  (2, 'Ulric', 'Perkins'), (3, 'Sebastian', 'Barnett'), (1, 'Carlos', 'Gonzales'), (2, 'Xenos', 'Fuller');\n" +
//                "INSERT INTO \"teachers\" (first_name, last_name)\n" +
//                "VALUES ('Barrett', 'Clements'), ('Sean', 'Hopper'), ('Cedric', 'Serrano'), ('Cody', 'Riddle'), ('Aidan', 'King'),\n" +
//                "  ('Neville', 'Harrison'), ('Ian', 'Cash'), ('Sean', 'Cleveland'), ('Stewart', 'Lamb'), ('Trevor', 'Frazier');";
//        try (Connection connection = PgDaoFactory.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.execute(sql);
//        } catch (Exception e) {
//            e.printStackTrace();
//      }
//    }
}
