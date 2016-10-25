package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.PeopleService;
import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.plugins.people.Person;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class PeopleMapper {
    private static PeopleService peopleSvc;

    private static PeopleService getSvc() {
        if (peopleSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                peopleSvc = new PeopleService(new URL(Login.getServerUrl() + "/People?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return peopleSvc;
    }

    public static Person get(int id) {
        if (PeopleMapper.getSvc() != null) {
            PersonEntity p = PeopleMapper.getSvc().getPeoplePort().getPerson(id);
            if (p != null) {
                return new Person(p.getId(), p.getName(), p.getForename(), p.getCode(), p.getLanguage(), p.getAddressId());
            }
        }
        return null;
    }

    public static int save(Person item) {
        if (PeopleMapper.getSvc() != null)
            return PeopleMapper.getSvc().getPeoplePort().savePerson(item.toService());
        return 0;
    }

    public static boolean delete(int id) {
        if (PeopleMapper.getSvc() != null)
            return PeopleMapper.getSvc().getPeoplePort().deletePerson(id);
        return false;
    }

    public static boolean changePwd(int personId, String oldPwd, String newPwd) {
        if (PeopleMapper.getSvc() != null)
            return PeopleMapper.getSvc().getPeoplePort().changePwd(personId, oldPwd, newPwd);
        return false;
    }
}
