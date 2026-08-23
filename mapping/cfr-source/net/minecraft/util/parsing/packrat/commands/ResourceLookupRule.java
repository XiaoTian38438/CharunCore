/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util.parsing.packrat.commands;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.resources.Identifier;
import net.minecraft.util.parsing.packrat.DelayedException;
import net.minecraft.util.parsing.packrat.NamedRule;
import net.minecraft.util.parsing.packrat.ParseState;
import net.minecraft.util.parsing.packrat.Rule;
import net.minecraft.util.parsing.packrat.commands.ResourceSuggestion;
import org.jspecify.annotations.Nullable;

public abstract class ResourceLookupRule<C, V>
implements Rule<StringReader, V>,
ResourceSuggestion {
    private final NamedRule<StringReader, Identifier> idParser;
    protected final C context;
    private final DelayedException<CommandSyntaxException> error;

    protected ResourceLookupRule(NamedRule<StringReader, Identifier> namedRule, C c) {
        this.idParser = namedRule;
        this.context = c;
        this.error = DelayedException.create(Identifier.ERROR_INVALID);
    }

    @Override
    public @Nullable V parse(ParseState<StringReader> parseState) {
        parseState.input().skipWhitespace();
        int n = parseState.mark();
        Identifier identifier = parseState.parse(this.idParser);
        if (identifier != null) {
            try {
                return this.validateElement(parseState.input(), identifier);
            }
            catch (Exception exception) {
                parseState.errorCollector().store(n, this, exception);
                return null;
            }
        }
        parseState.errorCollector().store(n, this, this.error);
        return null;
    }

    protected abstract V validateElement(ImmutableStringReader var1, Identifier var2) throws Exception;
}

