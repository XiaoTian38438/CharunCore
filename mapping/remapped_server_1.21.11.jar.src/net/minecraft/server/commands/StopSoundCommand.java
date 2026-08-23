/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StopSoundCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 30 */     RequiredArgumentBuilder requiredArgumentBuilder = (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players()).executes(paramCommandContext -> stopSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), null, null))).then(
/* 31 */         Commands.literal("*")
/* 32 */         .then(
/* 33 */           Commands.argument("sound", (ArgumentType)IdentifierArgument.id())
/* 34 */           .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))
/* 35 */           .executes(paramCommandContext -> stopSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), null, IdentifierArgument.getId(paramCommandContext, "sound")))));
/*    */ 
/*    */ 
/*    */     
/* 39 */     for (SoundSource soundSource : SoundSource.values()) {
/* 40 */       requiredArgumentBuilder.then((
/* 41 */           (LiteralArgumentBuilder)Commands.literal(soundSource.getName())
/* 42 */           .executes(paramCommandContext -> stopSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), paramSoundSource, null)))
/* 43 */           .then(
/* 44 */             Commands.argument("sound", (ArgumentType)IdentifierArgument.id())
/* 45 */             .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))
/* 46 */             .executes(paramCommandContext -> stopSound((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), paramSoundSource, IdentifierArgument.getId(paramCommandContext, "sound")))));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 51 */     paramCommandDispatcher.register(
/* 52 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stopsound")
/* 53 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 54 */         .then((ArgumentBuilder)requiredArgumentBuilder));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int stopSound(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, SoundSource paramSoundSource, Identifier paramIdentifier) {
/* 61 */     ClientboundStopSoundPacket clientboundStopSoundPacket = new ClientboundStopSoundPacket(paramIdentifier, paramSoundSource);
/* 62 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 63 */       serverPlayer.connection.send((Packet)clientboundStopSoundPacket);
/*    */     }
/*    */     
/* 66 */     if (paramSoundSource != null) {
/* 67 */       if (paramIdentifier != null) {
/* 68 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.sound", new Object[] { Component.translationArg(paramIdentifier), paramSoundSource.getName() }), true);
/*    */       } else {
/* 70 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.any", new Object[] { paramSoundSource.getName() }), true);
/*    */       }
/*    */     
/* 73 */     } else if (paramIdentifier != null) {
/* 74 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.sound", new Object[] { Component.translationArg(paramIdentifier) }), true);
/*    */     } else {
/* 76 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.any"), true);
/*    */     } 
/*    */ 
/*    */     
/* 80 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\StopSoundCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */