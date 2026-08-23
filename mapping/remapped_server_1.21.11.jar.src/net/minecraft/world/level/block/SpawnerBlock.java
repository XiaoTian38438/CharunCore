/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SpawnerBlock extends BaseEntityBlock {
/* 16 */   public static final MapCodec<SpawnerBlock> CODEC = simpleCodec(SpawnerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SpawnerBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SpawnerBlock(BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 29 */     return (BlockEntity)new SpawnerBlockEntity(paramBlockPos, paramBlockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level paramLevel, BlockState paramBlockState, BlockEntityType<T> paramBlockEntityType) {
/* 34 */     return createTickerHelper(paramBlockEntityType, BlockEntityType.MOB_SPAWNER, paramLevel.isClientSide() ? SpawnerBlockEntity::clientTick : SpawnerBlockEntity::serverTick);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnAfterBreak(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack, boolean paramBoolean) {
/* 39 */     super.spawnAfterBreak(paramBlockState, paramServerLevel, paramBlockPos, paramItemStack, paramBoolean);
/*    */     
/* 41 */     if (paramBoolean) {
/* 42 */       int i = 15 + paramServerLevel.random.nextInt(15) + paramServerLevel.random.nextInt(15);
/* 43 */       popExperience(paramServerLevel, paramBlockPos, i);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SpawnerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */