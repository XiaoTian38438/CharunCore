/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.permissions;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum PermissionLevel implements StringRepresentable
{
    ALL("all", 0),
    MODERATORS("moderators", 1),
    GAMEMASTERS("gamemasters", 2),
    ADMINS("admins", 3),
    OWNERS("owners", 4);

    public static final Codec<PermissionLevel> CODEC;
    private static final IntFunction<PermissionLevel> BY_ID;
    public static final Codec<PermissionLevel> INT_CODEC;
    private final String name;
    private final int id;

    private PermissionLevel(String string2, int n2) {
        this.name = string2;
        this.id = n2;
    }

    public boolean isEqualOrHigherThan(PermissionLevel permissionLevel) {
        return this.id >= permissionLevel.id;
    }

    public static PermissionLevel byId(int n) {
        return BY_ID.apply(n);
    }

    public int id() {
        return this.id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    static {
        CODEC = StringRepresentable.fromEnum(PermissionLevel::values);
        BY_ID = ByIdMap.continuous(permissionLevel -> permissionLevel.id, PermissionLevel.values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
        INT_CODEC = Codec.INT.xmap(BY_ID::apply, permissionLevel -> permissionLevel.id);
    }
}

