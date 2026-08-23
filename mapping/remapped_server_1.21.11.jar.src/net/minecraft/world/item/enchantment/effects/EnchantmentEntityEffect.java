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
/*    */ public interface EnchantmentEntityEffect
/*    */   extends EnchantmentLocationBasedEffect
/*    */ {
/*    */   static MapCodec<? extends EnchantmentEntityEffect> bootstrap(Registry<MapCodec<? extends EnchantmentEntityEffect>> paramRegistry) {
/* 17 */     Registry.register(paramRegistry, "all_of", AllOf.EntityEffects.CODEC);
/* 18 */     Registry.register(paramRegistry, "apply_mob_effect", ApplyMobEffect.CODEC);
/* 19 */     Registry.register(paramRegistry, "change_item_damage", ChangeItemDamage.CODEC);
/* 20 */     Registry.register(paramRegistry, "damage_entity", DamageEntity.CODEC);
/* 21 */     Registry.register(paramRegistry, "explode", ExplodeEffect.CODEC);
/* 22 */     Registry.register(paramRegistry, "ignite", Ignite.CODEC);
/* 23 */     Registry.register(paramRegistry, "apply_impulse", ApplyEntityImpulse.CODEC);
/* 24 */     Registry.register(paramRegistry, "apply_exhaustion", ApplyExhaustion.CODEC);
/* 25 */     Registry.register(paramRegistry, "play_sound", PlaySoundEffect.CODEC);
/* 26 */     Registry.register(paramRegistry, "replace_block", ReplaceBlock.CODEC);
/* 27 */     Registry.register(paramRegistry, "replace_disk", ReplaceDisk.CODEC);
/* 28 */     Registry.register(paramRegistry, "run_function", RunFunction.CODEC);
/* 29 */     Registry.register(paramRegistry, "set_block_properties", SetBlockProperties.CODEC);
/* 30 */     Registry.register(paramRegistry, "spawn_particles", SpawnParticlesEffect.CODEC);
/* 31 */     return (MapCodec<? extends EnchantmentEntityEffect>)Registry.register(paramRegistry, "summon_entity", SummonEntityEffect.CODEC);
/*    */   }
/*    */   
/* 34 */   public static final Codec<EnchantmentEntityEffect> CODEC = BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentEntityEffect::codec, Function.identity());
/*    */ 
/*    */   
/*    */   void apply(ServerLevel paramServerLevel, int paramInt, EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3);
/*    */   
/*    */   default void onChangedBlock(ServerLevel paramServerLevel, int paramInt, EnchantedItemInUse paramEnchantedItemInUse, Entity paramEntity, Vec3 paramVec3, boolean paramBoolean) {
/* 40 */     apply(paramServerLevel, paramInt, paramEnchantedItemInUse, paramEntity, paramVec3);
/*    */   }
/*    */   
/*    */   MapCodec<? extends EnchantmentEntityEffect> codec();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\enchantment\effects\EnchantmentEntityEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */