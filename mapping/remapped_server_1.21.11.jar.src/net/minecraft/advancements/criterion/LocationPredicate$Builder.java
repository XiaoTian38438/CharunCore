/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  99 */   private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;
/* 100 */   private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;
/* 101 */   private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;
/*     */   
/* 103 */   private Optional<HolderSet<Biome>> biomes = Optional.empty();
/* 104 */   private Optional<HolderSet<Structure>> structures = Optional.empty();
/* 105 */   private Optional<ResourceKey<Level>> dimension = Optional.empty();
/* 106 */   private Optional<Boolean> smokey = Optional.empty();
/*     */   
/* 108 */   private Optional<LightPredicate> light = Optional.empty();
/* 109 */   private Optional<BlockPredicate> block = Optional.empty();
/* 110 */   private Optional<FluidPredicate> fluid = Optional.empty();
/* 111 */   private Optional<Boolean> canSeeSky = Optional.empty();
/*     */   
/*     */   public static Builder location() {
/* 114 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder inBiome(Holder<Biome> paramHolder) {
/* 118 */     return location().setBiomes((HolderSet<Biome>)HolderSet.direct(new Holder[] { paramHolder }));
/*     */   }
/*     */   
/*     */   public static Builder inDimension(ResourceKey<Level> paramResourceKey) {
/* 122 */     return location().setDimension(paramResourceKey);
/*     */   }
/*     */   
/*     */   public static Builder inStructure(Holder<Structure> paramHolder) {
/* 126 */     return location().setStructures((HolderSet<Structure>)HolderSet.direct(new Holder[] { paramHolder }));
/*     */   }
/*     */   
/*     */   public static Builder atYLocation(MinMaxBounds.Doubles paramDoubles) {
/* 130 */     return location().setY(paramDoubles);
/*     */   }
/*     */   
/*     */   public Builder setX(MinMaxBounds.Doubles paramDoubles) {
/* 134 */     this.x = paramDoubles;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setY(MinMaxBounds.Doubles paramDoubles) {
/* 139 */     this.y = paramDoubles;
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setZ(MinMaxBounds.Doubles paramDoubles) {
/* 144 */     this.z = paramDoubles;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setBiomes(HolderSet<Biome> paramHolderSet) {
/* 149 */     this.biomes = Optional.of(paramHolderSet);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setStructures(HolderSet<Structure> paramHolderSet) {
/* 154 */     this.structures = Optional.of(paramHolderSet);
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDimension(ResourceKey<Level> paramResourceKey) {
/* 159 */     this.dimension = Optional.of(paramResourceKey);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLight(LightPredicate.Builder paramBuilder) {
/* 164 */     this.light = Optional.of(paramBuilder.build());
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setBlock(BlockPredicate.Builder paramBuilder) {
/* 169 */     this.block = Optional.of(paramBuilder.build());
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setFluid(FluidPredicate.Builder paramBuilder) {
/* 174 */     this.fluid = Optional.of(paramBuilder.build());
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSmokey(boolean paramBoolean) {
/* 179 */     this.smokey = Optional.of(Boolean.valueOf(paramBoolean));
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCanSeeSky(boolean paramBoolean) {
/* 184 */     this.canSeeSky = Optional.of(Boolean.valueOf(paramBoolean));
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public LocationPredicate build() {
/* 189 */     Optional<LocationPredicate.PositionPredicate> optional = LocationPredicate.PositionPredicate.of(this.x, this.y, this.z);
/* 190 */     return new LocationPredicate(optional, this.biomes, this.structures, this.dimension, this.smokey, this.light, this.block, this.fluid, this.canSeeSky);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\LocationPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */