--Inserting data for all tables 
SET IDENTITY_INSERT CloudGamingServices ON
INSERT INTO CloudGamingServices(Cgid, Name, Price, Provider) VALUES (1, 'Stadia', 10, 'Google')
SET IDENTITY_INSERT CloudGamingServices OFF
SET IDENTITY_INSERT Genre ON
INSERT INTO Genre(Gid, Type) VALUES (1, 'Sandbox'), (3, 'MMO')
SET IDENTITY_INSERT Genre OFF
SET IDENTITY_INSERT DevelopmentStudio ON
INSERT INTO DevelopmentStudio(Dsid, Name, FoundingDate, Origin) VALUES (1, 'Mojang', '01-05-2009', 'Sweden')
SET IDENTITY_INSERT DevelopmentStudio OFF
SET IDENTITY_INSERT Publisher ON
INSERT INTO Publisher(Pid, Name, FoundingDate, Origin) VALUES (1, 'Mojang', '01-05-2009', 'Sweden')
SET IDENTITY_INSERT Publisher OFF
SET IDENTITY_INSERT OnlinePlatforms ON
INSERT INTO OnlinePlatforms(Opid, Name, URL, Developers, Release) VALUES (1, 'Minecraft Launcher', 'https://www.minecraft.net/en-us/about-minecraft', 'Microsoft', '01-05-2009')
SET IDENTITY_INSERT OnlinePlatforms OFF
SET IDENTITY_INSERT Shop ON
INSERT INTO Shop(Sid, Name, City, Clients) VALUES (1, 'Gamestop', 'San Francisco', 1000000), (2, 'Target', 'New York', 2000000)
SET IDENTITY_INSERT Shop OFF
SET IDENTITY_INSERT ShopManager ON
INSERT INTO ShopManager(Smid, Name, Shop_id) VALUES (1, 'John', 1), (2, 'Sid', 2)
SET IDENTITY_INSERT ShopManager OFF
SET IDENTITY_INSERT Consoles ON
INSERT INTO Consoles(Cid, Model, Origin, Release, Price) VALUES (1, 'Nintendo Switch', 'Japan', '03-03-2017', 300), (2, 'Playstation', 'Japan', '11-15-2013', 400)
SET IDENTITY_INSERT Consoles OFF
SET IDENTITY_INSERT Product ON
INSERT INTO Product(Prid, Consoles_id, Console_dev_id) VALUES (1, 1, 1)
SET IDENTITY_INSERT Product OFF
SET IDENTITY_INSERT VideoGame ON
INSERT INTO VideoGame(Vid, Name, ReleaseDate, Price, Genre_id, Dev_id, Pub_id, Sales, Online_plat, Retail_shop, Cloud_gaming, Consoles) VALUES (1, 'Minecraft', '12-05-2009', 20, 1, 1, 1, 1000000, 1, 1, 1, 1)
SET IDENTITY_INSERT VideoGame OFF
--Wrong data inserted
SET IDENTITY_INSERT Genre ON
INSERT INTO Genre(Gid, Type) VALUES (1, 'Platformer')
SET IDENTITY_INSERT Genre OFF
--this will create a conflict because this uses the same primary key as another data already added into the database


--Updating data
UPDATE Genre
SET Type='Open World' WHERE Gid IN(2, 3)

UPDATE ShopManager
SET Name='Andrew' WHERE name LIKE 'John%'

UPDATE Genre
SET Type='RPG' WHERE Gid BETWEEN 1 AND 2


--Deleting data 
DELETE FROM Genre WHERE Gid = 3

DELETE FROM ShopManager WHERE Smid IN(2, 3)

--Union operation a)
SELECT Dsid FROM DevelopmentStudio
WHERE Name LIKE 'Mojang%' or Name LIKE 'Blizzard%'
UNION
SELECT Pid FROM Publisher

SELECT Cid FROM Consoles
WHERE Model LIKE 'Nintendo%' or Model LIKE 'Playstation%'
UNION
SELECT Cdid FROM ConsoleDeveloper

--Intersect operation b)
SELECT Smid, Name FROM ShopManager
WHERE Smid IN(1, 4)
INTERSECT
SELECT Sid, Name FROM Shop

SELECT Dsid, Name FROM DevelopmentStudio
INTERSECT
SELECT Pid, Name FROM Publisher
WHERE Pid IN(2, 4)

--Except operation c)
SELECT Cgid, Name FROM CloudGamingServices
EXCEPT
SELECT Vid, Name FROM VideoGame
WHERE Vid NOT IN(2, 4)

SELECT Opid, Name FROM OnlinePlatforms
EXCEPT
SELECT Vid, Name FROM VideoGame
WHERE Vid NOT IN(1, 3)

--4 queries with INNER JOIN, LEFT JOIN, RIGHT JOIN, and FULL JOIN d)
--show: consoles and console developers, unused consoledev
SELECT C.Model, CD.Name
FROM Consoles C
RIGHT OUTER JOIN Product P ON C.Cid = P.Consoles_id
RIGHT OUTER JOIN ConsoleDeveloper CD ON P.Console_dev_id = CD.Cdid

--show: Shops and Games, Publishers without games
SELECT S.Name, P.Name
FROM Shop S
LEFT OUTER JOIN VideoGame VG ON S.Sid = VG.Retail_shop
LEFT OUTER JOIN Publisher P ON P.Pid = VG.Pub_id

--show: ConsoleDev and Consoles that have a game named "Minecraft" (Many to many relation)
SELECT CD.Name, C.Model
FROM ConsoleDeveloper CD
INNER JOIN Product P ON CD.Cdid = P.Console_dev_id
INNER JOIN Consoles C ON C.Cid = P.Consoles_id
INNER JOIN VideoGame VG ON C.Cid = VG.Consoles
WHERE VG.Name = 'Minecraft'

--show: display all Consoles and ConsoleDevs, even if dependencies do not exist
SELECT * FROM ConsoleDeveloper CD
FULL JOIN Product P ON CD.Cdid = P.Console_dev_id
FULL JOIN Consoles C ON C.Cid = P.Consoles_id

--IN operation e)
SELECT Name FROM Publisher WHERE Pid IN (SELECT Pub_id FROM VideoGame)
--show: all publishers developing games that are present in online platforms
SELECT Name FROM DevelopmentStudio WHERE Dsid IN (SELECT Dev_id FROM VideoGame WHERE 
					Online_plat IN(SELECT Opid FROM OnlinePlatforms WHERE Name LIKE '%VideoGame'))

--EXISTS operation f)
--show: publisher that have many published games (more than one)
SELECT * FROM Publisher P WHERE EXISTS(SELECT Pub_id FROM VideoGame V WHERE P.Pid = V.Pub_id GROUP BY Pub_id HAVING COUNT(*) > 1)

--show: shop that have a shop manager
SELECT * FROM ShopManager SM WHERE EXISTS(SELECT * FROM Shop S WHERE S.Sid = SM.Shop_id)

--FROM operation g)
--show: display games
SELECT * FROM (SELECT Name FROM VideoGame) AS N ORDER BY Name;

--show: consoles for all video games
SELECT V.Name FROM VideoGame V INNER JOIN(SELECT * FROM Consoles) AS C ON V.Consoles = C.Cid

--GROUP BY clause h)
--show: publisher that have many published games (more than one)
SELECT * FROM Publisher P WHERE EXISTS(SELECT Pub_id FROM VideoGame V WHERE P.Pid = V.Pub_id GROUP BY Pub_id HAVING COUNT(*) > 1)

--show: display video game genres used in video games
SELECT Type FROM Genre WHERE Gid IN(SELECT Genre_id FROM VideoGame)GROUP BY Type

--show: Video games that appear on most platforms
SELECT * FROM OnlinePlatforms WHERE Opid IN(SELECT Online_plat FROM VideoGame GROUP BY Online_plat HAVING COUNT(*) =
											(SELECT MAX(A.games) FROM (SELECT COUNT(*) games FROM VideoGame VG GROUP BY VG.Online_plat) A))

--show: games that have the price more than average
SELECT V.Price FROM VideoGame V GROUP BY V.Price HAVING V.Price > (SELECT AVG(VG.Price) avgPrice FROM VideoGame VG) ORDER BY V.Price ASC

--ANY || ALL i)
--show: cheapest game (ALL)
SELECT * FROM VideoGame V WHERE V.Price < ALL(SELECT price FROM VideoGame WHERE Vid <> V.Vid)

--show: ConsoleDevs with the longest CEO name (ALL)
SELECT * FROM ConsoleDeveloper CD WHERE LEN(CD.CEO) > ALL(SELECT LEN(Name) FROM ConsoleDeveloper WHERE Cdid <> CD.Cdid)

--show: Video games that have publishers (ANY)
SELECT * FROM Publisher WHERE Pid = ANY(SELECT Pub_id FROM VideoGame)

--show: video games that have "Sandbox" genre (ANY)
SELECT * FROM VideoGame WHERE Vid = ANY(SELECT Genre_id FROM Genre G WHERE G.Type = 'Sandbox')

--REWRITE
--using aggregation
SELECT * FROM VideoGame WHERE Price = (SELECT MIN(Price) FROM VideoGame)

SELECT * FROM ConsoleDeveloper WHERE CEO = (SELECT MAX(CEO) FROM ConsoleDeveloper)

--using [NOT] IN
SELECT * FROM Publisher WHERE Pid IN(SELECT Pub_id FROM VideoGame)

SELECT * FROM VideoGame WHERE Vid NOT IN (SELECT Genre_id FROM Genre G WHERE G.Type <> 'Sandbox')