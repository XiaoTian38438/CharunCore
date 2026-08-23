/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SlotAccess
/*    */ {
/*    */   static SlotAccess of(final Supplier<ItemStack> getter, final Consumer<ItemStack> setter) {
/* 16 */     return new SlotAccess()
/*    */       {
/*    */         public ItemStack get() {
/* 19 */           return getter.get();
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean set(ItemStack param1ItemStack) {
/* 24 */           setter.accept(param1ItemStack);
/* 25 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static SlotAccess forEquipmentSlot(final LivingEntity entity, final EquipmentSlot slot, final Predicate<ItemStack> validator) {
/* 31 */     return new SlotAccess()
/*    */       {
/*    */         public ItemStack get() {
/* 34 */           return entity.getItemBySlot(slot);
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean set(ItemStack param1ItemStack) {
/* 39 */           if (!validator.test(param1ItemStack)) {
/* 40 */             return false;
/*    */           }
/*    */           
/* 43 */           entity.setItemSlot(slot, param1ItemStack);
/* 44 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static SlotAccess forEquipmentSlot(LivingEntity paramLivingEntity, EquipmentSlot paramEquipmentSlot) {
/* 50 */     return forEquipmentSlot(paramLivingEntity, paramEquipmentSlot, paramItemStack -> true);
/*    */   }
/*    */   
/*    */   static SlotAccess forListElement(final List<ItemStack> stacks, final int index) {
/* 54 */     return new SlotAccess()
/*    */       {
/*    */         public ItemStack get() {
/* 57 */           return stacks.get(index);
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean set(ItemStack param1ItemStack) {
/* 62 */           stacks.set(index, param1ItemStack);
/* 63 */           return true;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   ItemStack get();
/*    */   
/*    */   boolean set(ItemStack paramItemStack);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SlotAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */