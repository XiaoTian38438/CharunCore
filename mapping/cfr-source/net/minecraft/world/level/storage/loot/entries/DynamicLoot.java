/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class DynamicLoot
extends LootPoolSingletonContainer {
    public static final MapCodec<DynamicLoot> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)Identifier.CODEC.fieldOf("name")).forGetter(dynamicLoot -> dynamicLoot.name)).and(DynamicLoot.singletonFields(instance)).apply(instance, DynamicLoot::new));
    private final Identifier name;

    private DynamicLoot(Identifier identifier, int n, int n2, List<LootItemCondition> list, List<LootItemFunction> list2) {
        super(n, n2, list, list2);
        this.name = identifier;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.DYNAMIC;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumer, LootContext lootContext) {
        lootContext.addDynamicDrops(this.name, consumer);
    }

    public static LootPoolSingletonContainer.Builder<?> dynamicEntry(Identifier identifier) {
        return DynamicLoot.simpleBuilder((n, n2, list, list2) -> new DynamicLoot(identifier, n, n2, list, list2));
    }
}

