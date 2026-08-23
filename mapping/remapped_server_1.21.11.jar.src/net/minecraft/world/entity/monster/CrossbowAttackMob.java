/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public interface CrossbowAttackMob extends RangedAttackMob {
/*    */   void setChargingCrossbow(boolean paramBoolean);
/*    */   
/*    */   LivingEntity getTarget();
/*    */   
/*    */   void onCrossbowAttackPerformed();
/*    */   
/*    */   default void performCrossbowAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 19 */     InteractionHand interactionHand = ProjectileUtil.getWeaponHoldingHand(paramLivingEntity, Items.CROSSBOW);
/* 20 */     ItemStack itemStack = paramLivingEntity.getItemInHand(interactionHand);
/* 21 */     Item item = itemStack.getItem(); if (item instanceof CrossbowItem) { CrossbowItem crossbowItem = (CrossbowItem)item;
/* 22 */       crossbowItem.performShooting(paramLivingEntity.level(), paramLivingEntity, interactionHand, itemStack, paramFloat, (14 - paramLivingEntity.level().getDifficulty().getId() * 4), getTarget()); }
/*    */     
/* 24 */     onCrossbowAttackPerformed();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\CrossbowAttackMob.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */