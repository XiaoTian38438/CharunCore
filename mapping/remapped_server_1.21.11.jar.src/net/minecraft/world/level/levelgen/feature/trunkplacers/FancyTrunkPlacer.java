/*     */ package net.minecraft.world.level.levelgen.feature.trunkplacers;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ 
/*     */ public class FancyTrunkPlacer extends TrunkPlacer {
/*     */   public static final MapCodec<FancyTrunkPlacer> CODEC;
/*     */   
/*     */   static {
/*  21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> trunkPlacerParts(paramInstance).apply((Applicative)paramInstance, FancyTrunkPlacer::new));
/*     */   }
/*     */   private static final double TRUNK_HEIGHT_SCALE = 0.618D;
/*     */   private static final double CLUSTER_DENSITY_MAGIC = 1.382D;
/*     */   private static final double BRANCH_SLOPE = 0.381D;
/*     */   private static final double BRANCH_LENGTH_MAGIC = 0.328D;
/*     */   
/*     */   public FancyTrunkPlacer(int paramInt1, int paramInt2, int paramInt3) {
/*  29 */     super(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TrunkPlacerType<?> type() {
/*  34 */     return TrunkPlacerType.FANCY_TRUNK_PLACER;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, TreeConfiguration paramTreeConfiguration) {
/*  39 */     byte b = 5;
/*  40 */     int i = paramInt + 2;
/*  41 */     int j = Mth.floor(i * 0.618D);
/*     */     
/*  43 */     setDirtAt(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos.below(), paramTreeConfiguration);
/*     */ 
/*     */     
/*  46 */     double d = 1.0D;
/*  47 */     int k = Math.min(1, Mth.floor(1.382D + Math.pow(1.0D * i / 13.0D, 2.0D)));
/*     */     
/*  49 */     int m = paramBlockPos.getY() + j;
/*  50 */     int n = i - 5;
/*     */     
/*  52 */     ArrayList<FoliageCoords> arrayList = Lists.newArrayList();
/*  53 */     arrayList.add(new FoliageCoords(paramBlockPos.above(n), m));
/*     */     
/*  55 */     for (; n >= 0; n--) {
/*  56 */       float f = treeShape(i, n);
/*  57 */       if (f >= 0.0F)
/*     */       {
/*     */ 
/*     */         
/*  61 */         for (byte b1 = 0; b1 < k; b1++) {
/*  62 */           double d1 = 1.0D;
/*  63 */           double d2 = 1.0D * f * (paramRandomSource.nextFloat() + 0.328D);
/*  64 */           double d3 = (paramRandomSource.nextFloat() * 2.0F) * Math.PI;
/*     */           
/*  66 */           double d4 = d2 * Math.sin(d3) + 0.5D;
/*  67 */           double d5 = d2 * Math.cos(d3) + 0.5D;
/*     */           
/*  69 */           BlockPos blockPos1 = paramBlockPos.offset(Mth.floor(d4), n - 1, Mth.floor(d5));
/*  70 */           BlockPos blockPos2 = blockPos1.above(5);
/*     */ 
/*     */           
/*  73 */           if (makeLimb(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1, blockPos2, false, paramTreeConfiguration)) {
/*     */             
/*  75 */             int i1 = paramBlockPos.getX() - blockPos1.getX();
/*  76 */             int i2 = paramBlockPos.getZ() - blockPos1.getZ();
/*     */             
/*  78 */             double d6 = blockPos1.getY() - Math.sqrt((i1 * i1 + i2 * i2)) * 0.381D;
/*  79 */             int i3 = (d6 > m) ? m : (int)d6;
/*  80 */             BlockPos blockPos = new BlockPos(paramBlockPos.getX(), i3, paramBlockPos.getZ());
/*     */ 
/*     */             
/*  83 */             if (makeLimb(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, blockPos1, false, paramTreeConfiguration))
/*     */             {
/*  85 */               arrayList.add(new FoliageCoords(blockPos1, blockPos.getY())); } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*  90 */     makeLimb(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, paramBlockPos, paramBlockPos.above(j), true, paramTreeConfiguration);
/*  91 */     makeBranches(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, i, paramBlockPos, arrayList, paramTreeConfiguration);
/*     */     
/*  93 */     ArrayList<FoliagePlacer.FoliageAttachment> arrayList1 = Lists.newArrayList();
/*  94 */     for (FoliageCoords foliageCoords : arrayList) {
/*  95 */       if (trimBranches(i, foliageCoords.getBranchBase() - paramBlockPos.getY())) {
/*  96 */         arrayList1.add(foliageCoords.attachment);
/*     */       }
/*     */     } 
/*  99 */     return arrayList1;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean makeLimb(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, BlockPos paramBlockPos1, BlockPos paramBlockPos2, boolean paramBoolean, TreeConfiguration paramTreeConfiguration) {
/* 104 */     if (!paramBoolean && Objects.equals(paramBlockPos1, paramBlockPos2)) {
/* 105 */       return true;
/*     */     }
/*     */     
/* 108 */     BlockPos blockPos = paramBlockPos2.offset(-paramBlockPos1.getX(), -paramBlockPos1.getY(), -paramBlockPos1.getZ());
/*     */     
/* 110 */     int i = getSteps(blockPos);
/*     */     
/* 112 */     float f1 = blockPos.getX() / i;
/* 113 */     float f2 = blockPos.getY() / i;
/* 114 */     float f3 = blockPos.getZ() / i;
/*     */     
/* 116 */     for (byte b = 0; b <= i; b++) {
/* 117 */       BlockPos blockPos1 = paramBlockPos1.offset(Mth.floor(0.5F + b * f1), Mth.floor(0.5F + b * f2), Mth.floor(0.5F + b * f3));
/* 118 */       if (paramBoolean) {
/* 119 */         placeLog(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos1, paramTreeConfiguration, paramBlockState -> (BlockState)paramBlockState.trySetValue((Property)RotatedPillarBlock.AXIS, (Comparable)getLogAxis(paramBlockPos1, paramBlockPos2)));
/*     */       
/*     */       }
/* 122 */       else if (!isFree(paramLevelSimulatedReader, blockPos1)) {
/* 123 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   private int getSteps(BlockPos paramBlockPos) {
/* 131 */     int i = Mth.abs(paramBlockPos.getX());
/* 132 */     int j = Mth.abs(paramBlockPos.getY());
/* 133 */     int k = Mth.abs(paramBlockPos.getZ());
/*     */     
/* 135 */     return Math.max(i, Math.max(j, k));
/*     */   }
/*     */   
/*     */   private Direction.Axis getLogAxis(BlockPos paramBlockPos1, BlockPos paramBlockPos2) {
/* 139 */     Direction.Axis axis = Direction.Axis.Y;
/* 140 */     int i = Math.abs(paramBlockPos2.getX() - paramBlockPos1.getX());
/* 141 */     int j = Math.abs(paramBlockPos2.getZ() - paramBlockPos1.getZ());
/* 142 */     int k = Math.max(i, j);
/*     */     
/* 144 */     if (k > 0) {
/* 145 */       if (i == k) {
/* 146 */         axis = Direction.Axis.X;
/*     */       } else {
/* 148 */         axis = Direction.Axis.Z;
/*     */       } 
/*     */     }
/* 151 */     return axis;
/*     */   }
/*     */   
/*     */   private boolean trimBranches(int paramInt1, int paramInt2) {
/* 155 */     return (paramInt2 >= paramInt1 * 0.2D);
/*     */   }
/*     */   
/*     */   private void makeBranches(LevelSimulatedReader paramLevelSimulatedReader, BiConsumer<BlockPos, BlockState> paramBiConsumer, RandomSource paramRandomSource, int paramInt, BlockPos paramBlockPos, List<FoliageCoords> paramList, TreeConfiguration paramTreeConfiguration) {
/* 159 */     for (FoliageCoords foliageCoords : paramList) {
/* 160 */       int i = foliageCoords.getBranchBase();
/* 161 */       BlockPos blockPos = new BlockPos(paramBlockPos.getX(), i, paramBlockPos.getZ());
/*     */       
/* 163 */       if (!blockPos.equals(foliageCoords.attachment.pos()) && trimBranches(paramInt, i - paramBlockPos.getY())) {
/* 164 */         makeLimb(paramLevelSimulatedReader, paramBiConsumer, paramRandomSource, blockPos, foliageCoords.attachment.pos(), true, paramTreeConfiguration);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static float treeShape(int paramInt1, int paramInt2) {
/* 171 */     if (paramInt2 < paramInt1 * 0.3F) {
/* 172 */       return -1.0F;
/*     */     }
/*     */     
/* 175 */     float f1 = paramInt1 / 2.0F;
/* 176 */     float f2 = f1 - paramInt2;
/*     */     
/* 178 */     float f3 = Mth.sqrt(f1 * f1 - f2 * f2);
/* 179 */     if (f2 == 0.0F) {
/* 180 */       f3 = f1;
/* 181 */     } else if (Math.abs(f2) >= f1) {
/* 182 */       return 0.0F;
/*     */     } 
/*     */     
/* 185 */     return f3 * 0.5F;
/*     */   }
/*     */   
/*     */   private static class FoliageCoords {
/*     */     final FoliagePlacer.FoliageAttachment attachment;
/*     */     private final int branchBase;
/*     */     
/*     */     public FoliageCoords(BlockPos param1BlockPos, int param1Int) {
/* 193 */       this.attachment = new FoliagePlacer.FoliageAttachment(param1BlockPos, 0, false);
/* 194 */       this.branchBase = param1Int;
/*     */     }
/*     */     
/*     */     public int getBranchBase() {
/* 198 */       return this.branchBase;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\trunkplacers\FancyTrunkPlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */