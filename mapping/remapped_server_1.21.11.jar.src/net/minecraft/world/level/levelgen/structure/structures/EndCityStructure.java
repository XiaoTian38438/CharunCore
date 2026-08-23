/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class EndCityStructure extends Structure {
/* 16 */   public static final MapCodec<EndCityStructure> CODEC = simpleCodec(EndCityStructure::new);
/*    */   
/*    */   public EndCityStructure(Structure.StructureSettings paramStructureSettings) {
/* 19 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 24 */     Rotation rotation = Rotation.getRandom((RandomSource)paramGenerationContext.random());
/* 25 */     BlockPos blockPos = getLowestYIn5by5BoxOffset7Blocks(paramGenerationContext, rotation);
/*    */ 
/*    */     
/* 28 */     if (blockPos.getY() < 60) {
/* 29 */       return Optional.empty();
/*    */     }
/*    */     
/* 32 */     return Optional.of(new Structure.GenerationStub(blockPos, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramBlockPos, paramRotation, paramGenerationContext)));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, BlockPos paramBlockPos, Rotation paramRotation, Structure.GenerationContext paramGenerationContext) {
/* 36 */     ArrayList<StructurePiece> arrayList = Lists.newArrayList();
/* 37 */     EndCityPieces.startHouseTower(paramGenerationContext.structureTemplateManager(), paramBlockPos, paramRotation, arrayList, (RandomSource)paramGenerationContext.random());
/*    */     
/* 39 */     Objects.requireNonNull(paramStructurePiecesBuilder); arrayList.forEach(paramStructurePiecesBuilder::addPiece);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 44 */     return StructureType.END_CITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */