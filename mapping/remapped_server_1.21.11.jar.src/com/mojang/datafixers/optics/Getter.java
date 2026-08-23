/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.GetterP;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ interface Getter<S, T, A, B>
/*    */   extends App2<Getter.Mu<A, B>, S, T>, Optic<GetterP.Mu, S, T, A, B> {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Getter<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 18 */     return (Getter)paramApp2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends GetterP.Mu, P> paramApp) {
/* 25 */     GetterP getterP = GetterP.unbox(paramApp);
/* 26 */     return paramApp2 -> paramGetterP.lmap(paramGetterP.secondPhantom(paramApp2), this::get);
/*    */   }
/*    */   
/*    */   A get(S paramS);
/*    */   
/*    */   public static final class Instance<A2, B2> implements GetterP<Mu<A2, B2>, GetterP.Mu> { public <A, B, C, D> FunctionType<App2<Getter.Mu<A2, B2>, A, B>, App2<Getter.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 32 */       return param1App2 -> {
/*    */           Objects.requireNonNull(Getter.unbox(param1App2));
/*    */           return Optics.getter(param1Function.andThen(Getter.unbox(param1App2)::get));
/*    */         };
/*    */     } public <A, B, C, D> FunctionType<Supplier<App2<Getter.Mu<A2, B2>, A, B>>, App2<Getter.Mu<A2, B2>, C, D>> cimap(Function<C, A> param1Function, Function<D, B> param1Function1) {
/* 37 */       return param1Supplier -> {
/*    */           Objects.requireNonNull(Getter.unbox(param1Supplier.get()));
/*    */           return Optics.getter(param1Function.andThen(Getter.unbox(param1Supplier.get())::get));
/*    */         };
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Getter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */