/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.levelgen.structure.pools.alias;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;

public record DirectPoolAlias(ResourceKey<StructureTemplatePool> alias, ResourceKey<StructureTemplatePool> target) implements PoolAliasBinding
{
    static MapCodec<DirectPoolAlias> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("alias")).forGetter(DirectPoolAlias::alias), ((MapCodec)ResourceKey.codec(Registries.TEMPLATE_POOL).fieldOf("target")).forGetter(DirectPoolAlias::target)).apply((Applicative<DirectPoolAlias, ?>)instance, DirectPoolAlias::new));

    @Override
    public void forEachResolved(RandomSource randomSource, BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> biConsumer) {
        biConsumer.accept(this.alias, this.target);
    }

    @Override
    public Stream<ResourceKey<StructureTemplatePool>> allTargets() {
        return Stream.of(this.target);
    }

    public MapCodec<DirectPoolAlias> codec() {
        return CODEC;
    }
}

