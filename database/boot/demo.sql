------------------------------------------------------------------------------------------------------------------------
-- COMMON

-- user

insert into common."user" (id, avatar_id, login, password, name, surname, patronymic, address, phone) VALUES
  (1, null, 'root', '$2a$10$6ugKXFk4PvEWwxapdDTY7e3TLIu3pkRVr4Elf6ltTbImptM..EHc2',
   'Намеднев', 'Артем', 'Александрович', 'г. Воронеж', '+7(999)999-99-99');

alter sequence common.user_id_seq restart with 2;

-- authority

insert into common.authority (user_id, role_id) values
  (1, 1),
  (1, 2);

------------------------------------------------------------------------------------------------------------------------
-- LEASE

-- playground

insert into lease.playground (id, name, address, phone, rate, opening, closing, cost) values
  (1, 'Площадка-1', 'Адрес-1', '+7(999)999-99-99', 1, '0001-01-01 08:00:00', '0001-01-01 22:00:00', 200000),
  (2, 'Площадка-2', 'Адрес-2', '+7(999)999-99-99', 5, '0001-01-01 08:00:00', '0001-01-01 22:00:00', 250000),
  (3, 'Площадка-3', 'Адрес-3', '+7(999)999-99-99', 6, '0001-01-01 08:00:00', '0001-01-01 22:00:00', 300000),
  (4, 'Площадка-4', 'Адрес-4', '+7(999)999-99-99', 10, '0001-01-01 08:00:00', '0001-01-01 22:00:00', 190000);

alter sequence lease.playground_id_seq restart with 5;

insert into lease.capability (feature_id, playground_id) values
  (1, 1),
  (2, 1),
  (2, 2),
  (3, 2);

insert into lease.specialization (sport_id, playground_id) values
  (1, 1),
  (2, 1),
  (2, 2),
  (3, 2);
