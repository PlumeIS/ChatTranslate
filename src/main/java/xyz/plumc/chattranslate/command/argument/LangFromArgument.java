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
import xyz.plumc.chattranslate.translate.Language;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class LangFromArgument implements ArgumentType<Language> {
    private static final Collection<String> EXAMPLES = Arrays.asList("auto", "en", "zh-Hans");

    public static LangFromArgument langFroms(){return new LangFromArgument();}
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType((language) -> {
        return Component.translatable("argument.enum.invalid", language);
    });

    public static Language getLangFrom(CommandContext<CommandSourceStack> context, String argument){
        return context.getArgument(argument, Language.class);
    }

    @Override
    public Language parse(StringReader reader) throws CommandSyntaxException {
        String translate = reader.readUnquotedString();
        Language language = Language.of(translate);
        if (language==null){
            throw ERROR_INVALID_VALUE.create(translate);
        } else{
            return language;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.stream(Language.values()).map((l)-> l.value), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
