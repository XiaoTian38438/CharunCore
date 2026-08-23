/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 107 */   private Optional<Identifier> parent = Optional.empty();
/* 108 */   private Optional<DisplayInfo> display = Optional.empty();
/* 109 */   private AdvancementRewards rewards = AdvancementRewards.EMPTY;
/* 110 */   private final ImmutableMap.Builder<String, Criterion<?>> criteria = ImmutableMap.builder();
/* 111 */   private Optional<AdvancementRequirements> requirements = Optional.empty();
/* 112 */   private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
/*     */   private boolean sendsTelemetryEvent;
/*     */   
/*     */   public static Builder advancement() {
/* 116 */     return (new Builder()).sendsTelemetryEvent();
/*     */   }
/*     */   
/*     */   public static Builder recipeAdvancement() {
/* 120 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder parent(AdvancementHolder paramAdvancementHolder) {
/* 124 */     this.parent = Optional.of(paramAdvancementHolder.id());
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public Builder parent(Identifier paramIdentifier) {
/* 131 */     this.parent = Optional.of(paramIdentifier);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public Builder display(ItemStack paramItemStack, Component paramComponent1, Component paramComponent2, Identifier paramIdentifier, AdvancementType paramAdvancementType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 136 */     return display(new DisplayInfo(paramItemStack, paramComponent1, paramComponent2, Optional.<Identifier>ofNullable(paramIdentifier).map(net.minecraft.core.ClientAsset.ResourceTexture::new), paramAdvancementType, paramBoolean1, paramBoolean2, paramBoolean3));
/*     */   }
/*     */   
/*     */   public Builder display(ItemLike paramItemLike, Component paramComponent1, Component paramComponent2, Identifier paramIdentifier, AdvancementType paramAdvancementType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 140 */     return display(new DisplayInfo(new ItemStack((ItemLike)paramItemLike.asItem()), paramComponent1, paramComponent2, Optional.<Identifier>ofNullable(paramIdentifier).map(net.minecraft.core.ClientAsset.ResourceTexture::new), paramAdvancementType, paramBoolean1, paramBoolean2, paramBoolean3));
/*     */   }
/*     */   
/*     */   public Builder display(DisplayInfo paramDisplayInfo) {
/* 144 */     this.display = Optional.of(paramDisplayInfo);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder rewards(AdvancementRewards.Builder paramBuilder) {
/* 149 */     return rewards(paramBuilder.build());
/*     */   }
/*     */   
/*     */   public Builder rewards(AdvancementRewards paramAdvancementRewards) {
/* 153 */     this.rewards = paramAdvancementRewards;
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addCriterion(String paramString, Criterion<?> paramCriterion) {
/* 158 */     this.criteria.put(paramString, paramCriterion);
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements.Strategy paramStrategy) {
/* 163 */     this.requirementsStrategy = paramStrategy;
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public Builder requirements(AdvancementRequirements paramAdvancementRequirements) {
/* 168 */     this.requirements = Optional.of(paramAdvancementRequirements);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public Builder sendsTelemetryEvent() {
/* 173 */     this.sendsTelemetryEvent = true;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   public AdvancementHolder build(Identifier paramIdentifier) {
/* 178 */     ImmutableMap immutableMap = this.criteria.buildOrThrow();
/* 179 */     AdvancementRequirements advancementRequirements = this.requirements.orElseGet(() -> this.requirementsStrategy.create(paramMap.keySet()));
/* 180 */     return new AdvancementHolder(paramIdentifier, new Advancement(this.parent, this.display, this.rewards, (Map<String, Criterion<?>>)immutableMap, advancementRequirements, this.sendsTelemetryEvent));
/*     */   }
/*     */   
/*     */   public AdvancementHolder save(Consumer<AdvancementHolder> paramConsumer, String paramString) {
/* 184 */     AdvancementHolder advancementHolder = build(Identifier.parse(paramString));
/* 185 */     paramConsumer.accept(advancementHolder);
/* 186 */     return advancementHolder;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\Advancement$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */