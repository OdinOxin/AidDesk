package de.odinoxin.aiddesk.controls.translateable;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Translator {

    public static String getTranslation(int textId) {
        try {
            if(Login.getHuman() == null)
                return null;
            String languageCode = Login.getHuman().getLanguageCode();
            if(languageCode == null || languageCode.isEmpty())
                languageCode = "USA";
            PreparedStatement statement = Database.DB.prepareStatement("SELECT " + languageCode + " FROM Translations WHERE ID = ?");
            statement.setInt(1, textId);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                return dbRes.getString(1);
                //return "<" + dbRes.getString(1) + ">";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
