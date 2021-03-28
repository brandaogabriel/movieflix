-- Genres
INSERT INTO genre (name, create_moment, status) VALUES ('aventura', NOW(), true);
INSERT INTO genre (name, create_moment, status) VALUES ('terror', NOW(), true);
INSERT INTO genre (name, create_moment, status) VALUES ('romance', NOW(), true);

-- Users
INSERT INTO user (name, email, password, create_moment, status) VALUES ('Bob', 'bob@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);
INSERT INTO user (name, email, password, create_moment, status) VALUES ('Ana', 'ana@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);
INSERT INTO user (name, email, password, create_moment, status) VALUES ('Admin', 'admin@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW(), true);

-- Roles
INSERT INTO role (authority) VALUES ('ROLE_VISITOR');
INSERT INTO role (authority) VALUES ('ROLE_MEMBER');
INSERT INTO role (authority) VALUES ('ROLE_ADMIN');

-- Users roles
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 3);