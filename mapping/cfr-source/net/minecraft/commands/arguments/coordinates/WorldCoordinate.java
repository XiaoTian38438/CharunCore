/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;

public record WorldCoordinate(boolean relative, double value) {
    private static final char PREFIX_RELATIVE = '~';
    public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.double"));
    public static final SimpleCommandExceptionType ERROR_EXPECTED_INT = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.int"));

    public double get(double d) {
        if (this.relative) {
            return this.value + d;
        }
        return this.value;
    }

    public static WorldCoordinate parseDouble(StringReader stringReader, boolean bl) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader);
        }
        if (!stringReader.canRead()) {
            throw ERROR_EXPECTED_DOUBLE.createWithContext(stringReader);
        }
        boolean bl2 = WorldCoordinate.isRelative(stringReader);
        int n = stringReader.getCursor();
        double d = stringReader.canRead() && stringReader.peek() != ' ' ? stringReader.readDouble() : 0.0;
        String string = stringReader.getString().substring(n, stringReader.getCursor());
        if (bl2 && string.isEmpty()) {
            return new WorldCoordinate(true, 0.0);
        }
        if (!string.contains(".") && !bl2 && bl) {
            d += 0.5;
        }
        return new WorldCoordinate(bl2, d);
    }

    public static WorldCoordinate parseInt(StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader);
        }
        if (!stringReader.canRead()) {
            throw ERROR_EXPECTED_INT.createWithContext(stringReader);
        }
        boolean bl = WorldCoordinate.isRelative(stringReader);
        double d = stringReader.canRead() && stringReader.peek() != ' ' ? (bl ? stringReader.readDouble() : (double)stringReader.readInt()) : 0.0;
        return new WorldCoordinate(bl, d);
    }

    public static boolean isRelative(StringReader stringReader) {
        boolean bl;
        if (stringReader.peek() == '~') {
            bl = true;
            stringReader.skip();
        } else {
            bl = false;
        }
        return bl;
    }

    public boolean isRelative() {
        return this.relative;
    }
}

