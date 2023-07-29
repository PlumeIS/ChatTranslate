package xyz.plumc.chattranslate;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import xyz.plumc.chattranslate.command.TranslateCommand;
import xyz.plumc.chattranslate.command.TranslatorCommand;

@Mod(ChatTranslate.MODID)
public class ChatTranslate {
    public static final String MODID = "chattranslate";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean shouldTranslate = true;
    public static String translatePrefix = "§6[§e%s§6]§f ";
    public static String modChatPrefix = "§6[ChatTranslate]§f ";


    public ChatTranslate() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(new CommandsRegister());
        MinecraftForge.EVENT_BUS.register(new TickTaskHandler());
    }

    @Mod.EventBusSubscriber
    static class CommandsRegister{
        @SubscribeEvent
        public static void onRegisterCommands(RegisterClientCommandsEvent event){
            new TranslatorCommand(event.getDispatcher());
            new TranslateCommand(event.getDispatcher());
        }
    }

    @Mod.EventBusSubscriber
    public static class TickTaskHandler{
        private static Runnable delayedTask = null;


        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && delayedTask != null) {
                Minecraft.getInstance().execute(delayedTask);
                delayedTask = null;
            }
        }
        public static void runOnNextClientTick(Runnable task) {
            delayedTask = task;
        }
    }
}
