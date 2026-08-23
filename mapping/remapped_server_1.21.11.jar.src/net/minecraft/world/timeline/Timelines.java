/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.attribute.modifier.BooleanModifier;
/*     */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*     */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public interface Timelines {
/*  21 */   public static final ResourceKey<Timeline> DAY = key("day");
/*  22 */   public static final ResourceKey<Timeline> MOON = key("moon");
/*  23 */   public static final ResourceKey<Timeline> VILLAGER_SCHEDULE = key("villager_schedule");
/*  24 */   public static final ResourceKey<Timeline> EARLY_GAME = key("early_game");
/*     */   
/*     */   public static final float DAY_SKY_LIGHT_LEVEL = 15.0F;
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_LEVEL = 4.0F;
/*  29 */   public static final int NIGHT_SKY_LIGHT_COLOR = ARGB.colorFromFloat(1.0F, 0.48F, 0.48F, 1.0F);
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_FACTOR = 0.24F;
/*     */   public static final int NIGHT_SKY_COLOR_MULTIPLIER = -16777216;
/*  33 */   public static final int NIGHT_FOG_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.06F, 0.06F, 0.09F);
/*  34 */   public static final int NIGHT_CLOUD_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.15F);
/*     */ 
/*     */   
/*     */   static void bootstrap(BootstrapContext<Timeline> paramBootstrapContext) {
/*  38 */     EasingType easingType = EasingType.symmetricCubicBezier(0.362F, 0.241F);
/*     */     
/*  40 */     char c1 = 'ㄸ';
/*  41 */     char c2 = '孩';
/*  42 */     char c3 = 'ᝰ';
/*     */     
/*  44 */     paramBootstrapContext.register(DAY, Timeline.builder()
/*  45 */         .setPeriodTicks(24000)
/*  46 */         .addTrack(EnvironmentAttributes.SUN_ANGLE, paramBuilder -> paramBuilder.setEasing(paramEasingType).addKeyframe(6000, Float.valueOf(360.0F)).addKeyframe(6000, Float.valueOf(0.0F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  51 */         .addTrack(EnvironmentAttributes.MOON_ANGLE, paramBuilder -> paramBuilder.setEasing(paramEasingType).addKeyframe(6000, Float.valueOf(540.0F)).addKeyframe(6000, Float.valueOf(180.0F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  56 */         .addTrack(EnvironmentAttributes.STAR_ANGLE, paramBuilder -> paramBuilder.setEasing(paramEasingType).addKeyframe(6000, Float.valueOf(360.0F)).addKeyframe(6000, Float.valueOf(0.0F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  61 */         .addModifierTrack(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, (AttributeModifier<?, ?>)BooleanModifier.OR, paramBuilder -> paramBuilder.addKeyframe(12600, Boolean.valueOf(true)).addKeyframe(23401, Boolean.valueOf(false)))
/*     */ 
/*     */ 
/*     */         
/*  65 */         .addModifierTrack(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, paramBuilder -> paramBuilder.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER)).addKeyframe(22330, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         .addModifierTrack(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, paramBuilder -> paramBuilder.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(-16777216)).addKeyframe(22330, Integer.valueOf(-16777216)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  77 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, paramBuilder -> paramBuilder.addKeyframe(730, Integer.valueOf(-1)).addKeyframe(11270, Integer.valueOf(-1)).addKeyframe(13140, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR)).addKeyframe(22860, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  83 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, paramBuilder -> paramBuilder.addKeyframe(730, Float.valueOf(1.0F)).addKeyframe(11270, Float.valueOf(1.0F)).addKeyframe(13140, Float.valueOf(0.24F)).addKeyframe(22860, Float.valueOf(0.24F)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, paramBuilder -> paramBuilder.addKeyframe(133, Float.valueOf(1.0F)).addKeyframe(11867, Float.valueOf(1.0F)).addKeyframe(13670, Float.valueOf(0.26666668F)).addKeyframe(22330, Float.valueOf(0.26666668F)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  95 */         .addTrack(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, paramBuilder -> paramBuilder.addKeyframe(71, Integer.valueOf(1609540403)).addKeyframe(310, Integer.valueOf(703969843)).addKeyframe(565, Integer.valueOf(117167155)).addKeyframe(730, Integer.valueOf(16770355)).addKeyframe(11270, Integer.valueOf(16770355)).addKeyframe(11397, Integer.valueOf(83679283)).addKeyframe(11522, Integer.valueOf(268028723)).addKeyframe(11690, Integer.valueOf(703969843)).addKeyframe(11929, Integer.valueOf(1609540403)).addKeyframe(12243, Integer.valueOf(-1310226637)).addKeyframe(12358, Integer.valueOf(-857440717)).addKeyframe(12512, Integer.valueOf(-371166669)).addKeyframe(12613, Integer.valueOf(-153261261)).addKeyframe(12732, Integer.valueOf(-19242189)).addKeyframe(12841, Integer.valueOf(-19440589)).addKeyframe(13035, Integer.valueOf(-321760973)).addKeyframe(13252, Integer.valueOf(-1043577037)).addKeyframe(13775, Integer.valueOf(918435635)).addKeyframe(13888, Integer.valueOf(532362547)).addKeyframe(14039, Integer.valueOf(163001139)).addKeyframe(14192, Integer.valueOf(11744051)).addKeyframe(21807, Integer.valueOf(11678515)).addKeyframe(21961, Integer.valueOf(163001139)).addKeyframe(22112, Integer.valueOf(532362547)).addKeyframe(22225, Integer.valueOf(918435635)).addKeyframe(22748, Integer.valueOf(-1043577037)).addKeyframe(22965, Integer.valueOf(-321760973)).addKeyframe(23159, Integer.valueOf(-19440589)).addKeyframe(23272, Integer.valueOf(-19242189)).addKeyframe(23488, Integer.valueOf(-371166669)).addKeyframe(23642, Integer.valueOf(-857440717)).addKeyframe(23757, Integer.valueOf(-1310226637)))
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
/* 129 */         .addModifierTrack(EnvironmentAttributes.STAR_BRIGHTNESS, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, paramBuilder -> paramBuilder.addKeyframe(92, Float.valueOf(0.037F)).addKeyframe(627, Float.valueOf(0.0F)).addKeyframe(11373, Float.valueOf(0.0F)).addKeyframe(11732, Float.valueOf(0.016F)).addKeyframe(11959, Float.valueOf(0.044F)).addKeyframe(12399, Float.valueOf(0.143F)).addKeyframe(12729, Float.valueOf(0.258F)).addKeyframe(13228, Float.valueOf(0.5F)).addKeyframe(22772, Float.valueOf(0.5F)).addKeyframe(23032, Float.valueOf(0.364F)).addKeyframe(23356, Float.valueOf(0.225F)).addKeyframe(23758, Float.valueOf(0.101F)))
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
/* 143 */         .addModifierTrack(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_ARGB, paramBuilder -> paramBuilder.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER)).addKeyframe(22330, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 149 */         .addTrack(EnvironmentAttributes.EYEBLOSSOM_OPEN, paramBuilder -> paramBuilder.addKeyframe(12600, TriState.TRUE).addKeyframe(23401, TriState.FALSE))
/*     */ 
/*     */ 
/*     */         
/* 153 */         .addModifierTrack(EnvironmentAttributes.CREAKING_ACTIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, paramBuilder -> paramBuilder.addKeyframe(12600, Boolean.valueOf(true)).addKeyframe(23401, Boolean.valueOf(false)))
/*     */ 
/*     */ 
/*     */         
/* 157 */         .addModifierTrack(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, paramBuilder -> paramBuilder.setEasing(EasingType.CONSTANT).addKeyframe(21062, Float.valueOf(1.0F)).addKeyframe(21905, Float.valueOf(0.002F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 162 */         .addModifierTrack(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, paramBuilder -> paramBuilder.setEasing(EasingType.CONSTANT).addKeyframe(362, Float.valueOf(0.0F)).addKeyframe(23667, Float.valueOf(0.7F)))
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 167 */         .addModifierTrack(EnvironmentAttributes.BEES_STAY_IN_HIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, paramBuilder -> paramBuilder.addKeyframe(12542, Boolean.valueOf(true)).addKeyframe(23460, Boolean.valueOf(false)))
/*     */ 
/*     */ 
/*     */         
/* 171 */         .addModifierTrack(EnvironmentAttributes.MONSTERS_BURN, (AttributeModifier<?, ?>)BooleanModifier.OR, paramBuilder -> paramBuilder.addKeyframe(12542, Boolean.valueOf(false)).addKeyframe(23460, Boolean.valueOf(true)))
/*     */ 
/*     */ 
/*     */         
/* 175 */         .build());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Timeline.Builder builder = Timeline.builder().setPeriodTicks(24000 * MoonPhase.COUNT).addTrack(EnvironmentAttributes.MOON_PHASE, paramBuilder -> { for (MoonPhase moonPhase : MoonPhase.values()) paramBuilder.addKeyframe(moonPhase.startTick(), moonPhase);  }).addModifierTrack(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, paramBuilder -> {
/*     */           paramBuilder.setEasing(EasingType.CONSTANT);
/*     */           
/*     */           for (MoonPhase moonPhase : MoonPhase.values()) {
/*     */             paramBuilder.addKeyframe(moonPhase.startTick(), Float.valueOf(DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()] * 0.5F));
/*     */           }
/*     */         });
/* 192 */     paramBootstrapContext.register(MOON, builder.build());
/*     */     
/* 194 */     char c4 = 'ߐ';
/* 195 */     char c5 = '᭘';
/*     */     
/* 197 */     paramBootstrapContext.register(VILLAGER_SCHEDULE, Timeline.builder()
/* 198 */         .setPeriodTicks(24000)
/* 199 */         .addTrack(EnvironmentAttributes.VILLAGER_ACTIVITY, paramBuilder -> paramBuilder.addKeyframe(10, Activity.IDLE).addKeyframe(2000, Activity.WORK).addKeyframe(9000, Activity.MEET).addKeyframe(11000, Activity.IDLE).addKeyframe(12000, Activity.REST))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 206 */         .addTrack(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, paramBuilder -> paramBuilder.addKeyframe(10, Activity.IDLE).addKeyframe(3000, Activity.PLAY).addKeyframe(6000, Activity.IDLE).addKeyframe(10000, Activity.PLAY).addKeyframe(12000, Activity.REST))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 213 */         .build());
/*     */ 
/*     */     
/* 216 */     paramBootstrapContext.register(EARLY_GAME, Timeline.builder()
/* 217 */         .addModifierTrack(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, (AttributeModifier<?, ?>)BooleanModifier.AND, paramBuilder -> paramBuilder.addKeyframe(0, Boolean.valueOf(false)).addKeyframe(120000, Boolean.valueOf(true)))
/*     */ 
/*     */ 
/*     */         
/* 221 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResourceKey<Timeline> key(String paramString) {
/* 226 */     return ResourceKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\timeline\Timelines.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */