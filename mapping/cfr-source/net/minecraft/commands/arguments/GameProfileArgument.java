/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.server.players.NameAndId;

public class GameProfileArgument
implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e");
    public static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType(Component.translatable("argument.player.unknown"));

    public static Collection<NameAndId> getGameProfiles(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return commandContext.getArgument(string, Result.class).getNames(commandContext.getSource());
    }

    public static GameProfileArgument gameProfile() {
        return new GameProfileArgument();
    }

    @Override
    public <S> Result parse(StringReader stringReader, S s) throws CommandSyntaxException {
        return GameProfileArgument.parse(stringReader, EntitySelectorParser.allowSelectors(s));
    }

    @Override
    public Result parse(StringReader stringReader) throws CommandSyntaxException {
        return GameProfileArgument.parse(stringReader, true);
    }

    private static Result parse(StringReader stringReader, boolean bl) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '@') {
            EntitySelectorParser entitySelectorParser = new EntitySelectorParser(stringReader, bl);
            EntitySelector entitySelector = entitySelectorParser.parse();
            if (entitySelector.includesEntities()) {
                throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(stringReader);
            }
            return new SelectorResult(entitySelector);
        }
        int n = stringReader.getCursor();
        while (stringReader.canRead() && stringReader.peek() != ' ') {
            stringReader.skip();
        }
        String string = stringReader.getString().substring(n, stringReader.getCursor());
        return commandSourceStack -> {
            Optional<NameAndId> optional = commandSourceStack.getServer().services().nameToIdCache().get(string);
            return Collections.singleton(optional.orElseThrow(ERROR_UNKNOWN_PLAYER::create));
        };
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder2) {
        Object object = commandContext.getSource();
        if (object instanceof SharedSuggestionProvider) {
            SharedSuggestionProvider sharedSuggestionProvider = (SharedSuggestionProvider)object;
            object = new StringReader(suggestionsBuilder2.getInput());
            ((StringReader)object).setCursor(suggestionsBuilder2.getStart());
            EntitySelectorParser entitySelectorParser = new EntitySelectorParser((StringReader)object, sharedSuggestionProvider.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
            try {
                entitySelectorParser.parse();
            }
            catch (CommandSyntaxException commandSyntaxException) {
                // empty catch block
            }
            return entitySelectorParser.fillSuggestions(suggestionsBuilder2, suggestionsBuilder -> SharedSuggestionProvider.suggest(sharedSuggestionProvider.getOnlinePlayerNames(), suggestionsBuilder));
        }
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public /* synthetic */ Object parse(StringReader stringReader, Object object) throws CommandSyntaxException {
        return this.parse(stringReader, object);
    }

    @Override
    public /* synthetic */ Object parse(StringReader stringReader) throws CommandSyntaxException {
        return this.parse(stringReader);
    }

    @FunctionalInterface
    public static interface Result {
        public Collection<NameAndId> getNames(CommandSourceStack var1) throws CommandSyntaxException;
    }

    public static class SelectorResult
    implements Result {
        private final EntitySelector selector;

        public SelectorResult(EntitySelector entitySelector) {
            this.selector = entitySelector;
        }

        @Override
        public Collection<NameAndId> getNames(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
            List<ServerPlayer> list = this.selector.findPlayers(commandSourceStack);
            if (list.isEmpty()) {
                throw EntityArgument.NO_PLAYERS_FOUND.create();
            }
            ArrayList<NameAndId> arrayList = new ArrayList<NameAndId>();
            for (ServerPlayer serverPlayer : list) {
                arrayList.add(serverPlayer.nameAndId());
            }
            return arrayList;
        }
    }
}

