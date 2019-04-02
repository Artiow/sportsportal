create table tournament.player_result
(
  game_id          integer not null
    constraint player_result_game_id_fk
    references tournament.game,

  player_id        integer not null,
  team_id          integer not null,
  tournament_id    integer not null,

  constraint player_result_player_id_fk
  foreign key (player_id, team_id, tournament_id)
  references tournament.player_participation (player_id, team_id, tournament_id),

  constraint player_result_player_pk
  primary key (game_id, player_id, team_id, tournament_id),

  attended         boolean not null default true,
  goals_num        integer not null default 0,
  autogoals_num    integer not null default 0,
  yellow_cards_num integer not null default 0,
  red_cards_num    integer not null default 0
);