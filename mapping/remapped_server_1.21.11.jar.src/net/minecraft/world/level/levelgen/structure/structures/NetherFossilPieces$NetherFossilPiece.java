/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NetherFossilPiece
/*    */   extends TemplateStructurePiece
/*    */ {
/*    */   public NetherFossilPiece(StructureTemplateManager paramStructureTemplateManager, Identifier paramIdentifier, BlockPos paramBlockPos, Rotation paramRotation) {
/* 51 */     super(StructurePieceType.NETHER_FOSSIL, 0, paramStructureTemplateManager, paramIdentifier, paramIdentifier.toString(), makeSettings(paramRotation), paramBlockPos);
/*    */   }
/*    */   
/*    */   public NetherFossilPiece(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag) {
/* 55 */     super(StructurePieceType.NETHER_FOSSIL, paramCompoundTag, paramStructureTemplateManager, paramIdentifier -> makeSettings(paramCompoundTag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*    */   }
/*    */   
/*    */   private static StructurePlaceSettings makeSettings(Rotation paramRotation) {
/* 59 */     return (new StructurePlaceSettings()).setRotation(paramRotation).setMirror(Mirror.NONE).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 64 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 65 */     paramCompoundTag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, RandomSource paramRandomSource, BoundingBox paramBoundingBox) {}
/*    */ 
/*    */   
/*    */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 74 */     BoundingBox boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/* 75 */     paramBoundingBox.encapsulate(boundingBox);
/* 76 */     super.postProcess(paramWorldGenLevel, paramStructureManager, paramChunkGenerator, paramRandomSource, paramBoundingBox, paramChunkPos, paramBlockPos);
/* 77 */     placeDriedGhast(paramWorldGenLevel, paramRandomSource, boundingBox, paramBoundingBox);
/*    */   }
/*    */   
/*    */   private void placeDriedGhast(WorldGenLevel paramWorldGenLevel, RandomSource paramRandomSource, BoundingBox paramBoundingBox1, BoundingBox paramBoundingBox2) {
/* 81 */     RandomSource randomSource = RandomSource.create(paramWorldGenLevel.getSeed()).forkPositional().at(paramBoundingBox1.getCenter());
/* 82 */     if (randomSource.nextFloat() < 0.5F) {
/* 83 */       int i = paramBoundingBox1.minX() + randomSource.nextInt(paramBoundingBox1.getXSpan());
/* 84 */       int j = paramBoundingBox1.minY();
/* 85 */       int k = paramBoundingBox1.minZ() + randomSource.nextInt(paramBoundingBox1.getZSpan());
/* 86 */       BlockPos blockPos = new BlockPos(i, j, k);
/*    */       
/* 88 */       if (paramWorldGenLevel.getBlockState(blockPos).isAir() && paramBoundingBox2.isInside((Vec3i)blockPos))
/* 89 */         paramWorldGenLevel.setBlock(blockPos, Blocks.DRIED_GHAST.defaultBlockState().rotate(Rotation.getRandom(randomSource)), 2); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilPieces$NetherFossilPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */