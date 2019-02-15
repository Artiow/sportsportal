-- noinspection SqlAddNotNullColumnForFile

truncate table security.key cascade;

alter table security.key
  add iat timestamp not null,
  add exp timestamp not null;