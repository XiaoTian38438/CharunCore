/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CoralPlantBlock extends BaseCoralPlantTypeBlock {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, CoralPlantBlock::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<CoralPlantBlock> CODEC;
/*    */   private final Block deadBlock;
/*    */   
/*    */   public MapCodec<CoralPlantBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected CoralPlantBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/* 34 */     super(paramProperties);
/* 35 */     this.deadBlock = paramBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 40 */     tryScheduleDieTick(paramBlockState1, (BlockGetter)paramLevel, (ScheduledTickAccess)paramLevel, paramLevel.random, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 45 */     if (!scanForWater(paramBlockState, (BlockGetter)paramServerLevel, paramBlockPos)) {
/* 46 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 52 */     if (paramDirection == Direction.DOWN && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 53 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 56 */     tryScheduleDieTick(paramBlockState1, (BlockGetter)paramLevelReader, paramScheduledTickAccess, paramRandomSource, paramBlockPos1);
/*    */     
/* 58 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 59 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*    */     }
/*    */     
/* 62 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 67 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CoralPlantBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */