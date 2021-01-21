CREATE DATABASE IF NOT EXISTS db;
INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
INSERT IGNORE INTO users(created_at, updated_at, password, username) VALUES
(NOW(), NOW(), '$2a$10$8QmO5bzFEFi5bp7uFkgK4eFbfAArA9u/Ef2X9tlpm2.E0U0ZDr9KW', 'admin');
INSERT IGNORE INTO user_roles(user_id, role_id) VALUES ((SELECT u.id FROM users u WHERE u.username='admin'),
                                                        (SELECT u.id FROM roles u WHERE u.name='ROLE_ADMIN'));