/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ 
/*    */ public interface EntityTypeTest<B, T extends B>
/*    */ {
/*    */   static <B, T extends B> EntityTypeTest<B, T> forClass(final Class<T> cls) {
/*  7 */     return new EntityTypeTest<B, T>()
/*    */       {
/*    */         public T tryCast(B param1B)
/*    */         {
/* 11 */           return cls.isInstance(param1B) ? (T)param1B : null;
/*    */         }
/*    */ 
/*    */         
/*    */         public Class<? extends B> getBaseClass() {
/* 16 */           return cls;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <B, T extends B> EntityTypeTest<B, T> forExactClass(final Class<T> cls) {
/* 22 */     return new EntityTypeTest<B, T>()
/*    */       {
/*    */         public T tryCast(B param1B)
/*    */         {
/* 26 */           return cls.equals(param1B.getClass()) ? (T)param1B : null;
/*    */         }
/*    */ 
/*    */         
/*    */         public Class<? extends B> getBaseClass() {
/* 31 */           return cls;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   T tryCast(B paramB);
/*    */   
/*    */   Class<? extends B> getBaseClass();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\entity\EntityTypeTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */