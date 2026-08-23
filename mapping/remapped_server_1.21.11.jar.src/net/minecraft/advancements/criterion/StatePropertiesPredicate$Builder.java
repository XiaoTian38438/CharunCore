/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 149 */   private final ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers = ImmutableList.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder properties() {
/* 155 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<?> paramProperty, String paramString) {
/* 159 */     this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(paramProperty.getName(), new StatePropertiesPredicate.ExactMatcher(paramString)));
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<Integer> paramProperty, int paramInt) {
/* 164 */     return hasProperty(paramProperty, Integer.toString(paramInt));
/*     */   }
/*     */   
/*     */   public Builder hasProperty(Property<Boolean> paramProperty, boolean paramBoolean) {
/* 168 */     return hasProperty(paramProperty, Boolean.toString(paramBoolean));
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T> & StringRepresentable> Builder hasProperty(Property<T> paramProperty, T paramT) {
/* 172 */     return hasProperty(paramProperty, ((StringRepresentable)paramT).getSerializedName());
/*     */   }
/*     */   
/*     */   public Optional<StatePropertiesPredicate> build() {
/* 176 */     return Optional.of(new StatePropertiesPredicate((List<StatePropertiesPredicate.PropertyMatcher>)this.matchers.build()));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\StatePropertiesPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */