DROP TABLE Translations
CREATE TABLE Translations
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	DEU VARCHAR(450),
	USA VARCHAR(450),
	CONSTRAINT cUnique UNIQUE(DEU, USA),
)

DROP TABLE RefBoxViews
CREATE TABLE RefBoxViews
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Name VARCHAR(50),
	ViewName VARCHAR(50),
	CONSTRAINT cUniqueName UNIQUE(ViewName),
	CONSTRAINT cUniqueViewName UNIQUE(ViewName),
)

DROP TABLE Countries
CREATE TABLE Countries
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Alpha2 VARCHAR(2),
	Alpha3 VARCHAR(3),
	Name VARCHAR(250),
	AreaCode VARCHAR(5),
)

DROP VIEW V_Country
CREATE VIEW V_Country AS SELECT
C.ID AS ID,
ISNULL(CONVERT(varchar(50), C.Name), '') AS Text,
ISNULL(CONVERT(varchar(50), C.Alpha2), '') + ISNULL(' / ' + CONVERT(varchar(50), C.Alpha3), '') + ISNULL(' / ' + CONVERT(varchar(50), C.AreaCode), '') AS SubText
FROM Countries C
SELECT * FROM V_Country

DROP TABLE Addresses
CREATE TABLE Addresses
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Country INT,
	Zip VARCHAR(7),
	City VARCHAR(75),
	Street VARCHAR(100),
	HsNo VARCHAR(10),
	CONSTRAINT fkCountry FOREIGN KEY (Country) REFERENCES Countries(ID),
)

DROP VIEW V_Addresses
CREATE VIEW V_Addresses AS SELECT
Adr.ID AS ID,
ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') AS Text,
ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') + ISNULL('
' + CONVERT(varchar(50), C.Name), '') AS SubText
FROM Addresses Adr
INNER JOIN Countries C ON Adr.Country = C.ID
SELECT * FROM V_Addresses

DROP TABLE People
CREATE TABLE People
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Code VARCHAR(16),
	Name VARCHAR(250),
	Forename VARCHAR(250),
	Pwd VARCHAR(250) NOT NULL,
	Language VARCHAR(3),
	Address INT,
	CONSTRAINT fkAddress FOREIGN KEY (Address) REFERENCES Addresses(ID),
)

DROP VIEW V_Login
CREATE VIEW V_Login AS SELECT ID, Forename + ' ' + Name AS Text, Code AS SubText FROM People
SELECT * FROM V_Login

DROP VIEW V_People
CREATE VIEW V_People AS SELECT
P.ID AS ID,
ISNULL(CONVERT(varchar(50), Forename), '') + ISNULL(' ' + CONVERT(varchar(50), Name), '') AS Text,
ISNULL(CONVERT(varchar(50), Code), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') AS SubText
FROM People P
INNER JOIN Addresses Adr ON P.Address = Adr.ID
SELECT * FROM V_People