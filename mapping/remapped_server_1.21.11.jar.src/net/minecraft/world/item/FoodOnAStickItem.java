/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.ItemSteerable;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class FoodOnAStickItem<T extends Entity & ItemSteerable> extends Item {
/*    */   private final EntityType<T> canInteractWith;
/*    */   
/*    */   public FoodOnAStickItem(EntityType<T> paramEntityType, int paramInt, Item.Properties paramProperties) {
/* 18 */     super(paramProperties);
/*    */     
/* 20 */     this.canInteractWith = paramEntityType;
/* 21 */     this.consumeItemDamage = paramInt;
/*    */   }
/*    */   private final int consumeItemDamage;
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 26 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 27 */     if (paramLevel.isClientSide()) {
/* 28 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */     
/* 31 */     Entity entity = paramPlayer.getControlledVehicle();
/*    */     
/* 33 */     if (paramPlayer.isPassenger() && entity instanceof ItemSteerable) { ItemSteerable itemSteerable = (ItemSteerable)entity; if (entity.getType() == this.canInteractWith && 
/* 34 */         itemSteerable.boost()) {
/* 35 */         EquipmentSlot equipmentSlot = paramInteractionHand.asEquipmentSlot();
/* 36 */         ItemStack itemStack1 = itemStack.hurtAndConvertOnBreak(this.consumeItemDamage, Items.FISHING_ROD, (LivingEntity)paramPlayer, equipmentSlot);
/* 37 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(itemStack1);
/*    */       }  }
/*    */ 
/*    */     
/* 41 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/*    */     
/* 43 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\FoodOnAStickItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */