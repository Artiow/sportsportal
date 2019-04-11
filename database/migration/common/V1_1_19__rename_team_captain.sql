-- noinspection SqlResolveForFile

alter table tournament.team
  rename captain_id to main_captain_id;

alter table tournament.team
  rename constraint team_captain_id_fk to team_main_captain_id_fk;