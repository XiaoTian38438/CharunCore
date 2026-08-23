/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TitleCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/*  32 */     paramCommandDispatcher.register(
/*  33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("title")
/*  34 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  35 */         .then((
/*  36 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  37 */           .then(
/*  38 */             Commands.literal("clear")
/*  39 */             .executes(paramCommandContext -> clearTitle((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets")))))
/*     */           
/*  41 */           .then(
/*  42 */             Commands.literal("reset")
/*  43 */             .executes(paramCommandContext -> resetTitle((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets")))))
/*     */           
/*  45 */           .then(
/*  46 */             Commands.literal("title")
/*  47 */             .then(
/*  48 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  49 */               .executes(paramCommandContext -> showTitle((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ComponentArgument.getRawComponent(paramCommandContext, "title"), "title", net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket::new)))))
/*     */ 
/*     */           
/*  52 */           .then(
/*  53 */             Commands.literal("subtitle")
/*  54 */             .then(
/*  55 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  56 */               .executes(paramCommandContext -> showTitle((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ComponentArgument.getRawComponent(paramCommandContext, "title"), "subtitle", net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket::new)))))
/*     */ 
/*     */           
/*  59 */           .then(
/*  60 */             Commands.literal("actionbar")
/*  61 */             .then(
/*  62 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(paramCommandBuildContext))
/*  63 */               .executes(paramCommandContext -> showTitle((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ComponentArgument.getRawComponent(paramCommandContext, "title"), "actionbar", net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket::new)))))
/*     */ 
/*     */           
/*  66 */           .then(
/*  67 */             Commands.literal("times")
/*  68 */             .then(
/*  69 */               Commands.argument("fadeIn", (ArgumentType)TimeArgument.time())
/*  70 */               .then(
/*  71 */                 Commands.argument("stay", (ArgumentType)TimeArgument.time())
/*  72 */                 .then(
/*  73 */                   Commands.argument("fadeOut", (ArgumentType)TimeArgument.time())
/*  74 */                   .executes(paramCommandContext -> setTimes((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), IntegerArgumentType.getInteger(paramCommandContext, "fadeIn"), IntegerArgumentType.getInteger(paramCommandContext, "stay"), IntegerArgumentType.getInteger(paramCommandContext, "fadeOut")))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int clearTitle(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection) {
/*  84 */     ClientboundClearTitlesPacket clientboundClearTitlesPacket = new ClientboundClearTitlesPacket(false);
/*  85 */     for (ServerPlayer serverPlayer : paramCollection) {
/*  86 */       serverPlayer.connection.send((Packet)clientboundClearTitlesPacket);
/*     */     }
/*     */     
/*  89 */     if (paramCollection.size() == 1) {
/*  90 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.cleared.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  92 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.cleared.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/*  95 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int resetTitle(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection) {
/*  99 */     ClientboundClearTitlesPacket clientboundClearTitlesPacket = new ClientboundClearTitlesPacket(true);
/* 100 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 101 */       serverPlayer.connection.send((Packet)clientboundClearTitlesPacket);
/*     */     }
/*     */     
/* 104 */     if (paramCollection.size() == 1) {
/* 105 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.reset.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 107 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.reset.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 110 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int showTitle(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Component paramComponent, String paramString, Function<Component, Packet<?>> paramFunction) throws CommandSyntaxException {
/* 114 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 115 */       serverPlayer.connection.send(paramFunction.apply(ComponentUtils.updateForEntity(paramCommandSourceStack, paramComponent, (Entity)serverPlayer, 0)));
/*     */     }
/*     */     
/* 118 */     if (paramCollection.size() == 1) {
/* 119 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.show." + paramString + ".single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 121 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.show." + paramString + ".multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 124 */     return paramCollection.size();
/*     */   }
/*     */   
/*     */   private static int setTimes(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, int paramInt1, int paramInt2, int paramInt3) {
/* 128 */     ClientboundSetTitlesAnimationPacket clientboundSetTitlesAnimationPacket = new ClientboundSetTitlesAnimationPacket(paramInt1, paramInt2, paramInt3);
/* 129 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 130 */       serverPlayer.connection.send((Packet)clientboundSetTitlesAnimationPacket);
/*     */     }
/*     */     
/* 133 */     if (paramCollection.size() == 1) {
/* 134 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.times.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 136 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.title.times.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*     */     } 
/*     */     
/* 139 */     return paramCollection.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TitleCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */