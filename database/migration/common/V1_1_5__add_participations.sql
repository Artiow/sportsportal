create table tournament.team_participation
(
  team_id            integer     not null
    constraint team_participation_team_id_fk
    references tournament.team,

  tournament_id      integer     not null
    constraint team_participation_tournament_id_fk
    references tournament.tournament,

  participation_name varchar(45) not null,

  constraint team_participation_pk
  primary key (team_id, tournament_id)
);

create table tournament.player_participation
(
  player_id integer not null
    constraint player_participation_player_id_fk
    references tournament.player,

  team_id   integer not null
    constraint player_participation_team_id_fk
    references tournament.team,

  constraint player_participation_pk
  primary key (player_id, team_id)
);