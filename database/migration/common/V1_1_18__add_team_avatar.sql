alter table tournament.team
  add column avatar_id integer constraint team_avatar_id_fk references common.picture;