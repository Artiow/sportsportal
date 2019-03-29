------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY

-- tour_bundle_structure (updating)

alter table tournament.tour_bundle_structure
  add column tourless boolean not null default false,
  add column immutable boolean not null default false;

------------------------------------------------------------------------------------------------------------------------
-- REPOSITORY

-- tour

create table tournament.tour
(
  id             serial      not null
    constraint tour_pk
    primary key,

  tour_bundle_id integer     not null
    constraint tour_tour_bundle_id_fk
    references tournament.tour_bundle,

  text_label     varchar(45) not null,
  numeric_label  integer     not null
);

create unique index tour_tour_bundle_id_text_label_uindex
  on tournament.tour (tour_bundle_id, text_label);

create unique index tour_tour_bundle_id_numeric_label_uindex
  on tournament.tour (tour_bundle_id, numeric_label);

-- game

create table tournament.game (
  id      serial  not null
    constraint game_pk
    primary key,

  tour_id integer null
    constraint game_tour_id_fk
    references tournament.tour
);
