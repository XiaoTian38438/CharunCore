/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class SpectateCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_NOT_SPECTATOR;
/*    */   private static final DynamicCommandExceptionType ERROR_CANNOT_SPECTATE;
/* 22 */   private static final SimpleCommandExceptionType ERROR_SELF = new SimpleCommandExceptionType((Message)Component.translatable("commands.spectate.self")); static {
/* 23 */     ERROR_NOT_SPECTATOR = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.spectate.not_spectator", new Object[] { paramObject }));
/* 24 */     ERROR_CANNOT_SPECTATE = new DynamicCommandExceptionType(paramObject -> Component.translatableEscape("commands.spectate.cannot_spectate", new Object[] { paramObject }));
/*    */   }
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 27 */     paramCommandDispatcher.register(
/* 28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spectate")
/* 29 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 30 */         .executes(paramCommandContext -> spectate((CommandSourceStack)paramCommandContext.getSource(), null, ((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException())))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)EntityArgument.entity())
/* 33 */           .executes(paramCommandContext -> spectate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), ((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException())))
/* 34 */           .then(
/* 35 */             Commands.argument("player", (ArgumentType)EntityArgument.player())
/* 36 */             .executes(paramCommandContext -> spectate((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getEntity(paramCommandContext, "target"), EntityArgument.getPlayer(paramCommandContext, "player"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int spectate(CommandSourceStack paramCommandSourceStack, Entity paramEntity, ServerPlayer paramServerPlayer) throws CommandSyntaxException {
/* 43 */     if (paramServerPlayer == paramEntity)
/* 44 */       throw ERROR_SELF.create(); 
/* 45 */     if (!paramServerPlayer.isSpectator())
/* 46 */       throw ERROR_NOT_SPECTATOR.create(paramServerPlayer.getDisplayName()); 
/* 47 */     if (paramEntity != null && paramEntity.getType().clientTrackingRange() == 0) {
/* 48 */       throw ERROR_CANNOT_SPECTATE.create(paramEntity.getDisplayName());
/*    */     }
/*    */     
/* 51 */     paramServerPlayer.setCamera(paramEntity);
/* 52 */     if (paramEntity != null) {
/* 53 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.spectate.success.started", new Object[] { paramEntity.getDisplayName() }), false);
/*    */     } else {
/* 55 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.spectate.success.stopped"), false);
/*    */     } 
/* 57 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SpectateCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */