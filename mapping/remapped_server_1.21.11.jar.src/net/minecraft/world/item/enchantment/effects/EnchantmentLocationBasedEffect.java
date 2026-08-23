/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public interface EnchantmentLocationBasedEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentLocationBasedEffect> bootstrap(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> paramRegistry) {
/* 17 */     Registry.register(paramRegistry, "all_of", AllOf.LocationBasedEffects.CODEC);
/* 18 */     Registry.register(paramRegistry, "apply_mob_effect", ApplyMobEffect.CODEC);
/* 19 */     Registry.register(paramRegistry, "attribute", EnchantmentAttributeEffect.CODEC);
/* 20 */     Registry.register(paramRegistry, "change_item_damage", ChangeItemDamage.CODEC);
/* 21 */     Registry.register(paramRegistry, "damage_entity", DamageEntity.CODEC);
/* 22 */     Registry.register(paramRegistry, "explode", ExplodeEffect.CODEC);
/* 23 */     Registry.register(paramRegistry, "ignite", Ignite.CODEC);
/* 24 */     Registry.register(paramRegistry, "apply_impulse", ApplyEntityImpulse.CODEC);
/* 25 */     Registry.register(paramRegistry, "apply_exhaustion", ApplyExhaustion.CODEC);
/* 26 */     Registry.register(paramRegistry, "play_sound", PlaySoundEffect.CODEC);
/* 27 */     Registry.register(paramRegistry, "replace_block", ReplaceBlock.CODEC);
/* 28 */     Registry.register(paramRegistry, "replace_disk", ReplaceDisk.CODEC);
/* 29 */     Registry.register(paramRegistry, "run_function", RunFunction.CODEC);
/* 30 */     Registry.register(paramRegistry, "set_block_properties", SetBlockProperties.CODEC);
/* 31 */     Registry.register(paramRegistry, "spawn_particles", SpawnParticlesEffect.CODEC);
/* 32 */     return (MapCodec<? extends EnchantmentLocationBasedEffect>)Registry.register(paramRegistry, "summon_entity", SummonEntityEffect.CODEC);
/*    */   }
/*    */   
/* 35 */   public static final Codec<EnchantmentLocationBasedEffect> CODEC = BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentLocationBasedEffect::codec, Function.identity());
/*    */   
/*    */   void onChangedBlock(ServerLevel paramServerLevel, int paramInt, EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3, boolean paramBoolean);
/*    */   
/*    */   default void onDeactivated(EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3, int paramInt) {}
/*    */   
/*    */   MapCodec<? extends EnchantmentLocationBasedEffect> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentLocationBasedEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */