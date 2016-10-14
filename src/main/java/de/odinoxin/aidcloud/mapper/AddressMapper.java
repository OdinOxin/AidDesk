package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aiddesk.Login;
import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aidcloud.service.AddressService;
import de.odinoxin.aiddesk.plugins.addresses.Address;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class AddressMapper {
    private static AddressService addressSvc;

    private static AddressService getSvc() {
        if (addressSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                addressSvc = new AddressService(new URL(Login.getServerUrl() + "/Addresses?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return addressSvc;
    }

    public static Address get(int addressId) {
        if (AddressMapper.getSvc() != null) {
            AddressEntity adr = AddressMapper.getSvc().getAddressPort().getAddress(addressId);
            if (adr != null) {
                return new Address(adr.getId(), adr.getStreet(), adr.getHsNo(), adr.getZip(), adr.getCity(), adr.getCountry());
            }
        }
        return null;
    }

    public static int save(Address adr) {
        if (AddressMapper.getSvc() != null)
            return AddressMapper.getSvc().getAddressPort().saveAddress(adr.toService());
        return 0;
    }

    public static boolean delete(int addressId) {
        if (AddressMapper.getSvc() != null)
            return AddressMapper.getSvc().getAddressPort().deleteAddress(addressId);
        return false;
    }
}