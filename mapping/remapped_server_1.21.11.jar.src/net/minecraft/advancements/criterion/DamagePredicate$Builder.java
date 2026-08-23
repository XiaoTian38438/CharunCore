/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 45 */   private MinMaxBounds.Doubles dealtDamage = MinMaxBounds.Doubles.ANY;
/* 46 */   private MinMaxBounds.Doubles takenDamage = MinMaxBounds.Doubles.ANY;
/* 47 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 48 */   private Optional<Boolean> blocked = Optional.empty();
/* 49 */   private Optional<DamageSourcePredicate> type = Optional.empty();
/*    */   
/*    */   public static Builder damageInstance() {
/* 52 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder dealtDamage(MinMaxBounds.Doubles paramDoubles) {
/* 56 */     this.dealtDamage = paramDoubles;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public Builder takenDamage(MinMaxBounds.Doubles paramDoubles) {
/* 61 */     this.takenDamage = paramDoubles;
/* 62 */     return this;
/*    */   }
/*    */   
/*    */   public Builder sourceEntity(EntityPredicate paramEntityPredicate) {
/* 66 */     this.sourceEntity = Optional.of(paramEntityPredicate);
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public Builder blocked(Boolean paramBoolean) {
/* 71 */     this.blocked = Optional.of(paramBoolean);
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate paramDamageSourcePredicate) {
/* 76 */     this.type = Optional.of(paramDamageSourcePredicate);
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder type(DamageSourcePredicate.Builder paramBuilder) {
/* 81 */     this.type = Optional.of(paramBuilder.build());
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public DamagePredicate build() {
/* 86 */     return new DamagePredicate(this.dealtDamage, this.takenDamage, this.sourceEntity, this.blocked, this.type);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\DamagePredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */