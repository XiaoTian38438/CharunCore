/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FunctionArgument
implements ArgumentType<Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "#foo");
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(object -> Component.translatableEscape("arguments.function.tag.unknown", object));
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_FUNCTION = new DynamicCommandExceptionType(object -> Component.translatableEscape("arguments.function.unknown", object));

    public static FunctionArgument functions() {
        return new FunctionArgument();
    }

    @Override
    public Result parse(StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '#') {
            stringReader.skip();
            final Identifier identifier = Identifier.read(stringReader);
            return new Result(){

                @Override
                public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                    return FunctionArgument.getFunctionTag(commandContext, identifier);
                }

                @Override
                public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                    return Pair.of(identifier, Either.right(FunctionArgument.getFunctionTag(commandContext, identifier)));
                }

                @Override
                public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                    return Pair.of(identifier, FunctionArgument.getFunctionTag(commandContext, identifier));
                }
            };
        }
        final Identifier identifier = Identifier.read(stringReader);
        return new Result(){

            @Override
            public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return Collections.singleton(FunctionArgument.getFunction(commandContext, identifier));
            }

            @Override
            public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return Pair.of(identifier, Either.left(FunctionArgument.getFunction(commandContext, identifier)));
            }

            @Override
            public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
                return Pair.of(identifier, Collections.singleton(FunctionArgument.getFunction(commandContext, identifier)));
            }
        };
    }

    static CommandFunction<CommandSourceStack> getFunction(CommandContext<CommandSourceStack> commandContext, Identifier identifier) throws CommandSyntaxException {
        return commandContext.getSource().getServer().getFunctions().get(identifier).orElseThrow(() -> ERROR_UNKNOWN_FUNCTION.create(identifier.toString()));
    }

    static Collection<CommandFunction<CommandSourceStack>> getFunctionTag(CommandContext<CommandSourceStack> commandContext, Identifier identifier) throws CommandSyntaxException {
        List<CommandFunction<CommandSourceStack>> list = commandContext.getSource().getServer().getFunctions().getTag(identifier);
        if (list == null) {
            throw ERROR_UNKNOWN_TAG.create(identifier.toString());
        }
        return list;
    }

    public static Collection<CommandFunction<CommandSourceStack>> getFunctions(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return commandContext.getArgument(string, Result.class).create(commandContext);
    }

    public static Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> getFunctionOrTag(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return commandContext.getArgument(string, Result.class).unwrap(commandContext);
    }

    public static Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> getFunctionCollection(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return commandContext.getArgument(string, Result.class).unwrapToCollection(commandContext);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public /* synthetic */ Object parse(StringReader stringReader) throws CommandSyntaxException {
        return this.parse(stringReader);
    }

    public static interface Result {
        public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;

        public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;

        public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;
    }
}

