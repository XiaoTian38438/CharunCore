/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LargeFireball
/*    */   extends Fireball {
/*    */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/* 20 */   private int explosionPower = 1;
/*    */   
/*    */   public LargeFireball(EntityType<? extends LargeFireball> paramEntityType, Level paramLevel) {
/* 23 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public LargeFireball(Level paramLevel, LivingEntity paramLivingEntity, Vec3 paramVec3, int paramInt) {
/* 27 */     super(EntityType.FIREBALL, paramLivingEntity, paramVec3, paramLevel);
/* 28 */     this.explosionPower = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 33 */     super.onHit(paramHitResult);
/* 34 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 35 */       boolean bool = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue();
/* 36 */       level().explode((Entity)this, getX(), getY(), getZ(), this.explosionPower, bool, Level.ExplosionInteraction.MOB);
/* 37 */       discard(); }
/*    */   
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*    */     ServerLevel serverLevel;
/* 43 */     super.onHitEntity(paramEntityHitResult);
/*    */     
/* 45 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 48 */      Entity entity1 = paramEntityHitResult.getEntity();
/* 49 */     Entity entity2 = getOwner();
/* 50 */     DamageSource damageSource = damageSources().fireball(this, entity2);
/* 51 */     entity1.hurtServer(serverLevel, damageSource, 6.0F);
/* 52 */     EnchantmentHelper.doPostAttackEffects(serverLevel, entity1, damageSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 57 */     super.addAdditionalSaveData(paramValueOutput);
/* 58 */     paramValueOutput.putByte("ExplosionPower", (byte)this.explosionPower);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 63 */     super.readAdditionalSaveData(paramValueInput);
/* 64 */     this.explosionPower = paramValueInput.getByteOr("ExplosionPower", (byte)1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\LargeFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */