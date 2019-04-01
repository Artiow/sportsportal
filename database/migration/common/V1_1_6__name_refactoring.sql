-- noinspection SqlResolveForFile

alter table lease."order"
  rename column by_owner to occupied;