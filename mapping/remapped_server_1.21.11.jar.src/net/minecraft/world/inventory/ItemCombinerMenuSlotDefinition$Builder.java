/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 47 */   private final List<ItemCombinerMenuSlotDefinition.SlotDefinition> inputSlots = new ArrayList<>();
/* 48 */   private ItemCombinerMenuSlotDefinition.SlotDefinition resultSlot = ItemCombinerMenuSlotDefinition.SlotDefinition.EMPTY;
/*    */   
/*    */   public Builder withSlot(int paramInt1, int paramInt2, int paramInt3, Predicate<ItemStack> paramPredicate) {
/* 51 */     this.inputSlots.add(new ItemCombinerMenuSlotDefinition.SlotDefinition(paramInt1, paramInt2, paramInt3, paramPredicate));
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withResultSlot(int paramInt1, int paramInt2, int paramInt3) {
/* 56 */     this.resultSlot = new ItemCombinerMenuSlotDefinition.SlotDefinition(paramInt1, paramInt2, paramInt3, paramItemStack -> false);
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemCombinerMenuSlotDefinition build() {
/* 63 */     int i = this.inputSlots.size();
/* 64 */     for (byte b = 0; b < i; b++) {
/* 65 */       ItemCombinerMenuSlotDefinition.SlotDefinition slotDefinition = this.inputSlots.get(b);
/* 66 */       if (slotDefinition.slotIndex != b) {
/* 67 */         throw new IllegalArgumentException("Expected input slots to have continous indexes");
/*    */       }
/*    */     } 
/* 70 */     if (this.resultSlot.slotIndex != i) {
/* 71 */       throw new IllegalArgumentException("Expected result slot index to follow last input slot");
/*    */     }
/*    */     
/* 74 */     return new ItemCombinerMenuSlotDefinition(this.inputSlots, this.resultSlot);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\ItemCombinerMenuSlotDefinition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */