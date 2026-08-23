/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import java.util.Objects;

public class IntegerSuggestion
extends Suggestion {
    private int value;

    public IntegerSuggestion(StringRange stringRange, int n) {
        this(stringRange, n, null);
    }

    public IntegerSuggestion(StringRange stringRange, int n, Message message) {
        super(stringRange, Integer.toString(n), message);
        this.value = n;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IntegerSuggestion)) {
            return false;
        }
        IntegerSuggestion integerSuggestion = (IntegerSuggestion)object;
        return this.value == integerSuggestion.value && super.equals(object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.value);
    }

    @Override
    public String toString() {
        return "IntegerSuggestion{value=" + this.value + ", range=" + this.getRange() + ", text='" + this.getText() + '\'' + ", tooltip='" + this.getTooltip() + '\'' + '}';
    }

    @Override
    public int compareTo(Suggestion suggestion) {
        if (suggestion instanceof IntegerSuggestion) {
            return Integer.compare(this.value, ((IntegerSuggestion)suggestion).value);
        }
        return super.compareTo(suggestion);
    }

    @Override
    public int compareToIgnoreCase(Suggestion suggestion) {
        return this.compareTo(suggestion);
    }
}

