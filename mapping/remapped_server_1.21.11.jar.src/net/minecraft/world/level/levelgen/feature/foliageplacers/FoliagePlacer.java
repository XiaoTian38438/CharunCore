/*     */ package net.minecraft.world.level.levelgen.feature.foliageplacers;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public abstract class FoliagePlacer {
/*  20 */   public static final Codec<FoliagePlacer> CODEC = BuiltInRegistries.FOLIAGE_PLACER_TYPE.byNameCodec().dispatch(FoliagePlacer::type, FoliagePlacerType::codec);
/*     */   
/*     */   protected final IntProvider radius;
/*     */   protected final IntProvider offset;
/*     */   
/*     */   protected static <P extends FoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> foliagePlacerParts(RecordCodecBuilder.Instance<P> paramInstance) {
/*  26 */     return paramInstance.group(
/*  27 */         (App)IntProvider.codec(0, 16).fieldOf("radius").forGetter(paramFoliagePlacer -> paramFoliagePlacer.radius), 
/*  28 */         (App)IntProvider.codec(0, 16).fieldOf("offset").forGetter(paramFoliagePlacer -> paramFoliagePlacer.offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FoliagePlacer(IntProvider paramIntProvider1, IntProvider paramIntProvider2) {
/*  39 */     this.radius = paramIntProvider1;
/*  40 */     this.offset = paramIntProvider2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3) {
/*  46 */     createFoliage(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramInt1, paramFoliageAttachment, paramInt2, paramInt3, offset(paramRandomSource));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int foliageRadius(RandomSource paramRandomSource, int paramInt) {
/*  54 */     return this.radius.sample(paramRandomSource);
/*     */   }
/*     */   
/*     */   private int offset(RandomSource paramRandomSource) {
/*  58 */     return this.offset.sample(paramRandomSource);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldSkipLocationSigned(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/*     */     int i;
/*     */     int j;
/*  66 */     if (paramBoolean) {
/*     */ 
/*     */       
/*  69 */       i = Math.min(Math.abs(paramInt1), Math.abs(paramInt1 - 1));
/*  70 */       j = Math.min(Math.abs(paramInt3), Math.abs(paramInt3 - 1));
/*     */     }
/*     */     else {
/*     */       
/*  74 */       i = Math.abs(paramInt1);
/*  75 */       j = Math.abs(paramInt3);
/*     */     } 
/*  77 */     return shouldSkipLocation(paramRandomSource, i, paramInt2, j, paramInt4, paramBoolean);
/*     */   }
/*     */   
/*     */   protected void placeLeavesRow(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, BlockPos paramBlockPos, int paramInt1, int paramInt2, boolean paramBoolean) {
/*  81 */     byte b = paramBoolean ? 1 : 0;
/*  82 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*  83 */     for (int i = -paramInt1; i <= paramInt1 + b; i++) {
/*  84 */       for (int j = -paramInt1; j <= paramInt1 + b; j++) {
/*  85 */         if (!shouldSkipLocationSigned(paramRandomSource, i, paramInt2, j, paramInt1, paramBoolean)) {
/*     */ 
/*     */           
/*  88 */           mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, i, paramInt2, j);
/*  89 */           tryPlaceLeaf(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, (BlockPos)mutableBlockPos);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void placeLeavesRowWithHangingLeavesBelow(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, BlockPos paramBlockPos, int paramInt1, int paramInt2, boolean paramBoolean, float paramFloat1, float paramFloat2) {
/*  98 */     placeLeavesRow(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramBlockPos, paramInt1, paramInt2, paramBoolean);
/*     */     
/* 100 */     byte b = paramBoolean ? 1 : 0;
/* 101 */     BlockPos blockPos = paramBlockPos.below();
/* 102 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 103 */     for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/* 104 */       Direction direction2 = direction1.getClockWise();
/* 105 */       int i = (direction2.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (paramInt1 + b) : paramInt1;
/*     */       
/* 107 */       mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, 0, paramInt2 - 1, 0)
/* 108 */         .move(direction2, i)
/* 109 */         .move(direction1, -paramInt1);
/*     */       
/* 111 */       for (int j = -paramInt1; j < paramInt1 + b; j++, mutableBlockPos.move(direction1)) {
/*     */ 
/*     */         
/* 114 */         boolean bool = paramFoliageSetter.isSet((BlockPos)mutableBlockPos.move(Direction.UP));
/* 115 */         mutableBlockPos.move(Direction.DOWN);
/*     */         
/* 117 */         if (bool)
/*     */         {
/*     */           
/* 120 */           if (tryPlaceExtension(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFloat1, blockPos, mutableBlockPos)) {
/*     */ 
/*     */             
/* 123 */             mutableBlockPos.move(Direction.DOWN);
/* 124 */             tryPlaceExtension(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, paramFloat2, blockPos, mutableBlockPos);
/* 125 */             mutableBlockPos.move(Direction.UP);
/*     */           }  } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static boolean tryPlaceExtension(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, float paramFloat, BlockPos paramBlockPos, BlockPos.MutableBlockPos paramMutableBlockPos) {
/* 131 */     if (paramMutableBlockPos.distManhattan((Vec3i)paramBlockPos) >= 7) {
/* 132 */       return false;
/*     */     }
/* 134 */     if (paramRandomSource.nextFloat() > paramFloat) {
/* 135 */       return false;
/*     */     }
/* 137 */     return tryPlaceLeaf(paramLevelSimulatedReader, paramFoliageSetter, paramRandomSource, paramTreeConfiguration, (BlockPos)paramMutableBlockPos);
/*     */   }
/*     */   
/*     */   protected static boolean tryPlaceLeaf(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, BlockPos paramBlockPos) {
/* 141 */     boolean bool = paramLevelSimulatedReader.isStateAtPosition(paramBlockPos, paramBlockState -> ((Boolean)paramBlockState.getValueOrElse((Property)BlockStateProperties.PERSISTENT, Boolean.valueOf(false))).booleanValue());
/* 142 */     if (bool || !TreeFeature.validTreePos(paramLevelSimulatedReader, paramBlockPos)) {
/* 143 */       return false;
/*     */     }
/* 145 */     BlockState blockState = paramTreeConfiguration.foliageProvider.getState(paramRandomSource, paramBlockPos);
/* 146 */     if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED)) {
/* 147 */       blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(paramLevelSimulatedReader.isFluidAtPosition(paramBlockPos, paramFluidState -> paramFluidState.isSourceOfType((Fluid)Fluids.WATER))));
/*     */     }
/* 149 */     paramFoliageSetter.set(paramBlockPos, blockState);
/* 150 */     return true;
/*     */   } protected abstract FoliagePlacerType<?> type(); protected abstract void createFoliage(LevelSimulatedReader paramLevelSimulatedReader, FoliageSetter paramFoliageSetter, RandomSource paramRandomSource, TreeConfiguration paramTreeConfiguration, int paramInt1, FoliageAttachment paramFoliageAttachment, int paramInt2, int paramInt3, int paramInt4);
/*     */   public abstract int foliageHeight(RandomSource paramRandomSource, int paramInt, TreeConfiguration paramTreeConfiguration);
/*     */   protected abstract boolean shouldSkipLocation(RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean);
/*     */   public static interface FoliageSetter {
/*     */     void set(BlockPos param1BlockPos, BlockState param1BlockState);
/*     */     boolean isSet(BlockPos param1BlockPos); }
/*     */   public static final class FoliageAttachment { private final BlockPos pos;
/*     */     public FoliageAttachment(BlockPos param1BlockPos, int param1Int, boolean param1Boolean) {
/* 159 */       this.pos = param1BlockPos;
/* 160 */       this.radiusOffset = param1Int;
/* 161 */       this.doubleTrunk = param1Boolean;
/*     */     }
/*     */     private final int radiusOffset; private final boolean doubleTrunk;
/*     */     public BlockPos pos() {
/* 165 */       return this.pos;
/*     */     }
/*     */     
/*     */     public int radiusOffset() {
/* 169 */       return this.radiusOffset;
/*     */     }
/*     */     
/*     */     public boolean doubleTrunk() {
/* 173 */       return this.doubleTrunk;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\foliageplacers\FoliagePlacer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */