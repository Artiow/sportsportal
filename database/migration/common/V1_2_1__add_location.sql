create table common.location
(
  id        serial           not null
    constraint location_pk primary key,
  latitude  double precision not null,
  longitude double precision not null
);

alter table booking.playground
  add column location_id integer null
    constraint playground_location_id_fk references common.location;