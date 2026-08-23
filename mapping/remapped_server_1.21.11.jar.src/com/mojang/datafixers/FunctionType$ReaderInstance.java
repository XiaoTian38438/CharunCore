/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Representable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReaderInstance<R>
/*    */   implements Representable<FunctionType.ReaderMu<R>, R, FunctionType.ReaderInstance.Mu<R>>
/*    */ {
/*    */   public static final class Mu<A>
/*    */     implements Representable.Mu {}
/*    */   
/*    */   public <T, R2> App<FunctionType.ReaderMu<R>, R2> map(Function<? super T, ? extends R2> paramFunction, App<FunctionType.ReaderMu<R>, T> paramApp) {
/* 53 */     return FunctionType.create(paramFunction.compose(FunctionType.unbox(paramApp)));
/*    */   }
/*    */ 
/*    */   
/*    */   public <B> App<FunctionType.ReaderMu<R>, B> to(App<FunctionType.ReaderMu<R>, B> paramApp) {
/* 58 */     return paramApp;
/*    */   }
/*    */ 
/*    */   
/*    */   public <B> App<FunctionType.ReaderMu<R>, B> from(App<FunctionType.ReaderMu<R>, B> paramApp) {
/* 63 */     return paramApp;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\FunctionType$ReaderInstance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */