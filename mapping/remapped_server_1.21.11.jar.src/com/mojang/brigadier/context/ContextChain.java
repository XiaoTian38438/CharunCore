/*     */ package com.mojang.brigadier.context;
/*     */ 
/*     */ import com.mojang.brigadier.RedirectModifier;
/*     */ import com.mojang.brigadier.ResultConsumer;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextChain<S>
/*     */ {
/*     */   private final List<CommandContext<S>> modifiers;
/*     */   private final CommandContext<S> executable;
/*  18 */   private ContextChain<S> nextStageCache = null;
/*     */   
/*     */   public ContextChain(List<CommandContext<S>> paramList, CommandContext<S> paramCommandContext) {
/*  21 */     if (paramCommandContext.getCommand() == null) {
/*  22 */       throw new IllegalArgumentException("Last command in chain must be executable");
/*     */     }
/*  24 */     this.modifiers = paramList;
/*  25 */     this.executable = paramCommandContext;
/*     */   }
/*     */   
/*     */   public static <S> Optional<ContextChain<S>> tryFlatten(CommandContext<S> paramCommandContext) {
/*  29 */     ArrayList<CommandContext<S>> arrayList = new ArrayList();
/*     */     
/*  31 */     CommandContext<S> commandContext = paramCommandContext;
/*     */     
/*     */     while (true) {
/*  34 */       CommandContext<S> commandContext1 = commandContext.getChild();
/*  35 */       if (commandContext1 == null) {
/*     */         
/*  37 */         if (commandContext.getCommand() == null) {
/*  38 */           return Optional.empty();
/*     */         }
/*     */         
/*  41 */         return Optional.of(new ContextChain<>(arrayList, commandContext));
/*     */       } 
/*     */       
/*  44 */       arrayList.add(commandContext);
/*  45 */       commandContext = commandContext1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <S> Collection<S> runModifier(CommandContext<S> paramCommandContext, S paramS, ResultConsumer<S> paramResultConsumer, boolean paramBoolean) throws CommandSyntaxException {
/*  50 */     RedirectModifier<S> redirectModifier = paramCommandContext.getRedirectModifier();
/*     */ 
/*     */     
/*  53 */     if (redirectModifier == null)
/*     */     {
/*  55 */       return Collections.singleton(paramS);
/*     */     }
/*     */     
/*  58 */     CommandContext<S> commandContext = paramCommandContext.copyFor(paramS);
/*     */     try {
/*  60 */       return redirectModifier.apply(commandContext);
/*  61 */     } catch (CommandSyntaxException commandSyntaxException) {
/*  62 */       paramResultConsumer.onCommandComplete(commandContext, false, 0);
/*  63 */       if (paramBoolean) {
/*  64 */         return Collections.emptyList();
/*     */       }
/*  66 */       throw commandSyntaxException;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <S> int runExecutable(CommandContext<S> paramCommandContext, S paramS, ResultConsumer<S> paramResultConsumer, boolean paramBoolean) throws CommandSyntaxException {
/*  71 */     CommandContext<S> commandContext = paramCommandContext.copyFor(paramS);
/*     */     try {
/*  73 */       int i = paramCommandContext.getCommand().run(commandContext);
/*  74 */       paramResultConsumer.onCommandComplete(commandContext, true, i);
/*  75 */       return paramBoolean ? 1 : i;
/*  76 */     } catch (CommandSyntaxException commandSyntaxException) {
/*  77 */       paramResultConsumer.onCommandComplete(commandContext, false, 0);
/*  78 */       if (paramBoolean) {
/*  79 */         return 0;
/*     */       }
/*  81 */       throw commandSyntaxException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeAll(S paramS, ResultConsumer<S> paramResultConsumer) throws CommandSyntaxException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield modifiers : Ljava/util/List;
/*     */     //   4: invokeinterface isEmpty : ()Z
/*     */     //   9: ifeq -> 23
/*     */     //   12: aload_0
/*     */     //   13: getfield executable : Lcom/mojang/brigadier/context/CommandContext;
/*     */     //   16: aload_1
/*     */     //   17: aload_2
/*     */     //   18: iconst_0
/*     */     //   19: invokestatic runExecutable : (Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/Object;Lcom/mojang/brigadier/ResultConsumer;Z)I
/*     */     //   22: ireturn
/*     */     //   23: iconst_0
/*     */     //   24: istore_3
/*     */     //   25: aload_1
/*     */     //   26: invokestatic singletonList : (Ljava/lang/Object;)Ljava/util/List;
/*     */     //   29: astore #4
/*     */     //   31: aload_0
/*     */     //   32: getfield modifiers : Ljava/util/List;
/*     */     //   35: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   40: astore #5
/*     */     //   42: aload #5
/*     */     //   44: invokeinterface hasNext : ()Z
/*     */     //   49: ifeq -> 148
/*     */     //   52: aload #5
/*     */     //   54: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   59: checkcast com/mojang/brigadier/context/CommandContext
/*     */     //   62: astore #6
/*     */     //   64: iload_3
/*     */     //   65: aload #6
/*     */     //   67: invokevirtual isForked : ()Z
/*     */     //   70: ior
/*     */     //   71: istore_3
/*     */     //   72: new java/util/ArrayList
/*     */     //   75: dup
/*     */     //   76: invokespecial <init> : ()V
/*     */     //   79: astore #7
/*     */     //   81: aload #4
/*     */     //   83: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   88: astore #8
/*     */     //   90: aload #8
/*     */     //   92: invokeinterface hasNext : ()Z
/*     */     //   97: ifeq -> 129
/*     */     //   100: aload #8
/*     */     //   102: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   107: astore #9
/*     */     //   109: aload #7
/*     */     //   111: aload #6
/*     */     //   113: aload #9
/*     */     //   115: aload_2
/*     */     //   116: iload_3
/*     */     //   117: invokestatic runModifier : (Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/Object;Lcom/mojang/brigadier/ResultConsumer;Z)Ljava/util/Collection;
/*     */     //   120: invokeinterface addAll : (Ljava/util/Collection;)Z
/*     */     //   125: pop
/*     */     //   126: goto -> 90
/*     */     //   129: aload #7
/*     */     //   131: invokeinterface isEmpty : ()Z
/*     */     //   136: ifeq -> 141
/*     */     //   139: iconst_0
/*     */     //   140: ireturn
/*     */     //   141: aload #7
/*     */     //   143: astore #4
/*     */     //   145: goto -> 42
/*     */     //   148: iconst_0
/*     */     //   149: istore #5
/*     */     //   151: aload #4
/*     */     //   153: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   158: astore #6
/*     */     //   160: aload #6
/*     */     //   162: invokeinterface hasNext : ()Z
/*     */     //   167: ifeq -> 198
/*     */     //   170: aload #6
/*     */     //   172: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   177: astore #7
/*     */     //   179: iload #5
/*     */     //   181: aload_0
/*     */     //   182: getfield executable : Lcom/mojang/brigadier/context/CommandContext;
/*     */     //   185: aload #7
/*     */     //   187: aload_2
/*     */     //   188: iload_3
/*     */     //   189: invokestatic runExecutable : (Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/Object;Lcom/mojang/brigadier/ResultConsumer;Z)I
/*     */     //   192: iadd
/*     */     //   193: istore #5
/*     */     //   195: goto -> 160
/*     */     //   198: iload #5
/*     */     //   200: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     //   #88	-> 12
/*     */     //   #91	-> 23
/*     */     //   #92	-> 25
/*     */     //   #94	-> 31
/*     */     //   #95	-> 64
/*     */     //   #97	-> 72
/*     */     //   #98	-> 81
/*     */     //   #99	-> 109
/*     */     //   #100	-> 126
/*     */     //   #101	-> 129
/*     */     //   #102	-> 139
/*     */     //   #104	-> 141
/*     */     //   #105	-> 145
/*     */     //   #107	-> 148
/*     */     //   #108	-> 151
/*     */     //   #109	-> 179
/*     */     //   #110	-> 195
/*     */     //   #112	-> 198
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage getStage() {
/* 116 */     return this.modifiers.isEmpty() ? Stage.EXECUTE : Stage.MODIFY;
/*     */   }
/*     */   
/*     */   public CommandContext<S> getTopContext() {
/* 120 */     if (this.modifiers.isEmpty()) {
/* 121 */       return this.executable;
/*     */     }
/* 123 */     return this.modifiers.get(0);
/*     */   }
/*     */   
/*     */   public ContextChain<S> nextStage() {
/* 127 */     int i = this.modifiers.size();
/* 128 */     if (i == 0) {
/* 129 */       return null;
/*     */     }
/*     */     
/* 132 */     if (this.nextStageCache == null) {
/* 133 */       this.nextStageCache = new ContextChain(this.modifiers.subList(1, i), this.executable);
/*     */     }
/* 135 */     return this.nextStageCache;
/*     */   }
/*     */   
/*     */   public enum Stage {
/* 139 */     MODIFY,
/* 140 */     EXECUTE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\brigadier\context\ContextChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */