package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ContactTypeEntity;
import de.odinoxin.aidcloud.service.ContactTypeProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.contact.types.ContactType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ContactTypeProvider implements Provider<ContactType> {

    private static ContactTypeProviderService svc;

    private static ContactTypeProviderService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new ContactTypeProviderService(new URL(Login.getServerUrl() + "/ContactTypeProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static ContactType get(int id) {
        if (ContactTypeProvider.getSvc() != null) {
            ContactTypeEntity entity = ContactTypeProvider.getSvc().getContactTypeProviderPort().getContactType(id);
            if (entity != null)
                return new ContactType(entity);
        }
        return null;
    }

    public static ContactType save(ContactType item) {
        if (ContactTypeProvider.getSvc() != null) {
            ContactTypeEntity entity = ContactTypeProvider.getSvc().getContactTypeProviderPort().saveContactType(item.toEntity());
            if (entity != null)
                return new ContactType(entity);
        }
        return null;
    }

    public static boolean delete(int id) {
        if (ContactTypeProvider.getSvc() != null)
            return ContactTypeProvider.getSvc().getContactTypeProviderPort().deleteContactType(id);
        return false;
    }

    @Override
    public List<RefBoxListItem<ContactType>> search(String[] expr) {
        return null;
    }
}
