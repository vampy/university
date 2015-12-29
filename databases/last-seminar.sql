USE trains_seminar;

DROP TABLE stations_routes;
DROP TABLE [routes];
DROP TABLE stations;
DROP TABLE trains;
DROP TABLE train_types;
GO;


CREATE TABLE train_types(
    type_id            TINYINT PRIMARY KEY IDENTITY(1, 1),
    description        VARCHAR(256)
);

CREATE TABLE trains(
    train_id     INT   PRIMARY KEY IDENTITY(1, 1),
    name         VARCHAR(256),
    type_id      TINYINT REFERENCES train_types(type_id)
);

CREATE TABLE stations(
    station_id   INT PRIMARY KEY IDENTITY(1, 1),
    name         VARCHAR(256) UNIQUE
);

CREATE TABLE [routes](
    route_id INT PRIMARY KEY IDENTITY(1, 1),
    name     VARCHAR(256) UNIQUE,
    train_id INT REFERENCES trains(train_id)
);

CREATE TABLE stations_routes(
    station_id       INT        REFERENCES stations(station_id),
    route_id         INT        REFERENCES [routes](route_id),
    atime            TIME,
    dtime            TIME,
    PRIMARY KEY(station_id, route_id)
);


-- 2. procedure that inserts into the database
CREATE PROCEDURE updateRoutes
    @name VARCHAR(256),
    @sname VARCHAR(256),
    @atime TIME,
    @dtime TIME
AS

    DECLARE @sid INT = (SELECT name FROM stations WHERE name = @sname);
    DECLARE @rid INT = (SELECT name FROM [routes] WHERE name = @name);
    IF EXISTS(
        SELECT station_id, route_id FROM stations_routes
        WHERE  station_id = @sid AND route_id = @rid
    )
        UPDATE stations_routes SET atime = @atime, dtime = @dtime
        WHERE station_id = @sid AND route_id = @rid
    ELSE
        INSERT stations_routes VALUES(@sid, @rid, @atime, @dtime);
    GO
GO

-- EXEC updateRoutes 't1', 's1', '8:10', '12:10';

-- 3. create view, shows name of all routes that containt all the stations
CREATE VIEW view_routes_all_stations
AS
SELECT name FROM [routes]
WHERE route_id IN (
    SELECT route_id, COUNT(station_id)
    FROM stations_routes
    GROUP BY route_id
    HAVING COUNT(*) = (SELECT COUNT(*) FROM stations)
)

-- 4.
CREATE FUNCTION crowdedStations(
    @time TIME
)
RETURNS TABLE
AS
RETURN
    SELECT name FROM stations
    WHERE station_id  IN (
        SELECT station_id
        FROM stations_routes
        WHERE atime <= @time AND @time <= dtime
        GROUP BY station_id
        HAVING COUNT(*) >= 3
    )
