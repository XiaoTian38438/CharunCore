/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.functions.Functions;
/*    */ import com.mojang.datafixers.functions.PointFree;
/*    */ import com.mojang.datafixers.functions.PointFreeRule;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class View<A, B> extends Record implements App2<View.Mu, A, B> {
/*    */   private final PointFree<Function<A, B>> function;
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/View;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */   }
/*    */   
/* 17 */   public View(PointFree<Function<A, B>> paramPointFree) { this.function = paramPointFree; } public final boolean equals(Object paramObject) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/View;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 17 */     //   #17	-> 0 } public PointFree<Function<A, B>> function() { return this.function; }
/*    */   
/*    */   static final class Mu implements K2 {}
/*    */   static <A, B> View<A, B> unbox(App2<Mu, A, B> paramApp2) {
/* 21 */     return (View)paramApp2;
/*    */   }
/*    */   
/*    */   public static <A> View<A, A> nopView(Type<A> paramType) {
/* 25 */     return new View<>(Functions.id(paramType));
/*    */   }
/*    */   
/*    */   public Type<A> type() {
/* 29 */     return ((Func)funcType()).first();
/*    */   }
/*    */   
/*    */   public Type<B> newType() {
/* 33 */     return ((Func)funcType()).second();
/*    */   }
/*    */   
/*    */   public Type<Function<A, B>> funcType() {
/* 37 */     return this.function.type();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "View[" + String.valueOf(this.function) + "," + String.valueOf(newType()) + "]";
/*    */   }
/*    */   
/*    */   public Optional<? extends View<A, B>> rewrite(PointFreeRule paramPointFreeRule) {
/* 46 */     return paramPointFreeRule.rewrite(function()).map(View::new);
/*    */   }
/*    */   
/*    */   public View<A, B> rewriteOrNop(PointFreeRule paramPointFreeRule) {
/* 50 */     return DataFixUtils.<View<A, B>>orElse(rewrite(paramPointFreeRule), this);
/*    */   }
/*    */   
/*    */   public <C> View<A, C> flatMap(Function<Type<B>, View<B, C>> paramFunction) {
/* 54 */     View view = paramFunction.apply(newType());
/* 55 */     return new View(Functions.comp(view.function(), function()));
/*    */   }
/*    */   
/*    */   public static <A, B> View<A, B> create(PointFree<Function<A, B>> paramPointFree) {
/* 59 */     return new View<>(paramPointFree);
/*    */   }
/*    */   
/*    */   public static <A, B> View<A, B> create(String paramString, Type<A> paramType, Type<B> paramType1, Function<DynamicOps<?>, Function<A, B>> paramFunction) {
/* 63 */     return new View<>(Functions.fun(paramString, paramFunction, paramType, paramType1));
/*    */   }
/*    */ 
/*    */   
/*    */   public <C> View<C, B> compose(View<C, A> paramView) {
/* 68 */     if (isNop()) {
/* 69 */       return new View(paramView.function());
/*    */     }
/* 71 */     if (paramView.isNop()) {
/* 72 */       return new View(function());
/*    */     }
/* 74 */     return new View(Functions.comp(function(), paramView.function()));
/*    */   }
/*    */   
/*    */   public boolean isNop() {
/* 78 */     return Functions.isId(function());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\View.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */