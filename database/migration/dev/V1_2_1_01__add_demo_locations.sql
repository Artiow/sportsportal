insert into common.location (id, latitude, longitude)
values (1, 55.755773, 37.617761),
       (2, 55.755773, 37.617761),
       (3, 55.755773, 37.617761),
       (4, 55.755773, 37.617761),
       (5, 55.755773, 37.617761),
       (6, 55.755773, 37.617761),
       (7, 55.755773, 37.617761);

alter sequence tournament.player_id_seq
  restart with 8;

update booking.playground
set location_id = common.location.id
from common.location
where common.location.id = booking.playground.id;