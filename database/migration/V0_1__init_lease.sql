create schema if not exists lease;

------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY

-- sport

create table lease.sport
(
  id          serial      not null
    constraint sport_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  description varchar(90)
);

create unique index sport_code_uindex
  on lease.sport (code);

create unique index sport_name_uindex
  on lease.sport (name);

-- feature

create table lease.feature
(
  id          serial      not null
    constraint feature_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  description varchar(90)
);

create unique index feature_code_uindex
  on lease.feature (code);

create unique index feature_name_uindex
  on lease.feature (name);

------------------------------------------------------------------------------------------------------------------------
-- REPOSITORY

-- playground

create table lease.playground
(
  id      serial               not null
    constraint user_pk
    primary key,

  name    varchar(45)          not null,
  address varchar(90)          not null,
  phone   varchar(16)          not null,
  rate    integer default 0    not null
);

-- photo

create table lease.photo
(
  picture_id    integer not null
    constraint photo_picture_id_fk
    references common.picture,

  playground_id integer not null
    constraint photo_playground_id_fk
    references lease.playground,

  constraint photo_pk
  primary key (picture_id, playground_id)
);

-- ownership

create table lease.ownership
(
  user_id       integer not null
    constraint ownership_user_id_fk
    references common."user",

  playground_id integer not null
    constraint ownership_playground_id_fk
    references lease.playground,

  constraint ownership_pk
  primary key (user_id, playground_id)
);

-- specialization

create table lease.specialization
(
  sport_id      integer not null
    constraint specialization_sport_id_fk
    references lease.sport,

  playground_id integer not null
    constraint specialization_playground_id_fk
    references lease.playground,

  constraint specialization_pk
  primary key (sport_id, playground_id)
);

-- capabilitiy

create table lease.capability
(
  feature_id    integer not null
    constraint capability_feature_id_fk
    references lease.feature,

  playground_id integer not null
    constraint capability_playground_id_fk
    references lease.playground,

  constraint capability_pk
  primary key (feature_id, playground_id)
);
