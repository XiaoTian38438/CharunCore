/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.parsing.packrat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.util.parsing.packrat.commands.StringReaderTerms;

public interface DelayedException<T extends Exception> {
    public T create(String var1, int var2);

    public static DelayedException<CommandSyntaxException> create(SimpleCommandExceptionType simpleCommandExceptionType) {
        return (string, n) -> simpleCommandExceptionType.createWithContext(StringReaderTerms.createReader(string, n));
    }

    public static DelayedException<CommandSyntaxException> create(DynamicCommandExceptionType dynamicCommandExceptionType, String string) {
        return (string2, n) -> dynamicCommandExceptionType.createWithContext(StringReaderTerms.createReader(string2, n), string);
    }
}

