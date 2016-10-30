package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aidcloud.service.AddressProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.addresses.Address;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AddressProvider implements Provider<Address> {
    private static AddressProviderService addressSvc;

    private static AddressProviderService getSvc() {
        if (addressSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                addressSvc = new AddressProviderService(new URL(Login.getServerUrl() + "/AddressProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return addressSvc;
    }

    public static Address get(int id) {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().getAddressProviderPort().getAddress(id);
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    public static Address save(Address item) {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().getAddressProviderPort().saveAddress(item.toEntity());
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    public static boolean delete(int id) {
        if (AddressProvider.getSvc() != null)
            return AddressProvider.getSvc().getAddressProviderPort().deleteAddress(id);
        return false;
    }

    @Override
    public RefBoxListItem<Address> getRefBoxItem(Address item) {
        return null;
    }

    @Override
    public List<RefBoxListItem<Address>> search(String[] expr) {
        return null;
    }
}