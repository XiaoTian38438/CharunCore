/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SoulFireBlock extends BaseFireBlock {
/* 13 */   public static final MapCodec<SoulFireBlock> CODEC = simpleCodec(SoulFireBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SoulFireBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SoulFireBlock(BlockBehaviour.Properties paramProperties) {
/* 21 */     super(paramProperties, 2.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 26 */     if (canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 27 */       return defaultBlockState();
/*    */     }
/*    */     
/* 30 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 35 */     return canSurviveOnBlock(paramLevelReader.getBlockState(paramBlockPos.below()));
/*    */   }
/*    */   
/*    */   public static boolean canSurviveOnBlock(BlockState paramBlockState) {
/* 39 */     return paramBlockState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canBurn(BlockState paramBlockState) {
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SoulFireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */