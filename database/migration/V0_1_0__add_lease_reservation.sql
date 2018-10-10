alter table lease.playground
  add opening timestamp not null default '0001-01-01 00:00:00',
  add closing timestamp not null default '0001-01-01 00:00:00',
  add half_hour_available boolean not null default true,
  add full_hour_required boolean not null default false,
  add price numeric(8, 2) not null default 0.00;

create table lease.order
(
  id          serial        not null
    constraint order_pk
    primary key,

  customer_id integer       not null
    constraint order_customer_id_fk
    references common."user",

  price       numeric(8, 2) not null default 0.00,
  paid        boolean       not null default false,
  datetime    timestamp     not null,
  expiration  timestamp
);

create table lease.reservation
(
  order_id      integer       not null
    constraint reservation_order_id_fk
    references lease.order,

  playground_id integer       not null
    constraint reservation_playground_id_fk
    references lease.playground,

  datetime      timestamp     not null,
  price         numeric(8, 2) not null default 0.00,

  constraint reservation_pk
  primary key (playground_id, datetime)
);

create unique index reservation_params_uindex
  on lease.reservation (playground_id, datetime);