package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.CountriesService;
import de.odinoxin.aidcloud.service.CountryEntity;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.plugins.countries.Country;

import java.net.MalformedURLException;
import java.net.URL;

public class CountriesMapper {
    private static CountriesService countriesSvc;

    private static CountriesService getSvc() {
        if (countriesSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                countriesSvc = new CountriesService(new URL(Login.getServerUrl() + "/Countries?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return countriesSvc;
    }

    public static Country getCountry(int countryId) {
        if (CountriesMapper.getSvc() != null) {
            CountryEntity country = CountriesMapper.getSvc().getCountriesPort().getCountry(countryId);
            if (country != null)
                return new Country(country.getId(), country.getAlpha2(), country.getAlpha3(), country.getName(), country.getAreaCode());
        }
        return null;
    }

    public static int saveCountry(Country country) {
        if (CountriesMapper.getSvc() != null)
            return CountriesMapper.getSvc().getCountriesPort().saveCountry(country.toService());
        return 0;
    }

    public static boolean deleteCountry(int countryId) {
        if (CountriesMapper.getSvc() != null)
            return CountriesMapper.getSvc().getCountriesPort().deleteCountry(countryId);
        return false;
    }
}
