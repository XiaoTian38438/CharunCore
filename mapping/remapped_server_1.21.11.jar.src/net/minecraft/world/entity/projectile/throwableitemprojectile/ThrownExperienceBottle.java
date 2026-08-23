/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ExperienceOrb;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ThrownExperienceBottle
/*    */   extends ThrowableItemProjectile
/*    */ {
/*    */   public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> paramEntityType, Level paramLevel) {
/* 19 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 23 */     super(EntityType.EXPERIENCE_BOTTLE, paramLivingEntity, paramLevel, paramItemStack);
/*    */   }
/*    */   
/*    */   public ThrownExperienceBottle(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 27 */     super(EntityType.EXPERIENCE_BOTTLE, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 32 */     return Items.EXPERIENCE_BOTTLE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getDefaultGravity() {
/* 37 */     return 0.07D;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult paramHitResult) {
/* 42 */     super.onHit(paramHitResult);
/*    */     
/* 44 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 45 */       serverLevel.levelEvent(2002, blockPosition(), -13083194);
/*    */       
/* 47 */       int i = 3 + serverLevel.random.nextInt(5) + serverLevel.random.nextInt(5);
/* 48 */       if (paramHitResult instanceof BlockHitResult) { BlockHitResult blockHitResult = (BlockHitResult)paramHitResult;
/* 49 */         Vec3 vec3 = blockHitResult.getDirection().getUnitVec3();
/* 50 */         ExperienceOrb.awardWithDirection(serverLevel, paramHitResult.getLocation(), vec3, i); }
/*    */       else
/* 52 */       { ExperienceOrb.awardWithDirection(serverLevel, paramHitResult.getLocation(), getDeltaMovement().scale(-1.0D), i); }
/*    */       
/* 54 */       discard(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownExperienceBottle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */