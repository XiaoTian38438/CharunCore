/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class BuriedTreasureStructure extends Structure {
/* 13 */   public static final MapCodec<BuriedTreasureStructure> CODEC = simpleCodec(BuriedTreasureStructure::new);
/*    */   
/*    */   public BuriedTreasureStructure(Structure.StructureSettings paramStructureSettings) {
/* 16 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 21 */     return onTopOfChunkCenter(paramGenerationContext, Heightmap.Types.OCEAN_FLOOR_WG, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 25 */     BlockPos blockPos = new BlockPos(paramGenerationContext.chunkPos().getBlockX(9), 90, paramGenerationContext.chunkPos().getBlockZ(9));
/* 26 */     paramStructurePiecesBuilder.addPiece(new BuriedTreasurePieces.BuriedTreasurePiece(blockPos));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 31 */     return StructureType.BURIED_TREASURE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\BuriedTreasureStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */