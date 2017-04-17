INSERT INTO "disciplines" (title)
VALUES ('Algebra'), ('Biology'), ('Chemistry'), ('Economics'), ('History'), ('English'), ('Physics');

INSERT INTO "groups" (title)
VALUES ('AA-2017'), ('AB-2017'), ('AC-2017');

INSERT INTO "accounts" (login, password)
VALUES ('kirill_sergeev', 111111);

INSERT INTO "users" (first_name, last_name, account_id, "role")
VALUES ('Kirill', 'Sergeev', 1, 'ADMIN');
