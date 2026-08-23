/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 */
package net.minecraft.commands.execution.tasks;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.commands.CommandResultCallback;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.CustomCommandExecutor;
import net.minecraft.commands.execution.CustomModifierExecutor;
import net.minecraft.commands.execution.EntryAction;
import net.minecraft.commands.execution.ExecutionContext;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.execution.Frame;
import net.minecraft.commands.execution.TraceCallbacks;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.commands.execution.tasks.ContinuationTask;
import net.minecraft.commands.execution.tasks.ExecuteCommand;
import net.minecraft.commands.execution.tasks.FallthroughTask;
import net.minecraft.network.chat.Component;

public class BuildContexts<T extends ExecutionCommandSource<T>> {
    @VisibleForTesting
    public static final DynamicCommandExceptionType ERROR_FORK_LIMIT_REACHED = new DynamicCommandExceptionType(object -> Component.translatableEscape("command.forkLimit", object));
    private final String commandInput;
    private final ContextChain<T> command;

    public BuildContexts(String string, ContextChain<T> contextChain) {
        this.commandInput = string;
        this.command = contextChain;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void execute(T t, List<T> list, ExecutionContext<T> executionContext, Frame frame, ChainModifiers chainModifiers) {
        Object object;
        Object object2;
        ContextChain<T> contextChain = this.command;
        ChainModifiers chainModifiers2 = chainModifiers;
        List<Object> list2 = list;
        if (contextChain.getStage() != ContextChain.Stage.EXECUTE) {
            executionContext.profiler().push(() -> "prepare " + this.commandInput);
            try {
                int n2 = executionContext.forkLimit();
                while (contextChain.getStage() != ContextChain.Stage.EXECUTE) {
                    object2 = contextChain.getTopContext();
                    if (((CommandContext)object2).isForked()) {
                        chainModifiers2 = chainModifiers2.setForked();
                    }
                    if ((object = ((CommandContext)object2).getRedirectModifier()) instanceof CustomModifierExecutor) {
                        CustomModifierExecutor customModifierExecutor = (CustomModifierExecutor)object;
                        customModifierExecutor.apply(t, list2, contextChain, chainModifiers2, ExecutionControl.create(executionContext, frame));
                        return;
                    }
                    if (object != null) {
                        executionContext.incrementCost();
                        boolean bl2 = chainModifiers2.isForked();
                        ObjectArrayList objectArrayList = new ObjectArrayList();
                        for (ExecutionCommandSource executionCommandSource : list2) {
                            Collection<ExecutionCommandSource> collection;
                            block21: {
                                try {
                                    collection = ContextChain.runModifier(object2, executionCommandSource, (commandContext, bl, n) -> {}, bl2);
                                    if (objectArrayList.size() + collection.size() < n2) break block21;
                                    t.handleError(ERROR_FORK_LIMIT_REACHED.create(n2), bl2, executionContext.tracer());
                                    return;
                                }
                                catch (CommandSyntaxException commandSyntaxException) {
                                    executionCommandSource.handleError(commandSyntaxException, bl2, executionContext.tracer());
                                    if (bl2) continue;
                                    executionContext.profiler().pop();
                                    return;
                                }
                            }
                            objectArrayList.addAll(collection);
                        }
                        list2 = objectArrayList;
                    }
                    contextChain = contextChain.nextStage();
                }
            }
            finally {
                executionContext.profiler().pop();
            }
        }
        if (list2.isEmpty()) {
            if (chainModifiers2.isReturn()) {
                executionContext.queueNext(new CommandQueueEntry(frame, FallthroughTask.instance()));
            }
            return;
        }
        CommandContext<T> commandContext2 = contextChain.getTopContext();
        object2 = commandContext2.getCommand();
        if (object2 instanceof CustomCommandExecutor) {
            object = (CustomCommandExecutor)object2;
            ExecutionControl executionControl = ExecutionControl.create(executionContext, frame);
            for (Object object3 : list2) {
                object.run(object3, contextChain, chainModifiers2, executionControl);
            }
        } else {
            if (chainModifiers2.isReturn()) {
                object = (ExecutionCommandSource)list2.get(0);
                object = object.withCallback(CommandResultCallback.chain(object.callback(), frame.returnValueConsumer()));
                list2 = List.of(object);
            }
            object = new ExecuteCommand<T>(this.commandInput, chainModifiers2, commandContext2);
            ContinuationTask.schedule(executionContext, frame, list2, (arg_0, arg_1) -> BuildContexts.lambda$execute$3((ExecuteCommand)object, arg_0, arg_1));
        }
    }

    protected void traceCommandStart(ExecutionContext<T> executionContext, Frame frame) {
        TraceCallbacks traceCallbacks = executionContext.tracer();
        if (traceCallbacks != null) {
            traceCallbacks.onCommand(frame.depth(), this.commandInput);
        }
    }

    public String toString() {
        return this.commandInput;
    }

    private static /* synthetic */ CommandQueueEntry lambda$execute$3(ExecuteCommand executeCommand, Frame frame, ExecutionCommandSource executionCommandSource) {
        return new CommandQueueEntry<ExecutionCommandSource>(frame, executeCommand.bind(executionCommandSource));
    }

    public static class TopLevel<T extends ExecutionCommandSource<T>>
    extends BuildContexts<T>
    implements EntryAction<T> {
        private final T source;

        public TopLevel(String string, ContextChain<T> contextChain, T t) {
            super(string, contextChain);
            this.source = t;
        }

        @Override
        public void execute(ExecutionContext<T> executionContext, Frame frame) {
            this.traceCommandStart(executionContext, frame);
            this.execute(this.source, List.of(this.source), executionContext, frame, ChainModifiers.DEFAULT);
        }
    }

    public static class Continuation<T extends ExecutionCommandSource<T>>
    extends BuildContexts<T>
    implements EntryAction<T> {
        private final ChainModifiers modifiers;
        private final T originalSource;
        private final List<T> sources;

        public Continuation(String string, ContextChain<T> contextChain, ChainModifiers chainModifiers, T t, List<T> list) {
            super(string, contextChain);
            this.originalSource = t;
            this.sources = list;
            this.modifiers = chainModifiers;
        }

        @Override
        public void execute(ExecutionContext<T> executionContext, Frame frame) {
            this.execute(this.originalSource, this.sources, executionContext, frame, this.modifiers);
        }
    }

    public static class Unbound<T extends ExecutionCommandSource<T>>
    extends BuildContexts<T>
    implements UnboundEntryAction<T> {
        public Unbound(String string, ContextChain<T> contextChain) {
            super(string, contextChain);
        }

        @Override
        public void execute(T t, ExecutionContext<T> executionContext, Frame frame) {
            this.traceCommandStart(executionContext, frame);
            this.execute(t, List.of(t), executionContext, frame, ChainModifiers.DEFAULT);
        }

        @Override
        public /* synthetic */ void execute(Object object, ExecutionContext executionContext, Frame frame) {
            this.execute((ExecutionCommandSource)object, executionContext, frame);
        }
    }
}

