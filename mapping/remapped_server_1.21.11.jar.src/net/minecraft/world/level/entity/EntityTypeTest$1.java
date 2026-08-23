/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements EntityTypeTest<B, T>
/*    */ {
/*    */   public T tryCast(B paramB) {
/* 11 */     return cls.isInstance(paramB) ? (T)paramB : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends B> getBaseClass() {
/* 16 */     return cls;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityTypeTest$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */