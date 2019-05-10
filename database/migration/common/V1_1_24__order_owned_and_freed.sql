alter table lease."order"
  rename column occupied to owned;

alter table lease."order"
  add column freed boolean not null default false;