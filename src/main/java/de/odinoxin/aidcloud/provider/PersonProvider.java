package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aidcloud.service.PersonProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.people.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonProvider implements Provider<Person> {
    private static PersonProviderService peopleSvc;

    private static PersonProviderService getSvc() {
        if (peopleSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                peopleSvc = new PersonProviderService(new URL(Login.getServerUrl() + "/PersonProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return peopleSvc;
    }

    public static Person get(int id) {
        if (PersonProvider.getSvc() != null) {
            PersonEntity entity = PersonProvider.getSvc().getPersonProviderPort().getPerson(id);
            if (entity != null)
                return new Person(entity);
        }
        return null;
    }

    public static Person save(Person item) {
        if (PersonProvider.getSvc() != null) {
            PersonEntity entity = PersonProvider.getSvc().getPersonProviderPort().savePerson(item.toEntity());
            if (entity != null)
                return new Person(entity);
        }
        return null;
    }

    public static boolean delete(int id) {
        if (PersonProvider.getSvc() != null)
            return PersonProvider.getSvc().getPersonProviderPort().deletePerson(id);
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
    public List<RefBoxListItem<Person>> search(List<String> expr) {
        if (PersonProvider.getSvc() != null) {
            List<PersonEntity> entities = PersonProvider.getSvc().getPersonProviderPort().searchPerson(expr);
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

    public static boolean changePwd(int id, String currentPwd, String newPwd) {
        if (PersonProvider.getSvc() != null)
            return PersonProvider.getSvc().getPersonProviderPort().changePwd(id, currentPwd, newPwd);
        return false;
    }
}
