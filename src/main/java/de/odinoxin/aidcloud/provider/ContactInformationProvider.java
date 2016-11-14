package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aidcloud.service.ContactInformationProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformation;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformationEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContactInformationProvider implements Provider<ContactInformation> {
    private static de.odinoxin.aidcloud.service.ContactInformationProvider svc;

    private static de.odinoxin.aidcloud.service.ContactInformationProvider getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new ContactInformationProviderService(new URL(Login.getServerUrl() + "/ContactInformationProvider?wsdl")).getContactInformationProviderPort();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        if (svc != null)
            Requester.setRequestHeaders(svc);
        return svc;
    }

    @Override
    public ContactInformation get(int id) {
        if (ContactInformationProvider.getSvc() != null) {
            ContactInformationEntity entity = ContactInformationProvider.getSvc().getContactInformation(id);
            if (entity != null)
                return new ContactInformation(entity);
        }
        return null;
    }

    @Override
    public ContactInformation save(ContactInformation item) {
        if (ContactInformationProvider.getSvc() != null) {
            ContactInformationEntity entity = ContactInformationProvider.getSvc().saveContactInformation(item.toEntity());
            if (entity != null)
                return new ContactInformation(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (ContactInformationProvider.getSvc() != null)
            return ContactInformationProvider.getSvc().deleteContactInformation(id);
        return false;
    }

    @Override
    public RefBoxListItem<ContactInformation> getRefBoxItem(ContactInformation item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getContactType() == null ? "" : item.getContactType().getCode() == null ? "" : item.getContactType().getCode()) + ": " +
                        (item.getInformation() == null ? "" : item.getInformation()),
                (item.getContactType() == null ? "" : item.getContactType().getName() == null ? "" : item.getContactType().getName()));
    }

    @Override
    public List<RefBoxListItem<ContactInformation>> search(List<String> expr, int max) {
        if (ContactInformationProvider.getSvc() != null) {
            List<ContactInformationEntity> entities = ContactInformationProvider.getSvc().searchContactInformation(expr, max);
            List<RefBoxListItem<ContactInformation>> result = new ArrayList<>();
            if (entities != null)
                for (ContactInformationEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new ContactInformation(entity)));
            return result;
        }
        return null;
    }

    @Override
    public ContactInformationEditor openEditor(ContactInformation entity) {
        return new ContactInformationEditor(entity);
    }
}
