------------------------------------------------------------------------------------------------------------------------
-- DEMO DATA EXAMPLE 0.2.3 FOR
-- SPORTSPORTAL DATABASE 0.2.1

------------------------------------------------------------------------------------------------------------------------
-- COMMON

-- user

insert into common."user" (id, avatar_id, login, password, name, surname, patronymic, address, phone)
values (1,
        null,
        'root',
        '$2a$10$6ugKXFk4PvEWwxapdDTY7e3TLIu3pkRVr4Elf6ltTbImptM..EHc2',
        'Иванов',
        'Иван',
        'Иванович',
        'г. Москва, ул. Ленина 0',
        '+7(999)999-99-99');

alter sequence common.user_id_seq
  restart with 2;

-- authority

insert into common.authority (user_id, role_id)
values (1, 1),
       (1, 2);

------------------------------------------------------------------------------------------------------------------------
-- LEASE

-- playground

insert into lease.playground (id,
                              name,
                              address,
                              phone,
                              opening,
                              closing,
                              half_hour_available,
                              full_hour_required,
                              price)
values (1,
        'Площадка один',
        'г. Москва, ул. Ленина 1',
        '+7(999)999-99-99',
        '0001-01-01 09:00:00',
        '0001-01-01 21:00:00',
        true,
        true,
        2000.00);

insert into lease.playground (id, name, address, phone, opening, closing, price)
values (2,
        'Площадка два',
        'г. Москва, ул. Ленина 2',
        '+7(999)999-99-99',
        '0001-01-01 08:00:00',
        '0001-01-01 23:00:00',
        2500.00),
       (3,
        'Площадка три',
        'г. Москва, ул. Ленина 3',
        '+7(999)999-99-99',
        '0001-01-01 08:00:00',
        '0001-01-01 23:00:00',
        3000.00),
       (4,
        'Площадка четыре',
        'г. Москва, ул. Ленина 4',
        '+7(999)999-99-99',
        '0001-01-01 09:00:00',
        '0001-01-01 18:00:00',
        1900.00),
       (5,
        'Площадка пять',
        'г. Москва, ул. Ленина 5',
        '+7(999)999-99-99',
        '0001-01-01 00:00:00',
        '0001-01-01 00:00:00',
        3500.00);

alter sequence lease.playground_id_seq
  restart with 6;

insert into lease.capability (feature_id, playground_id)
values (1, 1),
       (2, 1),
       (2, 2),
       (3, 2);

insert into lease.specialization (sport_id, playground_id)
values (1, 1),
       (2, 1),
       (2, 2),
       (3, 2);
