/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private final FeatureFlagUniverse universe;
/*     */   private int id;
/*  88 */   private final Map<Identifier, FeatureFlag> flags = new LinkedHashMap<>();
/*     */   
/*     */   public Builder(String paramString) {
/*  91 */     this.universe = new FeatureFlagUniverse(paramString);
/*     */   }
/*     */   
/*     */   public FeatureFlag createVanilla(String paramString) {
/*  95 */     return create(Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   public FeatureFlag create(Identifier paramIdentifier) {
/*  99 */     if (this.id >= 64)
/*     */     {
/* 101 */       throw new IllegalStateException("Too many feature flags");
/*     */     }
/* 103 */     FeatureFlag featureFlag1 = new FeatureFlag(this.universe, this.id++);
/* 104 */     FeatureFlag featureFlag2 = this.flags.put(paramIdentifier, featureFlag1);
/* 105 */     if (featureFlag2 != null) {
/* 106 */       throw new IllegalStateException("Duplicate feature flag " + String.valueOf(paramIdentifier));
/*     */     }
/* 108 */     return featureFlag1;
/*     */   }
/*     */   
/*     */   public FeatureFlagRegistry build() {
/* 112 */     FeatureFlagSet featureFlagSet = FeatureFlagSet.create(this.universe, this.flags.values());
/* 113 */     return new FeatureFlagRegistry(this.universe, featureFlagSet, Map.copyOf(this.flags));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureFlagRegistry$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */