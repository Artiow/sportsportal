alter table common.picture
  add loaded boolean default false;

-- noinspection SqlWithoutWhere
update common.picture set loaded = true;