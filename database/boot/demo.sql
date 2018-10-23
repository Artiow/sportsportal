------------------------------------------------------------------------------------------------------------------------
-- DEMO DATA EXAMPLE 0.4.0 FOR
-- SPORTSPORTAL DATABASE 0.4.0

------------------------------------------------------------------------------------------------------------------------
-- COMMON

-- user

insert into common."user" (id, name, surname, email, password)
values (1, 'Иван', 'Иванов', 'root@mail.com', '$2a$10$6ugKXFk4PvEWwxapdDTY7e3TLIu3pkRVr4Elf6ltTbImptM..EHc2'),
       (2, 'Максим', 'Максимов', 'user@mail.com', '$2a$10$Tf3DuvW9w.O.j3c5V.kxUeosg.8tFsbINs7rvZhz9U/bRAOSu/cJe');

alter sequence common.user_id_seq
  restart with 3;

-- authority

insert into common.authority (user_id, role_id)
values (1, 1),
       (1, 2),
       (2, 2);

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

insert into lease.ownership (user_id, playground_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5);

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
