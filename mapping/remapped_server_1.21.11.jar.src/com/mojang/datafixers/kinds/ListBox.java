/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public final class ListBox<T>
/*    */   implements App<ListBox.Mu, T> {
/*    */   private final List<T> value;
/*    */   
/*    */   public static final class Mu implements K1 {}
/*    */   
/*    */   public static <T> List<T> unbox(App<Mu, T> paramApp) {
/* 15 */     return ((ListBox)paramApp).value;
/*    */   }
/*    */   
/*    */   public static <T> ListBox<T> create(List<T> paramList) {
/* 19 */     return new ListBox<>(paramList);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ListBox(List<T> paramList) {
/* 25 */     this.value = paramList;
/*    */   }
/*    */   
/*    */   public static <F extends K1, A, B> App<F, List<B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, List<A> paramList) {
/* 29 */     return paramApplicative.map(ListBox::unbox, Instance.INSTANCE.traverse(paramApplicative, paramFunction, create(paramList)));
/*    */   }
/*    */   
/*    */   public static <F extends K1, A> App<F, List<A>> flip(Applicative<F, ?> paramApplicative, List<App<F, A>> paramList) {
/* 33 */     return paramApplicative.map(ListBox::unbox, Instance.INSTANCE.flip(paramApplicative, create(paramList)));
/*    */   }
/*    */   
/*    */   public enum Instance implements Traversable<Mu, Instance.Mu> {
/* 37 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Traversable.Mu {}
/*    */     
/*    */     public <T, R> App<ListBox.Mu, R> map(Function<? super T, ? extends R> param1Function, App<ListBox.Mu, T> param1App) {
/* 43 */       return ListBox.create((List<R>)ListBox.<T>unbox(param1App).stream().<R>map(param1Function).collect(Collectors.toList()));
/*    */     }
/*    */     
/*    */     public <F extends K1, A, B> App<F, App<ListBox.Mu, B>> traverse(Applicative<F, ?> param1Applicative, Function<A, App<F, B>> param1Function, App<ListBox.Mu, A> param1App) {
/*    */       // Byte code:
/*    */       //   0: aload_3
/*    */       //   1: invokestatic unbox : (Lcom/mojang/datafixers/kinds/App;)Ljava/util/List;
/*    */       //   4: astore #4
/*    */       //   6: aload_1
/*    */       //   7: invokestatic builder : ()Lcom/google/common/collect/ImmutableList$Builder;
/*    */       //   10: invokeinterface point : (Ljava/lang/Object;)Lcom/mojang/datafixers/kinds/App;
/*    */       //   15: astore #5
/*    */       //   17: aload #4
/*    */       //   19: invokeinterface iterator : ()Ljava/util/Iterator;
/*    */       //   24: astore #6
/*    */       //   26: aload #6
/*    */       //   28: invokeinterface hasNext : ()Z
/*    */       //   33: ifeq -> 84
/*    */       //   36: aload #6
/*    */       //   38: invokeinterface next : ()Ljava/lang/Object;
/*    */       //   43: astore #7
/*    */       //   45: aload_2
/*    */       //   46: aload #7
/*    */       //   48: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */       //   53: checkcast com/mojang/datafixers/kinds/App
/*    */       //   56: astore #8
/*    */       //   58: aload_1
/*    */       //   59: aload_1
/*    */       //   60: <illegal opcode> apply : ()Ljava/util/function/BiFunction;
/*    */       //   65: invokeinterface point : (Ljava/lang/Object;)Lcom/mojang/datafixers/kinds/App;
/*    */       //   70: aload #5
/*    */       //   72: aload #8
/*    */       //   74: invokeinterface ap2 : (Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
/*    */       //   79: astore #5
/*    */       //   81: goto -> 26
/*    */       //   84: aload_1
/*    */       //   85: <illegal opcode> apply : ()Ljava/util/function/Function;
/*    */       //   90: aload #5
/*    */       //   92: invokeinterface map : (Ljava/util/function/Function;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/kinds/App;
/*    */       //   97: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       //   #50	-> 6
/*    */       //   #52	-> 17
/*    */       //   #53	-> 45
/*    */       //   #54	-> 58
/*    */       //   #55	-> 81
/*    */       //   #57	-> 84
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu implements Traversable.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\ListBox.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */