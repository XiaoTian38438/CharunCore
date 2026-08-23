/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*     */ import net.minecraft.network.chat.contents.KeybindContents;
/*     */ import net.minecraft.network.chat.contents.NbtContents;
/*     */ import net.minecraft.network.chat.contents.ObjectContents;
/*     */ import net.minecraft.network.chat.contents.PlainTextContents;
/*     */ import net.minecraft.network.chat.contents.ScoreContents;
/*     */ import net.minecraft.network.chat.contents.SelectorContents;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ import net.minecraft.network.chat.contents.data.DataSource;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Component
/*     */   extends Message, FormattedText
/*     */ {
/*     */   default String getString() {
/*  36 */     return super.getString();
/*     */   }
/*     */   
/*     */   default String getString(int paramInt) {
/*  40 */     StringBuilder stringBuilder = new StringBuilder();
/*  41 */     visit(paramString -> {
/*     */           int i = paramInt - paramStringBuilder.length();
/*     */           if (i <= 0) {
/*     */             return STOP_ITERATION;
/*     */           }
/*     */           paramStringBuilder.append((paramString.length() <= i) ? paramString : paramString.substring(0, i));
/*     */           return Optional.empty();
/*     */         });
/*  49 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default String tryCollapseToString() {
/*  55 */     ComponentContents componentContents = getContents(); if (componentContents instanceof PlainTextContents) { PlainTextContents plainTextContents = (PlainTextContents)componentContents; if (getSiblings().isEmpty() && getStyle().isEmpty())
/*  56 */         return plainTextContents.text();  }
/*     */     
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default MutableComponent plainCopy() {
/*  68 */     return MutableComponent.create(getContents());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default MutableComponent copy() {
/*  78 */     return new MutableComponent(getContents(), new ArrayList<>(getSiblings()), getStyle());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> paramStyledContentConsumer, Style paramStyle) {
/*  85 */     Style style = getStyle().applyTo(paramStyle);
/*     */     
/*  87 */     Optional<T> optional = getContents().visit(paramStyledContentConsumer, style);
/*  88 */     if (optional.isPresent()) {
/*  89 */       return optional;
/*     */     }
/*     */     
/*  92 */     for (Component component : getSiblings()) {
/*  93 */       Optional<T> optional1 = component.visit(paramStyledContentConsumer, style);
/*  94 */       if (optional1.isPresent()) {
/*  95 */         return optional1;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   default <T> Optional<T> visit(FormattedText.ContentConsumer<T> paramContentConsumer) {
/* 104 */     Optional<T> optional = getContents().visit(paramContentConsumer);
/* 105 */     if (optional.isPresent()) {
/* 106 */       return optional;
/*     */     }
/*     */     
/* 109 */     for (Component component : getSiblings()) {
/* 110 */       Optional<T> optional1 = component.visit(paramContentConsumer);
/* 111 */       if (optional1.isPresent()) {
/* 112 */         return optional1;
/*     */       }
/*     */     } 
/*     */     
/* 116 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList() {
/* 120 */     return toFlatList(Style.EMPTY);
/*     */   }
/*     */   
/*     */   default List<Component> toFlatList(Style paramStyle) {
/* 124 */     ArrayList<Component> arrayList = Lists.newArrayList();
/* 125 */     visit((paramStyle, paramString) -> { if (!paramString.isEmpty()) paramList.add(literal(paramString).withStyle(paramStyle));  return Optional.empty(); }paramStyle);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     return arrayList;
/*     */   }
/*     */   
/*     */   default boolean contains(Component paramComponent) {
/* 135 */     if (equals(paramComponent)) {
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     List<Component> list1 = toFlatList();
/* 140 */     List<Component> list2 = paramComponent.toFlatList(getStyle());
/* 141 */     return (Collections.indexOfSubList(list1, list2) != -1);
/*     */   }
/*     */   
/*     */   static Component nullToEmpty(String paramString) {
/* 145 */     return (paramString != null) ? literal(paramString) : CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   static MutableComponent literal(String paramString) {
/* 149 */     return MutableComponent.create((ComponentContents)PlainTextContents.create(paramString));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String paramString) {
/* 153 */     return MutableComponent.create((ComponentContents)new TranslatableContents(paramString, null, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatable(String paramString, Object... paramVarArgs) {
/* 157 */     return MutableComponent.create((ComponentContents)new TranslatableContents(paramString, null, paramVarArgs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MutableComponent translatableEscape(String paramString, Object... paramVarArgs) {
/* 164 */     for (byte b = 0; b < paramVarArgs.length; b++) {
/* 165 */       Object object = paramVarArgs[b];
/* 166 */       if (!TranslatableContents.isAllowedPrimitiveArgument(object) && !(object instanceof Component)) {
/* 167 */         paramVarArgs[b] = String.valueOf(object);
/*     */       }
/*     */     } 
/* 170 */     return translatable(paramString, paramVarArgs);
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String paramString1, String paramString2) {
/* 174 */     return MutableComponent.create((ComponentContents)new TranslatableContents(paramString1, paramString2, TranslatableContents.NO_ARGS));
/*     */   }
/*     */   
/*     */   static MutableComponent translatableWithFallback(String paramString1, String paramString2, Object... paramVarArgs) {
/* 178 */     return MutableComponent.create((ComponentContents)new TranslatableContents(paramString1, paramString2, paramVarArgs));
/*     */   }
/*     */   
/*     */   static MutableComponent empty() {
/* 182 */     return MutableComponent.create((ComponentContents)PlainTextContents.EMPTY);
/*     */   }
/*     */   
/*     */   static MutableComponent keybind(String paramString) {
/* 186 */     return MutableComponent.create((ComponentContents)new KeybindContents(paramString));
/*     */   }
/*     */   
/*     */   static MutableComponent nbt(String paramString, boolean paramBoolean, Optional<Component> paramOptional, DataSource paramDataSource) {
/* 190 */     return MutableComponent.create((ComponentContents)new NbtContents(paramString, paramBoolean, paramOptional, paramDataSource));
/*     */   }
/*     */   
/*     */   static MutableComponent score(SelectorPattern paramSelectorPattern, String paramString) {
/* 194 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.left(paramSelectorPattern), paramString));
/*     */   }
/*     */   
/*     */   static MutableComponent score(String paramString1, String paramString2) {
/* 198 */     return MutableComponent.create((ComponentContents)new ScoreContents(Either.right(paramString1), paramString2));
/*     */   }
/*     */   
/*     */   static MutableComponent selector(SelectorPattern paramSelectorPattern, Optional<Component> paramOptional) {
/* 202 */     return MutableComponent.create((ComponentContents)new SelectorContents(paramSelectorPattern, paramOptional));
/*     */   }
/*     */   
/*     */   static MutableComponent object(ObjectInfo paramObjectInfo) {
/* 206 */     return MutableComponent.create((ComponentContents)new ObjectContents(paramObjectInfo));
/*     */   }
/*     */ 
/*     */   
/*     */   static Component translationArg(Date paramDate) {
/* 211 */     return literal(paramDate.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Message paramMessage) {
/* 215 */     Component component = (Component)paramMessage; return (paramMessage instanceof Component) ? component : literal(paramMessage.getString());
/*     */   }
/*     */   
/*     */   static Component translationArg(UUID paramUUID) {
/* 219 */     return literal(paramUUID.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(Identifier paramIdentifier) {
/* 223 */     return literal(paramIdentifier.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(ChunkPos paramChunkPos) {
/* 227 */     return literal(paramChunkPos.toString());
/*     */   }
/*     */   
/*     */   static Component translationArg(URI paramURI) {
/* 231 */     return literal(paramURI.toString());
/*     */   }
/*     */   
/*     */   Style getStyle();
/*     */   
/*     */   ComponentContents getContents();
/*     */   
/*     */   List<Component> getSiblings();
/*     */   
/*     */   FormattedCharSequence getVisualOrderText();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\Component.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */