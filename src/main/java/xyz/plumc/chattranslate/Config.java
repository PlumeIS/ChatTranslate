package xyz.plumc.chattranslate;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import xyz.plumc.chattranslate.translate.Translators;
import xyz.plumc.chattranslate.translate.translator.BingTranslator;
import xyz.plumc.chattranslate.translate.Language;
import xyz.plumc.chattranslate.translate.translator.Translator;

@Mod.EventBusSubscriber(modid = ChatTranslate.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static String LANGUAGE_SELECT = """
            af (Afrikaans)					sq (Albanian)					am (Amharic)
            ar (Arabic)						hy (Armenian)					as (Assamese)
            az (Azerbaijani)					bn (Bangla)						ba (Bashkir)
            eu (Basque)						bs (Bosnian)					bg (Bulgarian)
            yue (Cantonese (Traditional))		ca (Catalan)					lzh (Chinese (Literary))
            zh-Hans (Chinese Simplified)		zh-Hant (Chinese Traditional)	hr (Croatian)
            cs (Czech)						da (Danish)						prs (Dari)
            dv (Divehi)						nl (Dutch)						en (English)
            et (Estonian)						fo (Faroese)					fj (Fijian)
            fil (Filipino)					fi (Finnish)					fr (French)
            fr-CA (French (Canada))			gl (Galician)					lug (Ganda)
            ka (Georgian)						de (German)						el (Greek)
            gu (Gujarati)						ht (Haitian Creole)				ha (Hausa)
            he (Hebrew)						hi (Hindi)						mww (Hmong Daw)
            hu (Hungarian)					is (Icelandic)					ig (Igbo)
            id (Indonesian)					ikt (Inuinnaqtun)				iu (Inuktitut)
            iu-Latn (Inuktitut (Latin))		ga (Irish)						it (Italian)
            ja (Japanese)						kn (Kannada)					kk (Kazakh)
            km (Khmer)						rw (Kinyarwanda)				tlh-Latn (Klingon (Latin))
            gom (Konkani)						ko (Korean)						ku (Kurdish (Central))
            kmr (Kurdish (Northern))			ky (Kyrgyz)						lo (Lao)
            lv (Latvian)						ln (Lingala)					lt (Lithuanian)
            dsb (Lower Sorbian)				mk (Macedonian)					mai (Maithili)
            mg (Malagasy)						ms (Malay)						ml (Malayalam)
            mt (Maltese)						mr (Marathi)					mn-Cyrl (Mongolian (Cyrillic))
            mn-Mong (Mongolian (Traditional))	my (Myanmar (Burmese))			mi (Māori)
            ne (Nepali)						nb (Norwegian)					nya (Nyanja)
            or (Odia)							ps (Pashto)						fa (Persian)
            pl (Polish)						pt (Portuguese (Brazil))		pt-PT (Portuguese (Portugal))
            pa (Punjabi)						otq (Querétaro Otomi)			ro (Romanian)
            run (Rundi)						ru (Russian)					sm (Samoan)
            sr-Cyrl (Serbian (Cyrillic))		sr-Latn (Serbian (Latin))		st (Sesotho)
            nso (Sesotho sa Leboa)			tn (Setswana)					sn (Shona)
            sd (Sindhi)						si (Sinhala)					sk (Slovak)
            sl (Slovenian)					so (Somali)						es (Spanish)
            sw (Swahili)						sv (Swedish)					ty (Tahitian)
            ta (Tamil)						tt (Tatar)						te (Telugu)
            th (Thai)							bo (Tibetan)					ti (Tigrinya)
            to (Tongan)						tr (Turkish)					tk (Turkmen)
            uk (Ukrainian)					hsb (Upper Sorbian)				ur (Urdu)
            ug (Uyghur)						uz (Uzbek (Latin))				vi (Vietnamese)
            cy (Welsh)						xh (Xhosa)						yo (Yoruba)
            yua (Yucatec Maya)				zu (Zulu)
           
            auto (Auto Detect)
           """;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    private static final ForgeConfigSpec.EnumValue<Translators> TRANSLATOR = BUILDER
            .comment("You can fill in the following translator: bing, google, baidu")
            .defineEnum("translator", Translators.BING);
    private static final ForgeConfigSpec.EnumValue<Language> LANG_FROM = BUILDER
            .comment("You can fill in the following language:\n"+LANGUAGE_SELECT)
            .defineEnum("lang-from", Language.AUTO_DETECT);

    private static final ForgeConfigSpec.EnumValue<Language> LANG_TO = BUILDER
            .comment("You can fill in the same language as above, except for \"auto\"")
            .defineEnum("lang-to", Language.CHINESE_SIMPLIFIED);
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Translator translator;
    public static Language langFrom;
    public static Language langTo;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        langFrom = LANG_FROM.get();
        langTo = LANG_TO.get();
        setupTranslator(TRANSLATOR.get());
    }

    private static void setupTranslator(Translators translate){
        new Thread(()-> {
            if (translate == Translators.BING) translator = new BingTranslator();
            if (translate == Translators.GOOGLE) translator = null;
            if (translate == Translators.BAIDU) translator = null;
        }).start();
    }
    public static void setTranslator(Translators translator){
        TRANSLATOR.set(translator);
        setupTranslator(translator);
    }
    public static void setLangFrom(Language langFrom){
        LANG_FROM.set(langFrom);
    };
    public static void setLangTo(Language langTo){
        LANG_TO.set(langTo);
    };
}
