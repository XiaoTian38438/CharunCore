/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.GetterP;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Instance<A2, B2>
/*    */   implements GetterP<Getter.Mu<A2, B2>, GetterP.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Getter.Mu<A2, B2>, A, B>, App2<Getter.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 32 */     return paramApp2 -> {
/*    */         Objects.requireNonNull(Getter.unbox(paramApp2));
/*    */         return Optics.getter(paramFunction.andThen(Getter.unbox(paramApp2)::get));
/*    */       };
/*    */   } public <A, B, C, D> FunctionType<Supplier<App2<Getter.Mu<A2, B2>, A, B>>, App2<Getter.Mu<A2, B2>, C, D>> cimap(Function<C, A> paramFunction, Function<D, B> paramFunction1) {
/* 37 */     return paramSupplier -> {
/*    */         Objects.requireNonNull(Getter.unbox(paramSupplier.get()));
/*    */         return Optics.getter(paramFunction.andThen(Getter.unbox(paramSupplier.get())::get));
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Getter$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */