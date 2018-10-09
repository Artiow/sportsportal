------------------------------------------------------------------------------------------------------------------------
-- BOOT DATA EXAMPLE 0.3.2 FOR
-- SPORTSPORTAL DATABASE 0.3.2

------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY BOOT

-- common.role

insert into common.role (id, code, name, description)
values (1, 'admin', 'Администратор', 'администратор сайта'),
       (2, 'user', 'Пользователь', 'обычный пользователь');

alter sequence common.role_id_seq
  restart with 3;

-- common.picture_size

insert into common.picture_size (id, code, name, width, height, description)
values (1, 'xs', 'очень маленькая', 320, 200, '320х200 8:5 очень маленькая'),
       (2, 'sm', 'маленькая', 400, 240, '400х240 5:3 маленькая'),
       (3, 'md', 'средняя', 800, 600, '800х600 4:3 средняя'),
       (4, 'mdh', 'средняя широкая', 854, 480, '854х480 16:9 широкая средняя'),
       (5, 'lg', 'большая', 1152, 864, '1152х864 4:3 большая'),
       (6, 'lgh', 'большая широкая', 1280, 720, '1280х720 16:9 широкая большая');

alter sequence common.picture_size_id_seq
  restart with 7;

-- lease.sport

insert into lease.sport (id, code, name, description)
values (1, 'football', 'футбол', 'футбол'),
       (2, 'basketball', 'баскетбол', 'баскетбол'),
       (3, 'volleyball', 'волейбол', 'волейбол');

alter sequence lease.sport_id_seq
  restart with 4;

-- lease.feature

insert into lease.feature (id, code, name, description)
values (1, 'locker', 'раздевалки', 'раздевалки'),
       (2, 'shower', 'душевая', 'душевая'),
       (3, 'parking', 'парковка', 'парковка');

alter sequence lease.feature_id_seq
  restart with 4;