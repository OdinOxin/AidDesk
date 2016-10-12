package de.odinoxin.aiddesk.plugins.people;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.TextField;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;

public class PersonEditor extends RecordEditor<Person> {

    private TextField txfForename;
    private TextField txfName;
    private TextField txfShortKey;
    private RefBox refBoxAddress;

    public PersonEditor() {
        super("/plugins/personeditor.fxml", "Personen");

        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfForename = (TextField) this.root.lookup("#txfForename");
        this.txfShortKey = (TextField) this.root.lookup("#txfShortKey");
        this.refBoxAddress = (RefBox) this.root.lookup("#refBoxAddress");
        this.refBoxAddress.setOnNewAction(ev -> new AddressEditor());
        this.refBoxAddress.setOnEditAction(ev -> new AddressEditor(this.refBoxAddress.getRef()).recordId().addListener((observable, oldValue, newValue) -> this.refBoxAddress.setRef((int) newValue)));
        this.loadRecord(Login.getPerson().getId());
    }

    public PersonEditor(int id) {
        this();
        this.loadRecord(id);
    }

    @Override
    protected void onNew() {
        this.txfForename.requestFocus();
    }

    @Override
    protected int onSave() throws SQLException {
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
        } else {
            PreparedStatement updateStmt = Database.DB.prepareStatement("UPDATE People SET Code = ?, Name = ?, Forename = ?, Language = ?, Address = ? WHERE ID = ?");
            updateStmt.setString(1, this.getRecordItem().getCode());
            updateStmt.setString(2, this.getRecordItem().getName());
            updateStmt.setString(3, this.getRecordItem().getForename());
            updateStmt.setString(4, this.getRecordItem().getLanguageCode());
            updateStmt.setInt(5, this.getRecordItem().getAddressId());
            updateStmt.setInt(6, this.getRecordItem().getId());
            if (updateStmt.executeUpdate() == 1)
                return this.getRecordItem().getId();
        }
        return 0;
    }

    @Override
    protected boolean onDelete() throws SQLException {
        PreparedStatement deleteStmt = Database.DB.prepareStatement("DELETE FROM People WHERE ID = ?");
        deleteStmt.setInt(1, this.getRecordItem().getId());
        if (deleteStmt.executeUpdate() == 1) {
            this.loadRecord(0);
            return true;
        }
        return false;
    }

    @Override
    protected boolean setRecord(int id) throws SQLException {
        if (id == 0) {
            this.setRecordItem(new Person());
            return true;
        } else {
            PreparedStatement statement = Database.DB.prepareStatement("SELECT * FROM People WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                this.setRecordItem(new Person(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Forename"), dbRes.getString("Code"), dbRes.getString("Language"), dbRes.getInt("Address")));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void bind() {
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfForename.textProperty().bindBidirectional(this.getRecordItem().forenameProperty());
        this.txfShortKey.textProperty().bindBidirectional(this.getRecordItem().codeProperty());
        this.refBoxAddress.refProperty().bindBidirectional(this.getRecordItem().addressIdProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxKeyView() {
        return "V_People";
    }
}
