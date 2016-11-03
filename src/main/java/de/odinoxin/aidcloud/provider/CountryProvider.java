package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.CountryEntity;
import de.odinoxin.aidcloud.service.CountryProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.countries.Country;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CountryProvider implements Provider<Country> {
    private static CountryProviderService countriesSvc;

    private static CountryProviderService getSvc() {
        if (countriesSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                countriesSvc = new CountryProviderService(new URL(Login.getServerUrl() + "/CountryProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return countriesSvc;
    }

    public static Country getCountry(int id) {
        if (CountryProvider.getSvc() != null) {
            CountryEntity entity = CountryProvider.getSvc().getCountryProviderPort().getCountry(id);
            if (entity != null)
                return new Country(entity);
        }
        return null;
    }

    public static Country saveCountry(Country item) {
        if (CountryProvider.getSvc() != null) {
            CountryEntity entity = CountryProvider.getSvc().getCountryProviderPort().saveCountry(item.toEntity());
            if (entity != null)
                return new Country(entity);
        }
        return null;
    }

    public static boolean deleteCountry(int id) {
        if (CountryProvider.getSvc() != null)
            return CountryProvider.getSvc().getCountryProviderPort().deleteCountry(id);
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
    public List<RefBoxListItem<Country>> search(List<String> expr) {
        if (CountryProvider.getSvc() != null) {
            List<CountryEntity> entities = CountryProvider.getSvc().getCountryProviderPort().searchCountry(expr);
            List<RefBoxListItem<Country>> result = new ArrayList<>();
            if (entities != null)
                for (CountryEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new Country(entity)));
            return result;
        }
        return null;
    }
}
