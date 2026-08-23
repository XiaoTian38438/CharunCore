/*    */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SmallFireball extends Fireball {
/*    */   public SmallFireball(EntityType<? extends SmallFireball> paramEntityType, Level paramLevel) {
/* 21 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level paramLevel, LivingEntity paramLivingEntity, Vec3 paramVec3) {
/* 25 */     super(EntityType.SMALL_FIREBALL, paramLivingEntity, paramVec3, paramLevel);
/*    */   }
/*    */   
/*    */   public SmallFireball(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, Vec3 paramVec3) {
/* 29 */     super(EntityType.SMALL_FIREBALL, paramDouble1, paramDouble2, paramDouble3, paramVec3, paramLevel);
/*    */   }
/*    */   
/*    */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*    */     ServerLevel serverLevel;
/* 34 */     super.onHitEntity(paramEntityHitResult);
/*    */     
/* 36 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 39 */      Entity entity1 = paramEntityHitResult.getEntity();
/* 40 */     Entity entity2 = getOwner();
/* 41 */     int i = entity1.getRemainingFireTicks();
/* 42 */     entity1.igniteForSeconds(5.0F);
/* 43 */     DamageSource damageSource = damageSources().fireball(this, entity2);
/* 44 */     if (!entity1.hurtServer(serverLevel, damageSource, 5.0F)) {
/*    */ 
/*    */       
/* 47 */       entity1.setRemainingFireTicks(i);
/*    */     } else {
/* 49 */       EnchantmentHelper.doPostAttackEffects(serverLevel, entity1, damageSource);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/*    */     ServerLevel serverLevel;
/* 55 */     super.onHitBlock(paramBlockHitResult);
/* 56 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*    */     else
/*    */     { return; }
/* 59 */      Entity entity = getOwner();
/* 60 */     if (!(entity instanceof net.minecraft.world.entity.Mob) || ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 61 */       BlockPos blockPos = paramBlockHitResult.getBlockPos().relative(paramBlockHitResult.getDirection());
/* 62 */       if (level().isEmptyBlock(blockPos)) {
/* 63 */         level().setBlockAndUpdate(blockPos, BaseFireBlock.getState((BlockGetter)level(), blockPos));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 70 */     super.onHit(paramHitResult);
/* 71 */     if (!level().isClientSide())
/* 72 */       discard(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\SmallFireball.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */