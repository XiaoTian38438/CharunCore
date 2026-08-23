/*     */ package net.minecraft.server.commands;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TagCommand {
/*  25 */   private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.tag.add.failed"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_REMOVE_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.tag.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  29 */     paramCommandDispatcher.register(
/*  30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tag")
/*  31 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  32 */         .then((
/*  33 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/*  34 */           .then(
/*  35 */             Commands.literal("add")
/*  36 */             .then(
/*  37 */               Commands.argument("name", (ArgumentType)StringArgumentType.word())
/*  38 */               .executes(paramCommandContext -> addTag((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), StringArgumentType.getString(paramCommandContext, "name"))))))
/*     */ 
/*     */           
/*  41 */           .then(
/*  42 */             Commands.literal("remove")
/*  43 */             .then(
/*  44 */               Commands.argument("name", (ArgumentType)StringArgumentType.word())
/*  45 */               .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(getTags(EntityArgument.getEntities(paramCommandContext, "targets")), paramSuggestionsBuilder))
/*  46 */               .executes(paramCommandContext -> removeTag((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"), StringArgumentType.getString(paramCommandContext, "name"))))))
/*     */ 
/*     */           
/*  49 */           .then(
/*  50 */             Commands.literal("list")
/*  51 */             .executes(paramCommandContext -> listTags((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntities(paramCommandContext, "targets"))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<String> getTags(Collection<? extends Entity> paramCollection) {
/*  58 */     HashSet<String> hashSet = Sets.newHashSet();
/*  59 */     for (Entity entity : paramCollection) {
/*  60 */       hashSet.addAll(entity.getTags());
/*     */     }
/*  62 */     return hashSet;
/*     */   }
/*     */   
/*     */   private static int addTag(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, String paramString) throws CommandSyntaxException {
/*  66 */     byte b = 0;
/*     */     
/*  68 */     for (Entity entity : paramCollection) {
/*  69 */       if (entity.addTag(paramString)) {
/*  70 */         b++;
/*     */       }
/*     */     } 
/*     */     
/*  74 */     if (b == 0) {
/*  75 */       throw ERROR_ADD_FAILED.create();
/*     */     }
/*     */     
/*  78 */     if (paramCollection.size() == 1) {
/*  79 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.add.success.single", new Object[] { paramString, ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  81 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.add.success.multiple", new Object[] { paramString, Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/*  84 */     return b;
/*     */   }
/*     */   
/*     */   private static int removeTag(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection, String paramString) throws CommandSyntaxException {
/*  88 */     byte b = 0;
/*     */     
/*  90 */     for (Entity entity : paramCollection) {
/*  91 */       if (entity.removeTag(paramString)) {
/*  92 */         b++;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     if (b == 0) {
/*  97 */       throw ERROR_REMOVE_FAILED.create();
/*     */     }
/*     */     
/* 100 */     if (paramCollection.size() == 1) {
/* 101 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.remove.success.single", new Object[] { paramString, ((Entity)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 103 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.remove.success.multiple", new Object[] { paramString, Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 106 */     return b;
/*     */   }
/*     */   
/*     */   private static int listTags(CommandSourceStack paramCommandSourceStack, Collection<? extends Entity> paramCollection) {
/* 110 */     HashSet hashSet = Sets.newHashSet();
/*     */     
/* 112 */     for (Entity entity : paramCollection) {
/* 113 */       hashSet.addAll(entity.getTags());
/*     */     }
/*     */     
/* 116 */     if (paramCollection.size() == 1) {
/* 117 */       Entity entity = paramCollection.iterator().next();
/*     */       
/* 119 */       if (hashSet.isEmpty()) {
/* 120 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.list.single.empty", new Object[] { paramEntity.getDisplayName() }), false);
/*     */       } else {
/* 122 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.list.single.success", new Object[] { paramEntity.getDisplayName(), Integer.valueOf(paramSet.size()), ComponentUtils.formatList(paramSet) }), false);
/*     */       }
/*     */     
/* 125 */     } else if (hashSet.isEmpty()) {
/* 126 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.empty", new Object[] { Integer.valueOf(paramCollection.size()) }), false);
/*     */     } else {
/* 128 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.tag.list.multiple.success", new Object[] { Integer.valueOf(paramCollection.size()), Integer.valueOf(paramSet.size()), ComponentUtils.formatList(paramSet) }), false);
/*     */     } 
/*     */ 
/*     */     
/* 132 */     return hashSet.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TagCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */