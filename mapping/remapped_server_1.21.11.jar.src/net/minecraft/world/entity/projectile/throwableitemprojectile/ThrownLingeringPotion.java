/*    */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownLingeringPotion extends AbstractThrownPotion {
/*    */   public ThrownLingeringPotion(EntityType<? extends ThrownLingeringPotion> paramEntityType, Level paramLevel) {
/* 16 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/* 20 */     super(EntityType.LINGERING_POTION, paramLevel, paramLivingEntity, paramItemStack);
/*    */   }
/*    */   
/*    */   public ThrownLingeringPotion(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 24 */     super(EntityType.LINGERING_POTION, paramLevel, paramDouble1, paramDouble2, paramDouble3, paramItemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 29 */     return Items.LINGERING_POTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onHitAsPotion(ServerLevel paramServerLevel, ItemStack paramItemStack, HitResult paramHitResult) {
/* 34 */     AreaEffectCloud areaEffectCloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
/* 35 */     Entity entity = getOwner(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 36 */       areaEffectCloud.setOwner(livingEntity); }
/*    */     
/* 38 */     areaEffectCloud.setRadius(3.0F);
/* 39 */     areaEffectCloud.setRadiusOnUse(-0.5F);
/* 40 */     areaEffectCloud.setDuration(600);
/* 41 */     areaEffectCloud.setWaitTime(10);
/* 42 */     areaEffectCloud.setRadiusPerTick(-areaEffectCloud.getRadius() / areaEffectCloud.getDuration());
/* 43 */     areaEffectCloud.applyComponentsFromItemStack(paramItemStack);
/*    */     
/* 45 */     paramServerLevel.addFreshEntity((Entity)areaEffectCloud);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownLingeringPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */