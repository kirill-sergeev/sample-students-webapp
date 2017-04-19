INSERT INTO "disciplines" (title)
VALUES ('Algebra'), ('Biology'), ('Chemistry'), ('Economics'), ('History'), ('English'), ('Physics');

INSERT INTO "groups" (title)
VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');

INSERT INTO "accounts" (login, password)
VALUES ('kirill_sergeev', 111111), ('test_student', 123456), ('test_teacher', 123456), ('test_admin', 123456);

INSERT INTO "users" (first_name, last_name, account_id, "role", group_id)
VALUES ('Kirill', 'Sergeev', 1, 'ADMIN', NULL),
  ('Test', 'Student', 2, 'STUDENT', 1),
  ('Test', 'Teacher', 3, 'TEACHER', NULL),
  ('Test', 'Admin', 4, 'ADMIN', NULL);
