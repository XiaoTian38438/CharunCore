/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class OffsetDoubleList extends AbstractDoubleList {
/*    */   private final DoubleList delegate;
/*    */   private final double offset;
/*    */   
/*    */   public OffsetDoubleList(DoubleList paramDoubleList, double paramDouble) {
/* 11 */     this.delegate = paramDoubleList;
/* 12 */     this.offset = paramDouble;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int paramInt) {
/* 17 */     return this.delegate.getDouble(paramInt) + this.offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 22 */     return this.delegate.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\OffsetDoubleList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */