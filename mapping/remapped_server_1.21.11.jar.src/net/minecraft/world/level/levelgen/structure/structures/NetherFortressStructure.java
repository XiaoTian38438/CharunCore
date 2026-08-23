/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class NetherFortressStructure extends Structure {
/* 18 */   public static final WeightedList<MobSpawnSettings.SpawnerData> FORTRESS_ENEMIES = WeightedList.builder()
/* 19 */     .add(new MobSpawnSettings.SpawnerData(EntityType.BLAZE, 2, 3), 10)
/* 20 */     .add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 4, 4), 5)
/* 21 */     .add(new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 5, 5), 8)
/* 22 */     .add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 5), 2)
/* 23 */     .add(new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 4), 3)
/* 24 */     .build();
/*    */   
/* 26 */   public static final MapCodec<NetherFortressStructure> CODEC = simpleCodec(NetherFortressStructure::new);
/*    */   
/*    */   public NetherFortressStructure(Structure.StructureSettings paramStructureSettings) {
/* 29 */     super(paramStructureSettings);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 34 */     ChunkPos chunkPos = paramGenerationContext.chunkPos();
/*    */     
/* 36 */     BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), 64, chunkPos.getMinBlockZ());
/* 37 */     return Optional.of(new Structure.GenerationStub(blockPos, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext)));
/*    */   }
/*    */ 
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 42 */     NetherFortressPieces.StartPiece startPiece = new NetherFortressPieces.StartPiece((RandomSource)paramGenerationContext.random(), paramGenerationContext.chunkPos().getBlockX(2), paramGenerationContext.chunkPos().getBlockZ(2));
/* 43 */     paramStructurePiecesBuilder.addPiece(startPiece);
/* 44 */     startPiece.addChildren(startPiece, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramGenerationContext.random());
/*    */     
/* 46 */     List<StructurePiece> list = startPiece.pendingChildren;
/* 47 */     while (!list.isEmpty()) {
/* 48 */       int i = paramGenerationContext.random().nextInt(list.size());
/* 49 */       StructurePiece structurePiece = list.remove(i);
/* 50 */       structurePiece.addChildren(startPiece, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramGenerationContext.random());
/*    */     } 
/*    */ 
/*    */     
/* 54 */     paramStructurePiecesBuilder.moveInsideHeights((RandomSource)paramGenerationContext.random(), 48, 70);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 59 */     return StructureType.FORTRESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */