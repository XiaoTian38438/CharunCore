/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
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
/*     */ public class ShipwreckPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   private final boolean isBeached;
/*     */   
/*     */   public ShipwreckPiece(StructureTemplateManager paramStructureTemplateManager, Identifier paramIdentifier, BlockPos paramBlockPos, Rotation paramRotation, boolean paramBoolean) {
/*  90 */     super(StructurePieceType.SHIPWRECK_PIECE, 0, paramStructureTemplateManager, paramIdentifier, paramIdentifier.toString(), makeSettings(paramRotation), paramBlockPos);
/*     */     
/*  92 */     this.isBeached = paramBoolean;
/*     */   }
/*     */   
/*     */   public ShipwreckPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/*  96 */     super(StructurePieceType.SHIPWRECK_PIECE, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */     
/*  98 */     this.isBeached = paramCompoundTag.getBooleanOr("isBeached", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 103 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 104 */     paramCompoundTag.putBoolean("isBeached", this.isBeached);
/* 105 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Rotation paramRotation) {
/* 109 */     return (new StructurePlaceSettings()).setRotation(paramRotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 114 */     ResourceKey resourceKey = ShipwreckPieces.MARKERS_TO_LOOT.get(paramString);
/* 115 */     if (resourceKey != null) {
/* 116 */       RandomizableContainer.setBlockEntityLootTable((BlockGetter)paramServerLevelAccessor, paramRandomSource, paramBlockPos.below(), resourceKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 122 */     if (isTooBigToFitInWorldGenRegion()) {
/*     */       
/* 124 */       super.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/*     */       
/*     */       return;
/*     */     } 
/* 128 */     int i = paramWorldGenLevel.getMaxY() + 1;
/* 129 */     int j = 0;
/* 130 */     Vec3i vec3i = this.template.getSize();
/* 131 */     Heightmap.Types types = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 132 */     int k = vec3i.getX() * vec3i.getZ();
/* 133 */     if (k == 0) {
/* 134 */       j = paramWorldGenLevel.getHeight(types, this.templatePosition.getX(), this.templatePosition.getZ());
/*     */     } else {
/* 136 */       BlockPos blockPos = this.templatePosition.offset(vec3i.getX() - 1, 0, vec3i.getZ() - 1);
/* 137 */       for (BlockPos blockPos1 : BlockPos.betweenClosed(this.templatePosition, blockPos)) {
/* 138 */         int m = paramWorldGenLevel.getHeight(types, blockPos1.getX(), blockPos1.getZ());
/* 139 */         j += m;
/* 140 */         i = Math.min(i, m);
/*     */       } 
/* 142 */       j /= k;
/*     */     } 
/* 144 */     adjustPositionHeight(this.isBeached ? calculateBeachedPosition(i, paramRandomSource) : j);
/* 145 */     super.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTooBigToFitInWorldGenRegion() {
/* 152 */     Vec3i vec3i = this.template.getSize();
/* 153 */     return (vec3i.getX() > 32 || vec3i.getY() > 32);
/*     */   }
/*     */   
/*     */   public int calculateBeachedPosition(int paramInt, RandomSource paramRandomSource) {
/* 157 */     return paramInt - this.template.getSize().getY() / 2 - paramRandomSource.nextInt(3);
/*     */   }
/*     */   
/*     */   public void adjustPositionHeight(int paramInt) {
/* 161 */     this.templatePosition = new BlockPos(this.templatePosition.getX(), paramInt, this.templatePosition.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckPieces$ShipwreckPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */