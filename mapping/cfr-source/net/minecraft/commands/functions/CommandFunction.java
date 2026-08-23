/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.Commands;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.commands.execution.tasks.BuildContexts;
import net.minecraft.commands.functions.FunctionBuilder;
import net.minecraft.commands.functions.InstantiatedFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface CommandFunction<T> {
    public Identifier id();

    public InstantiatedFunction<T> instantiate(@Nullable CompoundTag var1, CommandDispatcher<T> var2) throws FunctionInstantiationException;

    private static boolean shouldConcatenateNextLine(CharSequence charSequence) {
        int n = charSequence.length();
        return n > 0 && charSequence.charAt(n - 1) == '\\';
    }

    public static <T extends ExecutionCommandSource<T>> CommandFunction<T> fromLines(Identifier identifier, CommandDispatcher<T> commandDispatcher, T t, List<String> list) {
        FunctionBuilder<T> functionBuilder = new FunctionBuilder<T>();
        for (int i = 0; i < list.size(); ++i) {
            String string;
            String string2;
            Object object;
            int n = i + 1;
            String string3 = list.get(i).trim();
            if (CommandFunction.shouldConcatenateNextLine(string3)) {
                object = new StringBuilder(string3);
                do {
                    if (++i == list.size()) {
                        throw new IllegalArgumentException("Line continuation at end of file");
                    }
                    ((StringBuilder)object).deleteCharAt(((StringBuilder)object).length() - 1);
                    string2 = list.get(i).trim();
                    ((StringBuilder)object).append(string2);
                    CommandFunction.checkCommandLineLength((CharSequence)object);
                } while (CommandFunction.shouldConcatenateNextLine((CharSequence)object));
                string = ((StringBuilder)object).toString();
            } else {
                string = string3;
            }
            CommandFunction.checkCommandLineLength(string);
            object = new StringReader(string);
            if (!((StringReader)object).canRead() || ((StringReader)object).peek() == '#') continue;
            if (((StringReader)object).peek() == '/') {
                ((StringReader)object).skip();
                if (((StringReader)object).peek() == '/') {
                    throw new IllegalArgumentException("Unknown or invalid command '" + string + "' on line " + n + " (if you intended to make a comment, use '#' not '//')");
                }
                string2 = ((StringReader)object).readUnquotedString();
                throw new IllegalArgumentException("Unknown or invalid command '" + string + "' on line " + n + " (did you mean '" + string2 + "'? Do not use a preceding forwards slash.)");
            }
            if (((StringReader)object).peek() == '$') {
                functionBuilder.addMacro(string.substring(1), n, t);
                continue;
            }
            try {
                functionBuilder.addCommand(CommandFunction.parseCommand(commandDispatcher, t, (StringReader)object));
                continue;
            }
            catch (CommandSyntaxException commandSyntaxException) {
                throw new IllegalArgumentException("Whilst parsing command on line " + n + ": " + commandSyntaxException.getMessage());
            }
        }
        return functionBuilder.build(identifier);
    }

    public static void checkCommandLineLength(CharSequence charSequence) {
        if (charSequence.length() > 2000000) {
            CharSequence charSequence2 = charSequence.subSequence(0, Math.min(512, 2000000));
            throw new IllegalStateException("Command too long: " + charSequence.length() + " characters, contents: " + String.valueOf(charSequence2) + "...");
        }
    }

    public static <T extends ExecutionCommandSource<T>> UnboundEntryAction<T> parseCommand(CommandDispatcher<T> commandDispatcher, T t, StringReader stringReader) throws CommandSyntaxException {
        ParseResults<T> parseResults = commandDispatcher.parse(stringReader, t);
        Commands.validateParseResults(parseResults);
        Optional<ContextChain<T>> optional = ContextChain.tryFlatten(parseResults.getContext().build(stringReader.getString()));
        if (optional.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader());
        }
        return new BuildContexts.Unbound<T>(stringReader.getString(), optional.get());
    }
}

