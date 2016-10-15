package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aiddesk.Login;
import de.odinoxin.aidcloud.service.AddressesService;
import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aiddesk.plugins.addresses.Address;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AddressesMapper {
    private static AddressesService addressSvc;

    private static AddressesService getSvc() {
        if (addressSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                addressSvc = new AddressesService(new URL(Login.getServerUrl() + "/Addresses?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return addressSvc;
    }

    public static Address get(int addressId) {
        if (AddressesMapper.getSvc() != null) {
            AddressEntity adr = AddressesMapper.getSvc().getAddressesPort().getAddress(addressId);
            if (adr != null)
                return new Address(adr.getId(), adr.getStreet(), adr.getHsNo(), adr.getZip(), adr.getCity(), adr.getCountry());
        }
        return null;
    }

    public static int save(Address adr) {
        if (AddressesMapper.getSvc() != null)
            return AddressesMapper.getSvc().getAddressesPort().saveAddress(adr.toService());
        return 0;
    }

    public static boolean delete(int addressId) {
        if (AddressesMapper.getSvc() != null)
            return AddressesMapper.getSvc().getAddressesPort().deleteAddress(addressId);
        return false;
    }
}