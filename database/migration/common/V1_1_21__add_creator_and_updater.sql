alter table tournament.team
  add column creator_id integer not null default 1 constraint team_creator_id_fk references common."user",
  add column created_at timestamp not null default now(),
  add column updater_id integer not null default 1 constraint team_updater_id_fk references common."user",
  add column updated_at timestamp not null default now();

alter table tournament.player
  add column creator_id integer not null default 1 constraint player_creator_id_fk references common."user",
  add column created_at timestamp not null default now(),
  add column updater_id integer not null default 1 constraint player_updater_id_fk references common."user",
  add column updated_at timestamp not null default now();