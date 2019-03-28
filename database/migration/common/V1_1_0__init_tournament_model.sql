create schema if not exists tournament;

------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY

-- tour_bundle_type

create table tournament.tour_bundle_type
(
  id          serial      not null
    constraint tour_bundle_type_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  description varchar(90)
);

create unique index tour_bundle_type_code_uindex
  on tournament.tour_bundle_type (code);

create unique index tour_bundle_type_name_uindex
  on tournament.tour_bundle_type (name);

-- tour_bundle_structure

create table tournament.tour_bundle_structure
(
  id          serial      not null
    constraint tour_bundle_structure_pk
    primary key,

  code        varchar(45) not null,
  name        varchar(45) not null,
  description varchar(90)
);

create unique index tour_bundle_structure_code_uindex
  on tournament.tour_bundle_structure (code);

create unique index tour_bundle_structure_name_uindex
  on tournament.tour_bundle_structure (name);

------------------------------------------------------------------------------------------------------------------------
-- REPOSITORY

-- tournament

create table tournament.tournament
(
  id             serial    not null
    constraint tournament_pk
    primary key,

  tour_bundle_id integer   not null
    constraint tournament_tour_bundle_id_fk
    references tournament.tour_bundle,

  start_date     timestamp not null,
  finish_date    timestamp null
);

create unique index tournament_tour_bundle_id_uindex
  on tournament.tournament (tour_bundle_id);

-- tour_bundle

create table tournament.tour_bundle (
  id                  serial      not null
    constraint tour_bundle_pk
    primary key,

  parent_id           integer     null
    constraint tour_bundle_parent_id_fk
    references tournament.tour_bundle,

  bundle_type_id      integer     not null
    constraint tour_bundle_bundle_type_id_fk
    references tournament.tour_bundle_type,

  bundle_structure_id integer     not null
    constraint tour_bundle_bundle_structure_id_fk
    references tournament.tour_bundle_structure,

  text_label          varchar(45) not null,
  numeric_label       integer     not null,
  completded          boolean     not null default false,
  tourless            boolean     not null default false
);
