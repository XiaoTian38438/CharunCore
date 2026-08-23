/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CropBlock extends VegetationBlock implements BonemealableBlock {
/*  26 */   public static final MapCodec<CropBlock> CODEC = simpleCodec(CropBlock::new);
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*     */   public MapCodec<? extends CropBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  34 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7; private static final VoxelShape[] SHAPES;
/*     */   static {
/*  36 */     SHAPES = Block.boxes(7, paramInt -> Block.column(16.0D, 0.0D, (2 + paramInt * 2)));
/*     */   }
/*     */   protected CropBlock(BlockBehaviour.Properties paramProperties) {
/*  39 */     super(paramProperties);
/*  40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)getAgeProperty(), Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  45 */     return SHAPES[getAge(paramBlockState)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  50 */     return paramBlockState.is(Blocks.FARMLAND);
/*     */   }
/*     */   
/*     */   protected IntegerProperty getAgeProperty() {
/*  54 */     return AGE;
/*     */   }
/*     */   
/*     */   public int getMaxAge() {
/*  58 */     return 7;
/*     */   }
/*     */   
/*     */   public int getAge(BlockState paramBlockState) {
/*  62 */     return ((Integer)paramBlockState.getValue((Property)getAgeProperty())).intValue();
/*     */   }
/*     */   
/*     */   public BlockState getStateForAge(int paramInt) {
/*  66 */     return (BlockState)defaultBlockState().setValue((Property)getAgeProperty(), Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public final boolean isMaxAge(BlockState paramBlockState) {
/*  70 */     return (getAge(paramBlockState) >= getMaxAge());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  75 */     return !isMaxAge(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  80 */     if (paramServerLevel.getRawBrightness(paramBlockPos, 0) >= 9) {
/*  81 */       int i = getAge(paramBlockState);
/*  82 */       if (i < getMaxAge()) {
/*  83 */         float f = getGrowthSpeed(this, (BlockGetter)paramServerLevel, paramBlockPos);
/*     */         
/*  85 */         if (paramRandomSource.nextInt((int)(25.0F / f) + 1) == 0) {
/*  86 */           paramServerLevel.setBlock(paramBlockPos, getStateForAge(i + 1), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void growCrops(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  93 */     int i = Math.min(getMaxAge(), getAge(paramBlockState) + getBonemealAgeIncrease(paramLevel));
/*  94 */     paramLevel.setBlock(paramBlockPos, getStateForAge(i), 2);
/*     */   }
/*     */   
/*     */   protected int getBonemealAgeIncrease(Level paramLevel) {
/*  98 */     return Mth.nextInt(paramLevel.random, 2, 5);
/*     */   }
/*     */   
/*     */   protected static float getGrowthSpeed(Block paramBlock, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 102 */     float f = 1.0F;
/*     */     
/* 104 */     BlockPos blockPos1 = paramBlockPos.below();
/* 105 */     for (byte b = -1; b <= 1; b++) {
/* 106 */       for (byte b1 = -1; b1 <= 1; b1++) {
/* 107 */         float f1 = 0.0F;
/*     */         
/* 109 */         BlockState blockState = paramBlockGetter.getBlockState(blockPos1.offset(b, 0, b1));
/* 110 */         if (blockState.is(Blocks.FARMLAND)) {
/* 111 */           f1 = 1.0F;
/* 112 */           if (((Integer)blockState.getValue((Property)FarmBlock.MOISTURE)).intValue() > 0) {
/* 113 */             f1 = 3.0F;
/*     */           }
/*     */         } 
/*     */         
/* 117 */         if (b != 0 || b1 != 0) {
/* 118 */           f1 /= 4.0F;
/*     */         }
/*     */         
/* 121 */         f += f1;
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     BlockPos blockPos2 = paramBlockPos.north();
/* 126 */     BlockPos blockPos3 = paramBlockPos.south();
/* 127 */     BlockPos blockPos4 = paramBlockPos.west();
/* 128 */     BlockPos blockPos5 = paramBlockPos.east();
/*     */     
/* 130 */     boolean bool1 = (paramBlockGetter.getBlockState(blockPos4).is(paramBlock) || paramBlockGetter.getBlockState(blockPos5).is(paramBlock)) ? true : false;
/* 131 */     boolean bool2 = (paramBlockGetter.getBlockState(blockPos2).is(paramBlock) || paramBlockGetter.getBlockState(blockPos3).is(paramBlock)) ? true : false;
/*     */     
/* 133 */     if (bool1 && bool2) {
/* 134 */       f /= 2.0F;
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 140 */       boolean bool = (paramBlockGetter.getBlockState(blockPos4.north()).is(paramBlock) || paramBlockGetter.getBlockState(blockPos5.north()).is(paramBlock) || paramBlockGetter.getBlockState(blockPos5.south()).is(paramBlock) || paramBlockGetter.getBlockState(blockPos4.south()).is(paramBlock)) ? true : false;
/*     */       
/* 142 */       if (bool) {
/* 143 */         f /= 2.0F;
/*     */       }
/*     */     } 
/*     */     
/* 147 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 152 */     return (hasSufficientLight(paramLevelReader, paramBlockPos) && super.canSurvive(paramBlockState, paramLevelReader, paramBlockPos));
/*     */   }
/*     */   
/*     */   protected static boolean hasSufficientLight(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 156 */     return (paramLevelReader.getRawBrightness(paramBlockPos, 0) >= 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/* 161 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramEntity instanceof net.minecraft.world.entity.monster.Ravager && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 162 */         serverLevel.destroyBlock(paramBlockPos, true, paramEntity);  }
/*     */     
/* 164 */     super.entityInside(paramBlockState, paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemLike getBaseSeedId() {
/* 169 */     return (ItemLike)Items.WHEAT_SEEDS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 174 */     return new ItemStack(getBaseSeedId());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 179 */     return !isMaxAge(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 189 */     growCrops((Level)paramServerLevel, paramBlockPos, paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 194 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CropBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */