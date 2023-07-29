package xyz.plumc.chattranslate.translate;

public enum Translators {
    BING("bing"),
    GOOGLE("google"),
    BAIDU("baidu");

    public final String name;

    Translators(String name) {
        this.name = name;
    }

    public static Translators of(String value){
        for (Translators i: Translators.values()){
            if (i.name.equals(value)) return i;
        }
        return null;
    }
}
