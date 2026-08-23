/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.entity.animal.cow;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public record CowVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition>
{
    public static final Codec<CowVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(CowVariant::modelAndTexture), ((MapCodec)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions")).forGetter(CowVariant::spawnConditions)).apply((Applicative<CowVariant, ?>)instance, CowVariant::new));
    public static final Codec<CowVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(CowVariant::modelAndTexture)).apply((Applicative<CowVariant, ?>)instance, CowVariant::new));
    public static final Codec<Holder<CowVariant>> CODEC = RegistryFixedCodec.create(Registries.COW_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<CowVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.COW_VARIANT);

    private CowVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public static enum ModelType implements StringRepresentable
    {
        NORMAL("normal"),
        COLD("cold"),
        WARM("warm");

        public static final Codec<ModelType> CODEC;
        private final String name;

        private ModelType(String string2) {
            this.name = string2;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        static {
            CODEC = StringRepresentable.fromEnum(ModelType::values);
        }
    }
}

