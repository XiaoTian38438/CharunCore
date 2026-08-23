/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.functions;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetCustomDataFunction
extends LootItemConditionalFunction {
    public static final MapCodec<SetCustomDataFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> SetCustomDataFunction.commonFields(instance).and(((MapCodec)TagParser.LENIENT_CODEC.fieldOf("tag")).forGetter(setCustomDataFunction -> setCustomDataFunction.tag)).apply((Applicative<SetCustomDataFunction, ?>)instance, SetCustomDataFunction::new));
    private final CompoundTag tag;

    private SetCustomDataFunction(List<LootItemCondition> list, CompoundTag compoundTag) {
        super(list);
        this.tag = compoundTag;
    }

    public LootItemFunctionType<SetCustomDataFunction> getType() {
        return LootItemFunctions.SET_CUSTOM_DATA;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        CustomData.update(DataComponents.CUSTOM_DATA, itemStack, compoundTag -> compoundTag.merge(this.tag));
        return itemStack;
    }

    @Deprecated
    public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag compoundTag) {
        return SetCustomDataFunction.simpleBuilder(list -> new SetCustomDataFunction((List<LootItemCondition>)list, compoundTag));
    }
}

