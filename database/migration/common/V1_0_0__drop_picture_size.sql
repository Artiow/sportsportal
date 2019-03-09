-- noinspection SqlResolve

alter table common.picture
  drop column size;

alter table common.picture_size
  add column "default" boolean not null default false;