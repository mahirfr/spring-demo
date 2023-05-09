INSERT INTO role(name)
VALUES ("ROLE_ADMIN"), ("ROLE_USER");

INSERT INTO country(name)
VALUES ("Uganda"),
       ("Kosovo"),
       ("Corée du Nord");

INSERT INTO company(name)
VALUES ("GOooooogoooollllee"),
       ("Ammmmazooon"),
       ("Mamazon");

INSERT INTO job(id, name)
VALUES (1, 'Developer'),
       (2, 'Tester'),
       (3, 'Project Manager');

INSERT INTO user(first_name, last_name, country_id, company_id, created_on, updated_on, email, password, role_id)
VALUES ("TOTO",     "Titi",   "1", 2, "2022-06-25", UTC_TIMESTAMP(), "toto@a.com",    "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", 2),
       ("TITI",     "Toto",   "3", 1, "2022-06-25", UTC_TIMESTAMP(), "titi@a.com",    "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", 2),
       ("Monsieur", "Patate", "2", 3, "2022-06-25", UTC_TIMESTAMP(), "mr@patate.com", "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", 1);

INSERT INTO search_job_user(user_id, job_id)
VALUES (3, 1),
       (1, 2),
       (3, 2);
