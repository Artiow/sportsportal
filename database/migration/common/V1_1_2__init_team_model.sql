------------------------------------------------------------------------------------------------------------------------
-- REPOSITORY

-- player

create table tournament.player
(
  id         serial      not null
    constraint player_pk
    primary key,

  avatar_id  integer
    constraint player_avatar_id_fk
    references common.picture,

  name       varchar(45) not null,
  surname    varchar(45) not null,
  patronymic varchar(45) not null,
  birthdate  timestamp
);

-- player_binding

create table tournament.player_binding
(
  user_id   integer not null
    constraint player_binding_user_id_fk
    references common."user",

  player_id integer not null
    constraint player_binding_player_id_fk
    references tournament.player,

  constraint player_binding_pk
  primary key (user_id, player_id)
);

-- team

create table tournament.team (
  id              serial      not null
    constraint team_pk
    primary key,

  name            varchar(45) not null,

  captain_id      integer     not null
    constraint team_captain_id_fk
    references common."user",

  vice_captain_id integer     not null
    constraint team_vice_captain_id_fk
    references common."user",

  locked          boolean     not null default false,
  disabled        boolean     not null default true
);
