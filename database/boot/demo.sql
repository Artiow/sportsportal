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

insert into lease.playground (id, name, address, phone, opening, closing, cost) values
  (1, 'playground', 'address', '+7(999)999-99-99', '0001-01-01 08:00:00', '0001-01-01 22:00:00', 200000);

alter sequence common.user_id_seq restart with 2;
