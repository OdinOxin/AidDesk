package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.CountryEntity;
import de.odinoxin.aidcloud.service.CountryProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.countries.Country;

import java.net.MalformedURLException;
import java.net.URL;
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
    public List<RefBoxListItem<Country>> search(String[] expr) {
        return null;
    }
}
