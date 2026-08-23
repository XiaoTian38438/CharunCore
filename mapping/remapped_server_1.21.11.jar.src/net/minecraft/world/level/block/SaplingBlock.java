/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.grower.TreeGrower;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SaplingBlock extends VegetationBlock implements BonemealableBlock {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)TreeGrower.CODEC.fieldOf("tree").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, SaplingBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SaplingBlock> CODEC;
/*    */   
/*    */   public MapCodec<? extends SaplingBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*    */   
/* 32 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 12.0D);
/*    */   
/*    */   protected final TreeGrower treeGrower;
/*    */   
/*    */   protected SaplingBlock(TreeGrower paramTreeGrower, BlockBehaviour.Properties paramProperties) {
/* 37 */     super(paramProperties);
/* 38 */     this.treeGrower = paramTreeGrower;
/* 39 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STAGE, Integer.valueOf(0)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 44 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 49 */     if (paramServerLevel.getMaxLocalRawBrightness(paramBlockPos.above()) >= 9 && 
/* 50 */       paramRandomSource.nextInt(7) == 0) {
/* 51 */       advanceTree(paramServerLevel, paramBlockPos, paramBlockState, paramRandomSource);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void advanceTree(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, RandomSource paramRandomSource) {
/* 57 */     if (((Integer)paramBlockState.getValue((Property)STAGE)).intValue() == 0) {
/* 58 */       paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.cycle((Property)STAGE), 260);
/*    */     } else {
/* 60 */       this.treeGrower.growTree(paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramBlockPos, paramBlockState, paramRandomSource);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 71 */     return (paramLevel.random.nextFloat() < 0.45D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 76 */     advanceTree(paramServerLevel, paramBlockPos, paramBlockState, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 81 */     paramBuilder.add(new Property[] { (Property)STAGE });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SaplingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */