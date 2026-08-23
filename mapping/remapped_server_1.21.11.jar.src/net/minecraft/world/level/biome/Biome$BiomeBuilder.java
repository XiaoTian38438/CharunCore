/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
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
/*     */ public class BiomeBuilder
/*     */ {
/*     */   private boolean hasPrecipitation = true;
/*     */   private Float temperature;
/* 327 */   private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */   private Float downfall;
/* 329 */   private final EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder();
/*     */   private BiomeSpecialEffects specialEffects;
/*     */   private MobSpawnSettings mobSpawnSettings;
/*     */   private BiomeGenerationSettings generationSettings;
/*     */   
/*     */   public BiomeBuilder hasPrecipitation(boolean paramBoolean) {
/* 335 */     this.hasPrecipitation = paramBoolean;
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperature(float paramFloat) {
/* 340 */     this.temperature = Float.valueOf(paramFloat);
/* 341 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder downfall(float paramFloat) {
/* 345 */     this.downfall = Float.valueOf(paramFloat);
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder putAttributes(EnvironmentAttributeMap paramEnvironmentAttributeMap) {
/* 350 */     this.attributes.putAll(paramEnvironmentAttributeMap);
/* 351 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder putAttributes(EnvironmentAttributeMap.Builder paramBuilder) {
/* 355 */     return putAttributes(paramBuilder.build());
/*     */   }
/*     */   
/*     */   public <Value> BiomeBuilder setAttribute(EnvironmentAttribute<Value> paramEnvironmentAttribute, Value paramValue) {
/* 359 */     this.attributes.set(paramEnvironmentAttribute, paramValue);
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Parameter> BiomeBuilder modifyAttribute(EnvironmentAttribute<Value> paramEnvironmentAttribute, AttributeModifier<Value, Parameter> paramAttributeModifier, Parameter paramParameter) {
/* 364 */     this.attributes.modify(paramEnvironmentAttribute, paramAttributeModifier, paramParameter);
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder specialEffects(BiomeSpecialEffects paramBiomeSpecialEffects) {
/* 369 */     this.specialEffects = paramBiomeSpecialEffects;
/* 370 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder mobSpawnSettings(MobSpawnSettings paramMobSpawnSettings) {
/* 374 */     this.mobSpawnSettings = paramMobSpawnSettings;
/* 375 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder generationSettings(BiomeGenerationSettings paramBiomeGenerationSettings) {
/* 379 */     this.generationSettings = paramBiomeGenerationSettings;
/* 380 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier paramTemperatureModifier) {
/* 384 */     this.temperatureModifier = paramTemperatureModifier;
/* 385 */     return this;
/*     */   }
/*     */   
/*     */   public Biome build() {
/* 389 */     if (this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       throw new IllegalStateException("You are missing parameters to build a proper biome\n" + String.valueOf(this));
/*     */     }
/*     */     
/* 398 */     return new Biome(new Biome.ClimateSettings(this.hasPrecipitation, this.temperature
/* 399 */           .floatValue(), this.temperatureModifier, this.downfall.floatValue()), this.attributes
/* 400 */         .build(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 409 */     return "BiomeBuilder{\nhasPrecipitation=" + this.hasPrecipitation + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + String.valueOf(this.temperatureModifier) + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + String.valueOf(this.specialEffects) + ",\nmobSpawnSettings=" + String.valueOf(this.mobSpawnSettings) + ",\ngenerationSettings=" + String.valueOf(this.generationSettings) + ",\n}";
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\biome\Biome$BiomeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */