/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class FrostedIceBlock extends IceBlock {
/*  22 */   public static final MapCodec<FrostedIceBlock> CODEC = simpleCodec(FrostedIceBlock::new);
/*     */   public static final int MAX_AGE = 3;
/*     */   
/*     */   public MapCodec<FrostedIceBlock> codec() {
/*  26 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  30 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
/*     */   
/*     */   private static final int NEIGHBORS_TO_AGE = 4;
/*     */   private static final int NEIGHBORS_TO_MELT = 2;
/*     */   
/*     */   public FrostedIceBlock(BlockBehaviour.Properties paramProperties) {
/*  36 */     super(paramProperties);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  42 */     paramLevel.scheduleTick(paramBlockPos, this, Mth.nextInt(paramLevel.getRandom(), 60, 120));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  47 */     if (paramRandomSource.nextInt(3) == 0 || fewerNeigboursThan((BlockGetter)paramServerLevel, paramBlockPos, 4)) {
/*  48 */       int i = (paramServerLevel.dimension() == Level.END) ? paramServerLevel.getBrightness(LightLayer.BLOCK, paramBlockPos) : paramServerLevel.getMaxLocalRawBrightness(paramBlockPos);
/*  49 */       if (i > 11 - ((Integer)paramBlockState.getValue((Property)AGE)).intValue() - paramBlockState.getLightBlock() && slightlyMelt(paramBlockState, (Level)paramServerLevel, paramBlockPos)) {
/*  50 */         BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  51 */         for (Direction direction : Direction.values()) {
/*  52 */           mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/*  53 */           BlockState blockState = paramServerLevel.getBlockState((BlockPos)mutableBlockPos);
/*  54 */           if (blockState.is(this) && !slightlyMelt(blockState, (Level)paramServerLevel, (BlockPos)mutableBlockPos)) {
/*  55 */             paramServerLevel.scheduleTick((BlockPos)mutableBlockPos, this, Mth.nextInt(paramRandomSource, 20, 40));
/*     */           }
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*  61 */     paramServerLevel.scheduleTick(paramBlockPos, this, Mth.nextInt(paramRandomSource, 20, 40));
/*     */   }
/*     */   
/*     */   private boolean slightlyMelt(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  65 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  66 */     if (i < 3) {
/*  67 */       paramLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1)), 2);
/*  68 */       return false;
/*     */     } 
/*  70 */     melt(paramBlockState, paramLevel, paramBlockPos);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/*  77 */     if (paramBlock.defaultBlockState().is(this) && 
/*  78 */       fewerNeigboursThan((BlockGetter)paramLevel, paramBlockPos, 2)) {
/*  79 */       melt(paramBlockState, paramLevel, paramBlockPos);
/*     */     }
/*     */ 
/*     */     
/*  83 */     super.neighborChanged(paramBlockState, paramLevel, paramBlockPos, paramBlock, paramOrientation, paramBoolean);
/*     */   }
/*     */   
/*     */   private boolean fewerNeigboursThan(BlockGetter paramBlockGetter, BlockPos paramBlockPos, int paramInt) {
/*  87 */     byte b = 0;
/*  88 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  89 */     for (Direction direction : Direction.values()) {
/*  90 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction);
/*     */       
/*  92 */       b++;
/*  93 */       if (paramBlockGetter.getBlockState((BlockPos)mutableBlockPos).is(this) && b >= paramInt) {
/*  94 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 103 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 108 */     return ItemStack.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FrostedIceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */