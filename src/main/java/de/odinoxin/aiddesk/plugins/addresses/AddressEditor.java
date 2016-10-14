package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aidcloud.helper.AddressHelper;
import javafx.scene.control.TextField;
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

        this.loadRecord(0);
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
    protected int onSave() {
        return AddressHelper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return AddressHelper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Address());
            return true;
        } else {
            Address adr = AddressHelper.get(id);
            if (adr != null) {
                this.setRecordItem(adr);
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
