package xyz.plumc.chattranslate.translate.translator;

import xyz.plumc.chattranslate.translate.Language;

import java.util.HashMap;
import java.util.Map;

public abstract class Translator {
    public String translate(String message, Language langFrom, Language langTo){
        String fromLang = (LANGUAGE_MAPPING.containsKey(langFrom) ? LANGUAGE_MAPPING.get(langFrom) : langFrom.value);
        String toLang = (LANGUAGE_MAPPING.containsKey(langTo) ? LANGUAGE_MAPPING.get(langTo) : langTo.value);
        return translate(message, fromLang, toLang);
    };
    abstract public String translate(String message, String langFrom, String langTo);

    public static Map<Language, String> LANGUAGE_MAPPING = new HashMap<>();

}

