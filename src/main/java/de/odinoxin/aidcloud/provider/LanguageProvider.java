package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.LanguageEntity;
import de.odinoxin.aidcloud.service.LanguageProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.languages.Language;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class LanguageProvider implements Provider<Language> {

    private static LanguageProviderService svc;

    private static LanguageProviderService getSvc() {
        if (svc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                svc = new LanguageProviderService(new URL(Login.getServerUrl() + "/LanguageProvider?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return svc;
    }

    public static Language get(int id) {
        if (LanguageProvider.getSvc() != null) {
            LanguageEntity entity = LanguageProvider.getSvc().getLanguageProviderPort().getLanguage(id);
            if (entity != null)
                return new Language(entity);
        }
        return null;
    }

    public static Language save(Language item) {
        if (LanguageProvider.getSvc() != null) {
            LanguageEntity entity = LanguageProvider.getSvc().getLanguageProviderPort().saveLanguage(item.toEntity());
            if (entity != null)
                return new Language(entity);
        }
        return null;
    }

    public static boolean delete(int id) {
        if (LanguageProvider.getSvc() != null)
            return LanguageProvider.getSvc().getLanguageProviderPort().deleteLanguage(id);
        return false;
    }

    @Override
    public RefBoxListItem<Language> getRefBoxItem(Language item) {
        return null;
    }

    @Override
    public List<RefBoxListItem<Language>> search(String[] expr) {
        return null;
    }
}
