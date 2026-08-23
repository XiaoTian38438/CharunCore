/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
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
/*    */ public class FoundRectangle
/*    */ {
/*    */   public final BlockPos minCorner;
/*    */   public final int axis1Size;
/*    */   public final int axis2Size;
/*    */   
/*    */   public FoundRectangle(BlockPos paramBlockPos, int paramInt1, int paramInt2) {
/* 41 */     this.minCorner = paramBlockPos;
/* 42 */     this.axis1Size = paramInt1;
/* 43 */     this.axis2Size = paramInt2;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\BlockUtil$FoundRectangle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */