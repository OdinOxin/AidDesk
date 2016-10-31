package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aidcloud.service.ContactInformationProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ContactInformationProvider implements Provider<ContactInformation> {
    private static ContactInformationProviderService svc;

    private static ContactInformationProviderService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new ContactInformationProviderService(new URL(Login.getServerUrl() + "/ContactInformationProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static ContactInformation get(int id) {
        if (ContactInformationProvider.getSvc() != null) {
            ContactInformationEntity entity = ContactInformationProvider.getSvc().getContactInformationProviderPort().getContactInformation(id);
            if (entity != null)
                return new ContactInformation(entity);
        }
        return null;
    }

    public static ContactInformation save(ContactInformation item) {
        if (ContactInformationProvider.getSvc() != null) {
            ContactInformationEntity entity = ContactInformationProvider.getSvc().getContactInformationProviderPort().saveContactInformation(item.toEntity());
            if (entity != null)
                return new ContactInformation(entity);
        }
        return null;
    }

    public static boolean delete(int id) {
        if (ContactInformationProvider.getSvc() != null)
            return ContactInformationProvider.getSvc().getContactInformationProviderPort().deleteContactInformation(id);
        return false;
    }

    @Override
    public RefBoxListItem<ContactInformation> getRefBoxItem(ContactInformation item) {
        return null;
    }

    @Override
    public List<RefBoxListItem<ContactInformation>> search(List<String> expr) {
        return null;
    }
}
