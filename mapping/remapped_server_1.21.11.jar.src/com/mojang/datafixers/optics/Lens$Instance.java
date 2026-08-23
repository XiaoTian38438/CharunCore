/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.util.Pair;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Instance<A2, B2>
/*    */   implements Cartesian<Lens.Mu<A2, B2>, Cartesian.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Lens.Mu<A2, B2>, A, B>, App2<Lens.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 56 */     return paramApp2 -> Optics.lens((), ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Lens.Mu<A2, B2>, A, B> paramApp2) {
/* 64 */     return Optics.lens(paramPair -> Lens.unbox(paramApp2).view(paramPair.getFirst()), (paramObject, paramPair) -> Pair.of(Lens.unbox(paramApp2).update(paramObject, paramPair.getFirst()), paramPair.getSecond()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Lens.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Lens.Mu<A2, B2>, A, B> paramApp2) {
/* 72 */     return Optics.lens(paramPair -> Lens.unbox(paramApp2).view(paramPair.getSecond()), (paramObject, paramPair) -> Pair.of(paramPair.getFirst(), Lens.unbox(paramApp2).update(paramObject, paramPair.getSecond())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Lens$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */