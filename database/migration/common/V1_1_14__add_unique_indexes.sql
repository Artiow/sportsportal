alter table tournament.player
  alter column patronymic drop not null;

create unique index team_name_uindex
  on tournament.team (name);
create unique index team_participation_tournament_id_participation_name_uindex
  on tournament.team_participation (tournament_id, participation_name);
create unique index player_name_surname_birthdate_uindex
  on tournament.player (name, surname, birthdate);
create unique index player_participation_player_id_tournament_id_uindex
  on tournament.player_participation (player_id, tournament_id);
create unique index player_result_game_id_player_id_uindex
  on tournament.player_result (game_id, player_id);