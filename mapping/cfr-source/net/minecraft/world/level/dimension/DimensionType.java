/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.level.dimension;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.nio.file.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.timeline.Timeline;

public record DimensionType(boolean hasFixedTime, boolean hasSkyLight, boolean hasCeiling, double coordinateScale, int minY, int height, int logicalHeight, TagKey<Block> infiniburn, float ambientLight, MonsterSettings monsterSettings, Skybox skybox, CardinalLightType cardinalLightType, EnvironmentAttributeMap attributes, HolderSet<Timeline> timelines) {
    public static final int BITS_FOR_Y = BlockPos.PACKED_Y_LENGTH;
    public static final int MIN_HEIGHT = 16;
    public static final int Y_SIZE = (1 << BITS_FOR_Y) - 32;
    public static final int MAX_Y = (Y_SIZE >> 1) - 1;
    public static final int MIN_Y = MAX_Y - Y_SIZE + 1;
    public static final int WAY_ABOVE_MAX_Y = MAX_Y << 4;
    public static final int WAY_BELOW_MIN_Y = MIN_Y << 4;
    public static final Codec<DimensionType> DIRECT_CODEC = DimensionType.createDirectCodec(EnvironmentAttributeMap.CODEC);
    public static final Codec<DimensionType> NETWORK_CODEC = DimensionType.createDirectCodec(EnvironmentAttributeMap.NETWORK_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<DimensionType>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.DIMENSION_TYPE);
    public static final float[] MOON_BRIGHTNESS_PER_PHASE = new float[]{1.0f, 0.75f, 0.5f, 0.25f, 0.0f, 0.25f, 0.5f, 0.75f};
    public static final Codec<Holder<DimensionType>> CODEC = RegistryFileCodec.create(Registries.DIMENSION_TYPE, DIRECT_CODEC);

    public DimensionType {
        if (n2 < 16) {
            throw new IllegalStateException("height has to be at least 16");
        }
        if (n + n2 > MAX_Y + 1) {
            throw new IllegalStateException("min_y + height cannot be higher than: " + (MAX_Y + 1));
        }
        if (n3 > n2) {
            throw new IllegalStateException("logical_height cannot be higher than height");
        }
        if (n2 % 16 != 0) {
            throw new IllegalStateException("height has to be multiple of 16");
        }
        if (n % 16 != 0) {
            throw new IllegalStateException("min_y has to be a multiple of 16");
        }
    }

    private static Codec<DimensionType> createDirectCodec(Codec<EnvironmentAttributeMap> codec) {
        return ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.optionalFieldOf("has_fixed_time", false).forGetter(DimensionType::hasFixedTime), ((MapCodec)Codec.BOOL.fieldOf("has_skylight")).forGetter(DimensionType::hasSkyLight), ((MapCodec)Codec.BOOL.fieldOf("has_ceiling")).forGetter(DimensionType::hasCeiling), ((MapCodec)Codec.doubleRange(1.0E-5f, 3.0E7).fieldOf("coordinate_scale")).forGetter(DimensionType::coordinateScale), ((MapCodec)Codec.intRange(MIN_Y, MAX_Y).fieldOf("min_y")).forGetter(DimensionType::minY), ((MapCodec)Codec.intRange(16, Y_SIZE).fieldOf("height")).forGetter(DimensionType::height), ((MapCodec)Codec.intRange(0, Y_SIZE).fieldOf("logical_height")).forGetter(DimensionType::logicalHeight), ((MapCodec)TagKey.hashedCodec(Registries.BLOCK).fieldOf("infiniburn")).forGetter(DimensionType::infiniburn), ((MapCodec)Codec.FLOAT.fieldOf("ambient_light")).forGetter(DimensionType::ambientLight), MonsterSettings.CODEC.forGetter(DimensionType::monsterSettings), Skybox.CODEC.optionalFieldOf("skybox", Skybox.OVERWORLD).forGetter(DimensionType::skybox), CardinalLightType.CODEC.optionalFieldOf("cardinal_light", CardinalLightType.DEFAULT).forGetter(DimensionType::cardinalLightType), codec.optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(DimensionType::attributes), RegistryCodecs.homogeneousList(Registries.TIMELINE).optionalFieldOf("timelines", HolderSet.empty()).forGetter(DimensionType::timelines)).apply((Applicative<DimensionType, ?>)instance, DimensionType::new)));
    }

    public static double getTeleportationScale(DimensionType dimensionType, DimensionType dimensionType2) {
        double d = dimensionType.coordinateScale();
        double d2 = dimensionType2.coordinateScale();
        return d / d2;
    }

    public static Path getStorageFolder(ResourceKey<Level> resourceKey, Path path) {
        if (resourceKey == Level.OVERWORLD) {
            return path;
        }
        if (resourceKey == Level.END) {
            return path.resolve("DIM1");
        }
        if (resourceKey == Level.NETHER) {
            return path.resolve("DIM-1");
        }
        return path.resolve("dimensions").resolve(resourceKey.identifier().getNamespace()).resolve(resourceKey.identifier().getPath());
    }

    public IntProvider monsterSpawnLightTest() {
        return this.monsterSettings.monsterSpawnLightTest();
    }

    public int monsterSpawnBlockLightLimit() {
        return this.monsterSettings.monsterSpawnBlockLightLimit();
    }

    public boolean hasEndFlashes() {
        return this.skybox == Skybox.END;
    }

    public record MonsterSettings(IntProvider monsterSpawnLightTest, int monsterSpawnBlockLightLimit) {
        public static final MapCodec<MonsterSettings> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(((MapCodec)IntProvider.codec(0, 15).fieldOf("monster_spawn_light_level")).forGetter(MonsterSettings::monsterSpawnLightTest), ((MapCodec)Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit")).forGetter(MonsterSettings::monsterSpawnBlockLightLimit)).apply((Applicative<MonsterSettings, ?>)instance, MonsterSettings::new));
    }

    public static enum Skybox implements StringRepresentable
    {
        NONE("none"),
        OVERWORLD("overworld"),
        END("end");

        public static final Codec<Skybox> CODEC;
        private final String name;

        private Skybox(String string2) {
            this.name = string2;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        static {
            CODEC = StringRepresentable.fromEnum(Skybox::values);
        }
    }

    public static enum CardinalLightType implements StringRepresentable
    {
        DEFAULT("default"),
        NETHER("nether");

        public static final Codec<CardinalLightType> CODEC;
        private final String name;

        private CardinalLightType(String string2) {
            this.name = string2;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        static {
            CODEC = StringRepresentable.fromEnum(CardinalLightType::values);
        }
    }
}

