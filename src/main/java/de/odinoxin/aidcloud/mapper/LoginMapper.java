package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aiddesk.Login;
import de.odinoxin.aidcloud.service.LoginService;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class LoginMapper {
    private static LoginService loginSvc;

    private static LoginService getSvc() {
        if (loginSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                loginSvc = new LoginService(new URL(Login.getServerUrl() + "/Login?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return loginSvc;
    }

    public static boolean checkLogin(int userId, String pwd) {
        if (LoginMapper.getSvc() != null)
            return LoginMapper.getSvc().getLoginPort().checkLogin(userId, pwd);
        return false;
    }
}
