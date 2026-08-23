/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.UuidArgument;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ 
/*    */ public class ServerPackCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 24 */     paramCommandDispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("serverpack")
/* 26 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 27 */         .then(
/* 28 */           Commands.literal("push")
/* 29 */           .then((
/* 30 */             (RequiredArgumentBuilder)Commands.argument("url", (ArgumentType)StringArgumentType.string())
/* 31 */             .then((
/* 32 */               (RequiredArgumentBuilder)Commands.argument("uuid", (ArgumentType)UuidArgument.uuid())
/* 33 */               .then(
/* 34 */                 Commands.argument("hash", (ArgumentType)StringArgumentType.word())
/* 35 */                 .executes(paramCommandContext -> pushPack((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "url"), Optional.of(UuidArgument.getUuid(paramCommandContext, "uuid")), Optional.of(StringArgumentType.getString(paramCommandContext, "hash"))))))
/*    */               
/* 37 */               .executes(paramCommandContext -> pushPack((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "url"), Optional.of(UuidArgument.getUuid(paramCommandContext, "uuid")), Optional.empty()))))
/*    */             
/* 39 */             .executes(paramCommandContext -> pushPack((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "url"), Optional.empty(), Optional.empty())))))
/*    */ 
/*    */         
/* 42 */         .then(
/* 43 */           Commands.literal("pop")
/* 44 */           .then(
/* 45 */             Commands.argument("uuid", (ArgumentType)UuidArgument.uuid())
/* 46 */             .executes(paramCommandContext -> popPack((CommandSourceStack)paramCommandContext.getSource(), UuidArgument.getUuid(paramCommandContext, "uuid"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void sendToAllConnections(CommandSourceStack paramCommandSourceStack, Packet<?> paramPacket) {
/* 54 */     paramCommandSourceStack.getServer().getConnection().getConnections().forEach(paramConnection -> paramConnection.send(paramPacket));
/*    */   }
/*    */   
/*    */   private static int pushPack(CommandSourceStack paramCommandSourceStack, String paramString, Optional<UUID> paramOptional, Optional<String> paramOptional1) {
/* 58 */     UUID uUID = paramOptional.orElseGet(() -> UUID.nameUUIDFromBytes(paramString.getBytes(StandardCharsets.UTF_8)));
/* 59 */     String str = paramOptional1.orElse("");
/*    */     
/* 61 */     ClientboundResourcePackPushPacket clientboundResourcePackPushPacket = new ClientboundResourcePackPushPacket(uUID, paramString, str, false, null);
/* 62 */     sendToAllConnections(paramCommandSourceStack, (Packet<?>)clientboundResourcePackPushPacket);
/* 63 */     return 0;
/*    */   }
/*    */   
/*    */   private static int popPack(CommandSourceStack paramCommandSourceStack, UUID paramUUID) {
/* 67 */     ClientboundResourcePackPopPacket clientboundResourcePackPopPacket = new ClientboundResourcePackPopPacket(Optional.of(paramUUID));
/* 68 */     sendToAllConnections(paramCommandSourceStack, (Packet<?>)clientboundResourcePackPopPacket);
/* 69 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ServerPackCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */