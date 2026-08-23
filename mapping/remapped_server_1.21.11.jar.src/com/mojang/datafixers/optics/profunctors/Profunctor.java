/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Kind2;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public interface Profunctor<P extends com.mojang.datafixers.kinds.K2, Mu extends Profunctor.Mu>
/*    */   extends Kind2<P, Mu>
/*    */ {
/*    */   public static interface Mu
/*    */     extends Kind2.Mu
/*    */   {
/* 17 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {  }
/*    */     ; }
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Profunctor<P, Proof> unbox(App<Proof, P> paramApp) {
/* 21 */     return (Profunctor<P, Proof>)paramApp;
/*    */   }
/*    */   
/*    */   class null extends TypeToken<Mu> {}
/*    */   
/*    */   <A, B, C, D> FunctionType<App2<P, A, B>, App2<P, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1);
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> dimap(App2<P, A, B> paramApp2, Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 29 */     return (App2<P, C, D>)dimap(paramFunction, paramFunction1).apply(paramApp2);
/*    */   }
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> dimap(Supplier<App2<P, A, B>> paramSupplier, Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 33 */     return (App2<P, C, D>)dimap(paramFunction, paramFunction1).apply(paramSupplier.get());
/*    */   }
/*    */   
/*    */   default <A, B, C> App2<P, C, B> lmap(App2<P, A, B> paramApp2, Function<C, A> paramFunction) {
/* 37 */     return dimap(paramApp2, paramFunction, Function.identity());
/*    */   }
/*    */   
/*    */   default <A, B, D> App2<P, A, D> rmap(App2<P, A, B> paramApp2, Function<B, D> paramFunction) {
/* 41 */     return dimap(paramApp2, Function.identity(), paramFunction);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Profunctor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */