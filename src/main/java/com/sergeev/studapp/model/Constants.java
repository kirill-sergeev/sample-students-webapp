package com.sergeev.studapp.model;

public interface Constants {

    /**
     * Database names.
     */
    String DATABASE = "students";
    String TEST_DB = "students_test";

    /**
     * Table names.
     */
    String ACCOUNTS = "accounts";
    String COURSES = "courses";
    String DISCIPLINES = "disciplines";
    String GROUPS = "groups";
    String LESSONS = "lessons";
    String MARKS = "marks";
    String USERS = "users";

    /**
     * PK name.
     */
    String ID = "id";

    /**
     * FK names.
     */
    String COURSE_ID = "course_id";
    String DISCIPLINE_ID = "discipline_id";
    String GROUP_ID = "group_id";
    String LESSON_ID = "lesson_id";
    String USER_ID = "user_id";

    /**
     * Attribute names.
     */
    String DATE = "date";
    String FIRST_NAME = "first_name";
    String LAST_NAME = "last_name";
    String LOGIN = "login";
    String ORDER = "ordinal";
    String PASSWORD = "password";
    String ROLE = "role_id";
    String TITLE = "title";
    String TOKEN = "token";
    String TYPE = "type_id";
    String VALUE = "mark";

}
