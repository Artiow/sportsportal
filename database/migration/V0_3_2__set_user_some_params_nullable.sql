alter table common."user"
  alter column patronymic drop not null;

alter table common."user"
  alter column address drop not null;

alter table common."user"
  alter column phone drop not null;