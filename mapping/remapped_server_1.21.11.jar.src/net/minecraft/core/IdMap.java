/*    */ package net.minecraft.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IdMap<T>
/*    */   extends Iterable<T>
/*    */ {
/*    */   public static final int DEFAULT = -1;
/*    */   
/*    */   int getId(T paramT);
/*    */   
/*    */   T byId(int paramInt);
/*    */   
/*    */   default T byIdOrThrow(int paramInt) {
/* 16 */     T t = byId(paramInt);
/* 17 */     if (t == null) {
/* 18 */       throw new IllegalArgumentException("No value with id " + paramInt);
/*    */     }
/* 20 */     return t;
/*    */   }
/*    */   
/*    */   default int getIdOrThrow(T paramT) {
/* 24 */     int i = getId(paramT);
/* 25 */     if (i == -1) {
/* 26 */       throw new IllegalArgumentException("Can't find id for '" + String.valueOf(paramT) + "' in map " + String.valueOf(this));
/*    */     }
/* 28 */     return i;
/*    */   }
/*    */   
/*    */   int size();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\IdMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */