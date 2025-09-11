INSERT INTO
 day_forecasts (
    date, city_region
    ,city_region_romaji
    , max_temp, min_temp, avg_temp, condition,icon, daily_chance_of_rain, avg_humidity
)
 VALUES
  (
    '2025-09-07', '新宿区-東京都'
    ,'Shinjuku-ku-Tokyo-to'
    , 31.3, 25.9, 28.2, '晴れ', 'night/113', 0.0, 67.0
);

INSERT INTO
 hour_forecasts (date, time
 , city_region, city_region_romaji
, temp, condition,icon, chance_of_rain, humidity)
 VALUES
	('2025-09-07','00:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 26.0, '快晴', 'night/113' ,0.0, 73.0),
	('2025-09-07','01:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '所により曇り', 'night/113' ,0.0, 74.0),
	('2025-09-07','02:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '所により曇り', 'night/113' ,0.0, 75.0),
	('2025-09-07','03:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '所により曇り', 'night/113' ,0.0, 76.0),
	('2025-09-07','04:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '曇り', 'night/113' ,0.0, 77.0),
	('2025-09-07','05:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '所により曇り', 'night/113' ,0.0, 77.0),
	('2025-09-07','06:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 25.9, '所により曇り', 'night/113' ,0.0, 76.0),
	('2025-09-07','07:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 26.5, '所により曇り', 'night/113' ,0.0, 73.0),
	('2025-09-07','08:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 27.4, '所により曇り', 'night/113' ,0.0, 68.0),
	('2025-09-07','09:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.4, '所により曇り', 'night/113' ,0.0, 63.0),
	('2025-09-07','10:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 29.4, '晴れ', 'night/113' ,0.0, 59.0),
	('2025-09-07','11:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 30.2, '晴れ', 'night/113' ,0.0, 56.0),
	('2025-09-07','12:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 30.8, '晴れ', 'night/113' ,0.0, 55.0),
	('2025-09-07','13:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 31.2, '晴れ', 'night/113' ,0.0, 54.0),
	('2025-09-07','14:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 31.3, '晴れ', 'night/113' ,0.0, 54.0),
	('2025-09-07','15:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 31.1, '晴れ', 'night/113' ,0.0, 55.0),
	('2025-09-07','16:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 30.5, '晴れ', 'night/113' ,0.0, 58.0),
	('2025-09-07','17:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 31.0, '晴れ', 'night/113' ,0.0, 63.0),
	('2025-09-07','18:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.8, '快晴', 'night/113' ,0.0, 67.0),
	('2025-09-07','19:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.3, '快晴', 'night/113' ,0.0, 71.0),
	('2025-09-07','20:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.2, '快晴', 'night/113' ,0.0, 72.0),
	('2025-09-07','21:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.3, '快晴', 'night/113' ,0.0, 72.0),
	('2025-09-07','22:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.2, '快晴', 'night/113' ,0.0, 71.0),
	('2025-09-07','23:00', '新宿区-東京都','Shinjuku-ku-Tokyo-to', 28.0, '快晴', 'night/113' ,0.0, 72.0)
;

INSERT INTO
  locations(input,city_region,city_region_romaji,latlon)
VALUES
  ('草津','草津市-滋賀県','Kusatsu-shi-Shiga-ken','35.0094,135.9369')
  ,('草津','草津町-群馬県','Kusatsu-machi-Gunma-ken', '36.6269,138.6119')
;