create table common.key
(
  id   serial      not null
    constraint key_pk
    primary key,

  uuid varchar(60) not null
);

alter table common."user"
  add access_key_id integer null constraint user_access_key_id_fk references common.key,
  add refresh_key_id integer null constraint user_refresh_key_id_fk references common.key;