/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.providers.number;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record StorageValue(Identifier storage, NbtPathArgument.NbtPath path) implements NumberProvider
{
    public static final MapCodec<StorageValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("storage")).forGetter(StorageValue::storage), ((MapCodec)NbtPathArgument.NbtPath.CODEC.fieldOf("path")).forGetter(StorageValue::path)).apply((Applicative<StorageValue, ?>)instance, StorageValue::new));

    @Override
    public LootNumberProviderType getType() {
        return NumberProviders.STORAGE;
    }

    private Number getNumericTag(LootContext lootContext, Number number) {
        CompoundTag compoundTag = lootContext.getLevel().getServer().getCommandStorage().get(this.storage);
        try {
            Tag tag;
            List<Tag> list = this.path.get(compoundTag);
            if (list.size() == 1 && (tag = list.getFirst()) instanceof NumericTag) {
                NumericTag numericTag = (NumericTag)tag;
                return numericTag.box();
            }
        }
        catch (CommandSyntaxException commandSyntaxException) {
            // empty catch block
        }
        return number;
    }

    @Override
    public float getFloat(LootContext lootContext) {
        return this.getNumericTag(lootContext, Float.valueOf(0.0f)).floatValue();
    }

    @Override
    public int getInt(LootContext lootContext) {
        return this.getNumericTag(lootContext, 0).intValue();
    }
}

