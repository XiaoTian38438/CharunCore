/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class SwampHutStructure extends Structure {
/* 12 */   public static final MapCodec<SwampHutStructure> CODEC = simpleCodec(SwampHutStructure::new);
/*    */   
/*    */   public SwampHutStructure(Structure.StructureSettings paramStructureSettings) {
/* 15 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 20 */     return onTopOfChunkCenter(paramGenerationContext, Heightmap.Types.WORLD_SURFACE_WG, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 24 */     paramStructurePiecesBuilder.addPiece((StructurePiece)new SwampHutPiece((RandomSource)paramGenerationContext.random(), paramGenerationContext.chunkPos().getMinBlockX(), paramGenerationContext.chunkPos().getMinBlockZ()));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 29 */     return StructureType.SWAMP_HUT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\SwampHutStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */