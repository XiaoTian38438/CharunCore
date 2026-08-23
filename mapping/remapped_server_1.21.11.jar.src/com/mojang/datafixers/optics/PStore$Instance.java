/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
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
/*    */ public final class Instance<I, J>
/*    */   implements Functor<PStore.Mu<I, J>, PStore.Instance.Mu<I, J>>
/*    */ {
/*    */   public static final class Mu<I, J>
/*    */     implements Functor.Mu {}
/*    */   
/*    */   public <T, R> App<PStore.Mu<I, J>, R> map(Function<? super T, ? extends R> paramFunction, App<PStore.Mu<I, J>, T> paramApp) {
/* 27 */     PStore<I, J, T> pStore = PStore.unbox(paramApp);
/* 28 */     Objects.requireNonNull(pStore); Objects.requireNonNull(paramFunction.compose(pStore::peek)); Objects.requireNonNull(pStore); return Optics.pStore(paramFunction.compose(pStore::peek)::apply, pStore::pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\PStore$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */