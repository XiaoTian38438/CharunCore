/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public final class RegistryFileCodec<E>
implements Codec<Holder<E>> {
    private final ResourceKey<? extends Registry<E>> registryKey;
    private final Codec<E> elementCodec;
    private final boolean allowInline;

    public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> resourceKey, Codec<E> codec) {
        return RegistryFileCodec.create(resourceKey, codec, true);
    }

    public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> resourceKey, Codec<E> codec, boolean bl) {
        return new RegistryFileCodec<E>(resourceKey, codec, bl);
    }

    private RegistryFileCodec(ResourceKey<? extends Registry<E>> resourceKey, Codec<E> codec, boolean bl) {
        this.registryKey = resourceKey;
        this.elementCodec = codec;
        this.allowInline = bl;
    }

    @Override
    public <T> DataResult<T> encode(Holder<E> holder, DynamicOps<T> dynamicOps, T t) {
        RegistryOps registryOps;
        Optional optional;
        if (dynamicOps instanceof RegistryOps && (optional = (registryOps = (RegistryOps)dynamicOps).owner(this.registryKey)).isPresent()) {
            if (!holder.canSerializeIn(optional.get())) {
                return DataResult.error(() -> "Element " + String.valueOf(holder) + " is not valid in current registry set");
            }
            return holder.unwrap().map(resourceKey -> Identifier.CODEC.encode(resourceKey.identifier(), dynamicOps, t), object2 -> this.elementCodec.encode(object2, dynamicOps, t));
        }
        return this.elementCodec.encode(holder.value(), dynamicOps, t);
    }

    @Override
    public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        if (dynamicOps instanceof RegistryOps) {
            RegistryOps registryOps = (RegistryOps)dynamicOps;
            Optional optional = registryOps.getter(this.registryKey);
            if (optional.isEmpty()) {
                return DataResult.error(() -> "Registry does not exist: " + String.valueOf(this.registryKey));
            }
            HolderGetter holderGetter = optional.get();
            DataResult dataResult = Identifier.CODEC.decode(dynamicOps, t);
            if (dataResult.result().isEmpty()) {
                if (!this.allowInline) {
                    return DataResult.error(() -> "Inline definitions not allowed here");
                }
                return this.elementCodec.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Holder::direct));
            }
            Pair pair2 = dataResult.result().get();
            ResourceKey resourceKey = ResourceKey.create(this.registryKey, (Identifier)pair2.getFirst());
            return holderGetter.get(resourceKey).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Failed to get element " + String.valueOf(resourceKey))).map((? super R reference) -> Pair.of(reference, pair2.getSecond())).setLifecycle(Lifecycle.stable());
        }
        return this.elementCodec.decode(dynamicOps, t).map((? super R pair) -> pair.mapFirst(Holder::direct));
    }

    public String toString() {
        return "RegistryFileCodec[" + String.valueOf(this.registryKey) + " " + String.valueOf(this.elementCodec) + "]";
    }

    @Override
    public /* synthetic */ DataResult encode(Object object, DynamicOps dynamicOps, Object object2) {
        return this.encode((Holder)object, dynamicOps, object2);
    }
}

