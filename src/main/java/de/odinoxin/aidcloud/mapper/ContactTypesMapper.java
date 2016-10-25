package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.ContactTypeEntity;
import de.odinoxin.aidcloud.service.ContactTypesService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.plugins.contact.types.ContactType;

import java.net.MalformedURLException;
import java.net.URL;

public class ContactTypesMapper {

    private static ContactTypesService svc;

    private static ContactTypesService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new ContactTypesService(new URL(Login.getServerUrl() + "/ContactTypesService?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static ContactType get(int id) {
        if (ContactTypesMapper.getSvc() != null) {
            ContactTypeEntity item = ContactTypesMapper.getSvc().getContactTypesPort().getContactType(id);
            if (item != null)
                return new ContactType(item.getId(), item.getName(), item.getCode(), item.getRegex());
        }
        return null;
    }

    public static int save(ContactType item) {
        if (ContactTypesMapper.getSvc() != null)
            return ContactTypesMapper.getSvc().getContactTypesPort().saveContactType(item.toService());
        return 0;
    }

    public static boolean delete(int id) {
        if (ContactTypesMapper.getSvc() != null)
            return ContactTypesMapper.getSvc().getContactTypesPort().deleteContactType(id);
        return false;
    }
}
