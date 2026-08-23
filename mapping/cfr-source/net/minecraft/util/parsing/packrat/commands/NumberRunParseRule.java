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

public abstract class NumberRunParseRule
implements Rule<StringReader, String> {
    private final DelayedException<CommandSyntaxException> noValueError;
    private final DelayedException<CommandSyntaxException> underscoreNotAllowedError;

    public NumberRunParseRule(DelayedException<CommandSyntaxException> delayedException, DelayedException<CommandSyntaxException> delayedException2) {
        this.noValueError = delayedException;
        this.underscoreNotAllowedError = delayedException2;
    }

    @Override
    public @Nullable String parse(ParseState<StringReader> parseState) {
        int n;
        int n2;
        StringReader stringReader = parseState.input();
        stringReader.skipWhitespace();
        String string = stringReader.getString();
        for (n2 = n = stringReader.getCursor(); n2 < string.length() && this.isAccepted(string.charAt(n2)); ++n2) {
        }
        int n3 = n2 - n;
        if (n3 == 0) {
            parseState.errorCollector().store(parseState.mark(), this.noValueError);
            return null;
        }
        if (string.charAt(n) == '_' || string.charAt(n2 - 1) == '_') {
            parseState.errorCollector().store(parseState.mark(), this.underscoreNotAllowedError);
            return null;
        }
        stringReader.setCursor(n2);
        return string.substring(n, n2);
    }

    protected abstract boolean isAccepted(char var1);

    @Override
    public /* synthetic */ @Nullable Object parse(ParseState parseState) {
        return this.parse(parseState);
    }
}

