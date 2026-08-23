/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 190 */   private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/* 191 */   private GameTypePredicate gameType = GameTypePredicate.ANY;
/* 192 */   private final ImmutableList.Builder<PlayerPredicate.StatMatcher<?>> stats = ImmutableList.builder();
/* 193 */   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = (Object2BooleanMap<ResourceKey<Recipe<?>>>)new Object2BooleanOpenHashMap();
/* 194 */   private final Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();
/* 195 */   private Optional<EntityPredicate> lookingAt = Optional.empty();
/* 196 */   private Optional<InputPredicate> input = Optional.empty();
/*     */   
/*     */   public static Builder player() {
/* 199 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder setLevel(MinMaxBounds.Ints paramInts) {
/* 203 */     this.level = paramInts;
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder addStat(StatType<T> paramStatType, Holder.Reference<T> paramReference, MinMaxBounds.Ints paramInts) {
/* 208 */     this.stats.add(new PlayerPredicate.StatMatcher<>(paramStatType, (Holder<T>)paramReference, paramInts));
/* 209 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> paramResourceKey, boolean paramBoolean) {
/* 213 */     this.recipes.put(paramResourceKey, paramBoolean);
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setGameType(GameTypePredicate paramGameTypePredicate) {
/* 218 */     this.gameType = paramGameTypePredicate;
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLookingAt(EntityPredicate.Builder paramBuilder) {
/* 223 */     this.lookingAt = Optional.of(paramBuilder.build());
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementDone(Identifier paramIdentifier, boolean paramBoolean) {
/* 228 */     this.advancements.put(paramIdentifier, new PlayerPredicate.AdvancementDonePredicate(paramBoolean));
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementCriterions(Identifier paramIdentifier, Map<String, Boolean> paramMap) {
/* 233 */     this.advancements.put(paramIdentifier, new PlayerPredicate.AdvancementCriterionsPredicate((Object2BooleanMap<String>)new Object2BooleanOpenHashMap(paramMap)));
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasInput(InputPredicate paramInputPredicate) {
/* 238 */     this.input = Optional.of(paramInputPredicate);
/* 239 */     return this;
/*     */   }
/*     */   
/*     */   public PlayerPredicate build() {
/* 243 */     return new PlayerPredicate(this.level, this.gameType, (List<PlayerPredicate.StatMatcher<?>>)this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\PlayerPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */