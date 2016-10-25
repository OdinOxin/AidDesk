DROP VIEW V_Login
CREATE VIEW V_Login AS SELECT ID, Forename + ' ' + Name AS Text, Code AS SubText FROM People
SELECT * FROM V_Login

DROP VIEW V_Plugins
CREATE VIEW V_Plugins AS SELECT ID, Name AS Text, '' AS SubText FROM Plugins
SELECT * FROM V_Plugins

DROP VIEW V_People
CREATE VIEW V_People AS SELECT
P.ID AS ID,
ISNULL(CONVERT(varchar(50), Forename), '') + ISNULL(' ' + CONVERT(varchar(50), Name), '') AS Text,
ISNULL(CONVERT(varchar(50), Code), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') + '
' + ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') AS SubText
FROM People P
LEFT OUTER JOIN Addresses Adr ON P.Address = Adr.ID
SELECT * FROM V_People

DROP VIEW V_Countries
CREATE VIEW V_Countries AS SELECT
C.ID AS ID,
ISNULL(CONVERT(varchar(50), C.Name), '') AS Text,
ISNULL(CONVERT(varchar(50), C.Alpha2), '') + ISNULL(' / ' + CONVERT(varchar(50), C.Alpha3), '') + ISNULL(' / ' + CONVERT(varchar(50), C.AreaCode), '') AS SubText
FROM Countries C
SELECT * FROM V_Countries

DROP VIEW V_Addresses
CREATE VIEW V_Addresses AS SELECT
Adr.ID AS ID,
ISNULL(CONVERT(varchar(50), Adr.Street), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.HsNo), '') AS Text,
ISNULL(CONVERT(varchar(50), Adr.Zip), '') + ISNULL(' ' + CONVERT(varchar(50), Adr.City), '') + ISNULL('
' + CONVERT(varchar(50), C.Name), '') AS SubText
FROM Addresses Adr
LEFT OUTER JOIN Countries C ON Adr.Country = C.ID
SELECT * FROM V_Addresses

DROP VIEW V_ContactTypes
CREATE VIEW V_ContactTypes AS SELECT
CT.ID AS ID,
ISNULL(CONVERT(varchar(50), CT.Name), '') AS Text,
ISNULL(CONVERT(varchar(50), CT.Code), '') AS SubText
FROM ContactTypes CT
SELECT * FROM V_ContactTypes

DROP VIEW V_ContactInformation
CREATE VIEW V_ContactInformation AS SELECT
CI.ID AS ID,
ISNULL(CONVERT(VARCHAR(50), CT.Code), '') + ': ' + ISNULL(CONVERT(VARCHAR(50), CI.Information), '') AS Text,
ISNULL(CONVERT(VARCHAR(50), CT.Name), '') + ': ' + ISNULL(CONVERT(VARCHAR(50), CI.Information), '') AS SubText
FROM ContactInformation CI
LEFT OUTER JOIN ContactTypes CT ON CI.ContactType = CT.ID
SELECT * FROM V_ContactInformation

DROP VIEW V_Languages
CREATE VIEW V_Languages AS SELECT
L.ID AS ID,
L.Name AS Text,
L.Code AS SubText
FROM Languages L
SELECT * FROM V_Languages