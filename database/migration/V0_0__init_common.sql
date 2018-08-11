-- picture

create table common.picture
(
  id       serial    not null
    constraint picture_pk
    primary key,

  size     bigint    not null,
  uploaded timestamp not null
);

-- user

create table common."user"
(
  id         serial      not null
    constraint user_pk
    primary key,

  login      varchar(45) not null,
  password   varchar(90) not null,
  name       varchar(45) not null,
  surname    varchar(45) not null,
  patronymic varchar(45) not null,
  address    varchar(90) not null,
  phone      varchar(16) not null
);

create unique index user_login_uindex
  on common."user" (login);

-- role

create table common.role
(
  id          serial      not null
    constraint role_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  description varchar(90)
);

create unique index role_code_uindex
  on common.role (code);

create unique index role_name_uindex
  on common.role (name);

-- authority

create table common.authority
(
  user_id integer not null
    constraint authority_user_id_fk
    references common."user",

  role_id integer not null
    constraint authority_role_id_fk
    references common.role,

  constraint authority_pk
  primary key (user_id, role_id)
);
