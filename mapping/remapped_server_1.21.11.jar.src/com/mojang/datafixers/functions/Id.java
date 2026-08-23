/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Id<A>
/*    */   extends PointFree<Function<A, A>>
/*    */ {
/*    */   private final Type<Function<A, A>> type;
/*    */   
/*    */   Id(Type<Function<A, A>> paramType) {
/* 14 */     this.type = paramType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<Function<A, A>> type() {
/* 19 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 24 */     if (paramObject instanceof Id) { Id id = (Id)paramObject; if (this.type.equals(id.type)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 29 */     return this.type.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 34 */     return "id";
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, A>> eval() {
/* 39 */     return paramDynamicOps -> Function.identity();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Id.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */