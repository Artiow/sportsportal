insert into tournament.tour_bundle_type (id, code, name, description)
values (1, 'tournament', 'турнир', null),
       (2, 'stage', 'стадия', null),
       (3, 'group', 'группа', null);

alter sequence tournament.tour_bundle_type_id_seq
  restart with 4;

insert into tournament.tour_bundle_structure (id, code, name, description, tourless, immutable)
values (1, 'none', 'отсутствует', null, true, true),
       (2, 'round-robin', 'круговая система', null, false, true),
       (3, 'swiss-system', 'швейцарская система', null, false, true),
       (4, 'playoff-single', 'олимпийская система', null, false, true),
       (5, 'playoff-triple', 'олимпийская система с игрой за третье место', null, false, true);

alter sequence tournament.tour_bundle_structure_id_seq
  restart with 6;
