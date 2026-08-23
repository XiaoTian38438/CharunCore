/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 136 */   private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder addDefaultLayers(Level paramLevel) {
/* 142 */     EnvironmentAttributeSystem.addDefaultLayers(this, paramLevel);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addConstantLayer(EnvironmentAttributeMap paramEnvironmentAttributeMap) {
/* 147 */     for (EnvironmentAttribute<?> environmentAttribute : paramEnvironmentAttributeMap.keySet()) {
/* 148 */       addConstantEntry(environmentAttribute, paramEnvironmentAttributeMap);
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> paramEnvironmentAttribute, EnvironmentAttributeMap paramEnvironmentAttributeMap) {
/* 154 */     EnvironmentAttributeMap.Entry<Value, ?> entry = paramEnvironmentAttributeMap.get(paramEnvironmentAttribute);
/* 155 */     if (entry == null) {
/* 156 */       throw new IllegalArgumentException("Missing attribute " + String.valueOf(paramEnvironmentAttribute));
/*     */     }
/* 158 */     Objects.requireNonNull(entry); return addConstantLayer(paramEnvironmentAttribute, entry::applyModifier);
/*     */   }
/*     */   
/*     */   public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> paramEnvironmentAttribute, EnvironmentAttributeLayer.Constant<Value> paramConstant) {
/* 162 */     return addLayer(paramEnvironmentAttribute, paramConstant);
/*     */   }
/*     */   
/*     */   public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> paramEnvironmentAttribute, EnvironmentAttributeLayer.TimeBased<Value> paramTimeBased) {
/* 166 */     return addLayer(paramEnvironmentAttribute, paramTimeBased);
/*     */   }
/*     */   
/*     */   public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> paramEnvironmentAttribute, EnvironmentAttributeLayer.Positional<Value> paramPositional) {
/* 170 */     return addLayer(paramEnvironmentAttribute, paramPositional);
/*     */   }
/*     */   
/*     */   private <Value> Builder addLayer(EnvironmentAttribute<Value> paramEnvironmentAttribute, EnvironmentAttributeLayer<Value> paramEnvironmentAttributeLayer) {
/* 174 */     ((List<EnvironmentAttributeLayer<Value>>)this.layersByAttribute.computeIfAbsent(paramEnvironmentAttribute, paramEnvironmentAttribute -> new ArrayList())).add(paramEnvironmentAttributeLayer);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addTimelineLayer(Holder<Timeline> paramHolder, LongSupplier paramLongSupplier) {
/* 179 */     for (EnvironmentAttribute<?> environmentAttribute : (Iterable<EnvironmentAttribute<?>>)((Timeline)paramHolder.value()).attributes()) {
/* 180 */       addTimelineLayerForAttribute(paramHolder, environmentAttribute, paramLongSupplier);
/*     */     }
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   private <Value> void addTimelineLayerForAttribute(Holder<Timeline> paramHolder, EnvironmentAttribute<Value> paramEnvironmentAttribute, LongSupplier paramLongSupplier) {
/* 186 */     addTimeBasedLayer(paramEnvironmentAttribute, (EnvironmentAttributeLayer.TimeBased<Value>)((Timeline)paramHolder.value()).createTrackSampler(paramEnvironmentAttribute, paramLongSupplier));
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeSystem build() {
/* 190 */     return new EnvironmentAttributeSystem(this.layersByAttribute);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */