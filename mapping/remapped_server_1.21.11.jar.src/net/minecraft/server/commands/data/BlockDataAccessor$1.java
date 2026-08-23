/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements DataCommands.DataProvider
/*    */ {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException {
/* 38 */     BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(paramCommandContext, argPrefix + "Pos");
/* 39 */     BlockEntity blockEntity = ((CommandSourceStack)paramCommandContext.getSource()).getLevel().getBlockEntity(blockPos);
/* 40 */     if (blockEntity == null) {
/* 41 */       throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
/*    */     }
/* 43 */     return new BlockDataAccessor(blockEntity, blockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> paramFunction) {
/* 48 */     return paramArgumentBuilder.then(Commands.literal("block").then(paramFunction.apply(Commands.argument(argPrefix + "Pos", (ArgumentType)BlockPosArgument.blockPos()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\data\BlockDataAccessor$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */