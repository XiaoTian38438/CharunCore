/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.advancements.criterion;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.advancements.criterion.EntitySubPredicates;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record FishingHookPredicate(Optional<Boolean> inOpenWater) implements EntitySubPredicate
{
    public static final FishingHookPredicate ANY = new FishingHookPredicate(Optional.empty());
    public static final MapCodec<FishingHookPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.optionalFieldOf("in_open_water").forGetter(FishingHookPredicate::inOpenWater)).apply((Applicative<FishingHookPredicate, ?>)instance, FishingHookPredicate::new));

    public static FishingHookPredicate inOpenWater(boolean bl) {
        return new FishingHookPredicate(Optional.of(bl));
    }

    public MapCodec<FishingHookPredicate> codec() {
        return EntitySubPredicates.FISHING_HOOK;
    }

    @Override
    public boolean matches(Entity entity, ServerLevel serverLevel, @Nullable Vec3 vec3) {
        if (this.inOpenWater.isEmpty()) {
            return true;
        }
        if (entity instanceof FishingHook) {
            FishingHook fishingHook = (FishingHook)entity;
            return this.inOpenWater.get().booleanValue() == fishingHook.isOpenWaterFishing();
        }
        return false;
    }
}

