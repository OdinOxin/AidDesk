package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aidcloud.service.PersonProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.people.Person;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    public List<RefBoxListItem<Person>> search(String[] expr) {
        if (PersonProvider.getSvc() != null) {
            List<PersonEntity> entities = PersonProvider.getSvc().getPersonProviderPort().searchPerson(Arrays.asList(expr));
            List<RefBoxListItem<Person>> result = new ArrayList<>();
            for (PersonEntity entity : entities) {
                if (entity == null)
                    continue;
                result.add(new RefBoxListItem<Person>(new Person(entity),
                        entity.getForename() + " " + entity.getName(),
                        entity.getCode() + "\n" +
                                entity.getAddress().getStreet() + " " + entity.getAddress().getHsNo() + "\n" +
                                entity.getAddress().getZip() + " " + entity.getAddress().getCountry().getName()));
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
