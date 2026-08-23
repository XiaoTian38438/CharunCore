/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CoralWallFanBlock extends BaseCoralWallFanBlock {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CoralWallFanBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<CoralWallFanBlock> CODEC;
/*    */   private final Block deadBlock;
/*    */   
/*    */   public MapCodec<CoralWallFanBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected CoralWallFanBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/* 30 */     this.deadBlock = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 35 */     tryScheduleDieTick(paramBlockState1, (BlockGetter)paramLevel, (ScheduledTickAccess)paramLevel, paramLevel.random, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 40 */     if (!scanForWater(paramBlockState, (BlockGetter)paramServerLevel, paramBlockPos)) {
/* 41 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)((BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, paramBlockState.getValue((Property)FACING)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 47 */     if (paramDirection.getOpposite() == paramBlockState1.getValue((Property)FACING) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 48 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 51 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 52 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/* 54 */     tryScheduleDieTick(paramBlockState1, (BlockGetter)paramLevelReader, paramScheduledTickAccess, paramRandomSource, paramBlockPos1);
/*    */     
/* 56 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CoralWallFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */