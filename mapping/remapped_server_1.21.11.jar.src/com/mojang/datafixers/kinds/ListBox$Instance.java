/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
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
/*    */ public enum Instance
/*    */   implements Traversable<ListBox.Mu, ListBox.Instance.Mu>
/*    */ {
/* 37 */   INSTANCE;
/*    */   
/*    */   public static final class Mu
/*    */     implements Traversable.Mu {}
/*    */   
/*    */   public <T, R> App<ListBox.Mu, R> map(Function<? super T, ? extends R> paramFunction, App<ListBox.Mu, T> paramApp) {
/* 43 */     return ListBox.create((List<R>)ListBox.<T>unbox(paramApp).stream().<R>map(paramFunction).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   public <F extends K1, A, B> App<F, App<ListBox.Mu, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<ListBox.Mu, A> paramApp) {
/*    */     // Byte code:
/*    */     //   0: aload_3
/*    */     //   1: invokestatic unbox : (Lcom/mojang/datafixers/kinds/App;)Ljava/util/List;
/*    */     //   4: astore #4
/*    */     //   6: aload_1
/*    */     //   7: invokestatic builder : ()Lcom/google/common/collect/ImmutableList$Builder;
/*    */     //   10: invokeinterface point : (Ljava/lang/Object;)Lcom/mojang/datafixers/kinds/App;
/*    */     //   15: astore #5
/*    */     //   17: aload #4
/*    */     //   19: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */     //   24: astore #6
/*    */     //   26: aload #6
/*    */     //   28: invokeinterface hasNext : ()Z
/*    */     //   33: ifeq -> 84
/*    */     //   36: aload #6
/*    */     //   38: invokeinterface next : ()Ljava/lang/Object;
/*    */     //   43: astore #7
/*    */     //   45: aload_2
/*    */     //   46: aload #7
/*    */     //   48: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   53: checkcast com/mojang/datafixers/kinds/App
/*    */     //   56: astore #8
/*    */     //   58: aload_1
/*    */     //   59: aload_1
/*    */     //   60: <illegal opcode> apply : ()Ljava/util/function/BiFunction;
/*    */     //   65: invokeinterface point : (Ljava/lang/Object;)Lcom/mojang/datafixers/kinds/App;
/*    */     //   70: aload #5
/*    */     //   72: aload #8
/*    */     //   74: invokeinterface ap2 : (Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
/*    */     //   79: astore #5
/*    */     //   81: goto -> 26
/*    */     //   84: aload_1
/*    */     //   85: <illegal opcode> apply : ()Ljava/util/function/Function;
/*    */     //   90: aload #5
/*    */     //   92: invokeinterface map : (Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
/*    */     //   97: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     //   #50	-> 6
/*    */     //   #52	-> 17
/*    */     //   #53	-> 45
/*    */     //   #54	-> 58
/*    */     //   #55	-> 81
/*    */     //   #57	-> 84
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\ListBox$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */