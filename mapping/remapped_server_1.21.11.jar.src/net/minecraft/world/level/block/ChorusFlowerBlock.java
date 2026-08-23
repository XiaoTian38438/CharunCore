/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChorusFlowerBlock extends Block {
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("plant").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, ChorusFlowerBlock::new));
/*     */   }
/*     */   
/*     */   public static final MapCodec<ChorusFlowerBlock> CODEC;
/*     */   public static final int DEAD_AGE = 5;
/*     */   
/*     */   public MapCodec<ChorusFlowerBlock> codec() {
/*  32 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  36 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_5;
/*     */   
/*  38 */   private static final VoxelShape SHAPE_BLOCK_SUPPORT = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   private final Block plant;
/*     */   
/*     */   protected ChorusFlowerBlock(Block paramBlock, BlockBehaviour.Properties paramProperties) {
/*  43 */     super(paramProperties);
/*  44 */     this.plant = paramBlock;
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  50 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  51 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  57 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getBlockSupportShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  62 */     return SHAPE_BLOCK_SUPPORT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  68 */     BlockPos blockPos = paramBlockPos.above();
/*  69 */     if (!paramServerLevel.isEmptyBlock(blockPos) || blockPos.getY() > paramServerLevel.getMaxY()) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  74 */     if (i >= 5) {
/*     */       return;
/*     */     }
/*     */     
/*  78 */     boolean bool1 = false;
/*  79 */     boolean bool2 = false;
/*     */     
/*  81 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos.below());
/*  82 */     if (blockState.is(Blocks.END_STONE)) {
/*  83 */       bool1 = true;
/*  84 */     } else if (blockState.is(this.plant)) {
/*  85 */       byte b1 = 1;
/*  86 */       for (byte b2 = 0; b2 < 4; b2++) {
/*  87 */         BlockState blockState1 = paramServerLevel.getBlockState(paramBlockPos.below(b1 + 1));
/*  88 */         if (blockState1.is(this.plant)) {
/*  89 */           b1++;
/*     */         } else {
/*  91 */           if (blockState1.is(Blocks.END_STONE)) {
/*  92 */             bool2 = true;
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*  97 */       if (b1 < 2 || b1 <= paramRandomSource.nextInt(bool2 ? 5 : 4)) {
/*  98 */         bool1 = true;
/*     */       }
/* 100 */     } else if (blockState.isAir()) {
/* 101 */       bool1 = true;
/*     */     } 
/*     */     
/* 104 */     if (bool1 && allNeighborsEmpty((LevelReader)paramServerLevel, blockPos, (Direction)null) && paramServerLevel.isEmptyBlock(paramBlockPos.above(2))) {
/* 105 */       paramServerLevel.setBlock(paramBlockPos, ChorusPlantBlock.getStateWithConnections((BlockGetter)paramServerLevel, paramBlockPos, this.plant.defaultBlockState()), 2);
/* 106 */       placeGrownFlower((Level)paramServerLevel, blockPos, i);
/* 107 */     } else if (i < 4) {
/* 108 */       int j = paramRandomSource.nextInt(4);
/* 109 */       if (bool2) {
/* 110 */         j++;
/*     */       }
/*     */       
/* 113 */       boolean bool = false;
/* 114 */       for (byte b = 0; b < j; b++) {
/* 115 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 116 */         BlockPos blockPos1 = paramBlockPos.relative(direction);
/* 117 */         if (paramServerLevel.isEmptyBlock(blockPos1) && paramServerLevel.isEmptyBlock(blockPos1.below()) && allNeighborsEmpty((LevelReader)paramServerLevel, blockPos1, direction.getOpposite())) {
/* 118 */           placeGrownFlower((Level)paramServerLevel, blockPos1, i + 1);
/* 119 */           bool = true;
/*     */         } 
/*     */       } 
/*     */       
/* 123 */       if (bool) {
/* 124 */         paramServerLevel.setBlock(paramBlockPos, ChorusPlantBlock.getStateWithConnections((BlockGetter)paramServerLevel, paramBlockPos, this.plant.defaultBlockState()), 2);
/*     */       } else {
/* 126 */         placeDeadFlower((Level)paramServerLevel, paramBlockPos);
/*     */       } 
/*     */     } else {
/* 129 */       placeDeadFlower((Level)paramServerLevel, paramBlockPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeGrownFlower(Level paramLevel, BlockPos paramBlockPos, int paramInt) {
/* 134 */     paramLevel.setBlock(paramBlockPos, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(paramInt)), 2);
/* 135 */     paramLevel.levelEvent(1033, paramBlockPos, 0);
/*     */   }
/*     */   
/*     */   private void placeDeadFlower(Level paramLevel, BlockPos paramBlockPos) {
/* 139 */     paramLevel.setBlock(paramBlockPos, (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2);
/* 140 */     paramLevel.levelEvent(1034, paramBlockPos, 0);
/*     */   }
/*     */   
/*     */   private static boolean allNeighborsEmpty(LevelReader paramLevelReader, BlockPos paramBlockPos, Direction paramDirection) {
/* 144 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 145 */       if (direction != paramDirection && !paramLevelReader.isEmptyBlock(paramBlockPos.relative(direction))) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 154 */     if (paramDirection != Direction.UP && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 155 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*     */     
/* 158 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 163 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.below());
/* 164 */     if (blockState.is(this.plant) || blockState.is(Blocks.END_STONE)) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (!blockState.isAir()) {
/* 168 */       return false;
/*     */     }
/*     */     
/* 171 */     boolean bool = false;
/* 172 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 173 */       BlockState blockState1 = paramLevelReader.getBlockState(paramBlockPos.relative(direction));
/* 174 */       if (blockState1.is(this.plant)) {
/* 175 */         if (bool) {
/* 176 */           return false;
/*     */         }
/* 178 */         bool = true; continue;
/* 179 */       }  if (!blockState1.isAir()) {
/* 180 */         return false;
/*     */       }
/*     */     } 
/* 183 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 188 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */   
/*     */   public static void generatePlant(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, RandomSource paramRandomSource, int paramInt) {
/* 192 */     paramLevelAccessor.setBlock(paramBlockPos, ChorusPlantBlock.getStateWithConnections((BlockGetter)paramLevelAccessor, paramBlockPos, Blocks.CHORUS_PLANT.defaultBlockState()), 2);
/* 193 */     growTreeRecursive(paramLevelAccessor, paramBlockPos, paramRandomSource, paramBlockPos, paramInt, 0);
/*     */   }
/*     */   
/*     */   private static void growTreeRecursive(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos1, RandomSource paramRandomSource, BlockPos paramBlockPos2, int paramInt1, int paramInt2) {
/* 197 */     Block block = Blocks.CHORUS_PLANT;
/*     */     
/* 199 */     int i = paramRandomSource.nextInt(4) + 1;
/* 200 */     if (paramInt2 == 0) {
/* 201 */       i++;
/*     */     }
/*     */     byte b;
/* 204 */     for (b = 0; b < i; b++) {
/* 205 */       BlockPos blockPos = paramBlockPos1.above(b + 1);
/* 206 */       if (!allNeighborsEmpty((LevelReader)paramLevelAccessor, blockPos, (Direction)null)) {
/*     */         return;
/*     */       }
/*     */       
/* 210 */       paramLevelAccessor.setBlock(blockPos, ChorusPlantBlock.getStateWithConnections((BlockGetter)paramLevelAccessor, blockPos, block.defaultBlockState()), 2);
/* 211 */       paramLevelAccessor.setBlock(blockPos.below(), ChorusPlantBlock.getStateWithConnections((BlockGetter)paramLevelAccessor, blockPos.below(), block.defaultBlockState()), 2);
/*     */     } 
/*     */     
/* 214 */     b = 0;
/* 215 */     if (paramInt2 < 4) {
/* 216 */       int j = paramRandomSource.nextInt(4);
/* 217 */       if (paramInt2 == 0) {
/* 218 */         j++;
/*     */       }
/* 220 */       for (byte b1 = 0; b1 < j; b1++) {
/* 221 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 222 */         BlockPos blockPos = paramBlockPos1.above(i).relative(direction);
/* 223 */         if (Math.abs(blockPos.getX() - paramBlockPos2.getX()) < paramInt1 && Math.abs(blockPos.getZ() - paramBlockPos2.getZ()) < paramInt1)
/*     */         {
/*     */           
/* 226 */           if (paramLevelAccessor.isEmptyBlock(blockPos) && paramLevelAccessor.isEmptyBlock(blockPos.below()) && allNeighborsEmpty((LevelReader)paramLevelAccessor, blockPos, direction.getOpposite())) {
/* 227 */             b = 1;
/* 228 */             paramLevelAccessor.setBlock(blockPos, ChorusPlantBlock.getStateWithConnections((BlockGetter)paramLevelAccessor, blockPos, block.defaultBlockState()), 2);
/* 229 */             paramLevelAccessor.setBlock(blockPos.relative(direction.getOpposite()), ChorusPlantBlock.getStateWithConnections((BlockGetter)paramLevelAccessor, blockPos.relative(direction.getOpposite()), block.defaultBlockState()), 2);
/* 230 */             growTreeRecursive(paramLevelAccessor, blockPos, paramRandomSource, paramBlockPos2, paramInt1, paramInt2 + 1);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 235 */     if (b == 0) {
/* 236 */       paramLevelAccessor.setBlock(paramBlockPos1.above(i), (BlockState)Blocks.CHORUS_FLOWER.defaultBlockState().setValue((Property)AGE, Integer.valueOf(5)), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level paramLevel, BlockState paramBlockState, BlockHitResult paramBlockHitResult, Projectile paramProjectile) {
/* 242 */     BlockPos blockPos = paramBlockHitResult.getBlockPos();
/* 243 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel; if (paramProjectile.mayInteract(serverLevel, blockPos) && paramProjectile.mayBreak(serverLevel))
/* 244 */         paramLevel.destroyBlock(blockPos, true, (Entity)paramProjectile);  }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ChorusFlowerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */