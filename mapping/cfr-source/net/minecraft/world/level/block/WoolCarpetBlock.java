/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.block;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WoolCarpetBlock
extends CarpetBlock {
    public static final MapCodec<WoolCarpetBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)DyeColor.CODEC.fieldOf("color")).forGetter(WoolCarpetBlock::getColor), WoolCarpetBlock.propertiesCodec()).apply((Applicative<WoolCarpetBlock, ?>)instance, WoolCarpetBlock::new));
    private final DyeColor color;

    public MapCodec<WoolCarpetBlock> codec() {
        return CODEC;
    }

    protected WoolCarpetBlock(DyeColor dyeColor, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = dyeColor;
    }

    public DyeColor getColor() {
        return this.color;
    }
}

