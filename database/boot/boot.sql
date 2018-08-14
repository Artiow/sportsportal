------------------------------------------------------------------------------------------------------------------------
-- DICTIONARY BOOT

-- common.role

INSERT INTO common.role (id, code, name, description) VALUES
  (1, 'admin', 'Администратор', 'администратор сайта'),
  (2, 'user', 'Пользователь', 'обычный пользователь');

ALTER SEQUENCE common.role_id_seq RESTART WITH 3;

-- lease.sport

INSERT INTO lease.sport (id, code, name, description) VALUES
  (1, 'football', 'Футбол', 'футбол'),
  (2, 'basketball', 'Баскетбол', 'баскетбол'),
  (3, 'volleyball', 'Волейбол', 'волейбол');

ALTER SEQUENCE common.role_id_seq RESTART WITH 4;

-- lease.feature

INSERT INTO lease.feature (id, code, name, description) VALUES
  (1, 'locker', 'Раздевалки', 'раздевалки'),
  (2, 'shower', 'Душевая', 'душевая'),
  (3, 'parking', 'Парковка', 'парковка');

ALTER SEQUENCE common.role_id_seq RESTART WITH 4;