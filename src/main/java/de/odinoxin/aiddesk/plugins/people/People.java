package de.odinoxin.aiddesk.plugins.people;

import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
                PreparedStatement insertStmt = Database.DB.prepareStatement("INSERT INTO People VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, this.getRecordItem().getCode());
                insertStmt.setString(2, this.getRecordItem().getName());
                insertStmt.setString(3, this.getRecordItem().getForename());
                insertStmt.setString(4, this.getRecordItem().getPwd());
                insertStmt.setString(5, this.getRecordItem().getLanguageCode());
                insertStmt.setInt(6, this.getRecordItem().getAddressId());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            }
            return 0;
        });
        this.setDeleteAction(() ->
        {
            PreparedStatement deleteStmt = Database.DB.prepareStatement("DELETE FROM People WHERE ID = ?");
            deleteStmt.setInt(1, this.getRecordItem().getId());
            if (deleteStmt.executeUpdate() == 1) {
                this.loadRecord(0);
                return true;
            }
            return false;
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
