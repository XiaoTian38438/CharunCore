/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class CherryTrunkPlacer extends TrunkPlacer {
/*     */   static {
/*  24 */     BRANCH_START_CODEC = UniformInt.CODEC.codec().validate(paramUniformInt -> (paramUniformInt.getMaxValue() - paramUniformInt.getMinValue() < 1) ? DataResult.error(()) : DataResult.success(paramUniformInt));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  31 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).and(paramInstance.group((App)IntProvider.codec(1, 3).fieldOf("branch_count").forGetter(()), (App)IntProvider.codec(2, 16).fieldOf("branch_horizontal_length").forGetter(()), (App)IntProvider.validateCodec(-16, 0, BRANCH_START_CODEC).fieldOf("branch_start_offset_from_top").forGetter(()), (App)IntProvider.codec(-16, 16).fieldOf("branch_end_offset_from_top").forGetter(()))).apply((Applicative)paramInstance, CherryTrunkPlacer::new));
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Codec<UniformInt> BRANCH_START_CODEC;
/*     */   
/*     */   public static final MapCodec<CherryTrunkPlacer> CODEC;
/*     */   
/*     */   private final IntProvider branchCount;
/*     */   private final IntProvider branchHorizontalLength;
/*     */   private final UniformInt branchStartOffsetFromTop;
/*     */   private final UniformInt secondBranchStartOffsetFromTop;
/*     */   private final IntProvider branchEndOffsetFromTop;
/*     */   
/*     */   public CherryTrunkPlacer(int paramInt1, int paramInt2, int paramInt3, IntProvider paramIntProvider1, IntProvider paramIntProvider2, UniformInt paramUniformInt, IntProvider paramIntProvider3) {
/*  46 */     super(paramInt1, paramInt2, paramInt3);
/*  47 */     this.branchCount = paramIntProvider1;
/*  48 */     this.branchHorizontalLength = paramIntProvider2;
/*  49 */     this.branchStartOffsetFromTop = paramUniformInt;
/*  50 */     this.secondBranchStartOffsetFromTop = UniformInt.of(paramUniformInt.getMinValue(), paramUniformInt.getMaxValue() - 1);
/*  51 */     this.branchEndOffsetFromTop = paramIntProvider3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  56 */     return TrunkPlacerType.CHERRY_TRUNK_PLACER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/*     */     int m;
/*  68 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.below(), paramTreeConfiguration);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     int i = Math.max(0, paramInt - 1 + this.branchStartOffsetFromTop.sample(paramRandomSource));
/*  80 */     int j = Math.max(0, paramInt - 1 + this.secondBranchStartOffsetFromTop.sample(paramRandomSource));
/*  81 */     if (j >= i) {
/*  82 */       j++;
/*     */     }
/*     */     
/*  85 */     int k = this.branchCount.sample(paramRandomSource);
/*  86 */     boolean bool1 = (k == 3) ? true : false;
/*  87 */     boolean bool2 = (k >= 2) ? true : false;
/*     */ 
/*     */     
/*  90 */     if (bool1) {
/*  91 */       m = paramInt;
/*  92 */     } else if (bool2) {
/*  93 */       m = Math.max(i, j) + 1;
/*     */     } else {
/*  95 */       m = i + 1;
/*     */     } 
/*     */     
/*  98 */     for (byte b = 0; b < m; b++) {
/*  99 */       placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.above(b), paramTreeConfiguration);
/*     */     }
/*     */     
/* 102 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = new ArrayList();
/*     */     
/* 104 */     if (bool1) {
/* 105 */       arrayList.add(new FoliagePlacer.FoliageAttachment(paramBlockPos.above(m), 0, false));
/*     */     }
/*     */     
/* 108 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 109 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 110 */     Function<BlockState, BlockState> function = paramBlockState -> (BlockState)paramBlockState.trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)paramDirection.getAxis());
/*     */     
/* 112 */     arrayList.add(generateBranch(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramInt, paramBlockPos, paramTreeConfiguration, function, direction, i, (i < m - 1), mutableBlockPos));
/*     */     
/* 114 */     if (bool2) {
/* 115 */       arrayList.add(generateBranch(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramInt, paramBlockPos, paramTreeConfiguration, function, direction.getOpposite(), j, (j < m - 1), mutableBlockPos));
/*     */     }
/*     */     
/* 118 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FoliagePlacer.FoliageAttachment generateBranch(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt1, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration, Function<BlockState, BlockState> paramFunction, Direction paramDirection, int paramInt2, boolean paramBoolean, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 134 */     paramMutableBlockPos.set((Vec3i)paramBlockPos).move(Direction.UP, paramInt2);
/*     */     
/* 136 */     int i = paramInt1 - 1 + this.branchEndOffsetFromTop.sample(paramRandomSource);
/*     */     
/* 138 */     boolean bool = (paramBoolean || i < paramInt2) ? true : false;
/* 139 */     int j = this.branchHorizontalLength.sample(paramRandomSource) + (bool ? 1 : 0);
/*     */ 
/*     */     
/* 142 */     BlockPos blockPos = paramBlockPos.relative(paramDirection, j).above(i);
/*     */     
/* 144 */     byte b1 = bool ? 2 : 1;
/*     */     
/* 146 */     for (byte b2 = 0; b2 < b1; b2++) {
/* 147 */       placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)paramMutableBlockPos.move(paramDirection), paramTreeConfiguration, paramFunction);
/*     */     }
/*     */     
/* 150 */     Direction direction = (blockPos.getY() > paramMutableBlockPos.getY()) ? Direction.UP : Direction.DOWN;
/*     */     
/*     */     while (true) {
/* 153 */       int k = paramMutableBlockPos.distManhattan((Vec3i)blockPos);
/* 154 */       if (k == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 158 */       float f = Math.abs(blockPos.getY() - paramMutableBlockPos.getY()) / k;
/* 159 */       boolean bool1 = (paramRandomSource.nextFloat() < f) ? true : false;
/*     */       
/* 161 */       paramMutableBlockPos.move(bool1 ? direction : paramDirection);
/* 162 */       placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)paramMutableBlockPos, paramTreeConfiguration, bool1 ? Function.<BlockState>identity() : paramFunction);
/*     */     } 
/*     */     
/* 165 */     return new FoliagePlacer.FoliageAttachment(blockPos.above(), 0, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\CherryTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */