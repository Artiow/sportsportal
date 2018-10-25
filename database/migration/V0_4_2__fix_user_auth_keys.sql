-- contains unresolved references
-- noinspection SqlResolveForFile

alter table common."user"
  drop column access_key_id,
  drop column refresh_key_id;

drop table common.key cascade;

create schema if not exists security;

create table security.key
(
  id         serial      not null
    constraint key_pk
    primary key,

  uuid       uuid        not null,

  type       varchar(15) not null,

  user_id    integer     not null
    constraint key_user_id_fk
    references common."user",

  related_id integer
    constraint key_related_id_fk
    references security.key
);
