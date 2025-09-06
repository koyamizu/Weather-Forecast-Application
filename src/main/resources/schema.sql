CREATE TABLE cities(
	id INT AUTO_INCREMENT PRIMARY KEY
	,name VARCHAR(35) NOT NULL
	,country VARCHAR(25) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,CONSTRAINT uq_cities_name UNIQUE (name)
);

CREATE TABLE daily_forecasts(
	date date NOT NULL
	,city_id INT NOT NULL
	,max_temp INT NOT NULL
	,min_temp  INT NOT NULL
	,avg_temp INT NOT NULL
	,condition_code INT NOT NULL
	,chance_of_rain INT NOT NULL
	,avg_humidity INT NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date, city_id)
    ,CONSTRAINT fk_daily_forecasts_city_id FOREIGN KEY (city_id)
        REFERENCES cities(id)
    ,CONSTRAINT fk_daily_forecasts_condition_code FOREIGN KEY (condition_code)
        REFERENCES weather_conditions(code)
);

CREATE TABLE hourly_forecasts(
	time datetime NOT NULL
	,city_id INT NOT NULL
	,temp INT NOT NULL
	,condition_code INT NOT NULL
	,chance_of_rain INT NOT NULL
	,humidity INT NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (time, city_id)
    ,CONSTRAINT fk_hourly_forecasts_city_id FOREIGN KEY (city_id)
        REFERENCES cities(id)
    ,CONSTRAINT fk_hourly_forecasts_condition_code FOREIGN KEY (condition_code)
        REFERENCES weather_conditions(code)
);

CREATE TABLE weather_conditions (
    code INT PRIMARY KEY,
    condition VARCHAR(23) NOT NULL
    ,created_at datetime DEFAULT CURRENT_TIMESTAMP
);
