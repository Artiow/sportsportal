truncate table tournament.player_participation cascade;

-- noinspection SqlResolve
alter table tournament.player_participation
  drop constraint player_participation_team_id_fk,
  add column tournament_id integer not null;

alter table tournament.player_participation
  add foreign key (team_id, tournament_id) references tournament.team_participation (team_id, tournament_id);

-- noinspection SqlResolve
alter table tournament.player_participation
  drop constraint player_participation_pk;

alter table tournament.player_participation
  add primary key (player_id, team_id, tournament_id);

-- noinspection SqlResolve
alter table tournament.player_participation
  rename constraint player_participation_team_id_fkey to player_participation_team_id_fk;

-- noinspection SqlResolve
alter table tournament.player_participation
  rename constraint player_participation_pkey to player_participation_pk;