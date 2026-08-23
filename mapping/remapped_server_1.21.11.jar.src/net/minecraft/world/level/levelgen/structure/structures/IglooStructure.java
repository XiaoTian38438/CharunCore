/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class IglooStructure extends Structure {
/* 16 */   public static final MapCodec<IglooStructure> CODEC = simpleCodec(IglooStructure::new);
/*    */   
/*    */   public IglooStructure(Structure.StructureSettings paramStructureSettings) {
/* 19 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 24 */     return onTopOfChunkCenter(paramGenerationContext, Heightmap.Types.WORLD_SURFACE_WG, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 28 */     ChunkPos chunkPos = paramGenerationContext.chunkPos();
/* 29 */     WorldgenRandom worldgenRandom = paramGenerationContext.random();
/*    */     
/* 31 */     BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
/* 32 */     Rotation rotation = Rotation.getRandom((RandomSource)worldgenRandom);
/* 33 */     IglooPieces.addPieces(paramGenerationContext.structureTemplateManager(), blockPos, rotation, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)worldgenRandom);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 38 */     return StructureType.IGLOO;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */