/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class Builder
/*     */ {
/* 124 */   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap.Entry<?, ?>> entries = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder putAll(EnvironmentAttributeMap paramEnvironmentAttributeMap) {
/* 130 */     this.entries.putAll(paramEnvironmentAttributeMap.entries);
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Parameter> Builder modify(EnvironmentAttribute<Value> paramEnvironmentAttribute, AttributeModifier<Value, Parameter> paramAttributeModifier, Parameter paramParameter) {
/* 135 */     paramEnvironmentAttribute.type().checkAllowedModifier(paramAttributeModifier);
/* 136 */     this.entries.put(paramEnvironmentAttribute, new EnvironmentAttributeMap.Entry<>(paramParameter, paramAttributeModifier));
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   public <Value> Builder set(EnvironmentAttribute<Value> paramEnvironmentAttribute, Value paramValue) {
/* 141 */     return modify(paramEnvironmentAttribute, AttributeModifier.override(), paramValue);
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeMap build() {
/* 145 */     if (this.entries.isEmpty()) {
/* 146 */       return EnvironmentAttributeMap.EMPTY;
/*     */     }
/* 148 */     return new EnvironmentAttributeMap(Map.copyOf(this.entries));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\attribute\EnvironmentAttributeMap$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */