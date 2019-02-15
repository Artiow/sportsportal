alter table common."user"
  add confirm_code varchar(254);

create unique index user_confirm_code_uindex
  on common."user" (confirm_code);