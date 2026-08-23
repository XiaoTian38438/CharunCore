/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class IdenticalMerger implements IndexMerger {
/*    */   private final DoubleList coords;
/*    */   
/*    */   public IdenticalMerger(DoubleList paramDoubleList) {
/*  9 */     this.coords = paramDoubleList;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer paramIndexConsumer) {
/* 14 */     int i = this.coords.size() - 1;
/* 15 */     for (byte b = 0; b < i; b++) {
/* 16 */       if (!paramIndexConsumer.merge(b, b, b)) {
/* 17 */         return false;
/*    */       }
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 25 */     return this.coords.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 30 */     return this.coords;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\IdenticalMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */