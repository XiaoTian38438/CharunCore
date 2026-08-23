/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends LootItemConditionalFunction.Builder<CopyComponentsFunction.Builder>
/*     */ {
/*     */   private final LootContextArg<DataComponentGetter> source;
/*  97 */   private Optional<ImmutableList.Builder<DataComponentType<?>>> include = Optional.empty();
/*  98 */   private Optional<ImmutableList.Builder<DataComponentType<?>>> exclude = Optional.empty();
/*     */   
/*     */   Builder(LootContextArg<DataComponentGetter> paramLootContextArg) {
/* 101 */     this.source = paramLootContextArg;
/*     */   }
/*     */   
/*     */   public Builder include(DataComponentType<?> paramDataComponentType) {
/* 105 */     if (this.include.isEmpty()) {
/* 106 */       this.include = Optional.of(ImmutableList.builder());
/*     */     }
/* 108 */     ((ImmutableList.Builder)this.include.get()).add(paramDataComponentType);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public Builder exclude(DataComponentType<?> paramDataComponentType) {
/* 113 */     if (this.exclude.isEmpty()) {
/* 114 */       this.exclude = Optional.of(ImmutableList.builder());
/*     */     }
/*     */     
/* 117 */     ((ImmutableList.Builder)this.exclude.get()).add(paramDataComponentType);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Builder getThis() {
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunction build() {
/* 128 */     return new CopyComponentsFunction(
/* 129 */         getConditions(), this.source, this.include
/*     */         
/* 131 */         .map(ImmutableList.Builder::build), this.exclude
/* 132 */         .map(ImmutableList.Builder::build));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\functions\CopyComponentsFunction$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */