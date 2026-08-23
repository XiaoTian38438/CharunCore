/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class SetSpawnCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 31 */     paramCommandDispatcher.register(
/* 32 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint")
/* 33 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 34 */         .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 35 */         .then((
/* 36 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 37 */           .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 38 */           .then((
/* 39 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 40 */             .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), BlockPosArgument.getSpawnablePos(paramCommandContext, "pos"), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 41 */             .then(
/* 42 */               Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/* 43 */               .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), BlockPosArgument.getSpawnablePos(paramCommandContext, "pos"), RotationArgument.getRotation(paramCommandContext, "rotation")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, BlockPos paramBlockPos, Coordinates paramCoordinates) {
/* 51 */     ResourceKey resourceKey = paramCommandSourceStack.getLevel().dimension();
/* 52 */     Vec2 vec2 = paramCoordinates.getRotation(paramCommandSourceStack);
/* 53 */     float f1 = Mth.wrapDegrees(vec2.y);
/* 54 */     float f2 = Mth.clamp(vec2.x, -90.0F, 90.0F);
/* 55 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 56 */       serverPlayer.setRespawnPosition(new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(resourceKey, paramBlockPos, f1, f2), true), false);
/*    */     }
/*    */     
/* 59 */     String str = resourceKey.identifier().toString();
/* 60 */     if (paramCollection.size() == 1) {
/* 61 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.single", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()), Float.valueOf(paramFloat1), Float.valueOf(paramFloat2), paramString, ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 63 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.multiple", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()), Float.valueOf(paramFloat1), Float.valueOf(paramFloat2), paramString, Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/*    */     
/* 66 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SetSpawnCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */