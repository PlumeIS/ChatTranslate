package xyz.plumc.chattranslate.mixin;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.plumc.chattranslate.ChatTranslate;
import xyz.plumc.chattranslate.Config;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V")
    private void onAddMessage(Component message, MessageSignature p_241566_, int p_240583_, GuiMessageTag p_240624_, boolean p_240558_, CallbackInfo ci){
        String translatePrefix = ChatTranslate.translatePrefix.formatted(Config.langTo.name);
        if (!(message.getString().startsWith(translatePrefix)||message.getString().startsWith(ChatTranslate.modChatPrefix)||!ChatTranslate.shouldTranslate)){
            new Thread(()->{
            String translated = Config.translator.translate(message.getString(), Config.langFrom, Config.langTo);
            if (!message.getString().equals(translated)) Minecraft.getInstance().player.sendSystemMessage(Component.literal(translatePrefix+translated));}).start();
        }
    }
}
