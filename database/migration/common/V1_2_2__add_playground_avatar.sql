alter table booking.playground
  add column avatar_id integer null
  constraint playground_avatar_id_fk references common.picture;