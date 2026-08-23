/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.world.level.storage.loot;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jspecify.annotations.Nullable;

public interface LootContextArg<R> {
    public static final Codec<LootContextArg<Object>> ENTITY_OR_BLOCK = LootContextArg.createArgCodec(argCodecBuilder -> argCodecBuilder.anyOf(LootContext.EntityTarget.values()).anyOf(LootContext.BlockEntityTarget.values()));

    public @Nullable R get(LootContext var1);

    public ContextKey<?> contextParam();

    public static <U> LootContextArg<U> cast(LootContextArg<? extends U> lootContextArg) {
        return lootContextArg;
    }

    public static <R> Codec<LootContextArg<R>> createArgCodec(UnaryOperator<ArgCodecBuilder<R>> unaryOperator) {
        return ((ArgCodecBuilder)unaryOperator.apply(new ArgCodecBuilder())).build();
    }

    public static final class ArgCodecBuilder<R> {
        private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();

        ArgCodecBuilder() {
        }

        public <T> ArgCodecBuilder<R> anyOf(T[] TArray, Function<T, String> function, Function<T, ? extends LootContextArg<R>> function2) {
            for (T t : TArray) {
                this.sources.put(function.apply(t), function2.apply(t));
            }
            return this;
        }

        public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] TArray, Function<T, ? extends LootContextArg<R>> function) {
            return this.anyOf(TArray, StringRepresentable::getSerializedName, function);
        }

        public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] TArray) {
            return this.anyOf((StringRepresentable[])TArray, object -> LootContextArg.cast((LootContextArg)object));
        }

        public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> function) {
            return this.anyOf(LootContext.EntityTarget.values(), entityTarget -> (LootContextArg)function.apply(entityTarget.contextParam()));
        }

        public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> function) {
            return this.anyOf(LootContext.BlockEntityTarget.values(), blockEntityTarget -> (LootContextArg)function.apply(blockEntityTarget.contextParam()));
        }

        public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemStack>, ? extends LootContextArg<R>> function) {
            return this.anyOf(LootContext.ItemStackTarget.values(), itemStackTarget -> (LootContextArg)function.apply(itemStackTarget.contextParam()));
        }

        Codec<LootContextArg<R>> build() {
            return this.sources.codec(Codec.STRING);
        }
    }

    public static interface SimpleGetter<T>
    extends LootContextArg<T> {
        @Override
        public ContextKey<? extends T> contextParam();

        @Override
        default public @Nullable T get(LootContext lootContext) {
            return lootContext.getOptionalParameter(this.contextParam());
        }
    }

    public static interface Getter<T, R>
    extends LootContextArg<R> {
        public @Nullable R get(T var1);

        @Override
        public ContextKey<? extends T> contextParam();

        @Override
        default public @Nullable R get(LootContext lootContext) {
            T t = lootContext.getOptionalParameter(this.contextParam());
            return t != null ? (R)this.get(t) : null;
        }
    }
}

