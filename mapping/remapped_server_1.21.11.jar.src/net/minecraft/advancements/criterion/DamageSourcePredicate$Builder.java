/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.damagesource.DamageType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/* 53 */   private final ImmutableList.Builder<TagPredicate<DamageType>> tags = ImmutableList.builder();
/* 54 */   private Optional<EntityPredicate> directEntity = Optional.empty();
/* 55 */   private Optional<EntityPredicate> sourceEntity = Optional.empty();
/* 56 */   private Optional<Boolean> isDirect = Optional.empty();
/*    */   
/*    */   public static Builder damageType() {
/* 59 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder tag(TagPredicate<DamageType> paramTagPredicate) {
/* 63 */     this.tags.add(paramTagPredicate);
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public Builder direct(EntityPredicate.Builder paramBuilder) {
/* 68 */     this.directEntity = Optional.of(paramBuilder.build());
/* 69 */     return this;
/*    */   }
/*    */   
/*    */   public Builder source(EntityPredicate.Builder paramBuilder) {
/* 73 */     this.sourceEntity = Optional.of(paramBuilder.build());
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public Builder isDirect(boolean paramBoolean) {
/* 78 */     this.isDirect = Optional.of(Boolean.valueOf(paramBoolean));
/* 79 */     return this;
/*    */   }
/*    */   
/*    */   public DamageSourcePredicate build() {
/* 83 */     return new DamageSourcePredicate((List<TagPredicate<DamageType>>)this.tags.build(), this.directEntity, this.sourceEntity, this.isDirect);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\DamageSourcePredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */