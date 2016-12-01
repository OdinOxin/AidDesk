package de.odinoxin.aiddesk.plugins.addresses;

import de.odinoxin.aidcloud.provider.AddressProvider;
import de.odinoxin.aidcloud.provider.CountryProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.controls.refbox.RefBox;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aiddesk.plugins.countries.Country;
import javafx.application.Platform;
import javafx.scene.control.TextField;

public class AddressEditor extends RecordEditor<Address> {

    private TextField txfStreet;
    private TextField txftxfHsNo;
    private TextField txftxfZip;
    private TextField txftxfCity;
    private RefBox<Country> refBoxCountry;

    public AddressEditor(Address address) {
        super("/plugins/addresseditor.fxml", "Addresses");

        this.txfStreet = (TextField) this.root.lookup("#txfStreet");
        this.txftxfHsNo = (TextField) this.root.lookup("#txfHsNo");
        this.txftxfZip = (TextField) this.root.lookup("#txfZip");
        this.txftxfCity = (TextField) this.root.lookup("#txfCity");
        this.refBoxCountry = (RefBox<Country>) this.root.lookup("#refBoxCountry");
        this.refBoxCountry.setProvider(new CountryProvider());

        this.attemptLoadRecord(address);
        if (address == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        Platform.runLater(() -> this.txfStreet.requestFocus());
    }

    @Override
    protected Address onSave() throws ConcurrentFault_Exception {
        return this.getProvider().save(this.getRecordItem(), this.getOriginalItem());
    }

    @Override
    protected boolean onDelete() {
        return this.getProvider().delete(this.getRecordItem().getId());
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
        this.refBoxCountry.recordProperty().bindBidirectional(this.getRecordItem().countryProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Address> initProvider() {
        return new AddressProvider();
    }
}
