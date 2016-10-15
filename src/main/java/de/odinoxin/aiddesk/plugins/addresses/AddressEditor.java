package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aidcloud.mapper.AddressesMapper;
import de.odinoxin.aiddesk.plugins.countries.CountryEditor;
import javafx.application.Platform;
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
        this(0);
    }

    public AddressEditor(int id) {
        super("/plugins/addresseditor.fxml", "Adressen");

        this.txfStreet = (TextField) this.root.lookup("#txfStreet");
        this.txftxfHsNo = (TextField) this.root.lookup("#txfHsNo");
        this.txftxfZip = (TextField) this.root.lookup("#txfZip");
        this.txftxfCity = (TextField) this.root.lookup("#txfCity");
        this.refBoxCountry = (RefBox) this.root.lookup("#refBoxCountry");
        this.refBoxCountry.setOnNewAction(ev -> new CountryEditor());
        this.refBoxCountry.setOnEditAction(ev -> new CountryEditor(this.refBoxCountry.getRef()).recordId().addListener((observable, oldValue, newValue) -> this.refBoxCountry.setRef((int) newValue)));

        this.loadRecord(id);
        if (id == 0)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfStreet.requestFocus());
    }

    @Override
    protected int onSave() {
        return AddressesMapper.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return AddressesMapper.delete(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Address());
            return true;
        } else {
            Address adr = AddressesMapper.get(id);
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
    protected String getRefBoxName() {
        return "Addresses";
    }
}
