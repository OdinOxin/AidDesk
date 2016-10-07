CREATE TABLE Countries
(
	ID INT IDENTITY(1,1),
	Alpha2 VARCHAR(2),
	Alpha3 VARCHAR(3),
	Name VARCHAR(250),
	AreaCode VARCHAR(5),
)
INSERT INTO Countries VALUES ('DE', 'DEU', 'Deutschland', '+49')
SELECT * FROM Countries

CREATE TABLE Addresses
(
	ID INT IDENTITY(1,1),
	Country INT,
	Zip VARCHAR(7),
	City VARCHAR(75),
	Street VARCHAR(100),
	HsNo VARCHAR(10),
)
INSERT INTO Addresses VALUES (1, '48167', 'Münster', 'Agathastraße', '90')
INSERT INTO Addresses VALUES (1, '48159', 'Münster', 'Diesterwegstraße', '17')
INSERT INTO Addresses VALUES (1, '48155', 'Münster', 'Liboristraße', NULL)
SELECT * FROM Addresses

DROP VIEW V_Adresses
CREATE VIEW V_Adresses AS SELECT
Adr.ID AS ID,
ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') AS Text,
ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') + ISNULL('
' + CONVERT(varchar(50), C.Name), '') AS SubText
FROM Addresses Adr
INNER JOIN Countries C ON Adr.Country = C.ID
SELECT * FROM V_Adresses

DROP TABLE Humans
CREATE TABLE Humans
(
	ID INT IDENTITY(1,1),
	ShortKey VARCHAR(16),
	Name VARCHAR(250),
	Forename VARCHAR(250),
	Pwd VARCHAR(250),
	Address INT,
)
INSERT INTO Humans VALUES ('DavTo', 'Toboll', 'David', '13579', 3)
INSERT INTO Humans VALUES ('InBa', 'Bahloul', 'Ines', '67890', 2)
INSERT INTO Humans VALUES ('LuWec', 'Weckermann', 'Lucas', '12345', 1)
INSERT INTO Humans VALUES ('Chriss', 'Weckermann', 'Christian', '24680', 1)
SELECT * FROM Humans

DROP VIEW V_Login
CREATE VIEW V_Login AS SELECT ID, Forename + ' ' + Name AS Text, ShortKey AS SubText FROM Humans
SELECT * FROM V_Login

DROP VIEW V_Humans
CREATE VIEW V_Humans AS SELECT
H.ID AS ID,
ISNULL(CONVERT(varchar(50), Forename), '') + ISNULL(' ' + CONVERT(varchar(50), Name), '') AS Text,
ISNULL(CONVERT(varchar(50), ShortKey), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') AS SubText
FROM Humans H
INNER JOIN Addresses Adr ON H.Address = Adr.ID
SELECT * FROM V_Humans