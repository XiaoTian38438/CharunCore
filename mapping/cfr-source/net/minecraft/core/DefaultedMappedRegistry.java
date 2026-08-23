/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class DefaultedMappedRegistry<T>
extends MappedRegistry<T>
implements DefaultedRegistry<T> {
    private final Identifier defaultKey;
    private Holder.Reference<T> defaultValue;

    public DefaultedMappedRegistry(String string, ResourceKey<? extends Registry<T>> resourceKey, Lifecycle lifecycle, boolean bl) {
        super(resourceKey, lifecycle, bl);
        this.defaultKey = Identifier.parse(string);
    }

    @Override
    public Holder.Reference<T> register(ResourceKey<T> resourceKey, T t, RegistrationInfo registrationInfo) {
        Holder.Reference<T> reference = super.register(resourceKey, t, registrationInfo);
        if (this.defaultKey.equals(resourceKey.identifier())) {
            this.defaultValue = reference;
        }
        return reference;
    }

    @Override
    public int getId(@Nullable T t) {
        int n = super.getId(t);
        return n == -1 ? super.getId(this.defaultValue.value()) : n;
    }

    @Override
    public Identifier getKey(T t) {
        Identifier identifier = super.getKey(t);
        return identifier == null ? this.defaultKey : identifier;
    }

    @Override
    public T getValue(@Nullable Identifier identifier) {
        Object t = super.getValue(identifier);
        return t == null ? this.defaultValue.value() : t;
    }

    @Override
    public Optional<T> getOptional(@Nullable Identifier identifier) {
        return Optional.ofNullable(super.getValue(identifier));
    }

    @Override
    public Optional<Holder.Reference<T>> getAny() {
        return Optional.ofNullable(this.defaultValue);
    }

    @Override
    public T byId(int n) {
        Object t = super.byId(n);
        return t == null ? this.defaultValue.value() : t;
    }

    @Override
    public Optional<Holder.Reference<T>> getRandom(RandomSource randomSource) {
        return super.getRandom(randomSource).or(() -> Optional.of(this.defaultValue));
    }

    @Override
    public Identifier getDefaultKey() {
        return this.defaultKey;
    }
}

