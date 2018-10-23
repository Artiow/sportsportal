------------------------------------------------------------------------------------------------------------------------
-- CLEANING SPORTSPORTAL DATABASE 0.4.0

truncate table
common.role,
common.picture,
common.picture_size,
common."user",
common.authority,
lease.sport,
lease.feature,
lease.playground,
lease.photo,
lease.ownership,
lease.specialization,
lease.capability,
lease.order,
lease.reservation
cascade;

alter sequence common.role_id_seq
  restart with 1;
alter sequence common.picture_id_seq
  restart with 1;
alter sequence common.picture_size_id_seq
  restart with 1;
alter sequence common.user_id_seq
  restart with 1;
alter sequence lease.sport_id_seq
  restart with 1;
alter sequence lease.feature_id_seq
  restart with 1;
alter sequence lease.playground_id_seq
  restart with 1;
alter sequence lease.order_id_seq
  restart with 1;
