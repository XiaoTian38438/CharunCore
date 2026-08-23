/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.HashSet;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
/*     */ 
/*     */ public class DebugConfigCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  34 */     paramCommandDispatcher.register(
/*  35 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugconfig")
/*  36 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/*  37 */         .then(
/*  38 */           Commands.literal("config")
/*  39 */           .then(
/*  40 */             Commands.argument("target", (ArgumentType)EntityArgument.player())
/*  41 */             .executes(paramCommandContext -> config((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayer(paramCommandContext, "target"))))))
/*     */ 
/*     */         
/*  44 */         .then(
/*  45 */           Commands.literal("unconfig")
/*  46 */           .then(
/*  47 */             Commands.argument("target", (ArgumentType)UuidArgument.uuid())
/*  48 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(getUuidsInConfig(((CommandSourceStack)paramCommandContext.getSource()).getServer()), paramSuggestionsBuilder))
/*  49 */             .executes(paramCommandContext -> unconfig((CommandSourceStack)paramCommandContext.getSource(), UuidArgument.getUuid(paramCommandContext, "target"))))))
/*     */ 
/*     */         
/*  52 */         .then(
/*  53 */           Commands.literal("dialog")
/*  54 */           .then(
/*  55 */             Commands.argument("target", (ArgumentType)UuidArgument.uuid())
/*  56 */             .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(getUuidsInConfig(((CommandSourceStack)paramCommandContext.getSource()).getServer()), paramSuggestionsBuilder))
/*  57 */             .then(
/*  58 */               Commands.argument("dialog", (ArgumentType)ResourceOrIdArgument.dialog(paramCommandBuildContext))
/*  59 */               .executes(paramCommandContext -> showDialog((CommandSourceStack)paramCommandContext.getSource(), UuidArgument.getUuid(paramCommandContext, "target"), ResourceOrIdArgument.getDialog(paramCommandContext, "dialog")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Iterable<String> getUuidsInConfig(MinecraftServer paramMinecraftServer) {
/*  67 */     HashSet<String> hashSet = new HashSet();
/*  68 */     for (Connection connection : paramMinecraftServer.getConnection().getConnections()) {
/*  69 */       PacketListener packetListener = connection.getPacketListener(); if (packetListener instanceof ServerConfigurationPacketListenerImpl) { ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl = (ServerConfigurationPacketListenerImpl)packetListener;
/*  70 */         hashSet.add(serverConfigurationPacketListenerImpl.getOwner().id().toString()); }
/*     */     
/*     */     } 
/*  73 */     return hashSet;
/*     */   }
/*     */   
/*     */   private static int config(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer) {
/*  77 */     GameProfile gameProfile = paramServerPlayer.getGameProfile();
/*  78 */     paramServerPlayer.connection.switchToConfig();
/*  79 */     paramCommandSourceStack.sendSuccess(() -> Component.literal("Switched player " + paramGameProfile.name() + "(" + String.valueOf(paramGameProfile.id()) + ") to config mode"), false);
/*  80 */     return 1;
/*     */   }
/*     */   
/*     */   private static ServerConfigurationPacketListenerImpl findConfigPlayer(MinecraftServer paramMinecraftServer, UUID paramUUID) {
/*  84 */     for (Connection connection : paramMinecraftServer.getConnection().getConnections()) {
/*  85 */       PacketListener packetListener = connection.getPacketListener(); if (packetListener instanceof ServerConfigurationPacketListenerImpl) { ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl = (ServerConfigurationPacketListenerImpl)packetListener;
/*  86 */         if (serverConfigurationPacketListenerImpl.getOwner().id().equals(paramUUID)) {
/*  87 */           return serverConfigurationPacketListenerImpl;
/*     */         } }
/*     */     
/*     */     } 
/*     */     
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   private static int unconfig(CommandSourceStack paramCommandSourceStack, UUID paramUUID) {
/*  96 */     ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl = findConfigPlayer(paramCommandSourceStack.getServer(), paramUUID);
/*     */     
/*  98 */     if (serverConfigurationPacketListenerImpl != null) {
/*  99 */       serverConfigurationPacketListenerImpl.returnToWorld();
/* 100 */       return 1;
/*     */     } 
/* 102 */     paramCommandSourceStack.sendFailure((Component)Component.literal("Can't find player to unconfig"));
/* 103 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int showDialog(CommandSourceStack paramCommandSourceStack, UUID paramUUID, Holder<Dialog> paramHolder) {
/* 108 */     ServerConfigurationPacketListenerImpl serverConfigurationPacketListenerImpl = findConfigPlayer(paramCommandSourceStack.getServer(), paramUUID);
/*     */     
/* 110 */     if (serverConfigurationPacketListenerImpl != null) {
/* 111 */       serverConfigurationPacketListenerImpl.send((Packet)new ClientboundShowDialogPacket(paramHolder));
/* 112 */       return 1;
/*     */     } 
/* 114 */     paramCommandSourceStack.sendFailure((Component)Component.literal("Can't find player to talk to"));
/* 115 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DebugConfigCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */