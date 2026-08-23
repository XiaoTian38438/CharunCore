/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class BoolArgumentType
implements ArgumentType<Boolean> {
    private static final Collection<String> EXAMPLES = Arrays.asList("true", "false");

    private BoolArgumentType() {
    }

    public static BoolArgumentType bool() {
        return new BoolArgumentType();
    }

    public static boolean getBool(CommandContext<?> commandContext, String string) {
        return commandContext.getArgument(string, Boolean.class);
    }

    @Override
    public Boolean parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readBoolean();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        if ("true".startsWith(suggestionsBuilder.getRemainingLowerCase())) {
            suggestionsBuilder.suggest("true");
        }
        if ("false".startsWith(suggestionsBuilder.getRemainingLowerCase())) {
            suggestionsBuilder.suggest("false");
        }
        return suggestionsBuilder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

