/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.context;

import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ContextChain<S> {
    private final List<CommandContext<S>> modifiers;
    private final CommandContext<S> executable;
    private ContextChain<S> nextStageCache = null;

    public ContextChain(List<CommandContext<S>> list, CommandContext<S> commandContext) {
        if (commandContext.getCommand() == null) {
            throw new IllegalArgumentException("Last command in chain must be executable");
        }
        this.modifiers = list;
        this.executable = commandContext;
    }

    public static <S> Optional<ContextChain<S>> tryFlatten(CommandContext<S> commandContext) {
        ArrayList<CommandContext<S>> arrayList = new ArrayList<CommandContext<S>>();
        CommandContext<S> commandContext2 = commandContext;
        while (true) {
            CommandContext<S> commandContext3;
            if ((commandContext3 = commandContext2.getChild()) == null) {
                if (commandContext2.getCommand() == null) {
                    return Optional.empty();
                }
                return Optional.of(new ContextChain<S>(arrayList, commandContext2));
            }
            arrayList.add(commandContext2);
            commandContext2 = commandContext3;
        }
    }

    public static <S> Collection<S> runModifier(CommandContext<S> commandContext, S s, ResultConsumer<S> resultConsumer, boolean bl) throws CommandSyntaxException {
        RedirectModifier<S> redirectModifier = commandContext.getRedirectModifier();
        if (redirectModifier == null) {
            return Collections.singleton(s);
        }
        CommandContext<S> commandContext2 = commandContext.copyFor(s);
        try {
            return redirectModifier.apply(commandContext2);
        }
        catch (CommandSyntaxException commandSyntaxException) {
            resultConsumer.onCommandComplete(commandContext2, false, 0);
            if (bl) {
                return Collections.emptyList();
            }
            throw commandSyntaxException;
        }
    }

    public static <S> int runExecutable(CommandContext<S> commandContext, S s, ResultConsumer<S> resultConsumer, boolean bl) throws CommandSyntaxException {
        CommandContext<S> commandContext2 = commandContext.copyFor(s);
        try {
            int n = commandContext.getCommand().run(commandContext2);
            resultConsumer.onCommandComplete(commandContext2, true, n);
            return bl ? 1 : n;
        }
        catch (CommandSyntaxException commandSyntaxException) {
            resultConsumer.onCommandComplete(commandContext2, false, 0);
            if (bl) {
                return 0;
            }
            throw commandSyntaxException;
        }
    }

    public int executeAll(S s, ResultConsumer<S> resultConsumer) throws CommandSyntaxException {
        Object object;
        if (this.modifiers.isEmpty()) {
            return ContextChain.runExecutable(this.executable, s, resultConsumer, false);
        }
        boolean bl = false;
        Object object3 = Collections.singletonList(s);
        for (CommandContext<S> object22 : this.modifiers) {
            bl |= object22.isForked();
            object = new ArrayList();
            Iterator<S> iterator = object3.iterator();
            while (iterator.hasNext()) {
                S s2 = iterator.next();
                object.addAll(ContextChain.runModifier(object22, s2, resultConsumer, bl));
            }
            if (object.isEmpty()) {
                return 0;
            }
            object3 = object;
        }
        int n = 0;
        Iterator<S> iterator = object3.iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            n += ContextChain.runExecutable(this.executable, object, resultConsumer, bl);
        }
        return n;
    }

    public Stage getStage() {
        return this.modifiers.isEmpty() ? Stage.EXECUTE : Stage.MODIFY;
    }

    public CommandContext<S> getTopContext() {
        if (this.modifiers.isEmpty()) {
            return this.executable;
        }
        return this.modifiers.get(0);
    }

    public ContextChain<S> nextStage() {
        int n = this.modifiers.size();
        if (n == 0) {
            return null;
        }
        if (this.nextStageCache == null) {
            this.nextStageCache = new ContextChain<S>(this.modifiers.subList(1, n), this.executable);
        }
        return this.nextStageCache;
    }

    public static enum Stage {
        MODIFY,
        EXECUTE;

    }
}

