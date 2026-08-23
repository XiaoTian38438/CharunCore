/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class NetherrackBlock extends Block implements BonemealableBlock {
/* 13 */   public static final MapCodec<NetherrackBlock> CODEC = simpleCodec(NetherrackBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<NetherrackBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public NetherrackBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 26 */     if (!paramLevelReader.getBlockState(paramBlockPos.above()).propagatesSkylightDown()) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos.offset(-1, -1, -1), paramBlockPos.offset(1, 1, 1))) {
/* 31 */       if (paramLevelReader.getBlockState(blockPos).is(BlockTags.NYLIUM)) {
/* 32 */         return true;
/*    */       }
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 45 */     boolean bool1 = false;
/* 46 */     boolean bool2 = false;
/* 47 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos.offset(-1, -1, -1), paramBlockPos.offset(1, 1, 1))) {
/* 48 */       BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 49 */       if (blockState.is(Blocks.WARPED_NYLIUM)) {
/* 50 */         bool2 = true;
/*    */       }
/*    */       
/* 53 */       if (blockState.is(Blocks.CRIMSON_NYLIUM)) {
/* 54 */         bool1 = true;
/*    */       }
/*    */       
/* 57 */       if (bool2 && bool1) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 62 */     if (bool2 && bool1) {
/* 63 */       paramServerLevel.setBlock(paramBlockPos, paramRandomSource.nextBoolean() ? Blocks.WARPED_NYLIUM.defaultBlockState() : Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/* 64 */     } else if (bool2) {
/* 65 */       paramServerLevel.setBlock(paramBlockPos, Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
/* 66 */     } else if (bool1) {
/* 67 */       paramServerLevel.setBlock(paramBlockPos, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 73 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\NetherrackBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */