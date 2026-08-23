/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.permissions;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionLevel;

public interface Permission {
    public static final Codec<Permission> FULL_CODEC = BuiltInRegistries.PERMISSION_TYPE.byNameCodec().dispatch(Permission::codec, mapCodec -> mapCodec);
    public static final Codec<Permission> CODEC = Codec.either(FULL_CODEC, Identifier.CODEC).xmap(either -> either.map(permission -> permission, Atom::create), permission -> {
        Either<Permission, Object> either;
        if (permission instanceof Atom) {
            Atom atom = (Atom)permission;
            either = Either.right(atom.id());
        } else {
            either = Either.left(permission);
        }
        return either;
    });

    public MapCodec<? extends Permission> codec();

    public record Atom(Identifier id) implements Permission
    {
        public static final MapCodec<Atom> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("id")).forGetter(Atom::id)).apply((Applicative<Atom, ?>)instance, Atom::new));

        public MapCodec<Atom> codec() {
            return MAP_CODEC;
        }

        public static Atom create(String string) {
            return Atom.create(Identifier.withDefaultNamespace(string));
        }

        public static Atom create(Identifier identifier) {
            return new Atom(identifier);
        }
    }

    public record HasCommandLevel(PermissionLevel level) implements Permission
    {
        public static final MapCodec<HasCommandLevel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)PermissionLevel.CODEC.fieldOf("level")).forGetter(HasCommandLevel::level)).apply((Applicative<HasCommandLevel, ?>)instance, HasCommandLevel::new));

        public MapCodec<HasCommandLevel> codec() {
            return MAP_CODEC;
        }
    }
}

