/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Monster;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class Stray
/*    */   extends AbstractSkeleton
/*    */ {
/*    */   public Stray(EntityType<? extends Stray> paramEntityType, Level paramLevel) {
/* 24 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public static boolean checkStraySpawnRules(EntityType<Stray> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 28 */     BlockPos blockPos = paramBlockPos;
/*    */     while (true) {
/* 30 */       blockPos = blockPos.above();
/* 31 */       if (!paramServerLevelAccessor.getBlockState(blockPos).is(Blocks.POWDER_SNOW))
/* 32 */         return (Monster.checkMonsterSpawnRules(paramEntityType, paramServerLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource) && (
/* 33 */           EntitySpawnReason.isSpawner(paramEntitySpawnReason) || paramServerLevelAccessor.canSeeSky(blockPos.below()))); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 38 */     return SoundEvents.STRAY_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 43 */     return SoundEvents.STRAY_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 48 */     return SoundEvents.STRAY_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   SoundEvent getStepSound() {
/* 53 */     return SoundEvents.STRAY_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack paramItemStack1, float paramFloat, ItemStack paramItemStack2) {
/* 58 */     AbstractArrow abstractArrow = super.getArrow(paramItemStack1, paramFloat, paramItemStack2);
/* 59 */     if (abstractArrow instanceof Arrow) {
/* 60 */       ((Arrow)abstractArrow).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600));
/*    */     }
/* 62 */     return abstractArrow;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\skeleton\Stray.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */