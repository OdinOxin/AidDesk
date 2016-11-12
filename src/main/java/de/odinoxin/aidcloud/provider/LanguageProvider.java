package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.LanguageEntity;
import de.odinoxin.aidcloud.service.LanguageProviderService;
import de.odinoxin.aiddesk.Login;
import de.odinoxin.aiddesk.controls.refbox.RefBoxListItem;
import de.odinoxin.aiddesk.plugins.languages.Language;
import de.odinoxin.aiddesk.plugins.languages.LanguageEditor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    @Override
    public Language get(int id) {
        if (LanguageProvider.getSvc() != null) {
            LanguageEntity entity = LanguageProvider.getSvc().getLanguageProviderPort().getLanguage(id);
            if (entity != null)
                return new Language(entity);
        }
        return null;
    }

    @Override
    public Language save(Language item) {
        if (LanguageProvider.getSvc() != null) {
            LanguageEntity entity = LanguageProvider.getSvc().getLanguageProviderPort().saveLanguage(item.toEntity());
            if (entity != null)
                return new Language(entity);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        if (LanguageProvider.getSvc() != null)
            return LanguageProvider.getSvc().getLanguageProviderPort().deleteLanguage(id);
        return false;
    }

    @Override
    public RefBoxListItem<Language> getRefBoxItem(Language item) {
        if (item == null)
            return null;
        return new RefBoxListItem<>(item,
                (item.getName() == null ? "" : item.getName()),
                (item.getCode() == null ? "" : item.getCode()));
    }

    @Override
    public List<RefBoxListItem<Language>> search(List<String> expr) {
        if (LanguageProvider.getSvc() != null) {
            List<LanguageEntity> entities = LanguageProvider.getSvc().getLanguageProviderPort().searchLanguage(expr);
            List<RefBoxListItem<Language>> result = new ArrayList<>();
            if (entities != null)
                for (LanguageEntity entity : entities)
                    if (entity != null)
                        result.add(getRefBoxItem(new Language(entity)));
            return result;
        }
        return null;
    }

    @Override
    public LanguageEditor openEditor(Language entity) {
        return new LanguageEditor(entity);
    }
}
