CREATE TABLE locations(
	id INT AUTO_INCREMENT PRIMARY KEY
	,input VARCHAR(50) NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,city_region_romaji VARCHAR(60) NOT NULL
	,latlon VARCHAR(30) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE day_forecasts(
	date date NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,city_region_romaji VARCHAR(60) NOT NULL
	,max_temp DECIMAL(4,1) NOT NULL
	,min_temp  DECIMAL(4,1) NOT NULL
	,avg_temp DECIMAL(4,1) NOT NULL
    ,condition VARCHAR(23) NOT NULL
    ,icon VARCHAR(9) NOT NULL
	,daily_chance_of_rain DECIMAL(4,1) NOT NULL
	,avg_humidity DECIMAL(4,1) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date, city)
);

CREATE TABLE hour_forecasts(
	date date NOT NULL
	,time TIME NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,city_region_romaji VARCHAR(60) NOT NULL
	,temp DECIMAL(4,1) NOT NULL
    ,condition VARCHAR(23) NOT NULL
    ,icon VARCHAR(9) NOT NULL
	,chance_of_rain DECIMAL(4,1) NOT NULL
	,humidity DECIMAL(4,1) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date,time, city)
);