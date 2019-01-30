alter table common.picture
  add owner_id integer null constraint picture_owner_id_fk references common."user";