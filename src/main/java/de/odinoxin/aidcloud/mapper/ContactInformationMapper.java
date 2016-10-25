package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.ContactInformationEntity;
import de.odinoxin.aidcloud.service.ContactInformationService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.plugins.contact.information.ContactInformation;

import java.net.MalformedURLException;
import java.net.URL;

public class ContactInformationMapper {
    private static ContactInformationService svc;

    private static ContactInformationService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new ContactInformationService(new URL(Login.getServerUrl() + "/ContactInformationService?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static ContactInformation get(int id) {
        if (ContactInformationMapper.getSvc() != null) {
            ContactInformationEntity item = ContactInformationMapper.getSvc().getContactInformationPort().getContactInformation(id);
            if (item != null)
                return new ContactInformation(item.getId(), item.getContactType(), item.getInformation());
        }
        return null;
    }

    public static int save(ContactInformation item) {
        if (ContactInformationMapper.getSvc() != null)
            return ContactInformationMapper.getSvc().getContactInformationPort().saveContactInformation(item.toService());
        return 0;
    }

    public static boolean delete(int id) {
        if (ContactInformationMapper.getSvc() != null)
            return ContactInformationMapper.getSvc().getContactInformationPort().deleteContactInformation(id);
        return false;
    }
}
