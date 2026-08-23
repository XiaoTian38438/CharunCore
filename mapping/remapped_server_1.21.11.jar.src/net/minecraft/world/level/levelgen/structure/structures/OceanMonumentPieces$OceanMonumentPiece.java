/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
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
/*     */ public abstract class OceanMonumentPiece
/*     */   extends StructurePiece
/*     */ {
/*  34 */   protected static final BlockState BASE_GRAY = Blocks.PRISMARINE.defaultBlockState();
/*  35 */   protected static final BlockState BASE_LIGHT = Blocks.PRISMARINE_BRICKS.defaultBlockState();
/*  36 */   protected static final BlockState BASE_BLACK = Blocks.DARK_PRISMARINE.defaultBlockState();
/*     */   
/*  38 */   protected static final BlockState DOT_DECO_DATA = BASE_LIGHT;
/*     */   
/*  40 */   protected static final BlockState LAMP_BLOCK = Blocks.SEA_LANTERN.defaultBlockState();
/*     */   
/*     */   protected static final boolean DO_FILL = true;
/*  43 */   protected static final BlockState FILL_BLOCK = Blocks.WATER.defaultBlockState();
/*  44 */   protected static final Set<Block> FILL_KEEP = (Set<Block>)ImmutableSet.builder()
/*  45 */     .add(Blocks.ICE)
/*  46 */     .add(Blocks.PACKED_ICE)
/*  47 */     .add(Blocks.BLUE_ICE)
/*  48 */     .add(FILL_BLOCK.getBlock())
/*  49 */     .build();
/*     */   
/*     */   protected static final int GRIDROOM_WIDTH = 8;
/*     */   
/*     */   protected static final int GRIDROOM_DEPTH = 8;
/*     */   protected static final int GRIDROOM_HEIGHT = 4;
/*     */   protected static final int GRID_WIDTH = 5;
/*     */   protected static final int GRID_DEPTH = 5;
/*     */   protected static final int GRID_HEIGHT = 3;
/*     */   protected static final int GRID_FLOOR_COUNT = 25;
/*     */   protected static final int GRID_SIZE = 75;
/*  60 */   protected static final int GRIDROOM_SOURCE_INDEX = getRoomIndex(2, 0, 0);
/*  61 */   protected static final int GRIDROOM_TOP_CONNECT_INDEX = getRoomIndex(2, 2, 0);
/*  62 */   protected static final int GRIDROOM_LEFTWING_CONNECT_INDEX = getRoomIndex(0, 1, 0);
/*  63 */   protected static final int GRIDROOM_RIGHTWING_CONNECT_INDEX = getRoomIndex(4, 1, 0);
/*     */   
/*     */   protected static final int LEFTWING_INDEX = 1001;
/*     */   
/*     */   protected static final int RIGHTWING_INDEX = 1002;
/*     */   protected static final int PENTHOUSE_INDEX = 1003;
/*     */   protected OceanMonumentPieces.RoomDefinition roomDefinition;
/*     */   
/*     */   protected static int getRoomIndex(int paramInt1, int paramInt2, int paramInt3) {
/*  72 */     return paramInt2 * 25 + paramInt3 * 5 + paramInt1;
/*     */   }
/*     */   
/*     */   public OceanMonumentPiece(StructurePieceType paramStructurePieceType, Direction paramDirection, int paramInt, BoundingBox paramBoundingBox) {
/*  76 */     super(paramStructurePieceType, paramInt, paramBoundingBox);
/*  77 */     setOrientation(paramDirection);
/*     */   }
/*     */   
/*     */   protected OceanMonumentPiece(StructurePieceType paramStructurePieceType, int paramInt1, Direction paramDirection, OceanMonumentPieces.RoomDefinition paramRoomDefinition, int paramInt2, int paramInt3, int paramInt4) {
/*  81 */     super(paramStructurePieceType, paramInt1, makeBoundingBox(paramDirection, paramRoomDefinition, paramInt2, paramInt3, paramInt4));
/*     */     
/*  83 */     setOrientation(paramDirection);
/*  84 */     this.roomDefinition = paramRoomDefinition;
/*     */   }
/*     */   
/*     */   private static BoundingBox makeBoundingBox(Direction paramDirection, OceanMonumentPieces.RoomDefinition paramRoomDefinition, int paramInt1, int paramInt2, int paramInt3) {
/*  88 */     int i = paramRoomDefinition.index;
/*  89 */     int j = i % 5;
/*  90 */     int k = i / 5 % 5;
/*  91 */     int m = i / 25;
/*     */ 
/*     */ 
/*     */     
/*  95 */     BoundingBox boundingBox = makeBoundingBox(0, 0, 0, paramDirection, paramInt1 * 8, paramInt2 * 4, paramInt3 * 8);
/*     */     
/*  97 */     switch (OceanMonumentPieces.null.$SwitchMap$net$minecraft$core$Direction[paramDirection.ordinal()])
/*     */     { case 1:
/*  99 */         boundingBox.move(j * 8, m * 4, -(k + paramInt3) * 8 + 1);
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
/* 114 */         return boundingBox;case 2: boundingBox.move(j * 8, m * 4, k * 8); return boundingBox;case 3: boundingBox.move(-(k + paramInt3) * 8 + 1, m * 4, j * 8); return boundingBox; }  boundingBox.move(k * 8, m * 4, j * 8); return boundingBox;
/*     */   }
/*     */   
/*     */   public OceanMonumentPiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/* 118 */     super(paramStructurePieceType, paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {}
/*     */ 
/*     */   
/*     */   protected void generateWaterBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 126 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 127 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 128 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 129 */           BlockState blockState = getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox);
/* 130 */           if (!FILL_KEEP.contains(blockState.getBlock())) {
/* 131 */             if (getWorldY(i) >= paramWorldGenLevel.getSeaLevel() && blockState != FILL_BLOCK) {
/* 132 */               placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), j, i, k, paramBoundingBox);
/*     */             } else {
/* 134 */               placeBlock(paramWorldGenLevel, FILL_BLOCK, j, i, k, paramBoundingBox);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateDefaultFloor(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 143 */     if (paramBoolean) {
/* 144 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 0, 0, paramInt2 + 0, paramInt1 + 2, 0, paramInt2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/* 145 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 5, 0, paramInt2 + 0, paramInt1 + 8 - 1, 0, paramInt2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/* 146 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 3, 0, paramInt2 + 0, paramInt1 + 4, 0, paramInt2 + 2, BASE_GRAY, BASE_GRAY, false);
/* 147 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 3, 0, paramInt2 + 5, paramInt1 + 4, 0, paramInt2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*     */       
/* 149 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 3, 0, paramInt2 + 2, paramInt1 + 4, 0, paramInt2 + 2, BASE_LIGHT, BASE_LIGHT, false);
/* 150 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 3, 0, paramInt2 + 5, paramInt1 + 4, 0, paramInt2 + 5, BASE_LIGHT, BASE_LIGHT, false);
/* 151 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 2, 0, paramInt2 + 3, paramInt1 + 2, 0, paramInt2 + 4, BASE_LIGHT, BASE_LIGHT, false);
/* 152 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 5, 0, paramInt2 + 3, paramInt1 + 5, 0, paramInt2 + 4, BASE_LIGHT, BASE_LIGHT, false);
/*     */     } else {
/* 154 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1 + 0, 0, paramInt2 + 0, paramInt1 + 8 - 1, 0, paramInt2 + 8 - 1, BASE_GRAY, BASE_GRAY, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBoxOnFillOnly(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BlockState paramBlockState) {
/* 159 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 160 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 161 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 162 */           if (getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox) == FILL_BLOCK)
/*     */           {
/*     */             
/* 165 */             placeBlock(paramWorldGenLevel, paramBlockState, j, i, k, paramBoundingBox); } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean chunkIntersects(BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 172 */     int i = getWorldX(paramInt1, paramInt2);
/* 173 */     int j = getWorldZ(paramInt1, paramInt2);
/* 174 */     int k = getWorldX(paramInt3, paramInt4);
/* 175 */     int m = getWorldZ(paramInt3, paramInt4);
/* 176 */     return paramBoundingBox.intersects(Math.min(i, k), Math.min(j, m), Math.max(i, k), Math.max(j, m));
/*     */   }
/*     */   
/*     */   protected void spawnElder(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3) {
/* 180 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 181 */     if (paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 182 */       ElderGuardian elderGuardian = (ElderGuardian)EntityType.ELDER_GUARDIAN.create((Level)paramWorldGenLevel.getLevel(), EntitySpawnReason.STRUCTURE);
/* 183 */       if (elderGuardian != null) {
/* 184 */         elderGuardian.heal(elderGuardian.getMaxHealth());
/* 185 */         elderGuardian.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 186 */         elderGuardian.finalizeSpawn((ServerLevelAccessor)paramWorldGenLevel, paramWorldGenLevel.getCurrentDifficultyAt(elderGuardian.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/* 187 */         paramWorldGenLevel.addFreshEntityWithPassengers((Entity)elderGuardian);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanMonumentPieces$OceanMonumentPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */