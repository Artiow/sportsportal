------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY BOOT

-- common.role

insert into common.role (id, code, name, description) values
  (1, 'admin', 'Администратор', 'администратор сайта'),
  (2, 'user', 'Пользователь', 'обычный пользователь');

alter sequence common.role_id_seq restart with 3;

-- common.picture_size

insert into common.picture_size (id, code, name, width, height, description) values
  (1, 'xs', 'очень маленькая', 320, 200, '320х200 очень маленькая'),
  (2, 'sm', 'маленькая', 500, 250, '500х250 маленькая'),
  (3, 'md', 'средняя', 800, 600, '800х600 средняя'),
  (4, 'lg', 'большая', 1024, 768, '1024х768 большая'),
  (5, 'xl', 'очень большая', 1280, 960, '1280х960 очень большая');

alter sequence common.role_id_seq restart with 6;

-- lease.sport

insert into lease.sport (id, code, name, description) values
  (1, 'football', 'футбол', 'футбол'),
  (2, 'basketball', 'баскетбол', 'баскетбол'),
  (3, 'volleyball', 'волейбол', 'волейбол');

alter sequence common.role_id_seq restart with 4;

-- lease.feature

insert into lease.feature (id, code, name, description) values
  (1, 'locker', 'раздевалки', 'раздевалки'),
  (2, 'shower', 'душевая', 'душевая'),
  (3, 'parking', 'парковка', 'парковка');

alter sequence common.role_id_seq restart with 4;