DROP TABLE Translations
CREATE TABLE Translations
(
	ID INT IDENTITY(1,1) PRIMARY KEY,
	SYS VARCHAR(900) UNIQUE,
	DEU TEXT,
	USA TEXT,
)

INSERT INTO Translations VALUES ('ID', 'ID', 'ID')
INSERT INTO Translations VALUES ('OK', 'OK', 'OK')
INSERT INTO Translations VALUES ('Yes', 'Ja', 'Yes')
INSERT INTO Translations VALUES ('No', 'Nein', 'No')
INSERT INTO Translations VALUES ('Cancel', 'Abbrechen', 'Cancel')
INSERT INTO Translations VALUES ('Save', 'Speichern', 'Save')
INSERT INTO Translations VALUES ('Discard', 'Verwerfen', 'Discard')
INSERT INTO Translations VALUES ('Change', '�ndern', 'Change')
INSERT INTO Translations VALUES ('New', 'Neu', 'New')
INSERT INTO Translations VALUES ('Selection', 'Auswahl', 'Selection')

INSERT INTO Translations VALUES ('Main Menu', 'Hauptmen�', 'Main menu')
INSERT INTO Translations VALUES ('Log out', 'Ausloggen', 'Log out')
INSERT INTO Translations VALUES ('Log out?', 'Ausloggen?', 'Log out?')
INSERT INTO Translations VALUES ('Log out and close all related windows?', 'Ausloggen und alle zugeh�rigen Fenster schlie�en?', 'Log out and close all related windows?')
INSERT INTO Translations VALUES ('Exit', 'Beenden', 'Exit')
INSERT INTO Translations VALUES ('Exit?', 'Beenden?', 'Exit?')
INSERT INTO Translations VALUES ('Exit AidDesk?', 'AidDesk beenden und alle zugeh�rigen Fenster schlie�en?', 'Exit AidDesk and close all related windows?')

INSERT INTO Translations VALUES ('Invalid input!', 'Ung�ltige Eingabe!', 'Invalid input!')
INSERT INTO Translations VALUES ('Enter and repeat password.', 'Geben Sie das neue Passwort ein und wiederholen Sie dieses korrekt.', 'Enter the new password and repeat it correctly.')
INSERT INTO Translations VALUES ('Enter current password.', 'Geben Sie das aktuelle Passwort an, um ein neues Passwort zu speichern.', 'Enter the current password, in order to save a new one.')

INSERT INTO Translations VALUES ('Discard changes?', '�nderugen verwerfen?', 'Discard changes?')
INSERT INTO Translations VALUES ('Discard current changes?', 'M�chten Sie die aktuellen �nderungen verwerfen?', 'Do you want to discard the current changes?')
INSERT INTO Translations VALUES ('Delete', 'L�schen', 'Delete')
INSERT INTO Translations VALUES ('Delete data?', 'Daten l�schen?', 'Delete data?')
INSERT INTO Translations VALUES ('Delete data irrevocably?', 'Wollen Sie die Daten wirklich unwiderruflich l�schen?', 'Are you sure you want to delete the data irrevocably?')
INSERT INTO Translations VALUES ('Deleted!', 'Gel�scht!', 'Deleted!')
INSERT INTO Translations VALUES ('Successfully deleted.', 'Die Daten wurden erfolgreich gel�scht.', 'Data have been deleted successfully.')

INSERT INTO Translations VALUES ('Languages', 'Sprachen', 'Languages')
INSERT INTO Translations VALUES ('Language', 'Sprache', 'Language')

INSERT INTO Translations VALUES ('People', 'Personen', 'People')
INSERT INTO Translations VALUES ('Person', 'Person', 'Person')
INSERT INTO Translations VALUES ('Name', 'Name', 'Name')
INSERT INTO Translations VALUES ('Forename', 'Vorname', 'Forename')
INSERT INTO Translations VALUES ('K�rzel', 'K�rzel', 'Code')
INSERT INTO Translations VALUES ('Password', 'Passwort', 'Password')
INSERT INTO Translations VALUES ('Current password', 'Aktuelles Passwort', 'Current password')
INSERT INTO Translations VALUES ('New password', 'Neues Passwort', 'New password')
INSERT INTO Translations VALUES ('Repetition', 'Wiederholung', 'Repetition')

INSERT INTO Translations VALUES ('Addresses', 'Adressen', 'Addresses')
INSERT INTO Translations VALUES ('Address', 'Adresse', 'Address')
INSERT INTO Translations VALUES ('Street', 'Stra�e', 'Street')
INSERT INTO Translations VALUES ('HsNr', 'Hausnummer', 'House number')
INSERT INTO Translations VALUES ('ZIP', 'PLZ', 'ZIP')
INSERT INTO Translations VALUES ('City', 'Ort', 'City')
INSERT INTO Translations VALUES ('Country', 'Land', 'Country')

INSERT INTO Translations VALUES ('Countries', 'L�nder', 'Countries')
INSERT INTO Translations VALUES ('Alpha2', 'Alpha2', 'Alpha2')
INSERT INTO Translations VALUES ('Alpha3', 'Alpha3', 'Alpha3')
INSERT INTO Translations VALUES ('Vorwahl', 'Vorwahl', 'Area code')

INSERT INTO Translations VALUES ('Contact types', 'Kontaktarten', 'Contact types')
INSERT INTO Translations VALUES ('Contact type', 'Kontaktart', 'Contact type')
INSERT INTO Translations VALUES ('Abbreviation', 'Abk�rzung', 'Abbreviation')
INSERT INTO Translations VALUES ('Format', 'Format', 'Format')

INSERT INTO Translations VALUES ('Contact information', 'Kontaktinformationen', 'Contact information')
INSERT INTO Translations VALUES ('Information', 'Information', 'Information')

--INSERT INTO Translations VALUES ('', '')
SELECT * FROM Translations ORDER BY ID