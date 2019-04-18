alter table tournament.player
  add column locked boolean not null default false,
  add column disabled boolean not null default true;

drop index tournament.player_name_surname_birthdate_uindex;

create unique index player_name_surname_patronymic_birthdate_uindex
  on tournament.player (name, surname, patronymic, birthdate);