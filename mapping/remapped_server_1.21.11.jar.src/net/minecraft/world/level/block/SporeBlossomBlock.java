/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SporeBlossomBlock extends Block {
/* 18 */   public static final MapCodec<SporeBlossomBlock> CODEC = simpleCodec(SporeBlossomBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SporeBlossomBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(12.0D, 13.0D, 16.0D);
/*    */   
/*    */   private static final int ADD_PARTICLE_ATTEMPTS = 14;
/*    */   private static final int PARTICLE_XZ_RADIUS = 10;
/*    */   private static final int PARTICLE_Y_MAX = 10;
/*    */   
/*    */   public SporeBlossomBlock(BlockBehaviour.Properties paramProperties) {
/* 32 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 37 */     return (Block.canSupportCenter(paramLevelReader, paramBlockPos.above(), Direction.DOWN) && !paramLevelReader.isWaterAt(paramBlockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 42 */     if (paramDirection == Direction.UP && !canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/* 43 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 45 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 50 */     int i = paramBlockPos.getX();
/* 51 */     int j = paramBlockPos.getY();
/* 52 */     int k = paramBlockPos.getZ();
/*    */     
/* 54 */     double d1 = i + paramRandomSource.nextDouble();
/* 55 */     double d2 = j + 0.7D;
/* 56 */     double d3 = k + paramRandomSource.nextDouble();
/*    */     
/* 58 */     paramLevel.addParticle((ParticleOptions)ParticleTypes.FALLING_SPORE_BLOSSOM, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*    */     
/* 60 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 61 */     for (byte b = 0; b < 14; b++) {
/* 62 */       mutableBlockPos.set(i + Mth.nextInt(paramRandomSource, -10, 10), j - paramRandomSource.nextInt(10), k + Mth.nextInt(paramRandomSource, -10, 10));
/* 63 */       BlockState blockState = paramLevel.getBlockState((BlockPos)mutableBlockPos);
/* 64 */       if (!blockState.isCollisionShapeFullBlock((BlockGetter)paramLevel, (BlockPos)mutableBlockPos)) {
/* 65 */         paramLevel.addParticle((ParticleOptions)ParticleTypes.SPORE_BLOSSOM_AIR, mutableBlockPos.getX() + paramRandomSource.nextDouble(), mutableBlockPos.getY() + paramRandomSource.nextDouble(), mutableBlockPos.getZ() + paramRandomSource.nextDouble(), 0.0D, 0.0D, 0.0D);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 72 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SporeBlossomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */