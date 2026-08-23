/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface PStore<I, J, X>
/*    */   extends App<PStore.Mu<I, J>, X> {
/*    */   public static final class Mu<I, J>
/*    */     implements K1 {}
/*    */   
/*    */   static <I, J, X> PStore<I, J, X> unbox(App<Mu<I, J>, X> paramApp) {
/* 15 */     return (PStore)paramApp;
/*    */   }
/*    */   
/*    */   X peek(J paramJ);
/*    */   
/*    */   I pos();
/*    */   
/*    */   public static final class Instance<I, J>
/*    */     implements Functor<Mu<I, J>, Instance.Mu<I, J>> {
/*    */     public static final class Mu<I, J> implements Functor.Mu {}
/*    */     
/*    */     public <T, R> App<PStore.Mu<I, J>, R> map(Function<? super T, ? extends R> param1Function, App<PStore.Mu<I, J>, T> param1App) {
/* 27 */       PStore<I, J, T> pStore = PStore.unbox(param1App);
/* 28 */       Objects.requireNonNull(pStore); Objects.requireNonNull(param1Function.compose(pStore::peek)); Objects.requireNonNull(pStore); return Optics.pStore(param1Function.compose(pStore::peek)::apply, pStore::pos);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu<I, J> implements Functor.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\PStore.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */