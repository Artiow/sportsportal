alter table common."user"
  add version bigint not null default 1;

alter table lease.playground
  add version bigint not null default 1;