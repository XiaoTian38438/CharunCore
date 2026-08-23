/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CompositionOptic<Proof extends K1, S, T, A, B>
/*    */   extends Record
/*    */   implements Optic<Proof, S, T, A, B>
/*    */ {
/*    */   private final List<? extends Optic<? super Proof, ?, ?, ?, ?>> optics;
/*    */   
/*    */   public CompositionOptic(List<? extends Optic<? super Proof, ?, ?, ?, ?>> paramList) {
/* 22 */     this.optics = paramList; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/optics/Optic$CompositionOptic;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/* 22 */     //   #22	-> 0 } public List<? extends Optic<? super Proof, ?, ?, ?, ?>> optics() { return this.optics; }
/*    */    public final boolean equals(Object paramObject) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/optics/Optic$CompositionOptic;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */   } public <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> paramApp) {
/* 26 */     ArrayList arrayList = new ArrayList(this.optics.size());
/* 27 */     for (int i = this.optics.size() - 1; i >= 0; i--) {
/* 28 */       arrayList.add(((Optic)this.optics.get(i)).eval(paramApp));
/*    */     }
/* 30 */     return paramApp2 -> {
/*    */         App2<K2, ?, ?> app2 = paramApp2;
/*    */         for (Function<App2<K2, ?, ?>, ? extends App2<K2, ?, ?>> function : (Iterable<Function<App2<K2, ?, ?>, ? extends App2<K2, ?, ?>>>)paramList) {
/*    */           app2 = applyUnchecked(function, app2);
/*    */         }
/*    */         return app2;
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   private static <P extends K2, T extends App2<P, ?, ?>> App2<P, ?, ?> applyUnchecked(Function<T, ? extends App2<P, ?, ?>> paramFunction, App2<P, ?, ?> paramApp2) {
/* 41 */     return paramFunction.apply((T)paramApp2);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "(" + (String)this.optics.stream().map(Object::toString).collect(Collectors.joining(" ◦ ")) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Optic$CompositionOptic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */