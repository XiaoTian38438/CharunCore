/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 102 */   private Optional<HolderSet<Block>> blocks = Optional.empty();
/* 103 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/* 104 */   private Optional<NbtPredicate> nbt = Optional.empty();
/* 105 */   private DataComponentMatchers components = DataComponentMatchers.ANY;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder block() {
/* 111 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<Block> paramHolderGetter, Block... paramVarArgs) {
/* 115 */     return of(paramHolderGetter, Arrays.asList(paramVarArgs));
/*     */   }
/*     */ 
/*     */   
/*     */   public Builder of(HolderGetter<Block> paramHolderGetter, Collection<Block> paramCollection) {
/* 120 */     this.blocks = Optional.of(HolderSet.direct(Block::builtInRegistryHolder, paramCollection));
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<Block> paramHolderGetter, TagKey<Block> paramTagKey) {
/* 125 */     this.blocks = Optional.of(paramHolderGetter.getOrThrow(paramTagKey));
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasNbt(CompoundTag paramCompoundTag) {
/* 130 */     this.nbt = Optional.of(new NbtPredicate(paramCompoundTag));
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setProperties(StatePropertiesPredicate.Builder paramBuilder) {
/* 135 */     this.properties = paramBuilder.build();
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentMatchers paramDataComponentMatchers) {
/* 140 */     this.components = paramDataComponentMatchers;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public BlockPredicate build() {
/* 145 */     return new BlockPredicate(this.blocks, this.properties, this.nbt, this.components);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\BlockPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */