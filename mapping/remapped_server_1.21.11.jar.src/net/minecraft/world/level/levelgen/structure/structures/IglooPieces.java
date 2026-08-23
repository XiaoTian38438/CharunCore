/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
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
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
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
/*     */ public class IglooPieces {
/*     */   public static final int GENERATION_HEIGHT = 90;
/*  38 */   static final Identifier STRUCTURE_LOCATION_IGLOO = Identifier.withDefaultNamespace("igloo/top");
/*  39 */   private static final Identifier STRUCTURE_LOCATION_LADDER = Identifier.withDefaultNamespace("igloo/middle");
/*  40 */   private static final Identifier STRUCTURE_LOCATION_LABORATORY = Identifier.withDefaultNamespace("igloo/bottom");
/*     */   
/*  42 */   static final Map<Identifier, BlockPos> PIVOTS = (Map<Identifier, BlockPos>)ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   static final Map<Identifier, BlockPos> OFFSETS = (Map<Identifier, BlockPos>)ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPieces(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/*  55 */     if (paramRandomSource.nextDouble() < 0.5D) {
/*  56 */       int i = paramRandomSource.nextInt(8) + 4;
/*  57 */       paramStructurePieceAccessor.addPiece((StructurePiece)new IglooPiece(paramStructureTemplateManager, STRUCTURE_LOCATION_LABORATORY, paramBlockPos, paramRotation, i * 3));
/*  58 */       for (byte b = 0; b < i - 1; b++) {
/*  59 */         paramStructurePieceAccessor.addPiece((StructurePiece)new IglooPiece(paramStructureTemplateManager, STRUCTURE_LOCATION_LADDER, paramBlockPos, paramRotation, b * 3));
/*     */       }
/*     */     } 
/*     */     
/*  63 */     paramStructurePieceAccessor.addPiece((StructurePiece)new IglooPiece(paramStructureTemplateManager, STRUCTURE_LOCATION_IGLOO, paramBlockPos, paramRotation, 0));
/*     */   }
/*     */   
/*     */   public static class IglooPiece extends TemplateStructurePiece {
/*     */     public IglooPiece(StructureTemplateManager param1StructureTemplateManager, Identifier param1Identifier, BlockPos param1BlockPos, Rotation param1Rotation, int param1Int) {
/*  68 */       super(StructurePieceType.IGLOO, 0, param1StructureTemplateManager, param1Identifier, param1Identifier.toString(), makeSettings(param1Rotation, param1Identifier), makePosition(param1Identifier, param1BlockPos, param1Int));
/*     */     }
/*     */     
/*     */     public IglooPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/*  72 */       super(StructurePieceType.IGLOO, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow(), param1Identifier));
/*     */     }
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(Rotation param1Rotation, Identifier param1Identifier) {
/*  76 */       return (new StructurePlaceSettings()).setRotation(param1Rotation).setMirror(Mirror.NONE).setRotationPivot(IglooPieces.PIVOTS.get(param1Identifier)).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK).setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
/*     */     }
/*     */     
/*     */     private static BlockPos makePosition(Identifier param1Identifier, BlockPos param1BlockPos, int param1Int) {
/*  80 */       return param1BlockPos.offset((Vec3i)IglooPieces.OFFSETS.get(param1Identifier)).below(param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  85 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/*  86 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/*  91 */       if (!"chest".equals(param1String)) {
/*     */         return;
/*     */       }
/*     */       
/*  95 */       param1ServerLevelAccessor.setBlock(param1BlockPos, Blocks.AIR.defaultBlockState(), 3);
/*  96 */       BlockEntity blockEntity = param1ServerLevelAccessor.getBlockEntity(param1BlockPos.below());
/*  97 */       if (blockEntity instanceof ChestBlockEntity) {
/*  98 */         ((ChestBlockEntity)blockEntity).setLootTable(BuiltInLootTables.IGLOO_CHEST, param1RandomSource.nextLong());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 104 */       Identifier identifier = Identifier.parse(this.templateName);
/*     */       
/* 106 */       StructurePlaceSettings structurePlaceSettings = makeSettings(this.placeSettings.getRotation(), identifier);
/*     */       
/* 108 */       BlockPos blockPos1 = IglooPieces.OFFSETS.get(identifier);
/* 109 */       BlockPos blockPos2 = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(structurePlaceSettings, new BlockPos(3 - blockPos1.getX(), 0, -blockPos1.getZ())));
/* 110 */       int i = param1WorldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockPos2.getX(), blockPos2.getZ());
/* 111 */       BlockPos blockPos3 = this.templatePosition;
/* 112 */       this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
/*     */       
/* 114 */       super.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/*     */       
/* 116 */       if (identifier.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
/* 117 */         BlockPos blockPos = this.templatePosition.offset((Vec3i)StructureTemplate.calculateRelativePosition(structurePlaceSettings, new BlockPos(3, 0, 5)));
/* 118 */         BlockState blockState = param1WorldGenLevel.getBlockState(blockPos.below());
/* 119 */         if (!blockState.isAir() && !blockState.is(Blocks.LADDER)) {
/* 120 */           param1WorldGenLevel.setBlock(blockPos, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
/*     */         }
/*     */       } 
/*     */       
/* 124 */       this.templatePosition = blockPos3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */