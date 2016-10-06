CREATE TABLE Humans
(
	ID INT IDENTITY(1,1),
	ShortKey VARCHAR(16),
	Name VARCHAR(250),
	Forename VARCHAR(250),
	Pwd VARCHAR(250),
)

INSERT INTO Humans VALUES ('LuWec', 'Weckermann', 'Lucas', '12345')
INSERT INTO Humans VALUES ('InBa', 'Bahloul', 'Ines', '67890')
INSERT INTO Humans VALUES ('DavTo', 'Toboll', 'David', '13579')

SELECT * FROM Humans

CREATE VIEW V_Login AS SELECT ID, Forename + ' ' + Name AS Text FROM Humans

SELECT * FROM V_Login