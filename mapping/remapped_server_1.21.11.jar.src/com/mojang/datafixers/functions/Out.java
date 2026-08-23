/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Out<A>
/*    */   extends PointFree<Function<A, A>>
/*    */ {
/*    */   private final RecursivePoint.RecursivePointType<A> type;
/*    */   
/*    */   public Out(RecursivePoint.RecursivePointType<A> paramRecursivePointType) {
/* 17 */     this.type = paramRecursivePointType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<Function<A, A>> type() {
/* 22 */     return DSL.func((Type)this.type, this.type.unfold());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 27 */     return "Out[" + String.valueOf(this.type) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 32 */     if (this == paramObject) {
/* 33 */       return true;
/*    */     }
/* 35 */     return (paramObject instanceof Out && Objects.equals(this.type, ((Out)paramObject).type));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 40 */     return this.type.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, A>> eval() {
/* 45 */     return paramDynamicOps -> Function.identity();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Out.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */