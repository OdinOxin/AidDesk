package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.CountryEntity;
import de.odinoxin.aidcloud.service.CountryProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.countries.Country;
import de.odinoxin.aiddesk.plugins.countries.CountryEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CountryProvider implements Provider<Country> {
    private static de.odinoxin.aidcloud.service.CountryProvider svc;

    private static de.odinoxin.aidcloud.service.CountryProvider getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new CountryProviderService(new URL(Login.getServerUrl() + "/CountryProvider?wsdl")).getCountryProviderPort();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        if (svc != null)
            Requester.setRequestHeaders(svc);
        return svc;
    }

    @Override
    public Country get(int id) {
        if (CountryProvider.getSvc() != null) {
            CountryEntity entity = CountryProvider.getSvc().getCountry(id);
            if (entity != null)
                return new Country(entity);
        }
        return null;
    }

    @Override
    public Country save(Country item) {
        if (CountryProvider.getSvc() != null) {
            CountryEntity entity = CountryProvider.getSvc().saveCountry(item.toEntity());
            if (entity != null)
                return new Country(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (CountryProvider.getSvc() != null)
            return CountryProvider.getSvc().deleteCountry(id);
        return false;
    }

    @Override
    public RefBoxListItem<Country> getRefBoxItem(Country item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getName() == null ? "" : item.getName()),
                (item.getAlpha2() == null ? "" : item.getAlpha2()) + " " +
                        (item.getAlpha3() == null ? "" : item.getAlpha3()) + " " +
                        (item.getAreaCode() == null ? "" : item.getAreaCode()));
    }

    @Override
    public List<RefBoxListItem<Country>> search(List<String> expr, int max) {
        if (CountryProvider.getSvc() != null) {
            List<CountryEntity> entities = CountryProvider.getSvc().searchCountry(expr, max);
            List<RefBoxListItem<Country>> result = new ArrayList<>();
            if (entities != null)
                for (CountryEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new Country(entity)));
            return result;
        }
        return null;
    }

    @Override
    public CountryEditor openEditor(Country entity) {
        return new CountryEditor(entity);
    }
}
