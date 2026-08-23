/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class DynamicNCommandExceptionType
implements CommandExceptionType {
    private final Function function;

    public DynamicNCommandExceptionType(Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(Object object, Object ... objectArray) {
        return new CommandSyntaxException(this, this.function.apply(objectArray));
    }

    public CommandSyntaxException createWithContext(ImmutableStringReader immutableStringReader, Object ... objectArray) {
        return new CommandSyntaxException(this, this.function.apply(objectArray), immutableStringReader.getString(), immutableStringReader.getCursor());
    }

    public static interface Function {
        public Message apply(Object[] var1);
    }
}

