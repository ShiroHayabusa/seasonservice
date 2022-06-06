insert into roles (id, name)
values (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

insert into users (id, login, password, first_name, last_name, patronymic, email, role_id)
values (1, 'admin', 'pass', 'admin', 'admin', 'admin', 'null', 1);

create extension if not exists pgcrypto;
update users set password = crypt(password, gen_salt('bf', 8));