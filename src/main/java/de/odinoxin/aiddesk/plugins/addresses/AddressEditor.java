package de.odinoxin.aiddesk.plugins.addresses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.control.TextField;
import de.odinoxin.aiddesk.Database;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;

public class AddressEditor extends RecordEditor<Address> {

    private TextField txfStreet;
    private TextField txftxfHsNo;
    private TextField txftxfZip;
    private TextField txftxfCity;
    private RefBox refBoxCountry;

    public AddressEditor() {
        super("/plugins/addresseditor.fxml", "Adressen");

        this.txfStreet = (TextField) this.root.lookup("#txfStreet");
        this.txftxfHsNo = (TextField) this.root.lookup("#txfHsNo");
        this.txftxfZip = (TextField) this.root.lookup("#txfZip");
        this.txftxfCity = (TextField) this.root.lookup("#txfCity");
        this.refBoxCountry = (RefBox) this.root.lookup("#refBoxCountry");
    }

    public AddressEditor(int id) {
        this();
        this.loadRecord(id);
    }

    @Override
    protected void onNew() {
        this.txfStreet.requestFocus();
    }

    @Override
    protected int onSave() throws SQLException {
        if (this.getRecordItem().getId() == 0) {
            PreparedStatement insertStmt = Database.DB.prepareStatement("INSERT INTO Addresses VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, this.getRecordItem().getCountry());
            insertStmt.setString(2, this.getRecordItem().getZip());
            insertStmt.setString(3, this.getRecordItem().getCity());
            insertStmt.setString(4, this.getRecordItem().getStreet());
            insertStmt.setString(5, this.getRecordItem().getHsNo());
            if (insertStmt.executeUpdate() == 1) {
                ResultSet key = insertStmt.getGeneratedKeys();
                if (key.next())
                    return key.getInt(1);
            }
        } else {
            PreparedStatement updateStmt = Database.DB.prepareStatement("UPDATE Addresses SET Country = ?, Zip = ?, City = ?, Street = ?, HsNo = ? WHERE ID = ?");
            updateStmt.setInt(1, this.getRecordItem().getCountry());
            updateStmt.setString(2, this.getRecordItem().getZip());
            updateStmt.setString(3, this.getRecordItem().getCity());
            updateStmt.setString(4, this.getRecordItem().getStreet());
            updateStmt.setString(5, this.getRecordItem().getHsNo());
            updateStmt.setInt(6, this.getRecordItem().getId());
            if (updateStmt.executeUpdate() == 1)
                return this.getRecordItem().getId();
        }
        return 0;
    }

    @Override
    protected boolean onDelete() throws SQLException {
        PreparedStatement deleteStmt = Database.DB.prepareStatement("DELETE FROM Addresses WHERE ID = ?");
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
            this.setRecordItem(new Address());
            return true;
        } else {
            PreparedStatement statement = Database.DB.prepareStatement("SELECT * FROM Addresses WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                this.setRecordItem(new Address(dbRes.getInt("ID"), dbRes.getString("Street"), dbRes.getString("HsNo"), dbRes.getString("Zip"), dbRes.getString("City"), dbRes.getInt("Country")));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void bind() {
        this.txfStreet.textProperty().bindBidirectional(this.getRecordItem().streetProperty());
        this.txftxfHsNo.textProperty().bindBidirectional(this.getRecordItem().hsNoProperty());
        this.txftxfZip.textProperty().bindBidirectional(this.getRecordItem().zipProperty());
        this.txftxfCity.textProperty().bindBidirectional(this.getRecordItem().cityProperty());
        this.refBoxCountry.refProperty().bindBidirectional(this.getRecordItem().countryProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected String getRefBoxKeyView() {
        return "V_Adresses";
    }
}
