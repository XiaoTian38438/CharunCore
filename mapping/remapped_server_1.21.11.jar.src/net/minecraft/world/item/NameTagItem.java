/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class NameTagItem extends Item {
/*    */   public NameTagItem(Item.Properties paramProperties) {
/* 13 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack paramItemStack, Player paramPlayer, LivingEntity paramLivingEntity, InteractionHand paramInteractionHand) {
/* 18 */     Component component = (Component)paramItemStack.get(DataComponents.CUSTOM_NAME);
/* 19 */     if (component != null && paramLivingEntity.getType().canSerialize()) {
/* 20 */       if (!paramPlayer.level().isClientSide() && paramLivingEntity.isAlive()) {
/* 21 */         paramLivingEntity.setCustomName(component);
/* 22 */         if (paramLivingEntity instanceof Mob) { Mob mob = (Mob)paramLivingEntity;
/* 23 */           mob.setPersistenceRequired(); }
/*    */ 
/*    */         
/* 26 */         paramItemStack.shrink(1);
/*    */       } 
/*    */       
/* 29 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 31 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\NameTagItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */