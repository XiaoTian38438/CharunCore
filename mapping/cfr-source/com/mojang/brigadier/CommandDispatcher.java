/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommandDispatcher<S> {
    public static final String ARGUMENT_SEPARATOR = " ";
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";
    private final RootCommandNode<S> root;
    private final Predicate<CommandNode<S>> hasCommand = new Predicate<CommandNode<S>>(){

        @Override
        public boolean test(CommandNode<S> commandNode) {
            return commandNode != null && (commandNode.getCommand() != null || commandNode.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand));
        }
    };
    private ResultConsumer<S> consumer = (commandContext, bl, n) -> {};

    public CommandDispatcher(RootCommandNode<S> rootCommandNode) {
        this.root = rootCommandNode;
    }

    public CommandDispatcher() {
        this(new RootCommandNode());
    }

    public LiteralCommandNode<S> register(LiteralArgumentBuilder<S> literalArgumentBuilder) {
        CommandNode commandNode = literalArgumentBuilder.build();
        this.root.addChild(commandNode);
        return commandNode;
    }

    public void setConsumer(ResultConsumer<S> resultConsumer) {
        this.consumer = resultConsumer;
    }

    public int execute(String string, S s) throws CommandSyntaxException {
        return this.execute(new StringReader(string), s);
    }

    public int execute(StringReader stringReader, S s) throws CommandSyntaxException {
        ParseResults<S> parseResults = this.parse(stringReader, s);
        return this.execute(parseResults);
    }

    public int execute(ParseResults<S> parseResults) throws CommandSyntaxException {
        if (parseResults.getReader().canRead()) {
            if (parseResults.getExceptions().size() == 1) {
                throw parseResults.getExceptions().values().iterator().next();
            }
            if (parseResults.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader());
            }
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parseResults.getReader());
        }
        String string = parseResults.getReader().getString();
        CommandContext<S> commandContext = parseResults.getContext().build(string);
        Optional<ContextChain<S>> optional = ContextChain.tryFlatten(commandContext);
        if (!optional.isPresent()) {
            this.consumer.onCommandComplete(commandContext, false, 0);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseResults.getReader());
        }
        return optional.get().executeAll(commandContext.getSource(), this.consumer);
    }

    public ParseResults<S> parse(String string, S s) {
        return this.parse(new StringReader(string), s);
    }

    public ParseResults<S> parse(StringReader stringReader, S s) {
        CommandContextBuilder<S> commandContextBuilder = new CommandContextBuilder<S>(this, s, this.root, stringReader.getCursor());
        return this.parseNodes(this.root, stringReader, commandContextBuilder);
    }

    private ParseResults<S> parseNodes(CommandNode<S> commandNode, StringReader stringReader, CommandContextBuilder<S> commandContextBuilder) {
        S s = commandContextBuilder.getSource();
        LinkedHashMap<CommandNode<S>, CommandSyntaxException> linkedHashMap = null;
        ArrayList<ParseResults<S>> arrayList = null;
        int n = stringReader.getCursor();
        for (CommandNode<S> commandNode2 : commandNode.getRelevantNodes(stringReader)) {
            if (!commandNode2.canUse(s)) continue;
            CommandContextBuilder<S> commandContextBuilder2 = commandContextBuilder.copy();
            StringReader stringReader2 = new StringReader(stringReader);
            try {
                try {
                    commandNode2.parse(stringReader2, commandContextBuilder2);
                }
                catch (RuntimeException runtimeException) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(stringReader2, runtimeException.getMessage());
                }
                if (stringReader2.canRead() && stringReader2.peek() != ' ') {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(stringReader2);
                }
            }
            catch (CommandSyntaxException commandSyntaxException) {
                if (linkedHashMap == null) {
                    linkedHashMap = new LinkedHashMap<CommandNode<S>, CommandSyntaxException>();
                }
                linkedHashMap.put(commandNode2, commandSyntaxException);
                stringReader2.setCursor(n);
                continue;
            }
            commandContextBuilder2.withCommand(commandNode2.getCommand());
            if (stringReader2.canRead(commandNode2.getRedirect() == null ? 2 : 1)) {
                Object object;
                stringReader2.skip();
                if (commandNode2.getRedirect() != null) {
                    object = new CommandContextBuilder<S>(this, s, commandNode2.getRedirect(), stringReader2.getCursor());
                    ParseResults<S> parseResults3 = this.parseNodes(commandNode2.getRedirect(), stringReader2, (CommandContextBuilder<S>)object);
                    commandContextBuilder2.withChild(parseResults3.getContext());
                    return new ParseResults<S>(commandContextBuilder2, parseResults3.getReader(), parseResults3.getExceptions());
                }
                object = this.parseNodes(commandNode2, stringReader2, commandContextBuilder2);
                if (arrayList == null) {
                    arrayList = new ArrayList(1);
                }
                arrayList.add((ParseResults<S>)object);
                continue;
            }
            if (arrayList == null) {
                arrayList = new ArrayList<ParseResults<S>>(1);
            }
            arrayList.add(new ParseResults<S>(commandContextBuilder2, stringReader2, Collections.emptyMap()));
        }
        if (arrayList != null) {
            if (arrayList.size() > 1) {
                arrayList.sort((parseResults, parseResults2) -> {
                    if (!parseResults.getReader().canRead() && parseResults2.getReader().canRead()) {
                        return -1;
                    }
                    if (parseResults.getReader().canRead() && !parseResults2.getReader().canRead()) {
                        return 1;
                    }
                    if (parseResults.getExceptions().isEmpty() && !parseResults2.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!parseResults.getExceptions().isEmpty() && parseResults2.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return (ParseResults)arrayList.get(0);
        }
        return new ParseResults<S>(commandContextBuilder, stringReader, linkedHashMap == null ? Collections.emptyMap() : linkedHashMap);
    }

    public String[] getAllUsage(CommandNode<S> commandNode, S s, boolean bl) {
        ArrayList<String> arrayList = new ArrayList<String>();
        this.getAllUsage(commandNode, s, arrayList, "", bl);
        return arrayList.toArray(new String[arrayList.size()]);
    }

    private void getAllUsage(CommandNode<S> commandNode, S s, ArrayList<String> arrayList, String string, boolean bl) {
        if (bl && !commandNode.canUse(s)) {
            return;
        }
        if (commandNode.getCommand() != null) {
            arrayList.add(string);
        }
        if (commandNode.getRedirect() != null) {
            String string2 = commandNode.getRedirect() == this.root ? "..." : "-> " + commandNode.getRedirect().getUsageText();
            arrayList.add(string.isEmpty() ? commandNode.getUsageText() + ARGUMENT_SEPARATOR + string2 : string + ARGUMENT_SEPARATOR + string2);
        } else if (!commandNode.getChildren().isEmpty()) {
            for (CommandNode<S> commandNode2 : commandNode.getChildren()) {
                this.getAllUsage(commandNode2, s, arrayList, string.isEmpty() ? commandNode2.getUsageText() : string + ARGUMENT_SEPARATOR + commandNode2.getUsageText(), bl);
            }
        }
    }

    public Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> commandNode, S s) {
        LinkedHashMap<CommandNode<S>, String> linkedHashMap = new LinkedHashMap<CommandNode<S>, String>();
        boolean bl = commandNode.getCommand() != null;
        for (CommandNode<S> commandNode2 : commandNode.getChildren()) {
            String string = this.getSmartUsage(commandNode2, s, bl, false);
            if (string == null) continue;
            linkedHashMap.put(commandNode2, string);
        }
        return linkedHashMap;
    }

    private String getSmartUsage(CommandNode<S> commandNode2, S s, boolean bl, boolean bl2) {
        String string;
        if (!commandNode2.canUse(s)) {
            return null;
        }
        String string2 = bl ? USAGE_OPTIONAL_OPEN + commandNode2.getUsageText() + USAGE_OPTIONAL_CLOSE : commandNode2.getUsageText();
        boolean bl3 = commandNode2.getCommand() != null;
        String string3 = bl3 ? USAGE_OPTIONAL_OPEN : USAGE_REQUIRED_OPEN;
        String string4 = string = bl3 ? USAGE_OPTIONAL_CLOSE : USAGE_REQUIRED_CLOSE;
        if (!bl2) {
            if (commandNode2.getRedirect() != null) {
                String string5 = commandNode2.getRedirect() == this.root ? "..." : "-> " + commandNode2.getRedirect().getUsageText();
                return string2 + ARGUMENT_SEPARATOR + string5;
            }
            Collection collection = commandNode2.getChildren().stream().filter(commandNode -> commandNode.canUse(s)).collect(Collectors.toList());
            if (collection.size() == 1) {
                String string6 = this.getSmartUsage((CommandNode)collection.iterator().next(), s, bl3, bl3);
                if (string6 != null) {
                    return string2 + ARGUMENT_SEPARATOR + string6;
                }
            } else if (collection.size() > 1) {
                Object object;
                LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
                for (CommandNode commandNode3 : collection) {
                    String string7 = this.getSmartUsage(commandNode3, s, bl3, true);
                    if (string7 == null) continue;
                    linkedHashSet.add(string7);
                }
                if (linkedHashSet.size() == 1) {
                    object = (String)linkedHashSet.iterator().next();
                    return string2 + ARGUMENT_SEPARATOR + (String)(bl3 ? USAGE_OPTIONAL_OPEN + (String)object + USAGE_OPTIONAL_CLOSE : object);
                }
                if (linkedHashSet.size() > 1) {
                    object = new StringBuilder(string3);
                    int n = 0;
                    for (CommandNode commandNode4 : collection) {
                        if (n > 0) {
                            ((StringBuilder)object).append(USAGE_OR);
                        }
                        ((StringBuilder)object).append(commandNode4.getUsageText());
                        ++n;
                    }
                    if (n > 0) {
                        ((StringBuilder)object).append(string);
                        return string2 + ARGUMENT_SEPARATOR + ((StringBuilder)object).toString();
                    }
                }
            }
        }
        return string2;
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parseResults) {
        return this.getCompletionSuggestions(parseResults, parseResults.getReader().getTotalLength());
    }

    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parseResults, int n) {
        CommandContextBuilder<S> commandContextBuilder = parseResults.getContext();
        SuggestionContext<S> suggestionContext = commandContextBuilder.findSuggestionContext(n);
        CommandNode commandNode = suggestionContext.parent;
        int n2 = Math.min(suggestionContext.startPos, n);
        String string = parseResults.getReader().getString();
        String string2 = string.substring(0, n);
        String string3 = string2.toLowerCase(Locale.ROOT);
        CompletableFuture[] completableFutureArray = new CompletableFuture[commandNode.getChildren().size()];
        int n3 = 0;
        for (CommandNode<S> commandNode2 : commandNode.getChildren()) {
            CompletableFuture<Suggestions> completableFuture = Suggestions.empty();
            try {
                completableFuture = commandNode2.listSuggestions(commandContextBuilder.build(string2), new SuggestionsBuilder(string2, string3, n2));
            }
            catch (CommandSyntaxException commandSyntaxException) {
                // empty catch block
            }
            completableFutureArray[n3++] = completableFuture;
        }
        CompletableFuture completableFuture = new CompletableFuture();
        CompletableFuture.allOf(completableFutureArray).thenRun(() -> {
            ArrayList<Suggestions> arrayList = new ArrayList<Suggestions>();
            for (CompletableFuture completableFuture2 : completableFutureArray) {
                arrayList.add((Suggestions)completableFuture2.join());
            }
            completableFuture.complete(Suggestions.merge(string, arrayList));
        });
        return completableFuture;
    }

    public RootCommandNode<S> getRoot() {
        return this.root;
    }

    public Collection<String> getPath(CommandNode<S> commandNode) {
        ArrayList<List<CommandNode<S>>> arrayList = new ArrayList<List<CommandNode<S>>>();
        this.addPaths(this.root, arrayList, new ArrayList<CommandNode<S>>());
        for (List list : arrayList) {
            if (list.get(list.size() - 1) != commandNode) continue;
            ArrayList<String> arrayList2 = new ArrayList<String>(list.size());
            for (CommandNode commandNode2 : list) {
                if (commandNode2 == this.root) continue;
                arrayList2.add(commandNode2.getName());
            }
            return arrayList2;
        }
        return Collections.emptyList();
    }

    public CommandNode<S> findNode(Collection<String> collection) {
        CommandNode commandNode = this.root;
        for (String string : collection) {
            if ((commandNode = commandNode.getChild(string)) != null) continue;
            return null;
        }
        return commandNode;
    }

    public void findAmbiguities(AmbiguityConsumer<S> ambiguityConsumer) {
        this.root.findAmbiguities(ambiguityConsumer);
    }

    private void addPaths(CommandNode<S> commandNode, List<List<CommandNode<S>>> list, List<CommandNode<S>> list2) {
        ArrayList<CommandNode<S>> arrayList = new ArrayList<CommandNode<S>>(list2);
        arrayList.add(commandNode);
        list.add(arrayList);
        for (CommandNode<S> commandNode2 : commandNode.getChildren()) {
            this.addPaths(commandNode2, list, arrayList);
        }
    }
}

