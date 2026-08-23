/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.world.item.slot.SlotCollection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SlotProvider
/*    */ {
/*    */   SlotAccess getSlot(int paramInt);
/*    */   
/*    */   default SlotCollection getSlotsFromRange(IntList paramIntList) {
/* 17 */     List list = paramIntList.intStream().mapToObj(this::getSlot).filter(Objects::nonNull).toList();
/* 18 */     return SlotCollection.of(list);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\SlotProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */