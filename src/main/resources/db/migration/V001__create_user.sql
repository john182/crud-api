CREATE TABLE user
(
    id       serial       not null,
    name     VARCHAR(50)  NOT NULL,
    email    VARCHAR(50)  NOT NULL,
    password VARCHAR(150) NOT NULL,
    birthday timestamp,
    photo    VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE permission
(
    id          serial      not null,
    description VARCHAR(50) NOT NULL,
    primary key (id)
);

CREATE TABLE user_permission
(
    id_user       integer NOT NULL,
    id_permission integer NOT NULL,
    PRIMARY KEY (id_user, id_permission),
    FOREIGN KEY (id_user) REFERENCES user (id),
    FOREIGN KEY (id_permission) REFERENCES permission (id)
);

INSERT INTO user (id, name, email, password)
values (1, 'Administrator', 'admin@admin.com', '$2a$10$U3JNjayY4003N33VNlHYYehTJrUTwyz7x/xqxaUne/aVmoY3dUHFW');

INSERT INTO permission (id, description)
values (1, 'ROLE_DELETE_ANY_USER');
INSERT INTO permission (id, description)
values (2, 'ROLE_EDIT_ANY_USER');
INSERT INTO permission (id, description)
values (3, 'ROLE_UPDATE_ANY_USER');

INSERT INTO permission (id, description)
values (4, 'ROLE_DELETE_USER');
INSERT INTO permission (id, description)
values (5, 'ROLE_EDIT_USER');
INSERT INTO permission (id, description)
values (6, 'ROLE_UPDATE_USER');
INSERT INTO permission (id, description)
values (7, 'ROLE_SEARCH_USER');

INSERT INTO user_permission (id_user, id_permission)
values (1, 1);
INSERT INTO user_permission (id_user, id_permission)
values (1, 2);
INSERT INTO user_permission (id_user, id_permission)
values (1, 3);
INSERT INTO user_permission (id_user, id_permission)
values (1, 7);

