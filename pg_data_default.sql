INSERT INTO "disciplines" (title)
VALUES ('Algebra'), ('Biology'), ('Chemistry'), ('Economics'), ('History'), ('English'), ('Physics');

INSERT INTO "groups" (title)
VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');

INSERT INTO "users" (first_name, last_name, role_id, group_id)
VALUES ('Kirill', 'Sergeev', 3, NULL),
  ('Test', 'Student', 1, 1),
  ('Test', 'Teacher', 2, NULL),
  ('Test', 'Admin', 3,  NULL);

INSERT INTO "accounts" (id, login, password)
VALUES (1, 'kirill_sergeev', 111111), (2, 'test_student', 123456), (3, 'test_teacher', 123456), (4, 'test_admin', 123456);