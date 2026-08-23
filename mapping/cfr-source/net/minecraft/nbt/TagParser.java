/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtGrammar;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.parsing.packrat.commands.Grammar;

public class TagParser<T> {
    public static final SimpleCommandExceptionType ERROR_TRAILING_DATA = new SimpleCommandExceptionType(Component.translatable("argument.nbt.trailing"));
    public static final SimpleCommandExceptionType ERROR_EXPECTED_COMPOUND = new SimpleCommandExceptionType(Component.translatable("argument.nbt.expected.compound"));
    public static final char ELEMENT_SEPARATOR = ',';
    public static final char NAME_VALUE_SEPARATOR = ':';
    private static final TagParser<Tag> NBT_OPS_PARSER = TagParser.create(NbtOps.INSTANCE);
    public static final Codec<CompoundTag> FLATTENED_CODEC = Codec.STRING.comapFlatMap(string -> {
        try {
            Tag tag = NBT_OPS_PARSER.parseFully((String)string);
            if (tag instanceof CompoundTag) {
                CompoundTag compoundTag = (CompoundTag)tag;
                return DataResult.success(compoundTag, Lifecycle.stable());
            }
            return DataResult.error(() -> "Expected compound tag, got " + String.valueOf(tag));
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return DataResult.error(commandSyntaxException::getMessage);
        }
    }, CompoundTag::toString);
    public static final Codec<CompoundTag> LENIENT_CODEC = Codec.withAlternative(FLATTENED_CODEC, CompoundTag.CODEC);
    private final DynamicOps<T> ops;
    private final Grammar<T> grammar;

    private TagParser(DynamicOps<T> dynamicOps, Grammar<T> grammar) {
        this.ops = dynamicOps;
        this.grammar = grammar;
    }

    public DynamicOps<T> getOps() {
        return this.ops;
    }

    public static <T> TagParser<T> create(DynamicOps<T> dynamicOps) {
        return new TagParser<T>(dynamicOps, SnbtGrammar.createParser(dynamicOps));
    }

    private static CompoundTag castToCompoundOrThrow(StringReader stringReader, Tag tag) throws CommandSyntaxException {
        if (tag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag)tag;
            return compoundTag;
        }
        throw ERROR_EXPECTED_COMPOUND.createWithContext(stringReader);
    }

    public static CompoundTag parseCompoundFully(String string) throws CommandSyntaxException {
        StringReader stringReader = new StringReader(string);
        return TagParser.castToCompoundOrThrow(stringReader, NBT_OPS_PARSER.parseFully(stringReader));
    }

    public T parseFully(String string) throws CommandSyntaxException {
        return this.parseFully(new StringReader(string));
    }

    public T parseFully(StringReader stringReader) throws CommandSyntaxException {
        T t = this.grammar.parseForCommands(stringReader);
        stringReader.skipWhitespace();
        if (stringReader.canRead()) {
            throw ERROR_TRAILING_DATA.createWithContext(stringReader);
        }
        return t;
    }

    public T parseAsArgument(StringReader stringReader) throws CommandSyntaxException {
        return this.grammar.parseForCommands(stringReader);
    }

    public static CompoundTag parseCompoundAsArgument(StringReader stringReader) throws CommandSyntaxException {
        Tag tag = NBT_OPS_PARSER.parseAsArgument(stringReader);
        return TagParser.castToCompoundOrThrow(stringReader, tag);
    }
}

