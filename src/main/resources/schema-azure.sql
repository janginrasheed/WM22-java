/*
drop table if EXISTS matches;
drop table if EXISTS predictions;
drop table if EXISTS teams;
drop table if EXISTS stages;
drop TABLE if EXISTS users;
drop table if EXISTS roles;

create table roles
(
    id   int         not null IDENTITY(1,1),
    role varchar(20) not null,
    primary key (id)
);

create table users
(
    email      varchar(100) not null,
    first_name varchar(20)  not null,
    last_name  varchar(20)  not null,
    password   varchar(100) not null,
    role_id    int          not null,
    primary key (email),
    foreign key (role_id) references roles (id)
);

create table stages
(
    id    int         not null IDENTITY(1,1),
    stage varchar(15) not null,
    primary key (id)
);


create table teams
(
    id         int         not null,
    name       varchar(20) not null,
    short_name char(3),
    flag       varchar(100),
    group_name CHAR(1)     not null,
    primary key (id)
);

create table matches
(
    id                          int  not null,
    stage_id                    int  not null,
    first_team_id               int,
    first_team_goals            varchar(2),
    first_team_penalties_goals  int,
    second_team_id              int,
    second_team_goals           varchar(2),
    second_team_penalties_goals int,
    date                        date not null,
    primary key (id),
    foreign key (stage_id) references stages (id),
    foreign key (first_team_id) references teams (id),
    foreign key (second_team_id) references teams (id)
);

create table predictions
(
    id             int          not null,
    email          varchar(100) not null,
    first_team_id  int,
    second_team_id int,
    group_name     char(1),
    match_number   int,
    primary key (id),
    foreign key (email) references users (email),
    foreign key (first_team_id) references teams (id),
    foreign key (second_team_id) references teams (id)
);
*/
