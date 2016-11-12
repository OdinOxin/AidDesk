package de.odinoxin.aiddesk.plugins.countries;

import de.odinoxin.aidcloud.provider.CountryProvider;
import de.odinoxin.aidcloud.provider.Provider;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import javafx.scene.control.TextField;

public class CountryEditor extends RecordEditor<Country> {

    private TextField txfAlpha2;
    private TextField txfAlpha3;
    private TextField txfName;
    private TextField txfAreaCode;

    public CountryEditor(Country country) {
        super("/plugins/countryeditor.fxml", "Countries");

        this.txfAlpha2 = (TextField) this.root.lookup("#txfAlpha2");
        this.txfAlpha3 = (TextField) this.root.lookup("#txfAlpha3");
        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfAreaCode = (TextField) this.root.lookup("#txfAreaCode");

        this.loadRecord(country);
        if (country == null)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.txfAlpha2.requestFocus();
    }

    @Override
    protected Country onSave() {
        return this.getProvider().save(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return this.getProvider().delete(this.getRecordItem().getId());
    }

    @Override
    protected void setRecord(Country country) {
        if (country == null)
            this.setRecordItem(new Country());
        else
            this.setRecordItem(country);
    }

    @Override
    protected void bind() {
        this.txfAlpha2.textProperty().bindBidirectional(this.getRecordItem().alpha2Property());
        this.txfAlpha3.textProperty().bindBidirectional(this.getRecordItem().alpha3Property());
        this.txfName.textProperty().bindBidirectional(this.getRecordItem().nameProperty());
        this.txfAreaCode.textProperty().bindBidirectional(this.getRecordItem().areaCodeProperty());
        this.getRecordItem().setChanged(false);
    }

    @Override
    protected Provider<Country> initProvider() {
        return new CountryProvider();
    }
}
