/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Lens<S, T, A, B>
/*    */   extends App2<Lens.Mu<A, B>, S, T>, Optic<Cartesian.Mu, S, T, A, B> {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   public static final class Mu2<S, T>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Lens<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 20 */     return (Lens)paramApp2;
/*    */   }
/*    */   
/*    */   static <S, T, A, B> Lens<S, T, A, B> unbox2(App2<Mu2<S, T>, B, A> paramApp2) {
/* 24 */     return ((Box)paramApp2).lens;
/*    */   }
/*    */   
/*    */   static <S, T, A, B> App2<Mu2<S, T>, B, A> box(Lens<S, T, A, B> paramLens) {
/* 28 */     return new Box<>(paramLens);
/*    */   }
/*    */   
/*    */   public static final class Box<S, T, A, B> implements App2<Mu2<S, T>, B, A> {
/*    */     private final Lens<S, T, A, B> lens;
/*    */     
/*    */     public Box(Lens<S, T, A, B> param1Lens) {
/* 35 */       this.lens = param1Lens;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cartesian.Mu, P> paramApp) {
/* 45 */     Cartesian cartesian = Cartesian.unbox(paramApp);
/* 46 */     return paramApp2 -> paramCartesian.dimap(paramCartesian.first(paramApp2), (), ());
/*    */   }
/*    */   
/*    */   A view(S paramS);
/*    */   
/*    */   T update(B paramB, S paramS);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Cartesian<Mu<A2, B2>, Cartesian.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Lens.Mu<A2, B2>, A, B>, App2<Lens.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 56 */       return param1App2 -> Optics.lens((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Lens.Mu<A2, B2>, A, B> param1App2) {
/* 64 */       return Optics.lens(param1Pair -> Lens.unbox(param1App2).view(param1Pair.getFirst()), (param1Object, param1Pair) -> Pair.of(Lens.unbox(param1App2).update(param1Object, param1Pair.getFirst()), param1Pair.getSecond()));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Lens.Mu<A2, B2>, A, B> param1App2) {
/* 72 */       return Optics.lens(param1Pair -> Lens.unbox(param1App2).view(param1Pair.getSecond()), (param1Object, param1Pair) -> Pair.of(param1Pair.getFirst(), Lens.unbox(param1App2).update(param1Object, param1Pair.getSecond())));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Lens.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */