alter table tournament.player_participation
  add column games_num integer not null default 0,
  add column goals_num integer not null default 0,
  add column autogoals_num integer not null default 0,
  add column yellow_cards_num integer not null default 0,
  add column red_cards_num integer not null default 0;