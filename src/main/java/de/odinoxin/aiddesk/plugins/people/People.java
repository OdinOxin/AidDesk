package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class People extends RecordEditor<Person> {
    private RefBox refBoxKey;

    private TextField txfID;
    private TextField txfForename;
    private TextField txfName;
    private TextField txfShortKey;
    private RefBox refBoxAddress;

    public People() {
        super("/plugins/people.fxml", "People", 16);

        RefBox refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
        refBoxKey.setView("V_People");
        refBoxKey.setOnNewAction(ev -> this.setRecordItem(new Person()));

        this.refBoxKey = (RefBox) this.root.lookup("#refBoxKey");
        this.txfID = (TextField) this.root.lookup("#txfID");
        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfShortKey = (TextField) this.root.lookup("#txfShortKey");
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.refBoxKey.refProperty().addListener((observable, oldValue, newValue) -> this.loadPerson((int) newValue));
        this.txfName.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setName(newValue));
        this.txfForename.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setForename(newValue));
        this.txfShortKey.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setCode(newValue));
        this.refBoxAddress.refProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setAddressId((int) newValue));

        this.recordItemProperty().addListener((observable, oldValue, newValue) ->
        {
            this.txfID.setText(String.valueOf(newValue.getId()));
            this.txfForename.setText(newValue.getForename());
            this.txfName.setText(newValue.getName());
            this.txfShortKey.setText(newValue.getCode());
            this.refBoxAddress.setRef(newValue.getAddressId());
        });

        this.loadPerson(Login.getPerson().getId());
    }

    private void loadPerson(int id) {
        try {
            PreparedStatement statement = Database.DB.prepareStatement("SELECT * FROM People WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                this.setRecordItem(new Person(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Forename"), dbRes.getString("Code"), dbRes.getString("Language"), dbRes.getInt("Address")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
