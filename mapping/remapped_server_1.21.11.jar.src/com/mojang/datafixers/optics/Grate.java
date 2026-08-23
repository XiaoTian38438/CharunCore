/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Closed;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface Grate<S, T, A, B>
/*    */   extends App2<Grate.Mu<A, B>, S, T>, Optic<Closed.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Grate<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 17 */     return (Grate)paramApp2;
/*    */   }
/*    */ 
/*    */   
/*    */   T grate(FunctionType<FunctionType<S, A>, B> paramFunctionType);
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Closed.Mu, P> paramApp) {
/* 24 */     Closed closed = Closed.unbox(paramApp);
/* 25 */     return paramApp2 -> paramClosed.dimap(paramClosed.closed(paramApp2), (), this::grate);
/*    */   }
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Closed<Mu<A2, B2>, Closed.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Grate.Mu<A2, B2>, A, B>, App2<Grate.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 31 */       return param1App2 -> Optics.grate(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, X> App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>> closed(App2<Grate.Mu<A2, B2>, A, B> param1App2) {
/* 36 */       FunctionType<FunctionType<FunctionType<?, ?>, ?>, ?> functionType = param1FunctionType -> ();
/* 37 */       return (App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>>)Optics.grate(functionType).eval((App)this).apply(Grate.unbox(param1App2));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Grate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */