package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.dialogs.DecisionDialog;
import de.odinoxin.aiddesk.dialogs.MsgDialog;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class People extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfShortKey;
    private RefBox refBoxAddress;

    public People() {
        super("/plugins/people.fxml", "Person");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfShortKey = (TextField) this.root.lookup("#txfShortKey");
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.txfName.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setName(newValue));
        this.txfForename.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setForename(newValue));
        this.txfShortKey.textProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setCode(newValue));
        this.refBoxAddress.refProperty().addListener((observable, oldValue, newValue) -> this.getRecordItem().setAddressId((int) newValue));

        this.setNewAction(() -> this.txfForename.requestFocus());
        this.setSaveAction(() ->
        {
            if (this.getRecordItem().getId() == 0) {
                try {
                    PreparedStatement insertStmt = Database.DB.prepareStatement("INSERT INTO People VALUES (?, ?, ?, ?, ?, ?)");
                    insertStmt.setString(1, this.getRecordItem().getCode());
                    insertStmt.setString(2, this.getRecordItem().getName());
                    insertStmt.setString(3, this.getRecordItem().getForename());
                    insertStmt.setString(4, this.getRecordItem().getPwd());
                    insertStmt.setString(5, this.getRecordItem().getLanguageCode());
                    insertStmt.setInt(6, this.getRecordItem().getAddressId());
                    if (insertStmt.executeUpdate() == 1) {
                        PreparedStatement selectStmt = Database.DB.prepareStatement("SELECT ID FROM People WHERE Code = ?");
                        selectStmt.setString(1, this.getRecordItem().getCode());
                        ResultSet dbRes = selectStmt.executeQuery();
                        if (dbRes.next()) {
                            this.getRecordItem().setChanged(false);
                            this.loadRecord(dbRes.getInt("ID"));
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setDeleteAction(() ->
        {
            if (this.getRecordItem().getId() != 0) {
                DecisionDialog.showDialog(this, "Daten löschen?", "Wollen Sie die Daten wirklich unwiderruflich löschen?", () ->
                {
                    try {
                        PreparedStatement deleteStmt = Database.DB.prepareStatement("DELETE FROM People WHERE ID = ?");
                        deleteStmt.setInt(1, this.getRecordItem().getId());
                        if (deleteStmt.executeUpdate() == 1) {
                            this.setRecordItem(new Person());
                            MsgDialog.showMsg(this, "Gelöscht!", "Die Daten wurden erfolgreich gelöscht.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }, null);
            }
        });

        this.recordItemProperty().addListener((observable, oldValue, newValue) ->
        {
            this.txfForename.setText(newValue.getForename());
            this.txfName.setText(newValue.getName());
            this.txfShortKey.setText(newValue.getCode());
            this.refBoxAddress.setRef(newValue.getAddressId());
        });

        this.loadRecord(Login.getPerson().getId());
    }

    protected void setRecord(int id) {
        if (id == 0)
            this.setRecordItem(new Person());
        else {
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

    @Override
    protected String getRefBoxKeyView() {
        return "V_People";
    }
}
