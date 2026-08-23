/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  86 */   private Optional<ItemPredicate> head = Optional.empty();
/*  87 */   private Optional<ItemPredicate> chest = Optional.empty();
/*  88 */   private Optional<ItemPredicate> legs = Optional.empty();
/*  89 */   private Optional<ItemPredicate> feet = Optional.empty();
/*  90 */   private Optional<ItemPredicate> body = Optional.empty();
/*  91 */   private Optional<ItemPredicate> mainhand = Optional.empty();
/*  92 */   private Optional<ItemPredicate> offhand = Optional.empty();
/*     */   
/*     */   public static Builder equipment() {
/*  95 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder head(ItemPredicate.Builder paramBuilder) {
/*  99 */     this.head = Optional.of(paramBuilder.build());
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder chest(ItemPredicate.Builder paramBuilder) {
/* 104 */     this.chest = Optional.of(paramBuilder.build());
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder legs(ItemPredicate.Builder paramBuilder) {
/* 109 */     this.legs = Optional.of(paramBuilder.build());
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder feet(ItemPredicate.Builder paramBuilder) {
/* 114 */     this.feet = Optional.of(paramBuilder.build());
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder body(ItemPredicate.Builder paramBuilder) {
/* 119 */     this.body = Optional.of(paramBuilder.build());
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder mainhand(ItemPredicate.Builder paramBuilder) {
/* 124 */     this.mainhand = Optional.of(paramBuilder.build());
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder offhand(ItemPredicate.Builder paramBuilder) {
/* 129 */     this.offhand = Optional.of(paramBuilder.build());
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public EntityEquipmentPredicate build() {
/* 134 */     return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.body, this.mainhand, this.offhand);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\EntityEquipmentPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */