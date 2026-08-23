/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.IntegerSuggestion;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class SuggestionsBuilder {
    private final String input;
    private final String inputLowerCase;
    private final int start;
    private final String remaining;
    private final String remainingLowerCase;
    private final List<Suggestion> result = new ArrayList<Suggestion>();

    public SuggestionsBuilder(String string, String string2, int n) {
        this.input = string;
        this.inputLowerCase = string2;
        this.start = n;
        this.remaining = string.substring(n);
        this.remainingLowerCase = string2.substring(n);
    }

    public SuggestionsBuilder(String string, int n) {
        this(string, string.toLowerCase(Locale.ROOT), n);
    }

    public String getInput() {
        return this.input;
    }

    public int getStart() {
        return this.start;
    }

    public String getRemaining() {
        return this.remaining;
    }

    public String getRemainingLowerCase() {
        return this.remainingLowerCase;
    }

    public Suggestions build() {
        return Suggestions.create(this.input, this.result);
    }

    public CompletableFuture<Suggestions> buildFuture() {
        return CompletableFuture.completedFuture(this.build());
    }

    public SuggestionsBuilder suggest(String string) {
        if (string.equals(this.remaining)) {
            return this;
        }
        this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), string));
        return this;
    }

    public SuggestionsBuilder suggest(String string, Message message) {
        if (string.equals(this.remaining)) {
            return this;
        }
        this.result.add(new Suggestion(StringRange.between(this.start, this.input.length()), string, message));
        return this;
    }

    public SuggestionsBuilder suggest(int n) {
        this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), n));
        return this;
    }

    public SuggestionsBuilder suggest(int n, Message message) {
        this.result.add(new IntegerSuggestion(StringRange.between(this.start, this.input.length()), n, message));
        return this;
    }

    public SuggestionsBuilder add(SuggestionsBuilder suggestionsBuilder) {
        this.result.addAll(suggestionsBuilder.result);
        return this;
    }

    public SuggestionsBuilder createOffset(int n) {
        return new SuggestionsBuilder(this.input, this.inputLowerCase, n);
    }

    public SuggestionsBuilder restart() {
        return this.createOffset(this.start);
    }
}

