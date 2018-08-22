alter table lease.playground
  add opening timestamp not null,
  add closing timestamp not null,
  add half_hour_available boolean not null default true,
  add full_hour_required boolean not null default false,
  add cost integer not null;

create table lease.order
(
  id            serial    not null
    constraint order_pk
    primary key,

  customer_id   integer   not null
    constraint order_customer_id_fk
    references common."user",

  playground_id integer   not null
    constraint order_playground_id_fk
    references lease.playground,

  cost          integer   not null,
  paid          boolean   not null default false,
  datetime      timestamp not null,
  expiration    timestamp
);

create table lease.reservation
(
  order_id      integer   not null
    constraint reservation_order_id_fk
    references lease.order,

  reserved_date timestamp not null,
  reserved_time timestamp not null,
  cost          integer   not null,

  constraint reservation_pk
  primary key (order_id, reserved_date, reserved_time)
);