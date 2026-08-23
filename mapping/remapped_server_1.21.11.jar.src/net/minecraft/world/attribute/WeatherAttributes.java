/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*    */ import net.minecraft.world.attribute.modifier.FloatWithAlpha;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.timeline.Timelines;
/*    */ 
/*    */ public class WeatherAttributes
/*    */ {
/* 15 */   public static final EnvironmentAttributeMap RAIN = EnvironmentAttributeMap.builder()
/* 16 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.6F, 0.75F))
/* 17 */     .<Integer, Integer>modify(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_RGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F)))
/* 18 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.5F))
/* 19 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.3125F))
/* 20 */     .<Integer, Integer>modify(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.ALPHA_BLEND, Integer.valueOf(ARGB.color(0.3125F, Timelines.NIGHT_SKY_LIGHT_COLOR)))
/* 21 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.3125F))
/* 22 */     .<Float>set(EnvironmentAttributes.STAR_BRIGHTNESS, Float.valueOf(0.0F))
/* 23 */     .<Integer, Integer>modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_ARGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 0.6F)))
/* 24 */     .<Boolean>set(EnvironmentAttributes.BEES_STAY_IN_HIVE, Boolean.valueOf(true))
/* 25 */     .build();
/*    */   
/* 27 */   public static final EnvironmentAttributeMap THUNDER = EnvironmentAttributeMap.builder()
/* 28 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.94F))
/* 29 */     .<Integer, Integer>modify(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_RGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F)))
/* 30 */     .<Integer, ColorModifier.BlendToGray>modify(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<Integer, ColorModifier.BlendToGray>)ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.095F, 0.94F))
/* 31 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.52734375F))
/* 32 */     .<Integer, Integer>modify(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.ALPHA_BLEND, Integer.valueOf(ARGB.color(0.52734375F, Timelines.NIGHT_SKY_LIGHT_COLOR)))
/* 33 */     .<Float, FloatWithAlpha>modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<Float, FloatWithAlpha>)FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.52734375F))
/* 34 */     .<Float>set(EnvironmentAttributes.STAR_BRIGHTNESS, Float.valueOf(0.0F))
/* 35 */     .<Integer, Integer>modify(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, (AttributeModifier<Integer, Integer>)ColorModifier.MULTIPLY_ARGB, Integer.valueOf(ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 0.3F)))
/* 36 */     .<Boolean>set(EnvironmentAttributes.BEES_STAY_IN_HIVE, Boolean.valueOf(true))
/* 37 */     .build();
/*    */   
/* 39 */   private static final Set<EnvironmentAttribute<?>> WEATHER_ATTRIBUTES = (Set<EnvironmentAttribute<?>>)Sets.union(RAIN.keySet(), THUNDER.keySet());
/*    */   
/*    */   public static void addBuiltinLayers(EnvironmentAttributeSystem.Builder paramBuilder, WeatherAccess paramWeatherAccess) {
/* 42 */     for (EnvironmentAttribute<?> environmentAttribute : WEATHER_ATTRIBUTES) {
/* 43 */       addLayer(paramBuilder, paramWeatherAccess, environmentAttribute);
/*    */     }
/*    */   }
/*    */   
/*    */   private static <Value> void addLayer(EnvironmentAttributeSystem.Builder paramBuilder, WeatherAccess paramWeatherAccess, EnvironmentAttribute<Value> paramEnvironmentAttribute) {
/* 48 */     EnvironmentAttributeMap.Entry<Value, ?> entry1 = RAIN.get(paramEnvironmentAttribute);
/* 49 */     EnvironmentAttributeMap.Entry<Value, ?> entry2 = THUNDER.get(paramEnvironmentAttribute);
/* 50 */     paramBuilder.addTimeBasedLayer(paramEnvironmentAttribute, (paramObject, paramInt) -> {
/*    */           float f1 = paramWeatherAccess.thunderLevel();
/*    */           float f2 = paramWeatherAccess.rainLevel() - f1;
/*    */           if (paramEntry1 != null && f2 > 0.0F) {
/*    */             Object object = paramEntry1.applyModifier(paramObject);
/*    */             paramObject = paramEnvironmentAttribute.type().stateChangeLerp().apply(f2, paramObject, object);
/*    */           } 
/*    */           if (paramEntry2 != null && f1 > 0.0F) {
/*    */             Object object = paramEntry2.applyModifier(paramObject);
/*    */             paramObject = paramEnvironmentAttribute.type().stateChangeLerp().apply(f1, paramObject, object);
/*    */           } 
/*    */           return paramObject;
/*    */         });
/*    */   }
/*    */   
/*    */   public static interface WeatherAccess
/*    */   {
/* 67 */     static WeatherAccess from(final Level level) { return new WeatherAccess()
/*    */         {
/*    */           public float rainLevel() {
/* 70 */             return level.getRainLevel(1.0F);
/*    */           }
/*    */           
/*    */           public float thunderLevel()
/*    */           {
/* 75 */             return level.getThunderLevel(1.0F); } }; } float rainLevel(); float thunderLevel(); } class null implements WeatherAccess { public float thunderLevel() { return level.getThunderLevel(1.0F); }
/*    */ 
/*    */     
/*    */     public float rainLevel() {
/*    */       return level.getRainLevel(1.0F);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\WeatherAttributes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */