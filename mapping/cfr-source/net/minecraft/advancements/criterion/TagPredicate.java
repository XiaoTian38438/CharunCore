/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public record TagPredicate<T>(TagKey<T> tag, boolean expected) {
    public static <T> Codec<TagPredicate<T>> codec(ResourceKey<? extends Registry<T>> resourceKey) {
        return RecordCodecBuilder.create(instance -> instance.group(((MapCodec)TagKey.codec(resourceKey).fieldOf("id")).forGetter(TagPredicate::tag), ((MapCodec)Codec.BOOL.fieldOf("expected")).forGetter(TagPredicate::expected)).apply((Applicative<TagPredicate, ?>)instance, TagPredicate::new));
    }

    public static <T> TagPredicate<T> is(TagKey<T> tagKey) {
        return new TagPredicate<T>(tagKey, true);
    }

    public static <T> TagPredicate<T> isNot(TagKey<T> tagKey) {
        return new TagPredicate<T>(tagKey, false);
    }

    public boolean matches(Holder<T> holder) {
        return holder.is(this.tag) == this.expected;
    }
}

