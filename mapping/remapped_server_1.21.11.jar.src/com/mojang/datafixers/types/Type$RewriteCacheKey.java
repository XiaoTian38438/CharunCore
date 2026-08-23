/*    */ package com.mojang.datafixers.types;
/*    */ 
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.functions.PointFreeRule;
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
/*    */ final class RewriteCacheKey
/*    */   extends Record
/*    */ {
/*    */   private final Type<?> type;
/*    */   private final TypeRewriteRule rule;
/*    */   private final PointFreeRule optimizationRule;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */   }
/*    */   
/*    */   public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/types/Type$RewriteCacheKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */   }
/*    */   
/*    */   private RewriteCacheKey(Type<?> paramType, TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule) {
/* 39 */     this.type = paramType; this.rule = paramTypeRewriteRule; this.optimizationRule = paramPointFreeRule; } public Type<?> type() { return this.type; } public TypeRewriteRule rule() { return this.rule; } public PointFreeRule optimizationRule() { return this.optimizationRule; }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\Type$RewriteCacheKey.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */