/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.Func;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Apply<A, B>
/*    */   extends PointFree<B>
/*    */ {
/*    */   protected final PointFree<Function<A, B>> func;
/*    */   protected final PointFree<A> arg;
/*    */   protected final Type<B> type;
/*    */   
/*    */   public Apply(PointFree<Function<A, B>> paramPointFree, PointFree<A> paramPointFree1) {
/* 19 */     this(paramPointFree, paramPointFree1, ((Func)paramPointFree.type()).second());
/*    */   }
/*    */   
/*    */   Apply(PointFree<Function<A, B>> paramPointFree, PointFree<A> paramPointFree1, Type<B> paramType) {
/* 23 */     this.func = paramPointFree;
/* 24 */     this.arg = paramPointFree1;
/* 25 */     this.type = paramType;
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, B> eval() {
/* 30 */     return paramDynamicOps -> ((Function)this.func.evalCached().apply(paramDynamicOps)).apply(this.arg.evalCached().apply(paramDynamicOps));
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<B> type() {
/* 35 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 40 */     return "(ap " + this.func.toString(paramInt + 1) + "\n" + indent(paramInt + 1) + this.arg.toString(paramInt + 1) + "\n" + indent(paramInt) + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<B>> all(PointFreeRule paramPointFreeRule) {
/* 45 */     PointFree<Function<A, B>> pointFree = paramPointFreeRule.rewriteOrNop(this.func);
/* 46 */     PointFree<A> pointFree1 = paramPointFreeRule.rewriteOrNop(this.arg);
/* 47 */     if (pointFree == this.func && pointFree1 == this.arg) {
/* 48 */       return Optional.of(this);
/*    */     }
/* 50 */     return Optional.of(new Apply(pointFree, pointFree1, this.type));
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<? extends PointFree<B>> one(PointFreeRule paramPointFreeRule) {
/* 55 */     return paramPointFreeRule.<Function<A, B>>rewrite(this.func).map(paramPointFree -> new Apply(paramPointFree, this.arg, this.type))
/* 56 */       .or(() -> paramPointFreeRule.<A>rewrite(this.arg).map(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 61 */     if (this == paramObject) {
/* 62 */       return true;
/*    */     }
/* 64 */     if (!(paramObject instanceof Apply)) {
/* 65 */       return false;
/*    */     }
/* 67 */     Apply apply = (Apply)paramObject;
/* 68 */     return (Objects.equals(this.func, apply.func) && Objects.equals(this.arg, apply.arg));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 73 */     int i = this.func.hashCode();
/* 74 */     i = 31 * i + this.arg.hashCode();
/* 75 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Apply.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */