update common."user"
set phone = '+7(999)999-99-99' -- mock phone number
where phone isnull;

alter table common."user"
  alter column phone set not null;