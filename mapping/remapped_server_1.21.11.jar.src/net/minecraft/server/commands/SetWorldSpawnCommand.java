/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*    */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.storage.LevelData;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ 
/*    */ public class SetWorldSpawnCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 23 */     paramCommandDispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setworldspawn")
/* 25 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 26 */         .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), BlockPos.containing((Position)((CommandSourceStack)paramCommandContext.getSource()).getPosition()), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 27 */         .then((
/* 28 */           (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 29 */           .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getSpawnablePos(paramCommandContext, "pos"), (Coordinates)WorldCoordinates.ZERO_ROTATION)))
/* 30 */           .then(
/* 31 */             Commands.argument("rotation", (ArgumentType)RotationArgument.rotation())
/* 32 */             .executes(paramCommandContext -> setSpawn((CommandSourceStack)paramCommandContext.getSource(), BlockPosArgument.getSpawnablePos(paramCommandContext, "pos"), RotationArgument.getRotation(paramCommandContext, "rotation"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack paramCommandSourceStack, BlockPos paramBlockPos, Coordinates paramCoordinates) {
/* 39 */     ServerLevel serverLevel = paramCommandSourceStack.getLevel();
/* 40 */     Vec2 vec2 = paramCoordinates.getRotation(paramCommandSourceStack);
/* 41 */     float f1 = vec2.y;
/* 42 */     float f2 = vec2.x;
/* 43 */     LevelData.RespawnData respawnData = LevelData.RespawnData.of(serverLevel.dimension(), paramBlockPos, f1, f2);
/* 44 */     serverLevel.setRespawnData(respawnData);
/* 45 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.setworldspawn.success", new Object[] { Integer.valueOf(paramBlockPos.getX()), Integer.valueOf(paramBlockPos.getY()), Integer.valueOf(paramBlockPos.getZ()), Float.valueOf(paramRespawnData.yaw()), Float.valueOf(paramRespawnData.pitch()), paramServerLevel.dimension().identifier().toString() }), true);
/* 46 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SetWorldSpawnCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */