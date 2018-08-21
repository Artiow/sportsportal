------------------------------------------------------------------------------------------------------------------------
-- COMMON

-- picture

INSERT INTO common.picture (id, size, uploaded) VALUES
  (1, 4096, '2018-08-11 17:00:00.000000');

ALTER SEQUENCE common.picture_id_seq RESTART WITH 2;

-- user

INSERT INTO common."user" (id, avatar_id, login, password, name, surname, patronymic, address, phone) VALUES
  (1, 1, 'root', '$2a$10$6ugKXFk4PvEWwxapdDTY7e3TLIu3pkRVr4Elf6ltTbImptM..EHc2',
   'Намеднев', 'Артем', 'Александрович', 'г. Воронеж', '+7(920)425-12-58');

ALTER SEQUENCE common.user_id_seq RESTART WITH 2;

-- authority

INSERT INTO common.authority (user_id, role_id) VALUES
  (1, 1),
  (1, 2);
