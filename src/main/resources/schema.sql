CREATE TABLE location(
	id INT AUTO_INCREMENT PRIMARY KEY
	,name VARCHAR(50) NOT NULL
	,region VARCHAR(35) NOT NULL
	,country VARCHAR(25) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,CONSTRAINT uq_cities_name UNIQUE (name)
);

CREATE TABLE day_forecasts(
	date date NOT NULL
--	,city_id INT NOT NULL
--	「name」だとわかりづらいのでcityにしている。MyBatisでnameに変換する
	,city VARCHAR(35) NOT NULL
--	一度countryは廃止で。country→regionで復活予定
--	,country VARCHAR(25) NOT NULL
	,max_temp DECIMAL(4,1) NOT NULL
	,min_temp  DECIMAL(4,1) NOT NULL
	,avg_temp DECIMAL(4,1) NOT NULL
--	,condition_code INT NOT NULL
    ,condition VARCHAR(23) NOT NULL
    ,icon VARCHAR(9) NOT NULL
	,daily_chance_of_rain DECIMAL(4,1) NOT NULL
	,avg_humidity DECIMAL(4,1) NOT NULL
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

CREATE TABLE hour_forecasts(
	date date NOT NULL
	,time TIME NOT NULL
	,city VARCHAR(35) NOT NULL
--	一度countryは廃止で。country→regionで復活予定
--	,country VARCHAR(25) NOT NULL
--	,city_id INT NOT NULL
	,temp DECIMAL(4,1) NOT NULL
--	,condition_code INT NOT NULL
    ,condition VARCHAR(23) NOT NULL
    ,icon VARCHAR(9) NOT NULL
	,chance_of_rain DECIMAL(4,1) NOT NULL
	,humidity DECIMAL(4,1) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date,time, city)
--	,PRIMARY KEY (time, city_id)
--    ,CONSTRAINT fk_hourly_forecasts_city_id FOREIGN KEY (city_id)
--        REFERENCES cities(id)
--    ,CONSTRAINT fk_hourly_forecasts_condition_code FOREIGN KEY (condition_code)
--        REFERENCES weather_conditions(code)
);

CREATE TABLE alerts(
	alert_id INT AUTO_INCREMENT PRIMARY KEY
	date date NOT NULL
	,city VARCHAR(35) NOT NULL
	,alert_text text NOT NULL
);

--天気情報が英語だったので、全て翻訳してテーブルに格納してcodeをもとに日本語表示をしようとしたが、
--API自身が日本語に対応しているのでテーブルは不要。
--CREATE TABLE weather_conditions (
--    code INT PRIMARY KEY
--    ,condition VARCHAR(23) NOT NULL
--    ,created_at datetime DEFAULT CURRENT_TIMESTAMP
--);
