package de.odinoxin.aidcloud.provider;

import de.odinoxin.aidcloud.service.TranslatorService;
import de.odinoxin.aiddesk.Login;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class TranslatorProvider {
    private static TranslatorService translatorSvc;

    private static TranslatorService getSvc() {
        if (translatorSvc == null) {
            if (Login.getServerUrl() == null)
                return null;
            try {
                translatorSvc = new TranslatorService(new URL(Login.getServerUrl() + "/Translator?wsdl"));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return translatorSvc;
    }

    public static String getTranslation(String text) {
        if (TranslatorProvider.getSvc() != null)
            return TranslatorProvider.getSvc().getTranslatorPort().getTranslation(text, Login.getPerson() != null ? Login.getLanguage() : null);
        return text;
    }
}
