alter table common."user"
  add version bigint not null default 0;

alter table lease.playground
  add version bigint not null default 0;