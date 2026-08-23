/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public interface SlotRange
/*    */   extends StringRepresentable
/*    */ {
/*    */   IntList slots();
/*    */   
/*    */   default int size() {
/* 12 */     return slots().size();
/*    */   }
/*    */   
/*    */   static SlotRange of(final String name, final IntList slots) {
/* 16 */     return new SlotRange()
/*    */       {
/*    */         public IntList slots() {
/* 19 */           return slots;
/*    */         }
/*    */ 
/*    */         
/*    */         public String getSerializedName() {
/* 24 */           return name;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 29 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\inventory\SlotRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */