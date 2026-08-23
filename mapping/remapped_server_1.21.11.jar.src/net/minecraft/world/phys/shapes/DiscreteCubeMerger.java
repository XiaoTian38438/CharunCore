/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import com.google.common.math.IntMath;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public final class DiscreteCubeMerger implements IndexMerger {
/*    */   private final CubePointRange result;
/*    */   private final int firstDiv;
/*    */   private final int secondDiv;
/*    */   
/*    */   DiscreteCubeMerger(int paramInt1, int paramInt2) {
/* 12 */     this.result = new CubePointRange((int)Shapes.lcm(paramInt1, paramInt2));
/*    */     
/* 14 */     int i = IntMath.gcd(paramInt1, paramInt2);
/* 15 */     this.firstDiv = paramInt1 / i;
/* 16 */     this.secondDiv = paramInt2 / i;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer paramIndexConsumer) {
/* 21 */     int i = this.result.size() - 1;
/* 22 */     for (byte b = 0; b < i; b++) {
/* 23 */       if (!paramIndexConsumer.merge(b / this.secondDiv, b / this.firstDiv, b)) {
/* 24 */         return false;
/*    */       }
/*    */     } 
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 32 */     return this.result.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 37 */     return (DoubleList)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\DiscreteCubeMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */