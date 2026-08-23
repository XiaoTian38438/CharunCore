/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
/*     */ public class OceanRuinPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   private final OceanRuinStructure.Type biomeType;
/*     */   private final float integrity;
/*     */   private final boolean isLarge;
/*     */   
/*     */   public OceanRuinPiece(StructureTemplateManager paramStructureTemplateManager, Identifier paramIdentifier, BlockPos paramBlockPos, Rotation paramRotation, float paramFloat, OceanRuinStructure.Type paramType, boolean paramBoolean) {
/* 233 */     super(StructurePieceType.OCEAN_RUIN, 0, paramStructureTemplateManager, paramIdentifier, paramIdentifier.toString(), makeSettings(paramRotation, paramFloat, paramType), paramBlockPos);
/*     */     
/* 235 */     this.integrity = paramFloat;
/* 236 */     this.biomeType = paramType;
/* 237 */     this.isLarge = paramBoolean;
/*     */   }
/*     */   
/*     */   private OceanRuinPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag, Rotation paramRotation, float paramFloat, OceanRuinStructure.Type paramType, boolean paramBoolean) {
/* 241 */     super(StructurePieceType.OCEAN_RUIN, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramRotation, paramFloat, paramType));
/*     */     
/* 243 */     this.integrity = paramFloat;
/* 244 */     this.biomeType = paramType;
/* 245 */     this.isLarge = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Rotation paramRotation, float paramFloat, OceanRuinStructure.Type paramType) {
/* 251 */     StructureProcessor structureProcessor = (paramType == OceanRuinStructure.Type.COLD) ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
/*     */     
/* 253 */     return (new StructurePlaceSettings())
/* 254 */       .setRotation(paramRotation)
/* 255 */       .setMirror(Mirror.NONE)
/* 256 */       .addProcessor((StructureProcessor)new BlockRotProcessor(paramFloat))
/* 257 */       .addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR)
/* 258 */       .addProcessor(structureProcessor);
/*     */   }
/*     */   
/*     */   public static OceanRuinPiece create(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/* 262 */     Rotation rotation = paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow();
/* 263 */     float f = paramCompoundTag.getFloatOr("Integrity", 0.0F);
/* 264 */     OceanRuinStructure.Type type = paramCompoundTag.read("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC).orElseThrow();
/* 265 */     boolean bool = paramCompoundTag.getBooleanOr("IsLarge", false);
/* 266 */     return new OceanRuinPiece(paramStructureTemplateManager, paramCompoundTag, rotation, f, type, bool);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 271 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 272 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 273 */     paramCompoundTag.putFloat("Integrity", this.integrity);
/* 274 */     paramCompoundTag.store("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC, this.biomeType);
/* 275 */     paramCompoundTag.putBoolean("IsLarge", this.isLarge);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/* 280 */     if ("chest".equals(paramString)) {
/* 281 */       paramServerLevelAccessor.setBlock(paramBlockPos, (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.WATERLOGGED, Boolean.valueOf(paramServerLevelAccessor.getFluidState(paramBlockPos).is(FluidTags.WATER))), 2);
/*     */       
/* 283 */       BlockEntity blockEntity = paramServerLevelAccessor.getBlockEntity(paramBlockPos);
/* 284 */       if (blockEntity instanceof ChestBlockEntity) {
/* 285 */         ((ChestBlockEntity)blockEntity).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, paramRandomSource.nextLong());
/*     */       }
/*     */     }
/* 288 */     else if ("drowned".equals(paramString)) {
/* 289 */       Drowned drowned = (Drowned)EntityType.DROWNED.create((Level)paramServerLevelAccessor.getLevel(), EntitySpawnReason.STRUCTURE);
/* 290 */       if (drowned != null) {
/* 291 */         drowned.setPersistenceRequired();
/* 292 */         drowned.snapTo(paramBlockPos, 0.0F, 0.0F);
/* 293 */         drowned.finalizeSpawn(paramServerLevelAccessor, paramServerLevelAccessor.getCurrentDifficultyAt(paramBlockPos), EntitySpawnReason.STRUCTURE, null);
/* 294 */         paramServerLevelAccessor.addFreshEntityWithPassengers((Entity)drowned);
/* 295 */         if (paramBlockPos.getY() > paramServerLevelAccessor.getSeaLevel()) {
/* 296 */           paramServerLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 2);
/*     */         } else {
/* 298 */           paramServerLevelAccessor.setBlock(paramBlockPos, Blocks.WATER.defaultBlockState(), 2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 306 */     int i = paramWorldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
/* 307 */     this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
/* 308 */     BlockPos blockPos = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.placeSettings.getRotation(), BlockPos.ZERO).offset((Vec3i)this.templatePosition);
/* 309 */     this.templatePosition = new BlockPos(this.templatePosition.getX(), getHeight(this.templatePosition, (BlockGetter)paramWorldGenLevel, blockPos), this.templatePosition.getZ());
/*     */     
/* 311 */     super.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/*     */   }
/*     */   
/*     */   private int getHeight(BlockPos paramBlockPos1, BlockGetter paramBlockGetter, BlockPos paramBlockPos2) {
/* 315 */     int i = paramBlockPos1.getY();
/* 316 */     int j = 512;
/* 317 */     int k = i - 1;
/* 318 */     byte b = 0;
/* 319 */     for (BlockPos blockPos : BlockPos.betweenClosed(paramBlockPos1, paramBlockPos2)) {
/* 320 */       int n = blockPos.getX();
/* 321 */       int i1 = blockPos.getZ();
/* 322 */       int i2 = paramBlockPos1.getY() - 1;
/* 323 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(n, i2, i1);
/* 324 */       BlockState blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 325 */       FluidState fluidState = paramBlockGetter.getFluidState((BlockPos)mutableBlockPos);
/* 326 */       while ((blockState.isAir() || fluidState.is(FluidTags.WATER) || blockState.is(BlockTags.ICE)) && i2 > paramBlockGetter.getMinY() + 1) {
/* 327 */         i2--;
/* 328 */         mutableBlockPos.set(n, i2, i1);
/* 329 */         blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 330 */         fluidState = paramBlockGetter.getFluidState((BlockPos)mutableBlockPos);
/*     */       } 
/*     */       
/* 333 */       j = Math.min(j, i2);
/* 334 */       if (i2 < k - 2) {
/* 335 */         b++;
/*     */       }
/*     */     } 
/*     */     
/* 339 */     int m = Math.abs(paramBlockPos1.getX() - paramBlockPos2.getX());
/* 340 */     if (k - j > 2 && b > m - 2) {
/* 341 */       i = j + 1;
/*     */     }
/*     */     
/* 344 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinPieces$OceanRuinPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */