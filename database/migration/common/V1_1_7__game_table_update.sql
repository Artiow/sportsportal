truncate table tournament.game cascade;

-- noinspection SqlAddNotNullColumn
alter table tournament.game
  alter column tour_id set not null,
  add column "order" integer null default null,
  add column red_team_participation_id integer not null,
  add column blue_team_participation_id integer not null,
  add column tournament_id integer not null,
  add foreign key (red_team_participation_id, tournament_id) references tournament.team_participation (team_id, tournament_id),
  add foreign key (blue_team_participation_id, tournament_id) references tournament.team_participation (team_id, tournament_id),
  add column child_game_id integer null constraint game_child_game_id_fk references tournament.game;

-- noinspection SqlResolve
alter table tournament.game
  rename constraint game_red_team_participation_id_fkey to game_red_team_participation_id_fk;

-- noinspection SqlResolve
alter table tournament.game
  rename constraint game_blue_team_participation_id_fkey to game_blue_team_participation_id_fk;