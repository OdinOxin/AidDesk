package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ContactTypeEntity;
import de.odinoxin.aidcloud.service.ContactTypeProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.contact.types.ContactType;
import de.odinoxin.aiddesk.plugins.contact.types.ContactTypeEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    @Override
    public ContactType get(int id) {
        if (ContactTypeProvider.getSvc() != null) {
            ContactTypeEntity entity = ContactTypeProvider.getSvc().getContactTypeProviderPort().getContactType(id);
            if (entity != null)
                return new ContactType(entity);
        }
        return null;
    }

    @Override
    public ContactType save(ContactType item) {
        if (ContactTypeProvider.getSvc() != null) {
            ContactTypeEntity entity = ContactTypeProvider.getSvc().getContactTypeProviderPort().saveContactType(item.toEntity());
            if (entity != null)
                return new ContactType(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (ContactTypeProvider.getSvc() != null)
            return ContactTypeProvider.getSvc().getContactTypeProviderPort().deleteContactType(id);
        return false;
    }

    @Override
    public RefBoxListItem<ContactType> getRefBoxItem(ContactType item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getName() == null ? "" : item.getName()),
                (item.getCode() == null ? "" : item.getCode()));
    }

    @Override
    public List<RefBoxListItem<ContactType>> search(List<String> expr) {
        if (ContactTypeProvider.getSvc() != null) {
            List<ContactTypeEntity> entities = ContactTypeProvider.getSvc().getContactTypeProviderPort().searchContactType(expr);
            List<RefBoxListItem<ContactType>> result = new ArrayList<>();
            if (entities != null)
                for (ContactTypeEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new ContactType(entity)));
            return result;
        }
        return null;
    }

    @Override
    public ContactTypeEditor openEditor(ContactType entity) {
        return new ContactTypeEditor(entity);
    }
}
