package de.odinoxin.aiddesk.controls.translateable;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Translator {
    public static String getTranslation(String text) {
        try {
            if (text == null)
                return null;
            if (Login.getPerson() == null)
                return text;
            String languageCode = Login.getPerson().getLanguage();
            if (languageCode == null || languageCode.isEmpty())
                languageCode = "USA";
            PreparedStatement statement = Database.DB.prepareStatement("SELECT " + languageCode + " FROM Translations WHERE DEU LIKE ? OR USA LIKE ?");
            statement.setString(1, text);
            statement.setString(2, text);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                //return dbRes.getString(1);
                return "<" + dbRes.getString(1) + ">";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return text;
    }
}
