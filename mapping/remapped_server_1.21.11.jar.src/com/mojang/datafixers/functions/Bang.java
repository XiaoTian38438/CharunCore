/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Bang<A>
/*    */   extends PointFree<Function<A, Unit>>
/*    */ {
/*    */   private final Type<A> type;
/*    */   
/*    */   Bang(Type<A> paramType) {
/* 16 */     this.type = paramType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<Function<A, Unit>> type() {
/* 21 */     return DSL.func(this.type, DSL.emptyPartType());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 26 */     return "!";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 31 */     if (paramObject instanceof Bang) { Bang bang = (Bang)paramObject; if (this.type.equals(bang.type)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 36 */     return this.type.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, Unit>> eval() {
/* 41 */     return paramDynamicOps -> ();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Bang.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */