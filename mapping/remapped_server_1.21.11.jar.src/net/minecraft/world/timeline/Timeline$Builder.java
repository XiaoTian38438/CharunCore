/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
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
/*     */ public class Builder
/*     */ {
/* 102 */   private Optional<Integer> periodTicks = Optional.empty();
/* 103 */   private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Builder setPeriodTicks(int paramInt) {
/* 109 */     this.periodTicks = Optional.of(Integer.valueOf(paramInt));
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> paramEnvironmentAttribute, AttributeModifier<Value, Argument> paramAttributeModifier, Consumer<KeyframeTrack.Builder<Argument>> paramConsumer) {
/* 114 */     paramEnvironmentAttribute.type().checkAllowedModifier(paramAttributeModifier);
/* 115 */     KeyframeTrack.Builder<Argument> builder = new KeyframeTrack.Builder();
/* 116 */     paramConsumer.accept(builder);
/* 117 */     this.tracks.put(paramEnvironmentAttribute, new AttributeTrack<>(paramAttributeModifier, builder.build()));
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public <Value> Builder addTrack(EnvironmentAttribute<Value> paramEnvironmentAttribute, Consumer<KeyframeTrack.Builder<Value>> paramConsumer) {
/* 122 */     return addModifierTrack(paramEnvironmentAttribute, AttributeModifier.override(), paramConsumer);
/*     */   }
/*     */   
/*     */   public Timeline build() {
/* 126 */     return new Timeline(this.periodTicks, (Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>)this.tracks.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\timeline\Timeline$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */