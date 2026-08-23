/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.biome.OverworldBiomes;
/*     */ import net.minecraft.sounds.Musics;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TimelineTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.attribute.AmbientSounds;
/*     */ import net.minecraft.world.attribute.BackgroundMusic;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ 
/*     */ 
/*     */ public class DimensionTypes
/*     */ {
/*     */   public static void bootstrap(BootstrapContext<DimensionType> paramBootstrapContext) {
/*  28 */     HolderGetter<?> holderGetter = paramBootstrapContext.lookup(Registries.TIMELINE);
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
/*  40 */     EnvironmentAttributeMap environmentAttributeMap = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-4138753)).set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(OverworldBiomes.calculateSkyColor(0.8F))).set(EnvironmentAttributes.CLOUD_COLOR, Integer.valueOf(ARGB.white(0.8F))).set(EnvironmentAttributes.CLOUD_HEIGHT, Float.valueOf(192.33F)).set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD).set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK).set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(false)).set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, Boolean.valueOf(true)).set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS).build();
/*  41 */     paramBootstrapContext.register(BuiltinDimensionTypes.OVERWORLD, new DimensionType(false, true, false, 1.0D, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, 0.0F, new DimensionType.MonsterSettings(
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
/*  52 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, DimensionType.CardinalLightType.DEFAULT, environmentAttributeMap, (HolderSet)holderGetter
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  58 */           .getOrThrow(TimelineTags.IN_OVERWORLD)));
/*     */ 
/*     */     
/*  61 */     paramBootstrapContext.register(BuiltinDimensionTypes.NETHER, new DimensionType(true, false, true, 8.0D, 0, 256, 128, BlockTags.INFINIBURN_NETHER, 0.1F, new DimensionType.MonsterSettings(
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
/*  72 */             (IntProvider)ConstantInt.of(7), 15), DimensionType.Skybox.NONE, DimensionType.CardinalLightType.NETHER, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  77 */           EnvironmentAttributeMap.builder()
/*  78 */           .set(EnvironmentAttributes.FOG_START_DISTANCE, Float.valueOf(10.0F))
/*  79 */           .set(EnvironmentAttributes.FOG_END_DISTANCE, Float.valueOf(96.0F))
/*  80 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, Integer.valueOf(Timelines.NIGHT_SKY_LIGHT_COLOR))
/*  81 */           .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, Float.valueOf(4.0F))
/*  82 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, Float.valueOf(0.0F))
/*  83 */           .set(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, ParticleTypes.DRIPPING_DRIPSTONE_LAVA)
/*  84 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/*  85 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(true))
/*  86 */           .set(EnvironmentAttributes.WATER_EVAPORATES, Boolean.valueOf(true))
/*  87 */           .set(EnvironmentAttributes.FAST_LAVA, Boolean.valueOf(true))
/*  88 */           .set(EnvironmentAttributes.PIGLINS_ZOMBIFY, Boolean.valueOf(false))
/*  89 */           .set(EnvironmentAttributes.CAN_START_RAID, Boolean.valueOf(false))
/*  90 */           .set(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/*  91 */           .build(), (HolderSet)holderGetter
/*  92 */           .getOrThrow(TimelineTags.IN_NETHER)));
/*     */ 
/*     */     
/*  95 */     paramBootstrapContext.register(BuiltinDimensionTypes.END, new DimensionType(true, true, false, 1.0D, 0, 256, 256, BlockTags.INFINIBURN_END, 0.25F, new DimensionType.MonsterSettings(
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
/* 106 */             (IntProvider)ConstantInt.of(15), 0), DimensionType.Skybox.END, DimensionType.CardinalLightType.DEFAULT, 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           EnvironmentAttributeMap.builder()
/* 112 */           .set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-15199464))
/* 113 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, Integer.valueOf(-1736449))
/* 114 */           .set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(-16777216))
/* 115 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, Float.valueOf(0.0F))
/* 116 */           .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(Musics.END))
/* 117 */           .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
/* 118 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/* 119 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(false))
/* 120 */           .build(), (HolderSet)holderGetter
/* 121 */           .getOrThrow(TimelineTags.IN_END)));
/*     */ 
/*     */ 
/*     */     
/* 125 */     paramBootstrapContext.register(BuiltinDimensionTypes.OVERWORLD_CAVES, new DimensionType(false, true, true, 1.0D, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, 0.0F, new DimensionType.MonsterSettings(
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
/* 136 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, DimensionType.CardinalLightType.DEFAULT, environmentAttributeMap, (HolderSet)holderGetter
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 142 */           .getOrThrow(TimelineTags.IN_OVERWORLD)));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\DimensionTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */