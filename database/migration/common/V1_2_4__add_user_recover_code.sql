alter table common."user"
  add recover_code varchar(254);

create unique index user_recover_code_uindex
  on common."user" (recover_code);