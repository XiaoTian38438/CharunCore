/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.IdMap;
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
/*    */ class null
/*    */   extends Strategy<T>
/*    */ {
/*    */   null(IdMap<T> paramIdMap, int paramInt) {
/* 57 */     super(paramIdMap, paramInt);
/*    */   }
/*    */   public Configuration getConfigurationForBitCount(int paramInt) {
/* 60 */     switch (paramInt) { case 0: case 1: case 2: case 3:  }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 66 */       new Configuration.Global(this.globalPaletteBitsInMemory, paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\Strategy$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */