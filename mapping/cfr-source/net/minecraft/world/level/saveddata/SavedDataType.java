/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.saveddata;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public record SavedDataType<T extends SavedData>(String id, Supplier<T> constructor, Codec<T> codec, DataFixTypes dataFixType) {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SavedDataType)) return false;
        SavedDataType savedDataType = (SavedDataType)object;
        if (!this.id.equals(savedDataType.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "SavedDataType[" + this.id + "]";
    }
}

