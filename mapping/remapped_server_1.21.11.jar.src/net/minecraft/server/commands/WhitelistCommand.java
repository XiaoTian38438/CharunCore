/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.GameProfileArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.players.UserWhiteList;
/*     */ import net.minecraft.server.players.UserWhiteListEntry;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ public class WhitelistCommand {
/*  24 */   private static final SimpleCommandExceptionType ERROR_ALREADY_ENABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.whitelist.alreadyOn"));
/*  25 */   private static final SimpleCommandExceptionType ERROR_ALREADY_DISABLED = new SimpleCommandExceptionType((Message)Component.translatable("commands.whitelist.alreadyOff"));
/*  26 */   private static final SimpleCommandExceptionType ERROR_ALREADY_WHITELISTED = new SimpleCommandExceptionType((Message)Component.translatable("commands.whitelist.add.failed"));
/*  27 */   private static final SimpleCommandExceptionType ERROR_NOT_WHITELISTED = new SimpleCommandExceptionType((Message)Component.translatable("commands.whitelist.remove.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  30 */     paramCommandDispatcher.register(
/*  31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("whitelist")
/*  32 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  33 */         .then(
/*  34 */           Commands.literal("on")
/*  35 */           .executes(paramCommandContext -> enableWhitelist((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  37 */         .then(
/*  38 */           Commands.literal("off")
/*  39 */           .executes(paramCommandContext -> disableWhitelist((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  41 */         .then(
/*  42 */           Commands.literal("list")
/*  43 */           .executes(paramCommandContext -> showList((CommandSourceStack)paramCommandContext.getSource()))))
/*     */         
/*  45 */         .then(
/*  46 */           Commands.literal("add")
/*  47 */           .then(
/*  48 */             Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/*  49 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> {
/*     */                 PlayerList playerList = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList();
/*     */                 
/*     */                 return SharedSuggestionProvider.suggest(playerList.getPlayers().stream().map(Player::nameAndId).filter(()).map(NameAndId::name), paramSuggestionsBuilder);
/*  53 */               }).executes(paramCommandContext -> addPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets"))))))
/*     */ 
/*     */         
/*  56 */         .then(
/*  57 */           Commands.literal("remove")
/*  58 */           .then(
/*  59 */             Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/*  60 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getWhiteListNames(), paramSuggestionsBuilder))
/*  61 */             .executes(paramCommandContext -> removePlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets"))))))
/*     */ 
/*     */         
/*  64 */         .then(
/*  65 */           Commands.literal("reload")
/*  66 */           .executes(paramCommandContext -> reload((CommandSourceStack)paramCommandContext.getSource()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int reload(CommandSourceStack paramCommandSourceStack) {
/*  72 */     paramCommandSourceStack.getServer().getPlayerList().reloadWhiteList();
/*  73 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.reloaded"), true);
/*  74 */     paramCommandSourceStack.getServer().kickUnlistedPlayers();
/*  75 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addPlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection) throws CommandSyntaxException {
/*  79 */     UserWhiteList userWhiteList = paramCommandSourceStack.getServer().getPlayerList().getWhiteList();
/*  80 */     byte b = 0;
/*     */     
/*  82 */     for (NameAndId nameAndId : paramCollection) {
/*  83 */       if (!userWhiteList.isWhiteListed(nameAndId)) {
/*  84 */         UserWhiteListEntry userWhiteListEntry = new UserWhiteListEntry(nameAndId);
/*  85 */         userWhiteList.add(userWhiteListEntry);
/*  86 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.add.success", new Object[] { Component.literal(paramNameAndId.name()) }), true);
/*  87 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     if (b == 0) {
/*  92 */       throw ERROR_ALREADY_WHITELISTED.create();
/*     */     }
/*     */     
/*  95 */     return b;
/*     */   }
/*     */   
/*     */   private static int removePlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection) throws CommandSyntaxException {
/*  99 */     UserWhiteList userWhiteList = paramCommandSourceStack.getServer().getPlayerList().getWhiteList();
/* 100 */     byte b = 0;
/*     */     
/* 102 */     for (NameAndId nameAndId : paramCollection) {
/* 103 */       if (userWhiteList.isWhiteListed(nameAndId)) {
/* 104 */         UserWhiteListEntry userWhiteListEntry = new UserWhiteListEntry(nameAndId);
/* 105 */         userWhiteList.remove((StoredUserEntry)userWhiteListEntry);
/* 106 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.remove.success", new Object[] { Component.literal(paramNameAndId.name()) }), true);
/* 107 */         b++;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (b == 0) {
/* 112 */       throw ERROR_NOT_WHITELISTED.create();
/*     */     }
/*     */     
/* 115 */     paramCommandSourceStack.getServer().kickUnlistedPlayers();
/* 116 */     return b;
/*     */   }
/*     */   
/*     */   private static int enableWhitelist(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 120 */     if (paramCommandSourceStack.getServer().isUsingWhitelist()) {
/* 121 */       throw ERROR_ALREADY_ENABLED.create();
/*     */     }
/* 123 */     paramCommandSourceStack.getServer().setUsingWhitelist(true);
/* 124 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.enabled"), true);
/* 125 */     paramCommandSourceStack.getServer().kickUnlistedPlayers();
/* 126 */     return 1;
/*     */   }
/*     */   
/*     */   private static int disableWhitelist(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException {
/* 130 */     if (!paramCommandSourceStack.getServer().isUsingWhitelist()) {
/* 131 */       throw ERROR_ALREADY_DISABLED.create();
/*     */     }
/* 133 */     paramCommandSourceStack.getServer().setUsingWhitelist(false);
/* 134 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.disabled"), true);
/* 135 */     return 1;
/*     */   }
/*     */   
/*     */   private static int showList(CommandSourceStack paramCommandSourceStack) {
/* 139 */     String[] arrayOfString = paramCommandSourceStack.getServer().getPlayerList().getWhiteListNames();
/* 140 */     if (arrayOfString.length == 0) {
/* 141 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.none"), false);
/*     */     } else {
/* 143 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.whitelist.list", new Object[] { Integer.valueOf(paramArrayOfString.length), String.join(", ", (CharSequence[])paramArrayOfString) }), false);
/*     */     } 
/* 145 */     return arrayOfString.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\WhitelistCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */