/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class NetherFossilPieces {
/* 27 */   private static final Identifier[] FOSSILS = new Identifier[] { 
/* 28 */       Identifier.withDefaultNamespace("nether_fossils/fossil_1"), 
/* 29 */       Identifier.withDefaultNamespace("nether_fossils/fossil_2"), 
/* 30 */       Identifier.withDefaultNamespace("nether_fossils/fossil_3"), 
/* 31 */       Identifier.withDefaultNamespace("nether_fossils/fossil_4"), 
/* 32 */       Identifier.withDefaultNamespace("nether_fossils/fossil_5"), 
/* 33 */       Identifier.withDefaultNamespace("nether_fossils/fossil_6"), 
/* 34 */       Identifier.withDefaultNamespace("nether_fossils/fossil_7"), 
/* 35 */       Identifier.withDefaultNamespace("nether_fossils/fossil_8"), 
/* 36 */       Identifier.withDefaultNamespace("nether_fossils/fossil_9"), 
/* 37 */       Identifier.withDefaultNamespace("nether_fossils/fossil_10"), 
/* 38 */       Identifier.withDefaultNamespace("nether_fossils/fossil_11"), 
/* 39 */       Identifier.withDefaultNamespace("nether_fossils/fossil_12"), 
/* 40 */       Identifier.withDefaultNamespace("nether_fossils/fossil_13"), 
/* 41 */       Identifier.withDefaultNamespace("nether_fossils/fossil_14") };
/*    */ 
/*    */   
/*    */   public static void addPieces(StructureTemplateManager paramStructureTemplateManager, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 45 */     Rotation rotation = Rotation.getRandom(paramRandomSource);
/* 46 */     paramStructurePieceAccessor.addPiece((StructurePiece)new NetherFossilPiece(paramStructureTemplateManager, (Identifier)Util.getRandom((Object[])FOSSILS, paramRandomSource), paramBlockPos, rotation));
/*    */   }
/*    */   
/*    */   public static class NetherFossilPiece extends TemplateStructurePiece {
/*    */     public NetherFossilPiece(StructureTemplateManager param1StructureTemplateManager, Identifier param1Identifier, BlockPos param1BlockPos, Rotation param1Rotation) {
/* 51 */       super(StructurePieceType.NETHER_FOSSIL, 0, param1StructureTemplateManager, param1Identifier, param1Identifier.toString(), makeSettings(param1Rotation), param1BlockPos);
/*    */     }
/*    */     
/*    */     public NetherFossilPiece(StructureTemplateManager param1StructureTemplateManager, CompoundTag param1CompoundTag) {
/* 55 */       super(StructurePieceType.NETHER_FOSSIL, param1CompoundTag, param1StructureTemplateManager, param1Identifier -> makeSettings(param1CompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*    */     }
/*    */     
/*    */     private static StructurePlaceSettings makeSettings(Rotation param1Rotation) {
/* 59 */       return (new StructurePlaceSettings()).setRotation(param1Rotation).setMirror(Mirror.NONE).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 64 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 65 */       param1CompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected void handleDataMarker(String param1String, BlockPos param1BlockPos, ServerLevelAccessor param1ServerLevelAccessor, RandomSource param1RandomSource, BoundingBox param1BoundingBox) {}
/*    */ 
/*    */     
/*    */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 74 */       BoundingBox boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 75 */       param1BoundingBox.encapsulate(boundingBox);
/* 76 */       super.postProcess(param1WorldGenLevel, param1StructureManager, param1ChunkGenerator, param1RandomSource, param1BoundingBox, param1ChunkPos, param1BlockPos);
/* 77 */       placeDriedGhast(param1WorldGenLevel, param1RandomSource, boundingBox, param1BoundingBox);
/*    */     }
/*    */     
/*    */     private void placeDriedGhast(WorldGenLevel param1WorldGenLevel, RandomSource param1RandomSource, BoundingBox param1BoundingBox1, BoundingBox param1BoundingBox2) {
/* 81 */       RandomSource randomSource = RandomSource.create(param1WorldGenLevel.getSeed()).forkPositional().at(param1BoundingBox1.getCenter());
/* 82 */       if (randomSource.nextFloat() < 0.5F) {
/* 83 */         int i = param1BoundingBox1.minX() + randomSource.nextInt(param1BoundingBox1.getXSpan());
/* 84 */         int j = param1BoundingBox1.minY();
/* 85 */         int k = param1BoundingBox1.minZ() + randomSource.nextInt(param1BoundingBox1.getZSpan());
/* 86 */         BlockPos blockPos = new BlockPos(i, j, k);
/*    */         
/* 88 */         if (param1WorldGenLevel.getBlockState(blockPos).isAir() && param1BoundingBox2.isInside((Vec3i)blockPos))
/* 89 */           param1WorldGenLevel.setBlock(blockPos, Blocks.DRIED_GHAST.defaultBlockState().rotate(Rotation.getRandom(randomSource)), 2); 
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */