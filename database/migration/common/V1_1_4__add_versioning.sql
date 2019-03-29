alter table tournament.player
  add version bigint not null default 0;

alter table tournament.team
  add version bigint not null default 0;