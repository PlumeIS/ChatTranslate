package xyz.plumc.chattranslate.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xyz.plumc.chattranslate.ChatTranslate;
import xyz.plumc.chattranslate.Config;
import xyz.plumc.chattranslate.command.argument.LangFromArgument;
import xyz.plumc.chattranslate.command.argument.LangToArgument;
import xyz.plumc.chattranslate.command.argument.TranslatorArgument;
import xyz.plumc.chattranslate.translate.Language;
import xyz.plumc.chattranslate.translate.Translators;

public class TranslatorCommand {
    public TranslatorCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("translator").executes(TranslatorCommand::helper)
                        .then(Commands.literal("translator").executes(TranslatorCommand::helper).then(Commands.argument("translator", TranslatorArgument.translators()).executes(TranslatorCommand::setTranslator)))
                        .then(Commands.literal("langFrom").executes(TranslatorCommand::helper).then(Commands.argument("from", LangFromArgument.langFroms()).executes(TranslatorCommand::setLangFrom)))
                        .then(Commands.literal("langTo").executes(TranslatorCommand::helper).then(Commands.argument("to", LangToArgument.langTos()).executes(TranslatorCommand::setLangTo)))
                        .then(Commands.literal("start").executes(TranslatorCommand::setStart))
                        .then(Commands.literal("stop").executes(TranslatorCommand::setStop))
        );
    }

    private static int setLangTo(CommandContext<CommandSourceStack> context) {
        Language langTo = LangToArgument.getLangTo(context, "to");
        Config.setLangTo(langTo);
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.set.successful", langTo.name));
        return 1;
    }

    private static int setLangFrom(CommandContext<CommandSourceStack> context) {
        Language langFrom = LangFromArgument.getLangFrom(context, "from");
        Config.setLangFrom(langFrom);
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.set.successful", langFrom.name));
        return 1;
    }

    private static int setTranslator(CommandContext<CommandSourceStack> context) {
        Translators translator = TranslatorArgument.getTranslators(context, "translator");
        Config.setTranslator(translator);
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.set.successful", translator.name));
        return 1;
    }

    private static int setStop(CommandContext<CommandSourceStack> context) {
        ChatTranslate.shouldTranslate = false;
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.switch.off"));
        return 1;
    }

    private static int setStart(CommandContext<CommandSourceStack> context) {
        ChatTranslate.shouldTranslate = true;
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.switch.on"));
        return 1;
    }

    private static int helper(CommandContext<CommandSourceStack> context) {
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("commands.chattranslate.translate.help"));
        return 1;
    }

}
