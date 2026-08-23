/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.ParseState;
import net.minecraft.util.parsing.packrat.Rule;
import org.jspecify.annotations.Nullable;

public class UnquotedStringParseRule
implements Rule<StringReader, String> {
    private final int minSize;
    private final DelayedException<CommandSyntaxException> error;

    public UnquotedStringParseRule(int n, DelayedException<CommandSyntaxException> delayedException) {
        this.minSize = n;
        this.error = delayedException;
    }

    @Override
    public @Nullable String parse(ParseState<StringReader> parseState) {
        parseState.input().skipWhitespace();
        int n = parseState.mark();
        String string = parseState.input().readUnquotedString();
        if (string.length() < this.minSize) {
            parseState.errorCollector().store(n, this.error);
            return null;
        }
        return string;
    }

    @Override
    public /* synthetic */ @Nullable Object parse(ParseState parseState) {
        return this.parse(parseState);
    }
}

