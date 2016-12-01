package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.AddressEntity;
import de.odinoxin.aidcloud.service.AddressProviderService;
import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.addresses.Address;
import de.odinoxin.aiddesk.plugins.addresses.AddressEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddressProvider implements Provider<Address> {
    private static de.odinoxin.aidcloud.service.AddressProvider svc;

    private static de.odinoxin.aidcloud.service.AddressProvider getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new AddressProviderService(new URL(Login.getServerUrl() + "/AddressProvider?wsdl")).getAddressProviderPort();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        if (svc != null)
            Requester.setRequestHeaders(svc);
        return svc;
    }

    @Override
    public Address get(int id) {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().getAddress(id);
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    @Override
    public Address save(Address item, Address original) throws ConcurrentFault_Exception {
        if (AddressProvider.getSvc() != null) {
            AddressEntity entity = AddressProvider.getSvc().saveAddress(item.toEntity(), original.toEntity());
            if (entity != null)
                return new Address(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (AddressProvider.getSvc() != null)
            return AddressProvider.getSvc().deleteAddress(id);
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
    public List<RefBoxListItem<Address>> search(List<String> expr, int max) {
        if (AddressProvider.getSvc() != null) {
            List<AddressEntity> entities = AddressProvider.getSvc().searchAddress(expr, max);
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