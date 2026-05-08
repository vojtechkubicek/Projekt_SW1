-- 1. Totální čistka (vypneme kontroly, vymažeme data, zapneme kontroly)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;
TRUNCATE TABLE departments;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Oddělení
INSERT INTO departments (id, name) VALUES (1, 'Vývoj softwaru');
INSERT INTO departments (id, name) VALUES (2, 'Marketing');

-- 3. Role
INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- 4. Uživatelé (Heslo pro všechny: password)
INSERT INTO users (id, username, password, age, email, first_name, last_name, department_id) VALUES
                                                                                                 ('550e8400-e29b-41d4-a716-446655440001', 'john_doe', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 28, 'john.doe@example.com', 'John', 'Doe', 1),
                                                                                                 ('550e8400-e29b-41d4-a716-446655440002', 'jane_smith', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 34, 'jane.smith@example.com', 'Jane', 'Smith', 2),
                                                                                                 ('550e8400-e29b-41d4-a716-446655440003', 'bob_wilson', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lpk1C3G6', 45, 'bob.wilson@example.com', 'Bob', 'Wilson', 1);

-- 5. Propojení uživatelů s rolemi (M:N)
INSERT INTO user_roles (user_id, role_id) VALUES ('550e8400-e29b-41d4-a716-446655440001', 1);
INSERT INTO user_roles (user_id, role_id) VALUES ('550e8400-e29b-41d4-a716-446655440001', 2);
INSERT INTO user_roles (user_id, role_id) VALUES ('550e8400-e29b-41d4-a716-446655440002', 1);
INSERT INTO user_roles (user_id, role_id) VALUES ('550e8400-e29b-41d4-a716-446655440003', 1);