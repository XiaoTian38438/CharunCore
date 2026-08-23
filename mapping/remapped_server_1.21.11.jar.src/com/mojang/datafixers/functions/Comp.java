/*     */ package com.mojang.datafixers.functions;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Comp<A, B>
/*     */   extends PointFree<Function<A, B>>
/*     */ {
/*     */   protected final PointFree<? extends Function<?, ?>>[] functions;
/*     */   private final Type<Function<A, B>> type;
/*     */   
/*     */   protected Comp(PointFree<? extends Function<?, ?>>... paramVarArgs) {
/*  24 */     this.functions = paramVarArgs;
/*  25 */     PointFree<? extends Function<?, ?>> pointFree1 = paramVarArgs[0];
/*  26 */     PointFree<? extends Function<?, ?>> pointFree2 = paramVarArgs[paramVarArgs.length - 1];
/*  27 */     this.type = DSL.func(((Func)pointFree2
/*  28 */         .type()).first(), ((Func)pointFree1
/*  29 */         .type()).second());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Comp(PointFree<? extends Function<?, ?>>[] paramArrayOfPointFree, Type<Function<A, B>> paramType) {
/*  34 */     this.functions = paramArrayOfPointFree;
/*  35 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<Function<A, B>> type() {
/*  40 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int paramInt) {
/*  47 */     String str = Arrays.<PointFree<? extends Function<?, ?>>>stream(this.functions).map(paramPointFree -> paramPointFree.toString(paramInt + 1)).collect(Collectors.joining("\n" + indent(paramInt + 1) + "◦\n" + indent(paramInt + 1)));
/*  48 */     return "(\n" + indent(paramInt + 1) + str + "\n" + indent(paramInt) + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<Function<A, B>>> all(PointFreeRule paramPointFreeRule) {
/*  54 */     ArrayList<? super PointFree<? extends Function<?, ?>>> arrayList = new ArrayList(this.functions.length);
/*  55 */     boolean bool = false;
/*  56 */     for (PointFree<? extends Function<?, ?>> pointFree1 : this.functions) {
/*  57 */       PointFree<? extends Function<?, ?>> pointFree2 = paramPointFreeRule.rewriteOrNop(pointFree1);
/*  58 */       if (pointFree2 != pointFree1) {
/*  59 */         bool = true;
/*  60 */         if (pointFree2 instanceof Comp) { Comp comp = (Comp)pointFree2;
/*  61 */           Collections.addAll(arrayList, comp.functions); }
/*     */         else
/*  63 */         { arrayList.add(pointFree2); }
/*     */       
/*     */       } else {
/*  66 */         arrayList.add(pointFree1);
/*     */       } 
/*     */     } 
/*  69 */     return Optional.of(bool ? new Comp((PointFree<? extends Function<?, ?>>[])arrayList.toArray(paramInt -> new PointFree[paramInt]), this.type) : this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<? extends PointFree<Function<A, B>>> one(PointFreeRule paramPointFreeRule) {
/*  75 */     for (byte b = 0; b < this.functions.length; b++) {
/*  76 */       PointFree<? extends Function<?, ?>> pointFree = this.functions[b];
/*  77 */       Optional<? extends PointFree<? extends Function<?, ?>>> optional = paramPointFreeRule.rewrite(pointFree);
/*  78 */       if (optional.isPresent()) {
/*  79 */         Comp comp = (Comp)optional.get(); if (comp instanceof Comp) { Comp comp1 = comp;
/*  80 */           PointFree[] arrayOfPointFree1 = new PointFree[this.functions.length - 1 + comp1.functions.length];
/*  81 */           System.arraycopy(this.functions, 0, arrayOfPointFree1, 0, b);
/*  82 */           System.arraycopy(comp1.functions, 0, arrayOfPointFree1, b, comp1.functions.length);
/*  83 */           System.arraycopy(this.functions, b + 1, arrayOfPointFree1, b + comp1.functions.length, this.functions.length - b - 1);
/*  84 */           return Optional.of(new Comp((PointFree<? extends Function<?, ?>>[])arrayOfPointFree1, this.type)); }
/*     */         
/*  86 */         PointFree[] arrayOfPointFree = Arrays.<PointFree>copyOf((PointFree[])this.functions, this.functions.length);
/*  87 */         arrayOfPointFree[b] = optional.get();
/*  88 */         return Optional.of(new Comp((PointFree<? extends Function<?, ?>>[])arrayOfPointFree, this.type));
/*     */       } 
/*     */     } 
/*     */     
/*  92 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/*  97 */     if (this == paramObject) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 101 */       return false;
/*     */     }
/* 103 */     Comp comp = (Comp)paramObject;
/* 104 */     return Arrays.equals((Object[])this.functions, (Object[])comp.functions);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return Arrays.hashCode((Object[])this.functions);
/*     */   }
/*     */ 
/*     */   
/*     */   public Function<DynamicOps<?>, Function<A, B>> eval() {
/* 114 */     return paramDynamicOps -> ();
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
/*     */   private static <A, B> B applyUnchecked(Function<A, B> paramFunction, Object paramObject) {
/* 126 */     return paramFunction.apply((A)paramObject);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Comp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */