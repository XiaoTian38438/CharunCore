/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 */
package net.minecraft.data.worldgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class BastionSharedPools {
    public static void bootstrap(BootstrapContext<StructureTemplatePool> bootstrapContext) {
        HolderGetter<StructureTemplatePool> holderGetter = bootstrapContext.lookup(Registries.TEMPLATE_POOL);
        Holder.Reference<StructureTemplatePool> reference = holderGetter.getOrThrow(Pools.EMPTY);
        Pools.register(bootstrapContext, "bastion/mobs/piglin", new StructureTemplatePool(reference, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 1), Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 4), Pair.of(StructurePoolElement.single("bastion/mobs/crossbow_piglin"), 4), Pair.of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstrapContext, "bastion/mobs/hoglin", new StructureTemplatePool(reference, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/mobs/hoglin"), 2), Pair.of(StructurePoolElement.single("bastion/mobs/empty"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstrapContext, "bastion/blocks/gold", new StructureTemplatePool(reference, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/blocks/air"), 3), Pair.of(StructurePoolElement.single("bastion/blocks/gold"), 1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(bootstrapContext, "bastion/mobs/piglin_melee", new StructureTemplatePool(reference, (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin_always"), 1), Pair.of(StructurePoolElement.single("bastion/mobs/melee_piglin"), 5), Pair.of(StructurePoolElement.single("bastion/mobs/sword_piglin"), 1)), StructureTemplatePool.Projection.RIGID));
    }
}

