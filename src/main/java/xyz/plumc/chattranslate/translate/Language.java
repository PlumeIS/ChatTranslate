package xyz.plumc.chattranslate.translate;

public enum Language {
    AUTO_DETECT("Detect Language", "auto-detect"),
    AFRIKAANS("Afrikaans", "af"),
    ALBANIAN("Albanian", "sq"),
    AMHARIC("Amharic", "am"),
    ARABIC("Arabic", "ar"),
    ARMENIAN("Armenian", "hy"),
    ASSAMESE("Assamese", "as"),
    AZERBAIJANI("Azerbaijani", "az"),
    BANGLA("Bangla", "bn"),
    BASHKIR("Bashkir", "ba"),
    BASQUE("Basque", "eu"),
    BOSNIAN("Bosnian", "bs"),
    BULGARIAN("Bulgarian", "bg"),
    CANTONESE_TRADITIONAL("Cantonese (Traditional)", "yue"),
    CATALAN("Catalan", "ca"),
    CHINESE_LITERARY("Chinese (Literary)", "lzh"),
    CHINESE_SIMPLIFIED("Chinese Simplified", "zh-Hans"),
    CHINESE_TRADITIONAL("Chinese Traditional", "zh-Hant"),
    CROATIAN("Croatian", "hr"),
    CZECH("Czech", "cs"),
    DANISH("Danish", "da"),
    DARI("Dari", "prs"),
    DIVEHI("Divehi", "dv"),
    DUTCH("Dutch", "nl"),
    ENGLISH("English", "en"),
    ESTONIAN("Estonian", "et"),
    FAROESE("Faroese", "fo"),
    FIJIAN("Fijian", "fj"),
    FILIPINO("Filipino", "fil"),
    FINNISH("Finnish", "fi"),
    FRENCH("French", "fr"),
    FRENCH_CANADA("French (Canada)", "fr-CA"),
    GALICIAN("Galician", "gl"),
    GANDA("Ganda", "lug"),
    GEORGIAN("Georgian", "ka"),
    GERMAN("German", "de"),
    GREEK("Greek", "el"),
    GUJARATI("Gujarati", "gu"),
    HAITIAN_CREOLE("Haitian Creole", "ht"),
    HAUSA("Hausa", "ha"),
    HEBREW("Hebrew", "he"),
    HINDI("Hindi", "hi"),
    HMONG_DAW("Hmong Daw", "mww"),
    HUNGARIAN("Hungarian", "hu"),
    ICELANDIC("Icelandic", "is"),
    IGBO("Igbo", "ig"),
    INDONESIAN("Indonesian", "id"),
    INUINNAQTUN("Inuinnaqtun", "ikt"),
    INUKTITUT("Inuktitut", "iu"),
    INUKTITUT_LATIN("Inuktitut (Latin)", "iu-Latn"),
    IRISH("Irish", "ga"),
    ITALIAN("Italian", "it"),
    JAPANESE("Japanese", "ja"),
    KANNADA("Kannada", "kn"),
    KAZAKH("Kazakh", "kk"),
    KHMER("Khmer", "km"),
    KINYARWANDA("Kinyarwanda", "rw"),
    KLINGON_LATIN("Klingon (Latin)", "tlh-Latn"),
    KONKANI("Konkani", "gom"),
    KOREAN("Korean", "ko"),
    KURDISH_CENTRAL("Kurdish (Central)", "ku"),
    KURDISH_NORTHERN("Kurdish (Northern)", "kmr"),
    KYRGYZ("Kyrgyz", "ky"),
    LAO("Lao", "lo"),
    LATVIAN("Latvian", "lv"),
    LINGALA("Lingala", "ln"),
    LITHUANIAN("Lithuanian", "lt"),
    LOWER_SORBIAN("Lower Sorbian", "dsb"),
    MACEDONIAN("Macedonian", "mk"),
    MAITHILI("Maithili", "mai"),
    MALAGASY("Malagasy", "mg"),
    MALAY("Malay", "ms"),
    MALAYALAM("Malayalam", "ml"),
    MALTESE("Maltese", "mt"),
    MARATHI("Marathi", "mr"),
    MONGOLIAN_CYRILLIC("Mongolian (Cyrillic)", "mn-Cyrl"),
    MONGOLIAN_TRADITIONAL("Mongolian (Traditional)", "mn-Mong"),
    MYANMAR_BURMESE("Myanmar (Burmese)", "my"),
    MĀORI("Māori", "mi"),
    NEPALI("Nepali", "ne"),
    NORWEGIAN("Norwegian", "nb"),
    NYANJA("Nyanja", "nya"),
    ODIA("Odia", "or"),
    PASHTO("Pashto", "ps"),
    PERSIAN("Persian", "fa"),
    POLISH("Polish", "pl"),
    PORTUGUESE_BRAZIL("Portuguese (Brazil)", "pt"),
    PORTUGUESE_PORTUGAL("Portuguese (Portugal)", "pt-PT"),
    PUNJABI("Punjabi", "pa"),
    QUERÉTARO_OTOMI("Querétaro Otomi", "otq"),
    ROMANIAN("Romanian", "ro"),
    RUNDI("Rundi", "run"),
    RUSSIAN("Russian", "ru"),
    SAMOAN("Samoan", "sm"),
    SERBIAN_CYRILLIC("Serbian (Cyrillic)", "sr-Cyrl"),
    SERBIAN_LATIN("Serbian (Latin)", "sr-Latn"),
    SESOTHO("Sesotho", "st"),
    SESOTHO_SA_LEBOA("Sesotho sa Leboa", "nso"),
    SETSWANA("Setswana", "tn"),
    SHONA("Shona", "sn"),
    SINDHI("Sindhi", "sd"),
    SINHALA("Sinhala", "si"),
    SLOVAK("Slovak", "sk"),
    SLOVENIAN("Slovenian", "sl"),
    SOMALI("Somali", "so"),
    SPANISH("Spanish", "es"),
    SWAHILI("Swahili", "sw"),
    SWEDISH("Swedish", "sv"),
    TAHITIAN("Tahitian", "ty"),
    TAMIL("Tamil", "ta"),
    TATAR("Tatar", "tt"),
    TELUGU("Telugu", "te"),
    THAI("Thai", "th"),
    TIBETAN("Tibetan", "bo"),
    TIGRINYA("Tigrinya", "ti"),
    TONGAN("Tongan", "to"),
    TURKISH("Turkish", "tr"),
    TURKMEN("Turkmen", "tk"),
    UKRAINIAN("Ukrainian", "uk"),
    UPPER_SORBIAN("Upper Sorbian", "hsb"),
    URDU("Urdu", "ur"),
    UYGHUR("Uyghur", "ug"),
    UZBEK_LATIN("Uzbek (Latin)", "uz"),
    VIETNAMESE("Vietnamese", "vi"),
    WELSH("Welsh", "cy"),
    XHOSA("Xhosa", "xh"),
    YORUBA("Yoruba", "yo"),
    YUCATEC_MAYA("Yucatec Maya", "yua"),
    ZULU("Zulu", "zu");
    public final String name;
    public final String value;

    Language(String name, String value){
        this.name = name;
        this.value = value;
    }

    public static Language of(String value){
        for (Language i: Language.values()){
            if (i.value.equals(value)) return i;
        }
        return null;
    }
}