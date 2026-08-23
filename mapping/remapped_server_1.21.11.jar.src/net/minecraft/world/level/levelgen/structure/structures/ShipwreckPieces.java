/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class ShipwreckPieces {
/*  34 */   static final BlockPos PIVOT = new BlockPos(4, 0, 15);
/*     */   private static final int NUMBER_OF_BLOCKS_ALLOWED_IN_WORLD_GEN_REGION = 32;
/*  36 */   private static final Identifier[] STRUCTURE_LOCATION_BEACHED = new Identifier[] { 
/*  37 */       Identifier.withDefaultNamespace("shipwreck/with_mast"), 
/*  38 */       Identifier.withDefaultNamespace("shipwreck/sideways_full"), 
/*  39 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"), 
/*  40 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"), 
/*  41 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full"), 
/*  42 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"), 
/*  43 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"), 
/*  44 */       Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"), 
/*  45 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"), 
/*  46 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"), 
/*  47 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */   
/*  50 */   private static final Identifier[] STRUCTURE_LOCATION_OCEAN = new Identifier[] { 
/*  51 */       Identifier.withDefaultNamespace("shipwreck/with_mast"), 
/*  52 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_full"), 
/*  53 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf"), 
/*  54 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf"), 
/*  55 */       Identifier.withDefaultNamespace("shipwreck/sideways_full"), 
/*  56 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"), 
/*  57 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"), 
/*  58 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full"), 
/*  59 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"), 
/*  60 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"), 
/*  61 */       Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"), 
/*  62 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_full_degraded"), 
/*  63 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf_degraded"), 
/*  64 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf_degraded"), 
/*  65 */       Identifier.withDefaultNamespace("shipwreck/sideways_full_degraded"), 
/*  66 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf_degraded"), 
/*  67 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf_degraded"), 
/*  68 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"), 
/*  69 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"), 
/*  70 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */   
/*  73 */   static final Map<String, ResourceKey<LootTable>> MARKERS_TO_LOOT = Map.of("map_chest", BuiltInLootTables.SHIPWRECK_MAP, "treasure_chest", BuiltInLootTables.SHIPWRECK_TREASURE, "supply_chest", BuiltInLootTables.SHIPWRECK_SUPPLY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShipwreckPiece addRandomPiece(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, boolean paramBoolean) {
/*  80 */     Identifier identifier = (Identifier)Util.getRandom(paramBoolean ? (Object[])STRUCTURE_LOCATION_BEACHED : (Object[])STRUCTURE_LOCATION_OCEAN, paramRandomSource);
/*  81 */     ShipwreckPiece shipwreckPiece = new ShipwreckPiece(paramStructureTemplateManager, identifier, paramBlockPos, paramRotation, paramBoolean);
/*  82 */     paramStructurePieceAccessor.addPiece((StructurePiece)shipwreckPiece);
/*  83 */     return shipwreckPiece;
/*     */   }
/*     */   
/*     */   public static class ShipwreckPiece extends TemplateStructurePiece {
/*     */     private final boolean isBeached;
/*     */     
/*     */     public ShipwreckPiece(StructureTemplateManager param1StructureTemplateManager, Identifier param1Identifier, BlockPos param1BlockPos, Rotation param1Rotation, boolean param1Boolean) {
/*  90 */       super(StructurePieceType.SHIPWRECK_PIECE, 0, param1StructureTemplateManager, param1Identifier, param1Identifier.toString(), makeSettings(param1Rotation), param1BlockPos);
/*     */       
/*  92 */       this.isBeached = param1Boolean;
/*     */     }
/*     */     
/*     */     public ShipwreckPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/*  96 */       super(StructurePieceType.SHIPWRECK_PIECE, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */       
/*  98 */       this.isBeached = param1CompoundTag.getBooleanOr("isBeached", false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 103 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 104 */       param1CompoundTag.putBoolean("isBeached", this.isBeached);
/* 105 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */     }
/*     */     
/*     */     private static StructurePlaceSettings makeSettings(Rotation param1Rotation) {
/* 109 */       return (new StructurePlaceSettings()).setRotation(param1Rotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {
/* 114 */       ResourceKey resourceKey = ShipwreckPieces.MARKERS_TO_LOOT.get(param1String);
/* 115 */       if (resourceKey != null) {
/* 116 */         RandomizableContainer.setBlockEntityLootTable((BlockGetter)param1ServerLevelAccessor, param1RandomSource, param1BlockPos.below(), resourceKey);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 122 */       if (isTooBigToFitInWorldGenRegion()) {
/*     */         
/* 124 */         super.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       int i = param1WorldGenLevel.getMaxY() + 1;
/* 129 */       int j = 0;
/* 130 */       Vec3i vec3i = this.template.getSize();
/* 131 */       Heightmap.Types types = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 132 */       int k = vec3i.getX() * vec3i.getZ();
/* 133 */       if (k == 0) {
/* 134 */         j = param1WorldGenLevel.getHeight(types, this.templatePosition.getX(), this.templatePosition.getZ());
/*     */       } else {
/* 136 */         BlockPos blockPos = this.templatePosition.offset(vec3i.getX() - 1, 0, vec3i.getZ() - 1);
/* 137 */         for (BlockPos blockPos1 : BlockPos.betweenClosed(this.templatePosition, blockPos)) {
/* 138 */           int m = param1WorldGenLevel.getHeight(types, blockPos1.getX(), blockPos1.getZ());
/* 139 */           j += m;
/* 140 */           i = Math.min(i, m);
/*     */         } 
/* 142 */         j /= k;
/*     */       } 
/* 144 */       adjustPositionHeight(this.isBeached ? calculateBeachedPosition(i, param1RandomSource) : j);
/* 145 */       super.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isTooBigToFitInWorldGenRegion() {
/* 152 */       Vec3i vec3i = this.template.getSize();
/* 153 */       return (vec3i.getX() > 32 || vec3i.getY() > 32);
/*     */     }
/*     */     
/*     */     public int calculateBeachedPosition(int param1Int, RandomSource param1RandomSource) {
/* 157 */       return param1Int - this.template.getSize().getY() / 2 - param1RandomSource.nextInt(3);
/*     */     }
/*     */     
/*     */     public void adjustPositionHeight(int param1Int) {
/* 161 */       this.templatePosition = new BlockPos(this.templatePosition.getX(), param1Int, this.templatePosition.getZ());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */