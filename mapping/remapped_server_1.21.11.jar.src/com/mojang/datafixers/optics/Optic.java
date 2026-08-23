/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public interface Optic<Proof extends K1, S, T, A, B> {
/*    */   <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> paramApp);
/*    */   
/*    */   public static final class CompositionOptic<Proof extends K1, S, T, A, B> extends Record implements Optic<Proof, S, T, A, B> {
/*    */     private final List<? extends Optic<? super Proof, ?, ?, ?, ?>> optics;
/*    */     
/*    */     public CompositionOptic(List<? extends Optic<? super Proof, ?, ?, ?, ?>> param1List) {
/* 22 */       this.optics = param1List; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/optics/Optic$CompositionOptic;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/* 22 */       //   #22	-> 0 } public List<? extends Optic<? super Proof, ?, ?, ?, ?>> optics() { return this.optics; }
/*    */      public final boolean equals(Object param1Object) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/optics/Optic$CompositionOptic;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */     } public <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Proof, P> param1App) {
/* 26 */       ArrayList arrayList = new ArrayList(this.optics.size());
/* 27 */       for (int i = this.optics.size() - 1; i >= 0; i--) {
/* 28 */         arrayList.add(((Optic)this.optics.get(i)).eval(param1App));
/*    */       }
/* 30 */       return param1App2 -> {
/*    */           App2<K2, ?, ?> app2 = param1App2;
/*    */           for (Function<App2<K2, ?, ?>, ? extends App2<K2, ?, ?>> function : (Iterable<Function<App2<K2, ?, ?>, ? extends App2<K2, ?, ?>>>)param1List) {
/*    */             app2 = applyUnchecked(function, app2);
/*    */           }
/*    */           return app2;
/*    */         };
/*    */     }
/*    */ 
/*    */     
/*    */     private static <P extends K2, T extends App2<P, ?, ?>> App2<P, ?, ?> applyUnchecked(Function<T, ? extends App2<P, ?, ?>> param1Function, App2<P, ?, ?> param1App2) {
/* 41 */       return param1Function.apply((T)param1App2);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 46 */       return "(" + (String)this.optics.stream().map(Object::toString).collect(Collectors.joining(" ◦ ")) + ")";
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   default <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(Set<TypeToken<? extends K1>> paramSet, TypeToken<Proof2> paramTypeToken) {
/* 52 */     if (paramSet.stream().allMatch(paramTypeToken2 -> paramTypeToken2.isSupertypeOf(paramTypeToken1))) {
/* 53 */       return Optional.of(this);
/*    */     }
/* 55 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Optic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */