/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.chat.contents;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public interface PlainTextContents
extends ComponentContents {
    public static final MapCodec<PlainTextContents> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Codec.STRING.fieldOf("text")).forGetter(PlainTextContents::text)).apply((Applicative<PlainTextContents, ?>)instance, PlainTextContents::create));
    public static final PlainTextContents EMPTY = new PlainTextContents(){

        public String toString() {
            return "empty";
        }

        @Override
        public String text() {
            return "";
        }
    };

    public static PlainTextContents create(String string) {
        return string.isEmpty() ? EMPTY : new LiteralContents(string);
    }

    public String text();

    default public MapCodec<PlainTextContents> codec() {
        return MAP_CODEC;
    }

    public record LiteralContents(String text) implements PlainTextContents
    {
        @Override
        public <T> Optional<T> visit(FormattedText.ContentConsumer<T> contentConsumer) {
            return contentConsumer.accept(this.text);
        }

        @Override
        public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
            return styledContentConsumer.accept(style, this.text);
        }

        @Override
        public String toString() {
            return "literal{" + this.text + "}";
        }
    }
}

