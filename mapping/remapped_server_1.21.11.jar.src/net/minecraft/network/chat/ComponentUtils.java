/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ 
/*     */ public class ComponentUtils
/*     */ {
/*     */   public static final String DEFAULT_SEPARATOR_TEXT = ", ";
/*  22 */   public static final Component DEFAULT_SEPARATOR = Component.literal(", ").withStyle(ChatFormatting.GRAY);
/*  23 */   public static final Component DEFAULT_NO_STYLE_SEPARATOR = Component.literal(", ");
/*     */   
/*     */   @CheckReturnValue
/*     */   public static MutableComponent mergeStyles(MutableComponent paramMutableComponent, Style paramStyle) {
/*  27 */     if (paramStyle.isEmpty()) {
/*  28 */       return paramMutableComponent;
/*     */     }
/*     */     
/*  31 */     Style style = paramMutableComponent.getStyle();
/*  32 */     if (style.isEmpty()) {
/*  33 */       return paramMutableComponent.setStyle(paramStyle);
/*     */     }
/*     */     
/*  36 */     if (style.equals(paramStyle)) {
/*  37 */       return paramMutableComponent;
/*     */     }
/*     */     
/*  40 */     return paramMutableComponent.setStyle(style.applyTo(paramStyle));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static Component mergeStyles(Component paramComponent, Style paramStyle) {
/*  45 */     if (paramStyle.isEmpty()) {
/*  46 */       return paramComponent;
/*     */     }
/*     */     
/*  49 */     Style style = paramComponent.getStyle();
/*  50 */     if (style.isEmpty()) {
/*  51 */       return paramComponent.copy().setStyle(paramStyle);
/*     */     }
/*     */     
/*  54 */     if (style.equals(paramStyle)) {
/*  55 */       return paramComponent;
/*     */     }
/*     */     
/*  58 */     return paramComponent.copy().setStyle(style.applyTo(paramStyle));
/*     */   }
/*     */   
/*     */   public static Optional<MutableComponent> updateForEntity(CommandSourceStack paramCommandSourceStack, Optional<Component> paramOptional, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/*  62 */     return paramOptional.isPresent() ? Optional.<MutableComponent>of(updateForEntity(paramCommandSourceStack, paramOptional.get(), paramEntity, paramInt)) : Optional.<MutableComponent>empty();
/*     */   }
/*     */   
/*     */   public static MutableComponent updateForEntity(CommandSourceStack paramCommandSourceStack, Component paramComponent, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/*  66 */     if (paramInt > 100) {
/*  67 */       return paramComponent.copy();
/*     */     }
/*     */     
/*  70 */     MutableComponent mutableComponent = paramComponent.getContents().resolve(paramCommandSourceStack, paramEntity, paramInt + 1);
/*     */     
/*  72 */     for (Component component : paramComponent.getSiblings()) {
/*  73 */       mutableComponent.append(updateForEntity(paramCommandSourceStack, component, paramEntity, paramInt + 1));
/*     */     }
/*     */     
/*  76 */     return mutableComponent.withStyle(resolveStyle(paramCommandSourceStack, paramComponent.getStyle(), paramEntity, paramInt));
/*     */   }
/*     */   
/*     */   private static Style resolveStyle(CommandSourceStack paramCommandSourceStack, Style paramStyle, Entity paramEntity, int paramInt) throws CommandSyntaxException {
/*  80 */     HoverEvent hoverEvent = paramStyle.getHoverEvent();
/*  81 */     if (hoverEvent instanceof HoverEvent.ShowText) { HoverEvent.ShowText showText = (HoverEvent.ShowText)hoverEvent; try { Component component2 = showText.value(), component1 = component2;
/*  82 */         HoverEvent.ShowText showText1 = new HoverEvent.ShowText(updateForEntity(paramCommandSourceStack, component1, paramEntity, paramInt + 1));
/*  83 */         return paramStyle.withHoverEvent(showText1); }
/*     */       catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/*  86 */      return paramStyle;
/*     */   }
/*     */   
/*     */   public static Component formatList(Collection<String> paramCollection) {
/*  90 */     return formatAndSortList(paramCollection, paramString -> Component.literal(paramString).withStyle(ChatFormatting.GREEN));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> paramCollection, Function<T, Component> paramFunction) {
/*  94 */     if (paramCollection.isEmpty())
/*  95 */       return CommonComponents.EMPTY; 
/*  96 */     if (paramCollection.size() == 1) {
/*  97 */       return paramFunction.apply((T)paramCollection.iterator().next());
/*     */     }
/*     */     
/* 100 */     ArrayList<? extends T> arrayList = Lists.newArrayList(paramCollection);
/* 101 */     arrayList.sort(Comparable::compareTo);
/* 102 */     return formatList(arrayList, paramFunction);
/*     */   }
/*     */   
/*     */   public static <T> Component formatList(Collection<? extends T> paramCollection, Function<T, Component> paramFunction) {
/* 106 */     return formatList(paramCollection, DEFAULT_SEPARATOR, paramFunction);
/*     */   }
/*     */   
/*     */   public static <T> MutableComponent formatList(Collection<? extends T> paramCollection, Optional<? extends Component> paramOptional, Function<T, Component> paramFunction) {
/* 110 */     return formatList(paramCollection, (Component)DataFixUtils.orElse(paramOptional, DEFAULT_SEPARATOR), paramFunction);
/*     */   }
/*     */   
/*     */   public static Component formatList(Collection<? extends Component> paramCollection, Component paramComponent) {
/* 114 */     return formatList(paramCollection, paramComponent, Function.identity());
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
/*     */   public static <T> MutableComponent formatList(Collection<? extends T> paramCollection, Component paramComponent, Function<T, Component> paramFunction) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokeinterface isEmpty : ()Z
/*     */     //   6: ifeq -> 13
/*     */     //   9: invokestatic empty : ()Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   12: areturn
/*     */     //   13: aload_0
/*     */     //   14: invokeinterface size : ()I
/*     */     //   19: iconst_1
/*     */     //   20: if_icmpne -> 49
/*     */     //   23: aload_2
/*     */     //   24: aload_0
/*     */     //   25: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   30: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   35: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   40: checkcast net/minecraft/network/chat/Component
/*     */     //   43: invokeinterface copy : ()Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   48: areturn
/*     */     //   49: invokestatic empty : ()Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   52: astore_3
/*     */     //   53: iconst_1
/*     */     //   54: istore #4
/*     */     //   56: aload_0
/*     */     //   57: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   62: astore #5
/*     */     //   64: aload #5
/*     */     //   66: invokeinterface hasNext : ()Z
/*     */     //   71: ifeq -> 116
/*     */     //   74: aload #5
/*     */     //   76: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   81: astore #6
/*     */     //   83: iload #4
/*     */     //   85: ifne -> 94
/*     */     //   88: aload_3
/*     */     //   89: aload_1
/*     */     //   90: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   93: pop
/*     */     //   94: aload_3
/*     */     //   95: aload_2
/*     */     //   96: aload #6
/*     */     //   98: invokeinterface apply : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   103: checkcast net/minecraft/network/chat/Component
/*     */     //   106: invokevirtual append : (Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   109: pop
/*     */     //   110: iconst_0
/*     */     //   111: istore #4
/*     */     //   113: goto -> 64
/*     */     //   116: aload_3
/*     */     //   117: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #118	-> 0
/*     */     //   #119	-> 9
/*     */     //   #120	-> 13
/*     */     //   #121	-> 23
/*     */     //   #124	-> 49
/*     */     //   #125	-> 53
/*     */     //   #126	-> 56
/*     */     //   #127	-> 83
/*     */     //   #128	-> 88
/*     */     //   #130	-> 94
/*     */     //   #131	-> 110
/*     */     //   #132	-> 113
/*     */     //   #134	-> 116
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
/*     */   public static MutableComponent wrapInSquareBrackets(Component paramComponent) {
/* 138 */     return Component.translatable("chat.square_brackets", new Object[] { paramComponent });
/*     */   }
/*     */   
/*     */   public static Component fromMessage(Message paramMessage) {
/* 142 */     if (paramMessage instanceof Component) return (Component)paramMessage;
/*     */ 
/*     */     
/* 145 */     return Component.literal(paramMessage.getString());
/*     */   }
/*     */   
/*     */   public static boolean isTranslationResolvable(Component paramComponent) {
/* 149 */     if (paramComponent != null) { ComponentContents componentContents = paramComponent.getContents(); if (componentContents instanceof TranslatableContents) { TranslatableContents translatableContents = (TranslatableContents)componentContents;
/* 150 */         String str1 = translatableContents.getKey();
/* 151 */         String str2 = translatableContents.getFallback();
/* 152 */         return (str2 != null || Language.getInstance().has(str1)); }
/*     */        }
/* 154 */      return true;
/*     */   }
/*     */   
/*     */   public static MutableComponent copyOnClickText(String paramString) {
/* 158 */     return wrapInSquareBrackets(Component.literal(paramString).withStyle(paramStyle -> paramStyle.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent.CopyToClipboard(paramString)).withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click"))).withInsertion(paramString)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ComponentUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */