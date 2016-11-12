package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aidcloud.service.AddressProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.addresses.Address;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    @Override
    public Address get(int id) {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().getAddressProviderPort().getAddress(id);
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    @Override
    public Address save(Address item) {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().getAddressProviderPort().saveAddress(item.toEntity());
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (AddressProvider.getSvc() != null)
            return AddressProvider.getSvc().getAddressProviderPort().deleteAddress(id);
        return false;
    }

    @Override
    public RefBoxListItem<Address> getRefBoxItem(Address item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getStreet() == null ? "" : item.getStreet()) + " " +
                        (item.getHsNo() == null ? "" : item.getHsNo()),
                (item.getZip() == null ? "" : item.getZip()) + " " +
                        (item.getCity() == null ? "" : item.getCity()) + "\n" +
                        (item.getCountry() == null ? "" : item.getCountry().getName() == null ? "" : item.getCountry().getName()));
    }

    @Override
    public List<RefBoxListItem<Address>> search(List<String> expr) {
        if (AddressProvider.getSvc() != null) {
            List<AddressEntity> entities = AddressProvider.getSvc().getAddressProviderPort().searchAddress(expr);
            List<RefBoxListItem<Address>> result = new ArrayList<>();
            if (entities != null)
                for (AddressEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new Address(entity)));
            return result;
        }
        return null;
    }

    @Override
    public AddressEditor openEditor(Address entity) {
        return new AddressEditor(entity);
    }
}