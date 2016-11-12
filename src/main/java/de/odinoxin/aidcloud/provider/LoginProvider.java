package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.LoginService;
import de.odinoxin.aidcloud.service.PersonEntity;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.people.Person;
import de.odinoxin.aiddesk.plugins.people.PersonEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginProvider implements Provider<Person> {
    private static de.odinoxin.aidcloud.service.Login loginSvc;

    private static de.odinoxin.aidcloud.service.Login getSvc() {
        if (loginSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                loginSvc = new LoginService(new URL(Login.getServerUrl() + "/Login?wsdl")).getLoginPort();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return loginSvc;
    }

    public static boolean checkLogin(int userId, String pwd) {
        if (LoginProvider.getSvc() != null)
            return LoginProvider.getSvc().checkLogin(userId, pwd);
        return false;
    }

    @Override
    public Person get(int id) {
        return null;
    }

    @Override
    public Person save(Person item) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public RefBoxListItem<Person> getRefBoxItem(Person item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getForename() == null ? "" : item.getForename()) + " " + (item.getName() == null ? "" : item.getName()),
                (item.getCode() == null ? "" : item.getCode()));
    }

    @Override
    public List<RefBoxListItem<Person>> search(List<String> expr) {
        if (LoginProvider.getSvc() != null) {
            List<PersonEntity> entities = LoginProvider.getSvc().searchLogin(expr);
            List<RefBoxListItem<Person>> result = new ArrayList<>();
            if (entities != null)
                for (PersonEntity entity : entities)
                    if (entity != null)
                        result.add(this.getRefBoxItem(new Person(entity)));
            return result;
        }
        return null;
    }

    @Override
    public PersonEditor openEditor(Person entity) {
        return null;
    }
}
