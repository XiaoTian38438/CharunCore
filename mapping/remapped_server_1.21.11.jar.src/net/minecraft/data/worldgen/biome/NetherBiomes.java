/*     */ package net.minecraft.data.worldgen.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*     */ import net.minecraft.data.worldgen.Carvers;
/*     */ import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
/*     */ import net.minecraft.data.worldgen.placement.NetherPlacements;
/*     */ import net.minecraft.data.worldgen.placement.OrePlacements;
/*     */ import net.minecraft.data.worldgen.placement.TreePlacements;
/*     */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.attribute.AmbientAdditionsSettings;
/*     */ import net.minecraft.world.attribute.AmbientMoodSettings;
/*     */ import net.minecraft.world.attribute.AmbientParticle;
/*     */ import net.minecraft.world.attribute.AmbientSounds;
/*     */ import net.minecraft.world.attribute.BackgroundMusic;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ public class NetherBiomes {
/*     */   private static Biome.BiomeBuilder baseBiome() {
/*  35 */     return (new Biome.BiomeBuilder())
/*  36 */       .hasPrecipitation(false)
/*  37 */       .temperature(2.0F)
/*  38 */       .downfall(0.0F)
/*  39 */       .specialEffects((new BiomeSpecialEffects.Builder())
/*  40 */         .waterColor(4159204)
/*  41 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Biome netherWastes(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  53 */     MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 4, 4)).addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 4, 4)).addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 4)).addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4)).addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 4, 4)).addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2)).build();
/*     */ 
/*     */ 
/*     */     
/*  57 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addCarver(Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
/*     */     
/*  59 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/*  60 */     builder
/*  61 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
/*  62 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
/*  63 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE)
/*  64 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
/*  65 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
/*  66 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER)
/*  67 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER)
/*  68 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
/*  69 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
/*     */     
/*  71 */     BiomeDefaultFeatures.addNetherDefaultOres(builder);
/*     */     
/*  73 */     return baseBiome()
/*  74 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-13432824))
/*  75 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_NETHER_WASTES))
/*  76 */       .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
/*  77 */           Optional.of(SoundEvents.AMBIENT_NETHER_WASTES_LOOP), 
/*  78 */           Optional.of(new AmbientMoodSettings((Holder)SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  84 */           List.of(new AmbientAdditionsSettings((Holder)SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  89 */       .mobSpawnSettings(mobSpawnSettings)
/*  90 */       .generationSettings(builder.build())
/*  91 */       .build();
/*     */   }
/*     */   
/*     */   public static Biome soulSandValley(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/*  95 */     double d1 = 0.7D;
/*  96 */     double d2 = 0.15D;
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
/* 107 */     MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, 20, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 5)).addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 4, 4)).addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4)).addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2)).addMobCharge(EntityType.SKELETON, 0.7D, 0.15D).addMobCharge(EntityType.GHAST, 0.7D, 0.15D).addMobCharge(EntityType.ENDERMAN, 0.7D, 0.15D).addMobCharge(EntityType.STRIDER, 0.7D, 0.15D).build();
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
/* 122 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addCarver(Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA).addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_CRIMSON_ROOTS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);
/*     */     
/* 124 */     BiomeDefaultFeatures.addNetherDefaultOres(builder);
/*     */     
/* 126 */     return baseBiome()
/* 127 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-14989499))
/* 128 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY))
/* 129 */       .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of((ParticleOptions)ParticleTypes.ASH, 0.00625F))
/* 130 */       .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
/* 131 */           Optional.of(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP), 
/* 132 */           Optional.of(new AmbientMoodSettings((Holder)SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 138 */           List.of(new AmbientAdditionsSettings((Holder)SoundEvents.AMBIENT_SOUL_SAND_VALLEY_ADDITIONS, 0.0111D))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 143 */       .mobSpawnSettings(mobSpawnSettings)
/* 144 */       .generationSettings(builder.build())
/* 145 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Biome basaltDeltas(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 153 */     MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, 40, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 1, 1)).addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 5)).addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2)).build();
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
/* 172 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addCarver(Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.DELTA).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.SMALL_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.LARGE_BASALT_COLUMNS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.BASALT_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.BLACKSTONE_BLOBS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_DELTA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED_DOUBLE).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GOLD_DELTAS).addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_QUARTZ_DELTAS);
/* 173 */     BiomeDefaultFeatures.addAncientDebris(builder);
/*     */     
/* 175 */     return baseBiome()
/* 176 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-9937040))
/* 177 */       .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of((ParticleOptions)ParticleTypes.WHITE_ASH, 0.118093334F))
/* 178 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_BASALT_DELTAS))
/* 179 */       .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
/* 180 */           Optional.of(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP), 
/* 181 */           Optional.of(new AmbientMoodSettings((Holder)SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 187 */           List.of(new AmbientAdditionsSettings((Holder)SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111D))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       .mobSpawnSettings(mobSpawnSettings)
/* 193 */       .generationSettings(builder.build())
/* 194 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Biome crimsonForest(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 203 */     MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 2, 4)).addSpawn(MobCategory.MONSTER, 9, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 3, 4)).addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 3, 4)).addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2)).build();
/*     */ 
/*     */ 
/*     */     
/* 207 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addCarver(Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
/*     */     
/* 209 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/* 210 */     builder
/* 211 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
/* 212 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
/* 213 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
/* 214 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
/* 215 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
/* 216 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
/* 217 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES)
/* 218 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.CRIMSON_FUNGI)
/* 219 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
/*     */     
/* 221 */     BiomeDefaultFeatures.addNetherDefaultOres(builder);
/*     */     
/* 223 */     return baseBiome()
/* 224 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-13434109))
/* 225 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_CRIMSON_FOREST))
/* 226 */       .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of((ParticleOptions)ParticleTypes.CRIMSON_SPORE, 0.025F))
/* 227 */       .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
/* 228 */           Optional.of(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP), 
/* 229 */           Optional.of(new AmbientMoodSettings((Holder)SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 235 */           List.of(new AmbientAdditionsSettings((Holder)SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111D))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 240 */       .mobSpawnSettings(mobSpawnSettings)
/* 241 */       .generationSettings(builder.build())
/* 242 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Biome warpedForest(HolderGetter<PlacedFeature> paramHolderGetter, HolderGetter<ConfiguredWorldCarver<?>> paramHolderGetter1) {
/* 251 */     MobSpawnSettings mobSpawnSettings = (new MobSpawnSettings.Builder()).addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4)).addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2)).addMobCharge(EntityType.ENDERMAN, 1.0D, 0.12D).build();
/*     */ 
/*     */ 
/*     */     
/* 255 */     BiomeGenerationSettings.Builder builder = (new BiomeGenerationSettings.Builder(paramHolderGetter, paramHolderGetter1)).addCarver(Carvers.NETHER_CAVE).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
/*     */     
/* 257 */     BiomeDefaultFeatures.addDefaultMushrooms(builder);
/* 258 */     builder
/* 259 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
/* 260 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
/* 261 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE)
/* 262 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
/* 263 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
/* 264 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
/* 265 */       .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
/* 266 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.WARPED_FUNGI)
/* 267 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WARPED_FOREST_VEGETATION)
/* 268 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.NETHER_SPROUTS)
/* 269 */       .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.TWISTING_VINES);
/*     */     
/* 271 */     BiomeDefaultFeatures.addNetherDefaultOres(builder);
/*     */     
/* 273 */     return baseBiome()
/* 274 */       .setAttribute(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-15071974))
/* 275 */       .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic((Holder)SoundEvents.MUSIC_BIOME_WARPED_FOREST))
/* 276 */       .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of((ParticleOptions)ParticleTypes.WARPED_SPORE, 0.01428F))
/* 277 */       .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
/* 278 */           Optional.of(SoundEvents.AMBIENT_WARPED_FOREST_LOOP), 
/* 279 */           Optional.of(new AmbientMoodSettings((Holder)SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0D)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 285 */           List.of(new AmbientAdditionsSettings((Holder)SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111D))))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 290 */       .mobSpawnSettings(mobSpawnSettings)
/* 291 */       .generationSettings(builder.build())
/* 292 */       .build();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\biome\NetherBiomes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */