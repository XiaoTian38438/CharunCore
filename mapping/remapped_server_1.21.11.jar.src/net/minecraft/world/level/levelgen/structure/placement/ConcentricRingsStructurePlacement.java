/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function9;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*    */ 
/*    */ public class ConcentricRingsStructurePlacement extends StructurePlacement {
/*    */   public static final MapCodec<ConcentricRingsStructurePlacement> CODEC;
/*    */   private final int distance;
/*    */   
/*    */   private static Products.P9<RecordCodecBuilder.Mu<ConcentricRingsStructurePlacement>, Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>, Integer, Integer, Integer, HolderSet<Biome>> codec(RecordCodecBuilder.Instance<ConcentricRingsStructurePlacement> paramInstance) {
/* 20 */     Products.P5<RecordCodecBuilder.Mu<ConcentricRingsStructurePlacement>, Vec3i, StructurePlacement.FrequencyReductionMethod, Float, Integer, Optional<StructurePlacement.ExclusionZone>> p5 = placementCodec(paramInstance);
/* 21 */     Products.P4 p4 = paramInstance.group(
/* 22 */         (App)Codec.intRange(0, 1023).fieldOf("distance").forGetter(ConcentricRingsStructurePlacement::distance), 
/* 23 */         (App)Codec.intRange(0, 1023).fieldOf("spread").forGetter(ConcentricRingsStructurePlacement::spread), 
/* 24 */         (App)Codec.intRange(1, 4095).fieldOf("count").forGetter(ConcentricRingsStructurePlacement::count), 
/* 25 */         (App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("preferred_biomes").forGetter(ConcentricRingsStructurePlacement::preferredBiomes));
/*    */     
/* 27 */     return new Products.P9(p5.t1(), p5.t2(), p5.t3(), p5.t4(), p5.t5(), p4.t1(), p4.t2(), p4.t3(), p4.t4());
/*    */   } private final int spread; private final int count; private final HolderSet<Biome> preferredBiomes;
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> codec(paramInstance).apply((Applicative)paramInstance, ConcentricRingsStructurePlacement::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConcentricRingsStructurePlacement(Vec3i paramVec3i, StructurePlacement.FrequencyReductionMethod paramFrequencyReductionMethod, float paramFloat, int paramInt1, Optional<StructurePlacement.ExclusionZone> paramOptional, int paramInt2, int paramInt3, int paramInt4, HolderSet<Biome> paramHolderSet) {
/* 38 */     super(paramVec3i, paramFrequencyReductionMethod, paramFloat, paramInt1, paramOptional);
/* 39 */     this.distance = paramInt2;
/* 40 */     this.spread = paramInt3;
/* 41 */     this.count = paramInt4;
/* 42 */     this.preferredBiomes = paramHolderSet;
/*    */   }
/*    */   
/*    */   public ConcentricRingsStructurePlacement(int paramInt1, int paramInt2, int paramInt3, HolderSet<Biome> paramHolderSet) {
/* 46 */     this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, 0, Optional.empty(), paramInt1, paramInt2, paramInt3, paramHolderSet);
/*    */   }
/*    */   
/*    */   public int distance() {
/* 50 */     return this.distance;
/*    */   }
/*    */   
/*    */   public int spread() {
/* 54 */     return this.spread;
/*    */   }
/*    */   
/*    */   public int count() {
/* 58 */     return this.count;
/*    */   }
/*    */   
/*    */   public HolderSet<Biome> preferredBiomes() {
/* 62 */     return this.preferredBiomes;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPlacementChunk(ChunkGeneratorStructureState paramChunkGeneratorStructureState, int paramInt1, int paramInt2) {
/* 67 */     List list = paramChunkGeneratorStructureState.getRingPositionsFor(this);
/* 68 */     if (list == null) {
/* 69 */       return false;
/*    */     }
/* 71 */     return list.contains(new ChunkPos(paramInt1, paramInt2));
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePlacementType<?> type() {
/* 76 */     return StructurePlacementType.CONCENTRIC_RINGS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\placement\ConcentricRingsStructurePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */