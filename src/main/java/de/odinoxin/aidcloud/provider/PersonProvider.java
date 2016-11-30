package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.ConcurrentFault_Exception;
import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aidcloud.service.PersonProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.people.Person;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonProvider implements Provider<Person> {
    private static de.odinoxin.aidcloud.service.PersonProvider svc;

    private static de.odinoxin.aidcloud.service.PersonProvider getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new PersonProviderService(new URL(Login.getServerUrl() + "/PersonProvider?wsdl")).getPersonProviderPort();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        if (svc != null)
            Requester.setRequestHeaders(svc);
        return svc;
    }

    @Override
    public Person get(int id) {
        if (PersonProvider.getSvc() != null) {
            PersonEntity entity = PersonProvider.getSvc().getPerson(id);
            if (entity != null)
                return new Person(entity);
        }
        return null;
    }

    @Override
    public Person save(Person item, Person original) throws ConcurrentFault_Exception {
        if (PersonProvider.getSvc() != null) {
            PersonEntity entity = PersonProvider.getSvc().savePerson(item.toEntity(), original.toEntity());
            if (entity != null)
                return new Person(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (PersonProvider.getSvc() != null)
            return PersonProvider.getSvc().deletePerson(id);
        return false;
    }

    @Override
    public RefBoxListItem<Person> getRefBoxItem(Person item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getForename() == null ? "" : item.getForename()) + " " + (item.getName() == null ? "" : item.getName()),
                (item.getCode() == null ? "" : item.getCode()) + "\n" +
                        (item.getAddress() == null ? "" :
                                (item.getAddress().getStreet() == null ? "" : item.getAddress().getStreet()) + " " +
                                        (item.getAddress().getHsNo() == null ? "" : item.getAddress().getHsNo()) + "\n" +
                                        (item.getAddress().getZip() == null ? "" : item.getAddress().getZip()) + " " +
                                        (item.getAddress().getCountry() == null ? "" :
                                                (item.getAddress().getCountry().getName() == null ? "" : item.getAddress().getCountry().getName())))
        );
    }

    @Override
    public List<RefBoxListItem<Person>> search(List<String> expr, int max) {
        if (PersonProvider.getSvc() != null) {
            List<PersonEntity> entities = PersonProvider.getSvc().searchPerson(expr, max);
            List<RefBoxListItem<Person>> result = new ArrayList<>();
            if (entities != null)
                for (PersonEntity entity : entities) {
                    if (entity != null)
                        result.add(getRefBoxItem(new Person(entity)));
                }
            return result;
        }
        return null;
    }

    @Override
    public PersonEditor openEditor(Person entity) {
        return new PersonEditor(entity);
    }

    public static boolean changePwd(int id, String currentPwd, String newPwd) {
        if (PersonProvider.getSvc() != null)
            return PersonProvider.getSvc().changePwd(id, currentPwd, newPwd);
        return false;
    }
}
