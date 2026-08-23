/*     */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ public class MangroveRootPlacer extends RootPlacer {
/*     */   public static final int ROOT_WIDTH_LIMIT = 8;
/*     */   
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> rootPlacerParts(paramInstance).and((App)MangroveRootPlacement.CODEC.fieldOf("mangrove_root_placement").forGetter(())).apply((Applicative)paramInstance, MangroveRootPlacer::new));
/*     */   }
/*     */   public static final int ROOT_LENGTH_LIMIT = 15;
/*     */   public static final MapCodec<MangroveRootPlacer> CODEC;
/*     */   private final MangroveRootPlacement mangroveRootPlacement;
/*     */   
/*     */   public MangroveRootPlacer(IntProvider paramIntProvider, BlockStateProvider paramBlockStateProvider, Optional<AboveRootPlacement> paramOptional, MangroveRootPlacement paramMangroveRootPlacement) {
/*  32 */     super(paramIntProvider, paramBlockStateProvider, paramOptional);
/*  33 */     this.mangroveRootPlacement = paramMangroveRootPlacement;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeRoots(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos1, BlockPos paramBlockPos2, TreeConfiguration paramTreeConfiguration) {
/*  38 */     ArrayList<BlockPos> arrayList = Lists.newArrayList();
/*     */     
/*  40 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos1.mutable();
/*  41 */     while (mutableBlockPos.getY() < paramBlockPos2.getY()) {
/*  42 */       if (!canPlaceRoot(paramLevelSimulatedReader, (BlockPos)mutableBlockPos)) {
/*  43 */         return false;
/*     */       }
/*  45 */       mutableBlockPos.move(Direction.UP);
/*     */     } 
/*     */     
/*  48 */     arrayList.add(paramBlockPos2.below());
/*     */ 
/*     */     
/*  51 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  52 */       BlockPos blockPos = paramBlockPos2.relative(direction);
/*  53 */       ArrayList<BlockPos> arrayList1 = Lists.newArrayList();
/*     */       
/*  55 */       if (!simulateRoots(paramLevelSimulatedReader, paramRandomSource, blockPos, direction, paramBlockPos2, arrayList1, 0)) {
/*  56 */         return false;
/*     */       }
/*     */       
/*  59 */       arrayList.addAll(arrayList1);
/*  60 */       arrayList.add(paramBlockPos2.relative(direction));
/*     */     } 
/*     */     
/*  63 */     for (BlockPos blockPos : arrayList) {
/*  64 */       placeRoot(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, paramTreeConfiguration);
/*     */     }
/*     */     
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean simulateRoots(LevelSimulatedReader paramLevelSimulatedReader, RandomSource paramRandomSource, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, List<BlockPos> paramList, int paramInt) {
/*  72 */     int i = this.mangroveRootPlacement.maxRootLength();
/*  73 */     if (paramInt == i || paramList.size() > i) {
/*  74 */       return false;
/*     */     }
/*  76 */     List<BlockPos> list = potentialRootPositions(paramBlockPos1, paramDirection, paramRandomSource, paramBlockPos2);
/*  77 */     for (BlockPos blockPos : list) {
/*  78 */       if (canPlaceRoot(paramLevelSimulatedReader, blockPos)) {
/*  79 */         paramList.add(blockPos);
/*  80 */         if (!simulateRoots(paramLevelSimulatedReader, paramRandomSource, blockPos, paramDirection, paramBlockPos2, paramList, paramInt + 1)) {
/*  81 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   protected List<BlockPos> potentialRootPositions(BlockPos paramBlockPos1, Direction paramDirection, RandomSource paramRandomSource, BlockPos paramBlockPos2) {
/*  90 */     BlockPos blockPos1 = paramBlockPos1.below();
/*  91 */     BlockPos blockPos2 = paramBlockPos1.relative(paramDirection);
/*  92 */     int i = paramBlockPos1.distManhattan((Vec3i)paramBlockPos2);
/*  93 */     int j = this.mangroveRootPlacement.maxRootWidth();
/*  94 */     float f = this.mangroveRootPlacement.randomSkewChance();
/*     */ 
/*     */     
/*  97 */     if (i > j - 3 && i <= j) {
/*  98 */       return (paramRandomSource.nextFloat() < f) ? List.<BlockPos>of(blockPos1, blockPos2.below()) : List.<BlockPos>of(blockPos1);
/*     */     }
/*     */ 
/*     */     
/* 102 */     if (i > j) {
/* 103 */       return List.of(blockPos1);
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (paramRandomSource.nextFloat() < f) {
/* 108 */       return List.of(blockPos1);
/*     */     }
/*     */     
/* 111 */     return paramRandomSource.nextBoolean() ? List.<BlockPos>of(blockPos2) : List.<BlockPos>of(blockPos1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPlaceRoot(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 116 */     return (super.canPlaceRoot(paramLevelSimulatedReader, paramBlockPos) || paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> paramBlockState.is(this.mangroveRootPlacement.canGrowThrough())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void placeRoot(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/* 121 */     if (paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> paramBlockState.is(this.mangroveRootPlacement.muddyRootsIn()))) {
/* 122 */       BlockState blockState = this.mangroveRootPlacement.muddyRootsProvider().getState(paramRandomSource, paramBlockPos);
/* 123 */       paramBiConsumer.accept(paramBlockPos, getPotentiallyWaterloggedState(paramLevelSimulatedReader, paramBlockPos, blockState));
/*     */     } else {
/* 125 */       super.placeRoot(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos, paramTreeConfiguration);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected RootPlacerType<?> type() {
/* 131 */     return RootPlacerType.MANGROVE_ROOT_PLACER;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\MangroveRootPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */