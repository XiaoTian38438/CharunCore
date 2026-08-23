/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ 
/*    */ public class DebugMobSpawningCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 19 */     LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal("debugmobspawning").requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/*    */     
/* 21 */     for (MobCategory mobCategory : MobCategory.values()) {
/* 22 */       literalArgumentBuilder.then(
/* 23 */           Commands.literal(mobCategory.getName())
/* 24 */           .then(
/* 25 */             Commands.argument("at", (ArgumentType)BlockPosArgument.blockPos())
/* 26 */             .executes(paramCommandContext -> spawnMobs((CommandSourceStack)paramCommandContext.getSource(), paramMobCategory, BlockPosArgument.getLoadedBlockPos(paramCommandContext, "at")))));
/*    */     }
/*    */ 
/*    */     
/* 30 */     paramCommandDispatcher.register(literalArgumentBuilder);
/*    */   }
/*    */   
/*    */   private static int spawnMobs(CommandSourceStack paramCommandSourceStack, MobCategory paramMobCategory, BlockPos paramBlockPos) {
/* 34 */     NaturalSpawner.spawnCategoryForPosition(paramMobCategory, paramCommandSourceStack.getLevel(), paramBlockPos);
/* 35 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DebugMobSpawningCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */