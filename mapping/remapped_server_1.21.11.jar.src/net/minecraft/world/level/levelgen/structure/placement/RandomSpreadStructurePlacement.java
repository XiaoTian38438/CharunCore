/*    */ package net.minecraft.world.level.levelgen.structure.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ 
/*    */ public class RandomSpreadStructurePlacement
/*    */   extends StructurePlacement
/*    */ {
/*    */   public static final MapCodec<RandomSpreadStructurePlacement> CODEC;
/*    */   private final int spacing;
/*    */   private final int separation;
/*    */   private final RandomSpreadType spreadType;
/*    */   
/*    */   static {
/* 27 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> placementCodec(paramInstance).and(paramInstance.group((App)Codec.intRange(0, 4096).fieldOf("spacing").forGetter(RandomSpreadStructurePlacement::spacing), (App)Codec.intRange(0, 4096).fieldOf("separation").forGetter(RandomSpreadStructurePlacement::separation), (App)RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(RandomSpreadStructurePlacement::spreadType))).apply((Applicative)paramInstance, RandomSpreadStructurePlacement::new)).validate(RandomSpreadStructurePlacement::validate);
/*    */   }
/*    */   private static DataResult<RandomSpreadStructurePlacement> validate(RandomSpreadStructurePlacement paramRandomSpreadStructurePlacement) {
/* 30 */     if (paramRandomSpreadStructurePlacement.spacing <= paramRandomSpreadStructurePlacement.separation) {
/* 31 */       return DataResult.error(() -> "Spacing has to be larger than separation");
/*    */     }
/* 33 */     return DataResult.success(paramRandomSpreadStructurePlacement);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RandomSpreadStructurePlacement(Vec3i paramVec3i, StructurePlacement.FrequencyReductionMethod paramFrequencyReductionMethod, float paramFloat, int paramInt1, Optional<StructurePlacement.ExclusionZone> paramOptional, int paramInt2, int paramInt3, RandomSpreadType paramRandomSpreadType) {
/* 41 */     super(paramVec3i, paramFrequencyReductionMethod, paramFloat, paramInt1, paramOptional);
/* 42 */     this.spacing = paramInt2;
/* 43 */     this.separation = paramInt3;
/* 44 */     this.spreadType = paramRandomSpreadType;
/*    */   }
/*    */   
/*    */   public RandomSpreadStructurePlacement(int paramInt1, int paramInt2, RandomSpreadType paramRandomSpreadType, int paramInt3) {
/* 48 */     this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, paramInt3, Optional.empty(), paramInt1, paramInt2, paramRandomSpreadType);
/*    */   }
/*    */   
/*    */   public int spacing() {
/* 52 */     return this.spacing;
/*    */   }
/*    */   
/*    */   public int separation() {
/* 56 */     return this.separation;
/*    */   }
/*    */   
/*    */   public RandomSpreadType spreadType() {
/* 60 */     return this.spreadType;
/*    */   }
/*    */   
/*    */   public ChunkPos getPotentialStructureChunk(long paramLong, int paramInt1, int paramInt2) {
/* 64 */     int i = Math.floorDiv(paramInt1, this.spacing);
/* 65 */     int j = Math.floorDiv(paramInt2, this.spacing);
/*    */     
/* 67 */     WorldgenRandom worldgenRandom = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/* 68 */     worldgenRandom.setLargeFeatureWithSalt(paramLong, i, j, salt());
/*    */     
/* 70 */     int k = this.spacing - this.separation;
/* 71 */     int m = this.spreadType.evaluate((RandomSource)worldgenRandom, k);
/* 72 */     int n = this.spreadType.evaluate((RandomSource)worldgenRandom, k);
/*    */     
/* 74 */     return new ChunkPos(i * this.spacing + m, j * this.spacing + n);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isPlacementChunk(ChunkGeneratorStructureState paramChunkGeneratorStructureState, int paramInt1, int paramInt2) {
/* 82 */     ChunkPos chunkPos = getPotentialStructureChunk(paramChunkGeneratorStructureState.getLevelSeed(), paramInt1, paramInt2);
/* 83 */     return (chunkPos.x == paramInt1 && chunkPos.z == paramInt2);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePlacementType<?> type() {
/* 88 */     return StructurePlacementType.RANDOM_SPREAD;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\placement\RandomSpreadStructurePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */