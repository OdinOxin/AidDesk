package de.odinoxin.aidcloud.mapper;

import de.odinoxin.aidcloud.service.LanguageEntity;
import de.odinoxin.aidcloud.service.LanguagesService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.plugins.languages.Language;

import java.net.MalformedURLException;
import java.net.URL;

public class LanguagesMapper {

    private static LanguagesService svc;

    private static LanguagesService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new LanguagesService(new URL(Login.getServerUrl() + "/Languages?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static Language get(int id) {
        if (LanguagesMapper.getSvc() != null) {
            LanguageEntity item = LanguagesMapper.getSvc().getLanguagesPort().getLanguage(id);
            if (item != null)
                return new Language(item.getId(), item.getName(), item.getCode());
        }
        return null;
    }

    public static int save(Language item) {
        if (LanguagesMapper.getSvc() != null)
            return LanguagesMapper.getSvc().getLanguagesPort().saveLanguage(item.toService());
        return 0;
    }

    public static boolean delete(int id) {
        if (LanguagesMapper.getSvc() != null)
            return LanguagesMapper.getSvc().getLanguagesPort().deleteLanguage(id);
        return false;
    }
}
