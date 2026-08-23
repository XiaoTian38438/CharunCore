/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.functions;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetComponentsFunction
extends LootItemConditionalFunction {
    public static final MapCodec<SetComponentsFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> SetComponentsFunction.commonFields(instance).and(((MapCodec)DataComponentPatch.CODEC.fieldOf("components")).forGetter(setComponentsFunction -> setComponentsFunction.components)).apply((Applicative<SetComponentsFunction, ?>)instance, SetComponentsFunction::new));
    private final DataComponentPatch components;

    private SetComponentsFunction(List<LootItemCondition> list, DataComponentPatch dataComponentPatch) {
        super(list);
        this.components = dataComponentPatch;
    }

    public LootItemFunctionType<SetComponentsFunction> getType() {
        return LootItemFunctions.SET_COMPONENTS;
    }

    @Override
    public ItemStack run(ItemStack itemStack, LootContext lootContext) {
        itemStack.applyComponentsAndValidate(this.components);
        return itemStack;
    }

    public static <T> LootItemConditionalFunction.Builder<?> setComponent(DataComponentType<T> dataComponentType, T t) {
        return SetComponentsFunction.simpleBuilder(list -> new SetComponentsFunction((List<LootItemCondition>)list, DataComponentPatch.builder().set(dataComponentType, t).build()));
    }
}

