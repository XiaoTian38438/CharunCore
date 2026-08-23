/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BeetrootBlock extends CropBlock {
/* 19 */   public static final MapCodec<BeetrootBlock> CODEC = simpleCodec(BeetrootBlock::new);
/*    */   public static final int MAX_AGE = 3;
/*    */   
/*    */   public MapCodec<BeetrootBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3; private static final VoxelShape[] SHAPES;
/*    */   static {
/* 29 */     SHAPES = Block.boxes(3, paramInt -> Block.column(16.0D, 0.0D, (2 + paramInt * 2)));
/*    */   }
/*    */   public BeetrootBlock(BlockBehaviour.Properties paramProperties) {
/* 32 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IntegerProperty getAgeProperty() {
/* 37 */     return AGE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxAge() {
/* 42 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemLike getBaseSeedId() {
/* 47 */     return (ItemLike)Items.BEETROOT_SEEDS;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 53 */     if (paramRandomSource.nextInt(3) != 0) {
/* 54 */       super.randomTick(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBonemealAgeIncrease(Level paramLevel) {
/* 60 */     return super.getBonemealAgeIncrease(paramLevel) / 3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 65 */     paramBuilder.add(new Property[] { (Property)AGE });
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 70 */     return SHAPES[getAge(paramBlockState)];
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BeetrootBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */