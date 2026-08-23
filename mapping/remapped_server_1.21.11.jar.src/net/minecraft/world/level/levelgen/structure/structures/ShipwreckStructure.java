/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class ShipwreckStructure extends Structure {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)settingsCodec(paramInstance), (App)Codec.BOOL.fieldOf("is_beached").forGetter(())).apply((Applicative)paramInstance, ShipwreckStructure::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<ShipwreckStructure> CODEC;
/*    */   public final boolean isBeached;
/*    */   
/*    */   public ShipwreckStructure(Structure.StructureSettings paramStructureSettings, boolean paramBoolean) {
/* 26 */     super(paramStructureSettings);
/* 27 */     this.isBeached = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 32 */     Heightmap.Types types = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 33 */     return onTopOfChunkCenter(paramGenerationContext, types, paramStructurePiecesBuilder -> generatePieces(paramStructurePiecesBuilder, paramGenerationContext));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder paramStructurePiecesBuilder, Structure.GenerationContext paramGenerationContext) {
/* 37 */     Rotation rotation = Rotation.getRandom((RandomSource)paramGenerationContext.random());
/* 38 */     BlockPos blockPos = new BlockPos(paramGenerationContext.chunkPos().getMinBlockX(), 90, paramGenerationContext.chunkPos().getMinBlockZ());
/* 39 */     ShipwreckPieces.ShipwreckPiece shipwreckPiece = ShipwreckPieces.addRandomPiece(paramGenerationContext.structureTemplateManager(), blockPos, rotation, (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramGenerationContext.random(), this.isBeached);
/* 40 */     if (shipwreckPiece.isTooBigToFitInWorldGenRegion()) {
/* 41 */       int i; BoundingBox boundingBox = shipwreckPiece.getBoundingBox();
/*    */       
/* 43 */       if (this.isBeached) {
/* 44 */         int j = Structure.getLowestY(paramGenerationContext, boundingBox.minX(), boundingBox.getXSpan(), boundingBox.minZ(), boundingBox.getZSpan());
/* 45 */         i = shipwreckPiece.calculateBeachedPosition(j, (RandomSource)paramGenerationContext.random());
/*    */       } else {
/* 47 */         i = Structure.getMeanFirstOccupiedHeight(paramGenerationContext, boundingBox.minX(), boundingBox.getXSpan(), boundingBox.minZ(), boundingBox.getZSpan());
/*    */       } 
/* 49 */       shipwreckPiece.adjustPositionHeight(i);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 55 */     return StructureType.SHIPWRECK;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */