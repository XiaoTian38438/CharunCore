/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.NoiseColumn;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class NetherFossilStructure extends Structure {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)settingsCodec(paramInstance), (App)HeightProvider.CODEC.fieldOf("height").forGetter(())).apply((Applicative)paramInstance, NetherFossilStructure::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NetherFossilStructure> CODEC;
/*    */   public final HeightProvider height;
/*    */   
/*    */   public NetherFossilStructure(Structure.StructureSettings paramStructureSettings, HeightProvider paramHeightProvider) {
/* 29 */     super(paramStructureSettings);
/* 30 */     this.height = paramHeightProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext paramGenerationContext) {
/* 35 */     WorldgenRandom worldgenRandom = paramGenerationContext.random();
/* 36 */     int i = paramGenerationContext.chunkPos().getMinBlockX() + worldgenRandom.nextInt(16);
/* 37 */     int j = paramGenerationContext.chunkPos().getMinBlockZ() + worldgenRandom.nextInt(16);
/*    */     
/* 39 */     int k = paramGenerationContext.chunkGenerator().getSeaLevel();
/*    */ 
/*    */     
/* 42 */     WorldGenerationContext worldGenerationContext = new WorldGenerationContext(paramGenerationContext.chunkGenerator(), paramGenerationContext.heightAccessor());
/*    */     
/* 44 */     int m = this.height.sample((RandomSource)worldgenRandom, worldGenerationContext);
/*    */     
/* 46 */     NoiseColumn noiseColumn = paramGenerationContext.chunkGenerator().getBaseColumn(i, j, paramGenerationContext.heightAccessor(), paramGenerationContext.randomState());
/*    */     
/* 48 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(i, m, j);
/* 49 */     while (m > k) {
/* 50 */       BlockState blockState1 = noiseColumn.getBlock(m);
/*    */       
/* 52 */       m--;
/*    */       
/* 54 */       BlockState blockState2 = noiseColumn.getBlock(m);
/* 55 */       if (blockState1.isAir() && (blockState2.is(Blocks.SOUL_SAND) || blockState2.isFaceSturdy((BlockGetter)EmptyBlockGetter.INSTANCE, (BlockPos)mutableBlockPos.setY(m), Direction.UP))) {
/*    */         break;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     if (m <= k) {
/* 61 */       return Optional.empty();
/*    */     }
/*    */     
/* 64 */     BlockPos blockPos = new BlockPos(i, m, j);
/* 65 */     return Optional.of(new Structure.GenerationStub(blockPos, paramStructurePiecesBuilder -> NetherFossilPieces.addPieces(paramGenerationContext.structureTemplateManager(), (StructurePieceAccessor)paramStructurePiecesBuilder, (RandomSource)paramWorldgenRandom, paramBlockPos)));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureType<?> type() {
/* 70 */     return StructureType.NETHER_FOSSIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFossilStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */