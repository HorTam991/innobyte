create table airline (
    id INT AUTO_INCREMENT PRIMARY KEY,
    airline_name varchar(100)
);

create table city (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_name varchar(100),
    population int
);

create table flight (
    id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id int references airline(id),
    from_city_id int references city(id),
    to_city_id int references city(id),
    distance int,
    flight_time int
);
