alter table tournament.team_participation
  add column fixed boolean not null default false;
alter table tournament.tournament
  add column fixed boolean not null default false;