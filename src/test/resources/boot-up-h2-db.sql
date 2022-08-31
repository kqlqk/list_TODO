drop table if exists notes;
drop table if exists users;
drop table if exists roles;
drop table if exists refresh_tokens;

create table roles(
    id int not null unique auto_increment,
    name varchar(50) not null unique,

    primary key (id));

create table refresh_tokens(
    id bigint not null unique auto_increment,
    token varchar(255) not null unique,
    expires_in bigint not null,

    primary key (id));

create table users(
    id bigint not null unique auto_increment,
    email varchar(255) not null unique,
    login varchar(255) not null unique,
    password varchar(255) not null,
    role_id int not null default 1,
    oauth2 boolean not null default false,
    refresh_token_id bigint unique,

    primary key (id),

    foreign key (role_id) references roles(id)
        on update cascade
        on delete set default,

    foreign key (refresh_token_id) references refresh_tokens(id)
        on update cascade);

create table notes(
    id bigint not null unique auto_increment,
    title varchar(100) not null,
    body text,
    last_edited timestamp default now(),
    user_id bigint not null,
    full_title text not null,

    primary key (id),

    foreign key (user_id) references users(id)
        on update cascade
        on delete cascade);