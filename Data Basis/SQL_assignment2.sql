--Inserting data for all tables 
INSERT INTO CloudGamingServices(Cgid, Name, Price, Provider) VALUES (1, 'Stadia', 10, 'Google')

INSERT INTO Genre(Gid, Type) VALUES (1, 'Sandbox'), (3, 'MMO')

INSERT INTO DevelopmentStudio(Dsid, Name, FoundingDate, Origin) VALUES (1, 'Mojang', 01-05-2009, 'Sweden')

INSERT INTO Publisher(Pid, Name, FoundingDate, Origin) VALUES (1, 'Mojang', 01-05-2009, 'Sweden')

INSERT INTO OnlinePlatforms(Opid, Name, URL, Developers, Release) VALUES (1, 'Minecraft Launcher', 'https://www.minecraft.net/en-us/about-minecraft', 'Microsoft', 01-05-2009)

INSERT INTO Shop(Sid, Name, City, Clients) VALUES (1, 'Gamestop', 'San Francisco', 1000000)

INSERT INTO ShopManager(Smid, Name, Shop_id) VALUES (1, 'John', 1), (2, 'Sid', 1)

INSERT INTO Product(Prid, Consoles_id, Console_dev_id) VALUES (1, 1, 1)

INSERT INTO Consoles(Cid, Model, Origin, Release, Price) VALUES (1, 'Nintendo Switch', 'Japan', 03-03-2017, 300)

INSERT INTO VideoGame(Vid, Name, ReleaseDate, Price, Genre_id, Dev_id, Dev_id, Pub_id, Sales, Online_plat, Retail_shop, Cloud_gaming, Consoles) VALUES (1, 'Minecraft', 12-05-2009, 20, 1, 1, 1, 1, 1000000, 1, 1, 1, 1)

--Wrong data inserted

INSERT INTO Genre(Gid, Type) VALUES (1, 'Platformer')
--this will create a conflict because this uses the same primary key as another data already added into the database


--Updating data
UPDATE Genre
SET Type='Open World' WHERE Gid IN(1, 2, 3)

UPDATE ShopManager
SET Name='Andrew' WHERE name LIKE 'John%'

UPDATE Genre
SET Type='RPG' WHERE Gid BETWEEN 1 AND 2


--Deleting data 
DELETE FROM Genre WHERE Gid = 3

DELETE FROM ShopManager WHERE Smid IN(2, 3)

--Union operation
SELECT Dsid FROM DevelopmentStudio
WHERE Name LIKE 'Mojang%' or Name LIKE 'Blizzard%'
UNION
SELECT Pid FROM Publisher

SELECT Cid FROM Consoles
WHERE Model LIKE 'Nintendo%' or Model LIKE 'Playstation%'
UNION
SELECT Cdid FROM ConsoleDeveloper

--Intersect operation
SELECT Smid, Name FROM ShopManager
WHERE Smid IN(2, 4)
INTERSECT
SELECT Sid, Name FROM Shop

SELECT Dsid, Name FROM DevelopmentStudio
INTERSECT
SELECT Pid, Name FROM Publisher
WHERE Pid IN(2, 4)

--Except operation
SELECT Cgid, Name FROM CloudGamingServices
EXCEPT
SELECT Vid, Name FROM VideoGame
WHERE Vid NOT IN(2, 4)

SELECT Opid, Name FROM OnlinePlatforms
EXCEPT
SELECT Vid, Name FROM VideoGame
WHERE Vid NOT IN(1, 3)

