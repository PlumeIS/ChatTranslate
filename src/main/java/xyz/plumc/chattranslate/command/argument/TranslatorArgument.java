package xyz.plumc.chattranslate.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import xyz.plumc.chattranslate.translate.Translators;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TranslatorArgument implements ArgumentType<Translators> {
    private static final Collection<String> EXAMPLES = Arrays.asList("bing", "google", "baidu");

    public static TranslatorArgument translators(){return new TranslatorArgument();}
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType((translator) -> {
        return Component.translatable("argument.enum.invalid", translator);
    });

    public static Translators getTranslators(CommandContext<CommandSourceStack> context, String argument){
        return context.getArgument(argument, Translators.class);
    }

    @Override
    public Translators parse(StringReader reader) throws CommandSyntaxException {
        String translate = reader.readUnquotedString();
        Translators translator = Translators.of(translate);
        if (translator==null){
            throw ERROR_INVALID_VALUE.create(translate);
        } else{
            return translator;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.stream(Translators.values()).map((t)-> t.name), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
