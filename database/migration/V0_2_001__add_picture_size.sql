create table common.picture_size
(
  id          serial      not null
    constraint picture_size_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  width       smallint    not null,
  height      smallint    not null,
  description varchar(90)
);

create unique index picture_size_code_uindex
  on common.picture_size (code);

create unique index picture_size_name_uindex
  on common.picture_size (name);

create unique index picture_size_size_uindex
  on common.picture_size (width, height);