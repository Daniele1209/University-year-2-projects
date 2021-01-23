CREATE TABLE DevelopmentStudio
(Dsid INT PRIMARY KEY IDENTITY,
Name varchar(50),
FoundingDate date,
Origin varchar(50));

CREATE TABLE Genre
(Gid INT PRIMARY KEY IDENTITY,
Type varchar(50));

CREATE TABLE Publisher
(Pid INT PRIMARY KEY IDENTITY,
Name varchar(50),
FoundingDate date,
Origin varchar(50));

CREATE TABLE OnlinePlatforms
(Opid INT PRIMARY KEY IDENTITY,
Name varchar(50),
URL varchar(50),
Developers varchar(50),
Release date);

CREATE TABLE Consoles
(Cid INT PRIMARY KEY IDENTITY,
Model varchar(50),
Origin varchar(50),
Release date,
Price float);

CREATE TABLE CloudGamingServices
(Cgid INT PRIMARY KEY IDENTITY,
Name varchar(50),
Price float, 
Provider varchar(50));

CREATE TABLE Shop
(Sid INT PRIMARY KEY IDENTITY,
Name varchar(50),
City varchar(50),
Clients int);

CREATE TABLE VideoGame
(Vid INT PRIMARY KEY IDENTITY,
Name varchar(50),
ReleaseDate date,
Price float,
Genre_id int,
Dev_id int,
Pub_id int,
Sales int,
Online_plat int,
Retail_shop int,
Cloud_gaming int,
Consoles int,
constraint VideoGame_Genre_id_fk
	FOREIGN KEY (Genre_id) REFERENCES Genre(Gid),
constraint VideoGame_Developer_id_fk
	FOREIGN KEY (Dev_id) REFERENCES DevelopmentStudio(Dsid),
constraint VideoGame_Publisher_id_fk
	FOREIGN KEY (Pub_id) REFERENCES Publisher(Pid),
constraint VideoGame_Online_plat_id_fk
	FOREIGN KEY (Online_plat) REFERENCES OnlinePlatforms(Opid),
constraint VideoGame_Shop_plat_id_fk
	FOREIGN KEY (Retail_shop) REFERENCES Shop(Sid),
constraint VideoGame_Cloud_plat_id_fk
	FOREIGN KEY (Cloud_gaming) REFERENCES CloudGamingServices(Cgid),
constraint VideoGame_Consoles_plat_id_fk
	FOREIGN KEY (Consoles) REFERENCES Consoles(Cid)
);


CREATE TABLE ShopManager
(Smid INT PRIMARY KEY IDENTITY,
Name varchar(50),
Shop_id int,
constraint ShopManager_Shop_id_fk
	FOREIGN KEY (Shop_id) REFERENCES Shop(Sid));

CREATE TABLE ConsoleDeveloper
(Cdid INT PRIMARY KEY IDENTITY,
Name varchar(50),
Founding date,
Origin varchar(50),
CEO varchar(50));

CREATE TABLE Product
(Prid INT PRIMARY KEY IDENTITY,
Consoles_id int,
Console_dev_id int,
FOREIGN KEY (Consoles_id) REFERENCES Consoles(Cid),
FOREIGN KEY (Console_dev_id) REFERENCES ConsoleDeveloper(Cdid));