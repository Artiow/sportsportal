alter table lease.playground
  add opening timestamp not null,
  add closing timestamp not null,
  add half_hour_available boolean not null default true,
  add full_hour_required boolean not null default false,
  add cost integer not null;

create table lease.order
(
  id         serial                   not null
    constraint order_pk
    primary key,

  user_id    integer                  not null
    constraint order_user_id_fk
    references common."user",

  cost       integer                  not null,
  paid       boolean                  not null default false,
  datatime   timestamp with time zone not null,
  expiration timestamp with time zone not null
);

create table lease.reservation
(
  order_id      integer                  not null
    constraint reservation_order_id_fk
    references lease.order,

  playground_id integer                  not null
    constraint reservation_playground_id_fk
    references lease.playground,

  constraint reservation_pk
  primary key (order_id, playground_id),

  datatime      timestamp with time zone not null
);

create unique index reservation_constraint_uindex
  on lease.reservation (order_id, playground_id, datatime);