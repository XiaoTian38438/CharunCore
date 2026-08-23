/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public interface InventoryCarrier
/*    */ {
/*    */   public static final String TAG_INVENTORY = "Inventory";
/*    */   
/*    */   static void pickUpItem(ServerLevel paramServerLevel, Mob paramMob, InventoryCarrier paramInventoryCarrier, ItemEntity paramItemEntity) {
/* 17 */     ItemStack itemStack = paramItemEntity.getItem();
/* 18 */     if (paramMob.wantsToPickUp(paramServerLevel, itemStack)) {
/* 19 */       SimpleContainer simpleContainer = paramInventoryCarrier.getInventory();
/* 20 */       boolean bool = simpleContainer.canAddItem(itemStack);
/* 21 */       if (!bool) {
/*    */         return;
/*    */       }
/*    */       
/* 25 */       paramMob.onItemPickup(paramItemEntity);
/* 26 */       int i = itemStack.getCount();
/* 27 */       ItemStack itemStack1 = simpleContainer.addItem(itemStack);
/* 28 */       paramMob.take((Entity)paramItemEntity, i - itemStack1.getCount());
/* 29 */       if (itemStack1.isEmpty()) {
/* 30 */         paramItemEntity.discard();
/*    */       } else {
/* 32 */         itemStack.setCount(itemStack1.getCount());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   default void readInventoryFromTag(ValueInput paramValueInput) {
/* 38 */     paramValueInput.list("Inventory", ItemStack.CODEC).ifPresent(paramTypedInputList -> getInventory().fromItemList(paramTypedInputList));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default void writeInventoryToTag(ValueOutput paramValueOutput) {
/* 44 */     getInventory().storeAsItemList(paramValueOutput.list("Inventory", ItemStack.CODEC));
/*    */   }
/*    */   
/*    */   SimpleContainer getInventory();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\npc\InventoryCarrier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */