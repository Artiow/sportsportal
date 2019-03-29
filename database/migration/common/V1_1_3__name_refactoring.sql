-- noinspection SqlResolveForFile

alter table common.picture
  rename column uploaded to upload_date;

alter table common.picture
  rename column owner_id to uploader_id;

alter table common.picture
  rename constraint picture_owner_id_fk to picture_uploader_id_fk;

alter table lease.photo
  rename to playground_photo;

alter table lease.ownership
  rename to playground_ownership;

alter table lease.specialization
  rename to playground_specialization;

alter table lease.capability
  rename to playground_capability;