/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public interface CaveVines
/*    */ {
/* 24 */   public static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*    */   
/* 26 */   public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
/*    */   
/*    */   static InteractionResult use(Entity paramEntity, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 29 */     if (((Boolean)paramBlockState.getValue((Property)BERRIES)).booleanValue()) {
/* 30 */       if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/* 31 */         Block.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.HARVEST_CAVE_VINE, paramBlockState, paramLevel
/*    */ 
/*    */ 
/*    */             
/* 35 */             .getBlockEntity(paramBlockPos), null, paramEntity, (paramServerLevel, paramItemStack) -> Block.popResource((Level)paramServerLevel, paramBlockPos, paramItemStack));
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 40 */         float f = Mth.randomBetween(serverLevel.random, 0.8F, 1.2F);
/* 41 */         serverLevel.playSound(null, paramBlockPos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
/* 42 */         BlockState blockState = (BlockState)paramBlockState.setValue((Property)BERRIES, Boolean.valueOf(false));
/* 43 */         serverLevel.setBlock(paramBlockPos, blockState, 2);
/* 44 */         serverLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(paramEntity, blockState)); }
/*    */       
/* 46 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 48 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   static boolean hasGlowBerries(BlockState paramBlockState) {
/* 52 */     return (paramBlockState.hasProperty((Property)BERRIES) && ((Boolean)paramBlockState.getValue((Property)BERRIES)).booleanValue());
/*    */   }
/*    */   
/*    */   static ToIntFunction<BlockState> emission(int paramInt) {
/* 56 */     return paramBlockState -> ((Boolean)paramBlockState.getValue((Property)BlockStateProperties.BERRIES)).booleanValue() ? paramInt : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CaveVines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */