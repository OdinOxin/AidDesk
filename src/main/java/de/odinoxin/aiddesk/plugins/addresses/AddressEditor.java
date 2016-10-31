package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aidcloud.provider.AddressProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.countries.Country;
import de.odinoxin.aiddesk.plugins.countries.CountryEditor;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class AddressEditor extends RecordEditor<Address> {

    private TextField txfStreet;
    private TextField txftxfHsNo;
    private TextField txftxfZip;
    private TextField txftxfCity;
    private RefBox<Country> refBoxCountry;

    public AddressEditor() {
        this(null);
    }

    public AddressEditor(Address address) {
        super("/plugins/addresseditor.fxml", "Addresses");

        this.txfStreet = (TextField) this.root.lookup("#txfStreet");
        this.txftxfHsNo = (TextField) this.root.lookup("#txfHsNo");
        this.txftxfZip = (TextField) this.root.lookup("#txfZip");
        this.txftxfCity = (TextField) this.root.lookup("#txfCity");
        this.refBoxCountry = (RefBox<Country>) this.root.lookup("#refBoxCountry");
        this.refBoxCountry.setOnNewAction(ev -> new CountryEditor());
        this.refBoxCountry.setOnEditAction(ev -> new CountryEditor(this.refBoxCountry.getObj()).recordItem().addListener((observable, oldValue, newValue) -> this.refBoxCountry.setObj((Country) newValue)));

        this.loadRecord(address);
        if (address == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfStreet.requestFocus());
    }

    @Override
    protected Address onSave() {
        return AddressProvider.save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return AddressProvider.delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(Address address) {
        if (address == null)
            this.setRecordItem(new Address());
        else
            this.setRecordItem(address);
    }

    @Override
    protected void bind() {
        this.txfStreet.textProperty().bindBidirectional(this.getRecordItem().streetProperty());
        this.txftxfHsNo.textProperty().bindBidirectional(this.getRecordItem().hsNoProperty());
        this.txftxfZip.textProperty().bindBidirectional(this.getRecordItem().zipProperty());
        this.txftxfCity.textProperty().bindBidirectional(this.getRecordItem().cityProperty());
        this.refBoxCountry.objProperty().bindBidirectional(this.getRecordItem().countryProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Address> getProvider() {
        return new AddressProvider();
    }
}
