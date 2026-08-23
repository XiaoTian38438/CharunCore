/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.chase.ChaseClient;
/*     */ import net.minecraft.server.chase.ChaseServer;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
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
/*     */ public class ChaseCommand
/*     */ {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_CONNECT_HOST = "localhost";
/*     */   
/*     */   private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
/*     */   
/*     */   private static final int DEFAULT_PORT = 10000;
/*     */   
/*     */   private static final int BROADCAST_INTERVAL_MS = 100;
/*  46 */   public static BiMap<String, ResourceKey<Level>> DIMENSION_NAMES = (BiMap<String, ResourceKey<Level>>)ImmutableBiMap.of("o", Level.OVERWORLD, "n", Level.NETHER, "e", Level.END);
/*     */ 
/*     */   
/*     */   private static ChaseServer chaseServer;
/*     */ 
/*     */   
/*     */   private static ChaseClient chaseClient;
/*     */ 
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/*  56 */     paramCommandDispatcher.register(
/*  57 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("chase")
/*  58 */         .then((
/*  59 */           (LiteralArgumentBuilder)Commands.literal("follow")
/*  60 */           .then(((RequiredArgumentBuilder)Commands.argument("host", (ArgumentType)StringArgumentType.string())
/*  61 */             .executes(paramCommandContext -> follow((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "host"), 10000)))
/*  62 */             .then(Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(1, 65535))
/*  63 */               .executes(paramCommandContext -> follow((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "host"), IntegerArgumentType.getInteger(paramCommandContext, "port"))))))
/*     */           
/*  65 */           .executes(paramCommandContext -> follow((CommandSourceStack)paramCommandContext.getSource(), "localhost", 10000))))
/*     */         
/*  67 */         .then((
/*  68 */           (LiteralArgumentBuilder)Commands.literal("lead")
/*  69 */           .then(((RequiredArgumentBuilder)Commands.argument("bind_address", (ArgumentType)StringArgumentType.string())
/*  70 */             .executes(paramCommandContext -> lead((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "bind_address"), 10000)))
/*  71 */             .then(Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(1024, 65535))
/*  72 */               .executes(paramCommandContext -> lead((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "bind_address"), IntegerArgumentType.getInteger(paramCommandContext, "port"))))))
/*     */           
/*  74 */           .executes(paramCommandContext -> lead((CommandSourceStack)paramCommandContext.getSource(), "0.0.0.0", 10000))))
/*     */         
/*  76 */         .then(
/*  77 */           Commands.literal("stop")
/*  78 */           .executes(paramCommandContext -> stop((CommandSourceStack)paramCommandContext.getSource()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int stop(CommandSourceStack paramCommandSourceStack) {
/*  84 */     if (chaseClient != null) {
/*  85 */       chaseClient.stop();
/*  86 */       paramCommandSourceStack.sendSuccess(() -> Component.literal("You have now stopped chasing"), false);
/*  87 */       chaseClient = null;
/*     */     } 
/*  89 */     if (chaseServer != null) {
/*  90 */       chaseServer.stop();
/*  91 */       paramCommandSourceStack.sendSuccess(() -> Component.literal("You are no longer being chased"), false);
/*  92 */       chaseServer = null;
/*     */     } 
/*  94 */     return 0;
/*     */   }
/*     */   
/*     */   private static boolean alreadyRunning(CommandSourceStack paramCommandSourceStack) {
/*  98 */     if (chaseServer != null) {
/*  99 */       paramCommandSourceStack.sendFailure((Component)Component.literal("Chase server is already running. Stop it using /chase stop"));
/* 100 */       return true;
/*     */     } 
/* 102 */     if (chaseClient != null) {
/* 103 */       paramCommandSourceStack.sendFailure((Component)Component.literal("You are already chasing someone. Stop it using /chase stop"));
/* 104 */       return true;
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   private static int lead(CommandSourceStack paramCommandSourceStack, String paramString, int paramInt) {
/* 110 */     if (alreadyRunning(paramCommandSourceStack)) {
/* 111 */       return 0;
/*     */     }
/*     */     
/* 114 */     chaseServer = new ChaseServer(paramString, paramInt, paramCommandSourceStack.getServer().getPlayerList(), 100);
/*     */     try {
/* 116 */       chaseServer.start();
/* 117 */       paramCommandSourceStack.sendSuccess(() -> Component.literal("Chase server is now running on port " + paramInt + ". Clients can follow you using /chase follow <ip> <port>"), false);
/* 118 */     } catch (IOException iOException) {
/* 119 */       LOGGER.error("Failed to start chase server", iOException);
/* 120 */       paramCommandSourceStack.sendFailure((Component)Component.literal("Failed to start chase server on port " + paramInt));
/* 121 */       chaseServer = null;
/*     */     } 
/* 123 */     return 0;
/*     */   }
/*     */   
/*     */   private static int follow(CommandSourceStack paramCommandSourceStack, String paramString, int paramInt) {
/* 127 */     if (alreadyRunning(paramCommandSourceStack)) {
/* 128 */       return 0;
/*     */     }
/*     */     
/* 131 */     chaseClient = new ChaseClient(paramString, paramInt, paramCommandSourceStack.getServer());
/* 132 */     chaseClient.start();
/* 133 */     paramCommandSourceStack.sendSuccess(() -> Component.literal("You are now chasing " + paramString + ":" + paramInt + ". If that server does '/chase lead' then you will automatically go to the same position. Use '/chase stop' to stop chasing."), false);
/* 134 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ChaseCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */