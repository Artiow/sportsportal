-- noinspection SqlResolveForFile

alter table tournament.tour_bundle
  add column tournament_id integer null,
  add foreign key (tournament_id) references tournament.tournament (id);

create unique index tour_bundle_tournament_id_uindex
  on tournament.tour_bundle (tournament_id);

alter table tournament.tournament
  drop column bundle_id;