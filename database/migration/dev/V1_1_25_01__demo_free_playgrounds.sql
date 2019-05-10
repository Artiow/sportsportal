insert into lease.playground (id, name, address, phone, tested, freed)
values (6,
        'Открытая площадка один',
        'г. Москва, ул. Ленина 6',
        '+7(999)999-99-99',
        false,
        true),
       (7,
        'Открытая площадка два',
        'г. Москва, ул. Ленина 7',
        '+7(999)999-99-99',
        false,
        true);

alter sequence lease.playground_id_seq
  restart with 8;

insert into lease.playground_ownership (user_id, playground_id)
values (1, 6),
       (1, 7);

insert into lease.playground_capability (feature_id, playground_id)
values (1, 6),
       (2, 6),
       (2, 7),
       (3, 7);

insert into lease.playground_specialization (sport_id, playground_id)
values (1, 6),
       (2, 6),
       (2, 7),
       (3, 7);