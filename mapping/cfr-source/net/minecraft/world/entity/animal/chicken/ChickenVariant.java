/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.entity.animal.chicken;

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

public record ChickenVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition>
{
    public static final Codec<ChickenVariant> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ChickenVariant::modelAndTexture), ((MapCodec)SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions")).forGetter(ChickenVariant::spawnConditions)).apply((Applicative<ChickenVariant, ?>)instance, ChickenVariant::new));
    public static final Codec<ChickenVariant> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ChickenVariant::modelAndTexture)).apply((Applicative<ChickenVariant, ?>)instance, ChickenVariant::new));
    public static final Codec<Holder<ChickenVariant>> CODEC = RegistryFixedCodec.create(Registries.CHICKEN_VARIANT);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ChickenVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.CHICKEN_VARIANT);

    private ChickenVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public static enum ModelType implements StringRepresentable
    {
        NORMAL("normal"),
        COLD("cold");

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

