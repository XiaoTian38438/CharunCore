/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface EnvironmentAttributes
/*     */ {
/*  23 */   public static final EnvironmentAttribute<Integer> FOG_COLOR = register("visual/fog_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  24 */       .defaultValue(Integer.valueOf(0))
/*  25 */       .spatiallyInterpolated()
/*  26 */       .syncable());
/*  27 */   public static final EnvironmentAttribute<Float> FOG_START_DISTANCE = register("visual/fog_start_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  28 */       .defaultValue(Float.valueOf(0.0F))
/*     */       
/*  30 */       .spatiallyInterpolated()
/*  31 */       .syncable());
/*  32 */   public static final EnvironmentAttribute<Float> FOG_END_DISTANCE = register("visual/fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  33 */       .defaultValue(Float.valueOf(1024.0F))
/*  34 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  35 */       .spatiallyInterpolated()
/*  36 */       .syncable());
/*  37 */   public static final EnvironmentAttribute<Float> SKY_FOG_END_DISTANCE = register("visual/sky_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  38 */       .defaultValue(Float.valueOf(512.0F))
/*  39 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  40 */       .spatiallyInterpolated()
/*  41 */       .syncable());
/*  42 */   public static final EnvironmentAttribute<Float> CLOUD_FOG_END_DISTANCE = register("visual/cloud_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  43 */       .defaultValue(Float.valueOf(2048.0F))
/*  44 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  45 */       .spatiallyInterpolated()
/*  46 */       .syncable());
/*  47 */   public static final EnvironmentAttribute<Integer> WATER_FOG_COLOR = register("visual/water_fog_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  48 */       .defaultValue(Integer.valueOf(-16448205))
/*  49 */       .spatiallyInterpolated()
/*  50 */       .syncable());
/*  51 */   public static final EnvironmentAttribute<Float> WATER_FOG_START_DISTANCE = register("visual/water_fog_start_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  52 */       .defaultValue(Float.valueOf(-8.0F))
/*  53 */       .spatiallyInterpolated()
/*  54 */       .syncable());
/*  55 */   public static final EnvironmentAttribute<Float> WATER_FOG_END_DISTANCE = register("visual/water_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  56 */       .defaultValue(Float.valueOf(96.0F))
/*  57 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  58 */       .spatiallyInterpolated()
/*  59 */       .syncable());
/*     */ 
/*     */   
/*  62 */   public static final EnvironmentAttribute<Integer> SKY_COLOR = register("visual/sky_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  63 */       .defaultValue(Integer.valueOf(0))
/*  64 */       .spatiallyInterpolated()
/*  65 */       .syncable());
/*  66 */   public static final EnvironmentAttribute<Integer> SUNRISE_SUNSET_COLOR = register("visual/sunrise_sunset_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.ARGB_COLOR)
/*  67 */       .defaultValue(Integer.valueOf(0))
/*  68 */       .spatiallyInterpolated()
/*  69 */       .syncable());
/*  70 */   public static final EnvironmentAttribute<Integer> CLOUD_COLOR = register("visual/cloud_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.ARGB_COLOR)
/*  71 */       .defaultValue(Integer.valueOf(0))
/*  72 */       .spatiallyInterpolated()
/*  73 */       .syncable());
/*  74 */   public static final EnvironmentAttribute<Float> CLOUD_HEIGHT = register("visual/cloud_height", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  75 */       .defaultValue(Float.valueOf(192.33F))
/*  76 */       .spatiallyInterpolated()
/*  77 */       .syncable());
/*  78 */   public static final EnvironmentAttribute<Float> SUN_ANGLE = register("visual/sun_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  79 */       .defaultValue(Float.valueOf(0.0F))
/*  80 */       .spatiallyInterpolated()
/*  81 */       .syncable());
/*  82 */   public static final EnvironmentAttribute<Float> MOON_ANGLE = register("visual/moon_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  83 */       .defaultValue(Float.valueOf(0.0F))
/*  84 */       .spatiallyInterpolated()
/*  85 */       .syncable());
/*  86 */   public static final EnvironmentAttribute<Float> STAR_ANGLE = register("visual/star_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  87 */       .defaultValue(Float.valueOf(0.0F))
/*  88 */       .spatiallyInterpolated()
/*  89 */       .syncable());
/*  90 */   public static final EnvironmentAttribute<MoonPhase> MOON_PHASE = register("visual/moon_phase", EnvironmentAttribute.<MoonPhase>builder(AttributeTypes.MOON_PHASE)
/*  91 */       .defaultValue(MoonPhase.FULL_MOON)
/*  92 */       .syncable());
/*  93 */   public static final EnvironmentAttribute<Float> STAR_BRIGHTNESS = register("visual/star_brightness", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  94 */       .defaultValue(Float.valueOf(0.0F))
/*  95 */       .valueRange(AttributeRange.UNIT_FLOAT)
/*  96 */       .spatiallyInterpolated()
/*  97 */       .syncable());
/*     */ 
/*     */   
/* 100 */   public static final EnvironmentAttribute<Integer> SKY_LIGHT_COLOR = register("visual/sky_light_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/* 101 */       .defaultValue(Integer.valueOf(-1))
/* 102 */       .spatiallyInterpolated()
/* 103 */       .syncable());
/* 104 */   public static final EnvironmentAttribute<Float> SKY_LIGHT_FACTOR = register("visual/sky_light_factor", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 105 */       .defaultValue(Float.valueOf(1.0F))
/* 106 */       .valueRange(AttributeRange.UNIT_FLOAT)
/* 107 */       .spatiallyInterpolated()
/* 108 */       .syncable());
/*     */ 
/*     */   
/* 111 */   public static final EnvironmentAttribute<ParticleOptions> DEFAULT_DRIPSTONE_PARTICLE = register("visual/default_dripstone_particle", EnvironmentAttribute.builder(AttributeTypes.PARTICLE)
/* 112 */       .defaultValue(ParticleTypes.DRIPPING_DRIPSTONE_WATER)
/* 113 */       .syncable());
/* 114 */   public static final EnvironmentAttribute<List<AmbientParticle>> AMBIENT_PARTICLES = register("visual/ambient_particles", EnvironmentAttribute.<List<AmbientParticle>>builder(AttributeTypes.AMBIENT_PARTICLES)
/* 115 */       .defaultValue(List.of())
/* 116 */       .syncable());
/*     */ 
/*     */   
/* 119 */   public static final EnvironmentAttribute<BackgroundMusic> BACKGROUND_MUSIC = register("audio/background_music", EnvironmentAttribute.<BackgroundMusic>builder(AttributeTypes.BACKGROUND_MUSIC)
/* 120 */       .defaultValue(BackgroundMusic.EMPTY)
/* 121 */       .syncable());
/* 122 */   public static final EnvironmentAttribute<Float> MUSIC_VOLUME = register("audio/music_volume", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 123 */       .defaultValue(Float.valueOf(1.0F))
/* 124 */       .valueRange(AttributeRange.UNIT_FLOAT)
/* 125 */       .syncable());
/* 126 */   public static final EnvironmentAttribute<AmbientSounds> AMBIENT_SOUNDS = register("audio/ambient_sounds", EnvironmentAttribute.<AmbientSounds>builder(AttributeTypes.AMBIENT_SOUNDS)
/* 127 */       .defaultValue(AmbientSounds.EMPTY)
/* 128 */       .syncable());
/* 129 */   public static final EnvironmentAttribute<Boolean> FIREFLY_BUSH_SOUNDS = register("audio/firefly_bush_sounds", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 130 */       .defaultValue(Boolean.valueOf(false))
/* 131 */       .syncable());
/*     */ 
/*     */   
/* 134 */   public static final EnvironmentAttribute<Float> SKY_LIGHT_LEVEL = register("gameplay/sky_light_level", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 135 */       .defaultValue(Float.valueOf(15.0F))
/* 136 */       .valueRange(AttributeRange.ofFloat(0.0F, 15.0F))
/* 137 */       .notPositional()
/* 138 */       .syncable());
/* 139 */   public static final EnvironmentAttribute<Boolean> CAN_START_RAID = register("gameplay/can_start_raid", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 140 */       .defaultValue(Boolean.valueOf(true)));
/* 141 */   public static final EnvironmentAttribute<Boolean> WATER_EVAPORATES = register("gameplay/water_evaporates", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 142 */       .defaultValue(Boolean.valueOf(false))
/* 143 */       .syncable());
/*     */ 
/*     */   
/* 146 */   public static final EnvironmentAttribute<BedRule> BED_RULE = register("gameplay/bed_rule", EnvironmentAttribute.<BedRule>builder(AttributeTypes.BED_RULE)
/* 147 */       .defaultValue(BedRule.CAN_SLEEP_WHEN_DARK));
/* 148 */   public static final EnvironmentAttribute<Boolean> RESPAWN_ANCHOR_WORKS = register("gameplay/respawn_anchor_works", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 149 */       .defaultValue(Boolean.valueOf(false)));
/* 150 */   public static final EnvironmentAttribute<Boolean> NETHER_PORTAL_SPAWNS_PIGLINS = register("gameplay/nether_portal_spawns_piglin", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 151 */       .defaultValue(Boolean.valueOf(false)));
/* 152 */   public static final EnvironmentAttribute<Boolean> FAST_LAVA = register("gameplay/fast_lava", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 153 */       .defaultValue(Boolean.valueOf(false))
/* 154 */       .notPositional()
/* 155 */       .syncable());
/* 156 */   public static final EnvironmentAttribute<Boolean> INCREASED_FIRE_BURNOUT = register("gameplay/increased_fire_burnout", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 157 */       .defaultValue(Boolean.valueOf(false)));
/* 158 */   public static final EnvironmentAttribute<TriState> EYEBLOSSOM_OPEN = register("gameplay/eyeblossom_open", EnvironmentAttribute.<TriState>builder(AttributeTypes.TRI_STATE)
/* 159 */       .defaultValue(TriState.DEFAULT));
/* 160 */   public static final EnvironmentAttribute<Float> TURTLE_EGG_HATCH_CHANCE = register("gameplay/turtle_egg_hatch_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 161 */       .defaultValue(Float.valueOf(0.0F))
/* 162 */       .valueRange(AttributeRange.UNIT_FLOAT));
/*     */ 
/*     */   
/* 165 */   public static final EnvironmentAttribute<Boolean> PIGLINS_ZOMBIFY = register("gameplay/piglins_zombify", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 166 */       .defaultValue(Boolean.valueOf(true))
/* 167 */       .syncable());
/* 168 */   public static final EnvironmentAttribute<Boolean> SNOW_GOLEM_MELTS = register("gameplay/snow_golem_melts", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 169 */       .defaultValue(Boolean.valueOf(false)));
/* 170 */   public static final EnvironmentAttribute<Boolean> CREAKING_ACTIVE = register("gameplay/creaking_active", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 171 */       .defaultValue(Boolean.valueOf(false))
/* 172 */       .syncable());
/* 173 */   public static final EnvironmentAttribute<Float> SURFACE_SLIME_SPAWN_CHANCE = register("gameplay/surface_slime_spawn_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 174 */       .defaultValue(Float.valueOf(0.0F))
/* 175 */       .valueRange(AttributeRange.UNIT_FLOAT));
/* 176 */   public static final EnvironmentAttribute<Float> CAT_WAKING_UP_GIFT_CHANCE = register("gameplay/cat_waking_up_gift_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 177 */       .defaultValue(Float.valueOf(0.0F))
/* 178 */       .valueRange(AttributeRange.UNIT_FLOAT));
/* 179 */   public static final EnvironmentAttribute<Boolean> BEES_STAY_IN_HIVE = register("gameplay/bees_stay_in_hive", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 180 */       .defaultValue(Boolean.valueOf(false)));
/* 181 */   public static final EnvironmentAttribute<Boolean> MONSTERS_BURN = register("gameplay/monsters_burn", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 182 */       .defaultValue(Boolean.valueOf(false)));
/* 183 */   public static final EnvironmentAttribute<Boolean> CAN_PILLAGER_PATROL_SPAWN = register("gameplay/can_pillager_patrol_spawn", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 184 */       .defaultValue(Boolean.valueOf(true)));
/*     */ 
/*     */   
/* 187 */   public static final EnvironmentAttribute<Activity> VILLAGER_ACTIVITY = register("gameplay/villager_activity", EnvironmentAttribute.<Activity>builder(AttributeTypes.ACTIVITY)
/* 188 */       .defaultValue(Activity.IDLE));
/* 189 */   public static final EnvironmentAttribute<Activity> BABY_VILLAGER_ACTIVITY = register("gameplay/baby_villager_activity", EnvironmentAttribute.<Activity>builder(AttributeTypes.ACTIVITY)
/* 190 */       .defaultValue(Activity.IDLE));
/*     */   
/* 192 */   public static final Codec<EnvironmentAttribute<?>> CODEC = BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.byNameCodec();
/*     */   
/*     */   static EnvironmentAttribute<?> bootstrap(Registry<EnvironmentAttribute<?>> paramRegistry) {
/* 195 */     return RESPAWN_ANCHOR_WORKS;
/*     */   }
/*     */   
/*     */   private static <Value> EnvironmentAttribute<Value> register(String paramString, EnvironmentAttribute.Builder<Value> paramBuilder) {
/* 199 */     EnvironmentAttribute<Value> environmentAttribute = paramBuilder.build();
/* 200 */     Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Identifier.withDefaultNamespace(paramString), environmentAttribute);
/* 201 */     return environmentAttribute;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */