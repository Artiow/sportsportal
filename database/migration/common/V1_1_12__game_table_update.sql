-- noinspection SqlResolve
alter table tournament.game
  rename column red_team_participation_id to red_team_id;
-- noinspection SqlResolve
alter table tournament.game
  rename column blue_team_participation_id to blue_team_id;

-- noinspection SqlResolve
alter table tournament.game
  rename constraint game_red_team_participation_id_fk to game_red_team_id_fk;
-- noinspection SqlResolve
alter table tournament.game
  rename constraint game_blue_team_participation_id_fk to game_blue_team_id_fk;