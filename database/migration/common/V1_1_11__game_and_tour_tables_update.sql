-- noinspection SqlResolve
alter table tournament.game
  drop column "order";
alter table tournament.game
  add column datetime timestamp null,
  add column completed boolean not null default false;

-- noinspection SqlResolve
alter table tournament.tour
  rename column tour_bundle_id to bundle_id;
alter table tournament.tour
  add column completed boolean not null default false;

-- noinspection SqlResolve
alter table tournament.tournament
  rename column tour_bundle_id to bundle_id;
alter table tournament.tournament
  add column completed boolean not null default false;

alter table tournament.tournament
  alter column start_date drop not null;