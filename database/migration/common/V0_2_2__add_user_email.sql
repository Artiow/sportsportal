alter table common."user"
  alter column password set data type varchar(60),
  alter column address set data type varchar(254),
  add email varchar(254) not null default '';

create unique index user_email_uindex
  on common."user" (email);