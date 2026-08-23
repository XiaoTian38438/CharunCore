/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
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
/*     */ public class IglooPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   public IglooPiece(StructureTemplateManager paramStructureTemplateManager, Identifier paramIdentifier, BlockPos paramBlockPos, Rotation paramRotation, int paramInt) {
/*  68 */     super(StructurePieceType.IGLOO, 0, paramStructureTemplateManager, paramIdentifier, paramIdentifier.toString(), makeSettings(paramRotation, paramIdentifier), makePosition(paramIdentifier, paramBlockPos, paramInt));
/*     */   }
/*     */   
/*     */   public IglooPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/*  72 */     super(StructurePieceType.IGLOO, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow(), paramIdentifier));
/*     */   }
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Rotation paramRotation, Identifier paramIdentifier) {
/*  76 */     return (new StructurePlaceSettings()).setRotation(paramRotation).setMirror(Mirror.NONE).setRotationPivot(IglooPieces.PIVOTS.get(paramIdentifier)).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
/*     */   }
/*     */   
/*     */   private static BlockPos makePosition(Identifier paramIdentifier, BlockPos paramBlockPos, int paramInt) {
/*  80 */     return paramBlockPos.offset((Vec3i)IglooPieces.OFFSETS.get(paramIdentifier)).below(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  85 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*  86 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {
/*  91 */     if (!"chest".equals(paramString)) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     paramServerLevelAccessor.setBlock(paramBlockPos, Blocks.AIR.defaultBlockState(), 3);
/*  96 */     BlockEntity blockEntity = paramServerLevelAccessor.getBlockEntity(paramBlockPos.below());
/*  97 */     if (blockEntity instanceof ChestBlockEntity) {
/*  98 */       ((ChestBlockEntity)blockEntity).setLootTable(BuiltInLootTables.IGLOO_CHEST, paramRandomSource.nextLong());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 104 */     Identifier identifier = Identifier.parse(this.templateName);
/*     */     
/* 106 */     StructurePlaceSettings structurePlaceSettings = makeSettings(this.placeSettings.getRotation(), identifier);
/*     */     
/* 108 */     BlockPos blockPos1 = IglooPieces.OFFSETS.get(identifier);
/* 109 */     BlockPos blockPos2 = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(structurePlaceSettings, new BlockPos(3 - blockPos1.getX(), 0, -blockPos1.getZ())));
/* 110 */     int i = paramWorldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockPos2.getX(), blockPos2.getZ());
/* 111 */     BlockPos blockPos3 = this.templatePosition;
/* 112 */     this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
/*     */     
/* 114 */     super.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/*     */     
/* 116 */     if (identifier.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
/* 117 */       BlockPos blockPos = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(structurePlaceSettings, new BlockPos(3, 0, 5)));
/* 118 */       BlockState blockState = paramWorldGenLevel.getBlockState(blockPos.below());
/* 119 */       if (!blockState.isAir() && !blockState.is(Blocks.LADDER)) {
/* 120 */         paramWorldGenLevel.setBlock(blockPos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
/*     */       }
/*     */     } 
/*     */     
/* 124 */     this.templatePosition = blockPos3;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooPieces$IglooPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */