/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ListTraversal<A, B>
/*    */   implements Traversal<List<A>, List<B>, A, B>
/*    */ {
/* 14 */   static final ListTraversal<?, ?> INSTANCE = new ListTraversal();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<List<A>, App<F, List<B>>> wander(Applicative<F, ?> paramApplicative, FunctionType<A, App<F, B>> paramFunctionType) {
/* 21 */     return paramList -> {
/*    */         App app = paramApplicative.point(ImmutableList.builder());
/*    */         for (Object object : paramList) {
/*    */           app = paramApplicative.ap2(paramApplicative.point(ImmutableList.Builder::add), app, (App)paramFunctionType.apply(object));
/*    */         }
/*    */         return paramApplicative.map(ImmutableList.Builder::build, app);
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return "ListTraversal";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ListTraversal.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */