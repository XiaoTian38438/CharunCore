/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class MineShaftPiece
/*     */   extends StructurePiece
/*     */ {
/*     */   protected MineshaftStructure.Type type;
/*     */   
/*     */   public MineShaftPiece(StructurePieceType paramStructurePieceType, int paramInt, MineshaftStructure.Type paramType, BoundingBox paramBoundingBox) {
/*  57 */     super(paramStructurePieceType, paramInt, paramBoundingBox);
/*  58 */     this.type = paramType;
/*     */   }
/*     */   
/*     */   public MineShaftPiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/*  62 */     super(paramStructurePieceType, paramCompoundTag);
/*  63 */     this.type = MineshaftStructure.Type.byId(paramCompoundTag.getIntOr("MST", 0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(LevelReader paramLevelReader, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/*  69 */     BlockState blockState = getBlock((BlockGetter)paramLevelReader, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*  70 */     return (!blockState.is(this.type.getPlanksState().getBlock()) && 
/*  71 */       !blockState.is(this.type.getWoodState().getBlock()) && 
/*  72 */       !blockState.is(this.type.getFenceState().getBlock()) && 
/*  73 */       !blockState.is(Blocks.IRON_CHAIN));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  78 */     paramCompoundTag.putInt("MST", this.type.ordinal());
/*     */   }
/*     */   
/*     */   protected boolean isSupportingBox(BlockGetter paramBlockGetter, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  82 */     for (int i = paramInt1; i <= paramInt2; i++) {
/*  83 */       if (getBlock(paramBlockGetter, i, paramInt3 + 1, paramInt4, paramBoundingBox).isAir()) {
/*  84 */         return false;
/*     */       }
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isInInvalidLocation(LevelAccessor paramLevelAccessor, BoundingBox paramBoundingBox) {
/*  91 */     int i = Math.max(this.boundingBox.minX() - 1, paramBoundingBox.minX());
/*  92 */     int j = Math.max(this.boundingBox.minY() - 1, paramBoundingBox.minY());
/*  93 */     int k = Math.max(this.boundingBox.minZ() - 1, paramBoundingBox.minZ());
/*  94 */     int m = Math.min(this.boundingBox.maxX() + 1, paramBoundingBox.maxX());
/*  95 */     int n = Math.min(this.boundingBox.maxY() + 1, paramBoundingBox.maxY());
/*  96 */     int i1 = Math.min(this.boundingBox.maxZ() + 1, paramBoundingBox.maxZ());
/*     */     
/*  98 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos((i + m) / 2, (j + n) / 2, (k + i1) / 2);
/*     */     
/* 100 */     if (paramLevelAccessor.getBiome((BlockPos)mutableBlockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
/* 101 */       return true;
/*     */     }
/*     */     
/*     */     int i2;
/* 105 */     for (i2 = i; i2 <= m; i2++) {
/* 106 */       for (int i3 = k; i3 <= i1; i3++) {
/* 107 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, j, i3)).liquid()) {
/* 108 */           return true;
/*     */         }
/* 110 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, n, i3)).liquid()) {
/* 111 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     for (i2 = i; i2 <= m; i2++) {
/* 117 */       for (int i3 = j; i3 <= n; i3++) {
/* 118 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, i3, k)).liquid()) {
/* 119 */           return true;
/*     */         }
/* 121 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, i3, i1)).liquid()) {
/* 122 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     for (i2 = k; i2 <= i1; i2++) {
/* 128 */       for (int i3 = j; i3 <= n; i3++) {
/* 129 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i, i3, i2)).liquid()) {
/* 130 */           return true;
/*     */         }
/* 132 */         if (paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(m, i3, i2)).liquid()) {
/* 133 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   protected void setPlanksBlock(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3) {
/* 141 */     if (!isInterior((LevelReader)paramWorldGenLevel, paramInt1, paramInt2, paramInt3, paramBoundingBox)) {
/*     */       return;
/*     */     }
/* 144 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 145 */     BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 146 */     if (!blockState.isFaceSturdy((BlockGetter)paramWorldGenLevel, (BlockPos)mutableBlockPos, Direction.UP))
/*     */     {
/* 148 */       paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, paramBlockState, 2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */