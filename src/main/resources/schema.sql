--CREATE TABLE location(
--	id INT AUTO_INCREMENT PRIMARY KEY
--	,name VARCHAR(35) NOT NULL
--	,country VARCHAR(25) NOT NULL
--	,created_at datetime DEFAULT CURRENT_TIMESTAMP
--	,CONSTRAINT uq_cities_name UNIQUE (name)
--);

CREATE TABLE daily_forecasts(
	date date NOT NULL
--	,city_id INT NOT NULL
--	「name」だとわかりづらいのでcityにしている。MyBatisでnameに変換する
	,city VARCHAR(35) NOT NULL
	,country VARCHAR(25) NOT NULL
	,max_temp INT NOT NULL
	,min_temp  INT NOT NULL
	,avg_temp INT NOT NULL
--	,condition_code INT NOT NULL
    ,condition VARCHAR(23) NOT NULL
	,chance_of_rain INT NOT NULL
	,avg_humidity INT NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date, city)
--	このアプリでは観測地点名のみを利用する場面がないので、地点名と検索結果を一緒に登録してもいい気がする。
--	SELECTの際に毎回テーブル結合するのもメモリを食うし、INSERTの際にも地点コードを一度DBから取り出し
--	てから挿入するのは無駄な気がした。
--	,PRIMARY KEY (date, city_id)
--    ,CONSTRAINT fk_daily_forecasts_city_id FOREIGN KEY (city_id)
--        REFERENCES cities(id)
--    ,CONSTRAINT fk_daily_forecasts_condition_code FOREIGN KEY (condition_code)
--        REFERENCES weather_conditions(code)
);

CREATE TABLE hourly_forecasts(
	time datetime NOT NULL
	,city VARCHAR(35) NOT NULL
	,country VARCHAR(25) NOT NULL
--	,city_id INT NOT NULL
	,temp INT NOT NULL
--	,condition_code INT NOT NULL
    ,condition VARCHAR(23) NOT NULL
	,chance_of_rain INT NOT NULL
	,humidity INT NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (time, city)
--	,PRIMARY KEY (time, city_id)
--    ,CONSTRAINT fk_hourly_forecasts_city_id FOREIGN KEY (city_id)
--        REFERENCES cities(id)
--    ,CONSTRAINT fk_hourly_forecasts_condition_code FOREIGN KEY (condition_code)
--        REFERENCES weather_conditions(code)
);

--天気情報が英語だったので、全て翻訳してテーブルに格納してcodeをもとに日本語表示をしようとしたが、
--API自身が日本語に対応しているのでテーブルは不要。
--CREATE TABLE weather_conditions (
--    code INT PRIMARY KEY
--    ,condition VARCHAR(23) NOT NULL
--    ,created_at datetime DEFAULT CURRENT_TIMESTAMP
--);
