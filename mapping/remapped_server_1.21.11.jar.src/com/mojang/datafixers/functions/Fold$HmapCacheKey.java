/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.families.Algebra;
/*    */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class HmapCacheKey
/*    */   extends Record
/*    */ {
/*    */   private final RecursiveTypeFamily family;
/*    */   private final RecursiveTypeFamily newFamily;
/*    */   private final Algebra algebra;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */   }
/*    */   
/*    */   private HmapCacheKey(RecursiveTypeFamily paramRecursiveTypeFamily1, RecursiveTypeFamily paramRecursiveTypeFamily2, Algebra paramAlgebra) {
/* 29 */     this.family = paramRecursiveTypeFamily1; this.newFamily = paramRecursiveTypeFamily2; this.algebra = paramAlgebra; } public RecursiveTypeFamily family() { return this.family; } public RecursiveTypeFamily newFamily() { return this.newFamily; } public Algebra algebra() { return this.algebra; }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Fold$HmapCacheKey.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */