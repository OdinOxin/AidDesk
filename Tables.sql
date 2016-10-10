DROP TABLE Translations
CREATE TABLE Translations
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	DEU TEXT,
	USA TEXT,
)
INSERT INTO Translations VALUES ('OK', 'OK')
INSERT INTO Translations VALUES ('Ja', 'Yes')
INSERT INTO Translations VALUES ('Nein', 'No')
INSERT INTO Translations VALUES ('Abbrechen', 'Cancel')
INSERT INTO Translations VALUES ('Speichern', 'Save')
INSERT INTO Translations VALUES ('Verwerfen', 'Discard')
INSERT INTO Translations VALUES ('Ändern', 'Change')
INSERT INTO Translations VALUES ('Auswahl', 'Selection')
INSERT INTO Translations VALUES ('ID', 'ID')
INSERT INTO Translations VALUES ('Person', 'Person')
INSERT INTO Translations VALUES ('Name', 'Name')
INSERT INTO Translations VALUES ('Vorname', 'Forename')
INSERT INTO Translations VALUES ('Kürzel', 'Code')
INSERT INTO Translations VALUES ('Passwort', 'Password')
INSERT INTO Translations VALUES ('Adresse', 'Address')
INSERT INTO Translations VALUES ('Personen', 'People')
INSERT INTO Translations VALUES ('Änderugen verwerfen?', 'Discard changes?')
INSERT INTO Translations VALUES ('Es sind noch nicht gespeicherte Änderungen vorhanden!

Möchten Sie diese Änderungen verwerfen?', 'There are unsaved changes!

Do you want to discard these changes?')
INSERT INTO Translations VALUES ('Löschen', 'Delete')
INSERT INTO Translations VALUES ('Daten löschen?', 'Delete data?')
INSERT INTO Translations VALUES ('Wollen Sie die Daten wirklich unwiderruflich löschen?', 'Are you sure you want to delete the data irrevocably?')
INSERT INTO Translations VALUES ('Gelöscht!', 'Deleted!')
INSERT INTO Translations VALUES ('Die Daten wurden erfolgreich gelöscht.', 'Data have been deleted successfully.')
--INSERT INTO Translations VALUES ('', '')
SELECT * FROM Translations

DROP TABLE Countries
CREATE TABLE Countries
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Alpha2 VARCHAR(2),
	Alpha3 VARCHAR(3),
	Name VARCHAR(250),
	AreaCode VARCHAR(5),
)
INSERT INTO Countries VALUES ('DE', 'DEU', 'Deutschland', '+49')
SELECT * FROM Countries

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

DROP TABLE People
CREATE TABLE People
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	Code VARCHAR(16),
	Name VARCHAR(250),
	Forename VARCHAR(250),
	Pwd VARCHAR(250),
	Language VARCHAR(3),
	Address INT,
	CONSTRAINT fkAddress FOREIGN KEY (Address) REFERENCES Addresses(ID),
)
INSERT INTO People VALUES ('DavTo', 'Toboll', 'David', '13579', 'DEU', 3)
INSERT INTO People VALUES ('InBa', 'Bahloul', 'Ines', '67890', 'DEU', 2)
INSERT INTO People VALUES ('LuWec', 'Weckermann', 'Lucas', '12345', 'DEU', 1)
INSERT INTO People VALUES ('Chriss', 'Weckermann', 'Christian', '24680', 'DEU', 1)
SELECT * FROM People

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