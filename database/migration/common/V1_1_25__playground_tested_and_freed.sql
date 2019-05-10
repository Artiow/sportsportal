alter table lease.playground
  rename column test to tested;

alter table lease.playground
  alter column tested set default false;

alter table lease.playground
  add column freed boolean not null default false;