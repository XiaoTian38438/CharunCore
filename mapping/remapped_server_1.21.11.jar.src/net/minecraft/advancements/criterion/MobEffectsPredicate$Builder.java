/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
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
/* 41 */   private final ImmutableMap.Builder<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = ImmutableMap.builder();
/*    */   
/*    */   public static Builder effects() {
/* 44 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder and(Holder<MobEffect> paramHolder) {
/* 48 */     this.effectMap.put(paramHolder, new MobEffectsPredicate.MobEffectInstancePredicate());
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder and(Holder<MobEffect> paramHolder, MobEffectsPredicate.MobEffectInstancePredicate paramMobEffectInstancePredicate) {
/* 53 */     this.effectMap.put(paramHolder, paramMobEffectInstancePredicate);
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public Optional<MobEffectsPredicate> build() {
/* 58 */     return Optional.of(new MobEffectsPredicate((Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate>)this.effectMap.build()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\MobEffectsPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */