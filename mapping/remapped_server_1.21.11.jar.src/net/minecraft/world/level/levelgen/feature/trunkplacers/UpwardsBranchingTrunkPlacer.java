/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class UpwardsBranchingTrunkPlacer extends TrunkPlacer {
/*     */   static {
/*  24 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).and(paramInstance.group((App)IntProvider.POSITIVE_CODEC.fieldOf("extra_branch_steps").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("place_branch_per_log_probability").forGetter(()), (App)IntProvider.NON_NEGATIVE_CODEC.fieldOf("extra_branch_length").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(()))).apply((Applicative)paramInstance, UpwardsBranchingTrunkPlacer::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<UpwardsBranchingTrunkPlacer> CODEC;
/*     */   
/*     */   private final IntProvider extraBranchSteps;
/*     */   
/*     */   private final float placeBranchPerLogProbability;
/*     */   
/*     */   private final IntProvider extraBranchLength;
/*     */   
/*     */   private final HolderSet<Block> canGrowThrough;
/*     */   
/*     */   public UpwardsBranchingTrunkPlacer(int paramInt1, int paramInt2, int paramInt3, IntProvider paramIntProvider1, float paramFloat, IntProvider paramIntProvider2, HolderSet<Block> paramHolderSet) {
/*  39 */     super(paramInt1, paramInt2, paramInt3);
/*  40 */     this.extraBranchSteps = paramIntProvider1;
/*  41 */     this.placeBranchPerLogProbability = paramFloat;
/*  42 */     this.extraBranchLength = paramIntProvider2;
/*  43 */     this.canGrowThrough = paramHolderSet;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  48 */     return TrunkPlacerType.UPWARDS_BRANCHING_TRUNK_PLACER;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/*  53 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList = Lists.newArrayList();
/*     */     
/*  55 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  56 */     for (byte b = 0; b < paramInt; b++) {
/*  57 */       int i = paramBlockPos.getY() + b;
/*  58 */       if (placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)mutableBlockPos.set(paramBlockPos.getX(), i, paramBlockPos.getZ()), paramTreeConfiguration) && 
/*  59 */         b < paramInt - 1 && paramRandomSource.nextFloat() < this.placeBranchPerLogProbability) {
/*  60 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/*  61 */         int j = this.extraBranchLength.sample(paramRandomSource);
/*  62 */         int k = Math.max(0, j - this.extraBranchLength.sample(paramRandomSource) - 1);
/*  63 */         int m = this.extraBranchSteps.sample(paramRandomSource);
/*  64 */         placeBranch(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramInt, paramTreeConfiguration, arrayList, mutableBlockPos, i, direction, k, m);
/*     */       } 
/*     */ 
/*     */       
/*  68 */       if (b == paramInt - 1) {
/*  69 */         arrayList.add(new FoliagePlacer.FoliageAttachment((BlockPos)mutableBlockPos.set(paramBlockPos.getX(), i + 1, paramBlockPos.getZ()), 0, false));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  74 */     return arrayList;
/*     */   }
/*     */   
/*     */   private void placeBranch(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt1, TreeConfiguration paramTreeConfiguration, List<FoliagePlacer.FoliageAttachment> paramList, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt2, Direction paramDirection, int paramInt3, int paramInt4) {
/*  78 */     int i = paramInt2 + paramInt3;
/*  79 */     int j = paramMutableBlockPos.getX();
/*  80 */     int k = paramMutableBlockPos.getZ();
/*  81 */     for (int m = paramInt3; m < paramInt1 && paramInt4 > 0; m++, paramInt4--) {
/*  82 */       if (m >= 1) {
/*     */ 
/*     */         
/*  85 */         int n = paramInt2 + m;
/*  86 */         j += paramDirection.getStepX();
/*  87 */         k += paramDirection.getStepZ();
/*  88 */         i = n;
/*     */         
/*  90 */         if (placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, (BlockPos)paramMutableBlockPos.set(j, n, k), paramTreeConfiguration)) {
/*  91 */           i++;
/*     */         }
/*     */         
/*  94 */         paramList.add(new FoliagePlacer.FoliageAttachment(paramMutableBlockPos.immutable(), 0, false));
/*     */       } 
/*  96 */     }  if (i - paramInt2 > 1) {
/*  97 */       BlockPos blockPos = new BlockPos(j, i, k);
/*  98 */       paramList.add(new FoliagePlacer.FoliageAttachment(blockPos, 0, false));
/*  99 */       paramList.add(new FoliagePlacer.FoliageAttachment(blockPos.below(2), 0, false));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean validTreePos(LevelSimulatedReader paramLevelSimulatedReader, BlockPos paramBlockPos) {
/* 105 */     return (super.validTreePos(paramLevelSimulatedReader, paramBlockPos) || paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> paramBlockState.is(this.canGrowThrough)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\UpwardsBranchingTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */