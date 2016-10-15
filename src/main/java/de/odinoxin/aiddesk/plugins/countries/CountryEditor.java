package de.odinoxin.aiddesk.plugins.countries;

import javafx.scene.control.TextField;
import de.odinoxin.aiddesk.plugins.RecordEditor;
import de.odinoxin.aidcloud.mapper.CountriesMapper;

public class CountryEditor extends RecordEditor<Country> {

    private TextField txfAlpha2;
    private TextField txfAlpha3;
    private TextField txfName;
    private TextField txfAreaCode;

    public CountryEditor() {
        this(0);
    }

    public CountryEditor(int id) {
        super("/plugins/countryeditor.fxml", "Countries");

        this.txfAlpha2 = (TextField) this.root.lookup("#txfAlpha2");
        this.txfAlpha3 = (TextField) this.root.lookup("#txfAlpha3");
        this.txfName = (TextField) this.root.lookup("#txfName");
        this.txfAreaCode = (TextField) this.root.lookup("#txfAreaCode");

        this.loadRecord(id);
        if (id == 0)
            this.onNew();
    }

    @Override
    protected void onNew() {
        this.txfAlpha2.requestFocus();
    }

    @Override
    protected int onSave() {
        return CountriesMapper.saveCountry(this.getRecordItem());
    }

    @Override
    protected boolean onDelete() {
        return CountriesMapper.deleteCountry(this.getRecordItem().getId());
    }

    @Override
    protected boolean setRecord(int id) {
        if (id == 0) {
            this.setRecordItem(new Country());
            return true;
        } else {
            Country country = CountriesMapper.getCountry(id);
            if (country != null) {
                this.setRecordItem(country);
                return true;
            }
        }
        return false;
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
    protected String getRefBoxName() {
        return "Countries";
    }
}
