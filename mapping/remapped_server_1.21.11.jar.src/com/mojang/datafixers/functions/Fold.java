/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.Algebra;
/*     */ import com.mojang.datafixers.types.families.ListAlgebra;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ 
/*     */ final class Fold<A, B>
/*     */   extends PointFree<Function<A, B>>
/*     */ {
/*  26 */   private static final Map<HmapCacheKey, IntFunction<RewriteResult<?, ?>>> HMAP_CACHE = Maps.newConcurrentMap();
/*  27 */   private static final Map<Pair<IntFunction<RewriteResult<?, ?>>, Integer>, RewriteResult<?, ?>> HMAP_APPLY_CACHE = Maps.newConcurrentMap(); protected final RecursivePoint.RecursivePointType<A> aType; protected final RecursivePoint.RecursivePointType<B> bType; protected final Algebra algebra; protected final int index;
/*     */   private static final class HmapCacheKey extends Record { private final RecursiveTypeFamily family; private final RecursiveTypeFamily newFamily; private final Algebra algebra;
/*  29 */     private HmapCacheKey(RecursiveTypeFamily param1RecursiveTypeFamily1, RecursiveTypeFamily param1RecursiveTypeFamily2, Algebra param1Algebra) { this.family = param1RecursiveTypeFamily1; this.newFamily = param1RecursiveTypeFamily2; this.algebra = param1Algebra; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*  29 */       //   #29	-> 0 } public RecursiveTypeFamily family() { return this.family; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/functions/Fold$HmapCacheKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*  29 */       //   #29	-> 0 } public RecursiveTypeFamily newFamily() { return this.newFamily; } public Algebra algebra() { return this.algebra; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fold(RecursivePoint.RecursivePointType<A> paramRecursivePointType, RecursivePoint.RecursivePointType<B> paramRecursivePointType1, Algebra paramAlgebra, int paramInt) {
/*  38 */     this.aType = paramRecursivePointType;
/*  39 */     this.bType = paramRecursivePointType1;
/*  40 */     this.algebra = paramAlgebra;
/*  41 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<Function<A, B>> type() {
/*  46 */     return DSL.func((Type)this.aType, (Type)this.bType);
/*     */   }
/*     */ 
/*     */   
/*     */   Optional<? extends PointFree<Function<A, B>>> all(PointFreeRule paramPointFreeRule) {
/*  51 */     int i = this.aType.family().size();
/*  52 */     ArrayList<RewriteResult<?, ?>> arrayList = new ArrayList(i);
/*  53 */     boolean bool = false;
/*  54 */     for (byte b = 0; b < i; b++) {
/*  55 */       RewriteResult<?, ?> rewriteResult = this.algebra.apply(b);
/*  56 */       PointFree<?> pointFree1 = rewriteResult.view().function();
/*  57 */       PointFree<?> pointFree2 = paramPointFreeRule.rewriteOrNop(pointFree1);
/*  58 */       if (pointFree2 != pointFree1) {
/*  59 */         arrayList.add(cap(rewriteResult, (PointFree)pointFree2));
/*  60 */         bool = true;
/*     */       } else {
/*  62 */         arrayList.add(rewriteResult);
/*     */       } 
/*     */     } 
/*  65 */     if (bool) {
/*  66 */       return Optional.of(new Fold(this.aType, this.bType, (Algebra)new ListAlgebra("Rewrite all", arrayList), this.index));
/*     */     }
/*  68 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <A, B> RewriteResult<A, B> cap(RewriteResult<A, B> paramRewriteResult, PointFree<? extends Function<?, ?>> paramPointFree) {
/*  73 */     return RewriteResult.create(new View(paramPointFree), paramRewriteResult.recData());
/*     */   }
/*     */   
/*     */   private <FB> PointFree<Function<A, B>> cap(RewriteResult<?, FB> paramRewriteResult) {
/*  77 */     RewriteResult rewriteResult = this.algebra.apply(this.index);
/*  78 */     return Functions.comp(rewriteResult.view().function(), paramRewriteResult.view().function());
/*     */   }
/*     */ 
/*     */   
/*     */   public Function<DynamicOps<?>, Function<A, B>> eval() {
/*  83 */     return paramDynamicOps -> ();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int paramInt) {
/*  97 */     return "fold(" + String.valueOf(this.aType) + ", " + this.index + ", \n" + indent(paramInt + 1) + this.algebra.toString(paramInt + 1) + "\n" + indent(paramInt) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 102 */     if (this == paramObject) {
/* 103 */       return true;
/*     */     }
/* 105 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 106 */       return false;
/*     */     }
/* 108 */     Fold fold = (Fold)paramObject;
/* 109 */     return (Objects.equals(this.aType, fold.aType) && Objects.equals(this.bType, fold.bType) && Objects.equals(this.algebra, fold.algebra));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 114 */     int i = this.aType.hashCode();
/* 115 */     i = 31 * i + this.bType.hashCode();
/* 116 */     i = 31 * i + this.algebra.hashCode();
/* 117 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Fold.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */