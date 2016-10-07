package de.odinoxin.aiddesk.plugins;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.RefBox;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Humans extends Plugin {
    private RefBox refBoxKey;

    private TextField txfID;
    private TextField txfForename;
    private TextField txfName;
    private TextField txfShortKey;
    private RefBox refBoxAddress;

    public Humans() {
        super("/plugins/humans.fxml", "Humans");

        this.refBoxKey = (RefBox) this.grdMain.lookup("#refBoxKey");
        this.refBoxKey.refProperty().addListener((observable, oldValue, newValue) -> this.loadHuman((int) newValue));
        this.txfID = (TextField) this.grdMain.lookup("#txfID");
        this.txfForename = (TextField) this.grdMain.lookup("#txfForename");
        this.txfName = (TextField) this.grdMain.lookup("#txfName");
        this.txfShortKey = (TextField) this.grdMain.lookup("#txfShortKey");
        this.refBoxAddress = (RefBox) this.grdMain.lookup("#refBoxAddress");

        this.loadHuman(Login.HumanID);
    }

    private void loadHuman(int id) {
        try {
            PreparedStatement statement = Database.DB.prepareStatement("SELECT * FROM Humans WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                this.txfID.setText(dbRes.getString("ID"));
                this.txfForename.setText(dbRes.getString("Forename"));
                this.txfName.setText(dbRes.getString("Name"));
                this.txfShortKey.setText(dbRes.getString("ShortKey"));
                this.refBoxAddress.setRef(dbRes.getInt("Address"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}