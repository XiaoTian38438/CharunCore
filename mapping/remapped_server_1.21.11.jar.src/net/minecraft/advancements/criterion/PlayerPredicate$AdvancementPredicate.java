/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.AdvancementProgress;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ interface AdvancementPredicate
/*    */   extends Predicate<AdvancementProgress>
/*    */ {
/*    */   public static final Codec<AdvancementPredicate> CODEC;
/*    */   
/*    */   static {
/* 66 */     CODEC = Codec.either(PlayerPredicate.AdvancementDonePredicate.CODEC, PlayerPredicate.AdvancementCriterionsPredicate.CODEC).xmap(Either::unwrap, paramAdvancementPredicate -> {
/*    */           if (paramAdvancementPredicate instanceof PlayerPredicate.AdvancementDonePredicate) {
/*    */             PlayerPredicate.AdvancementDonePredicate advancementDonePredicate = (PlayerPredicate.AdvancementDonePredicate)paramAdvancementPredicate;
/*    */             return Either.left(advancementDonePredicate);
/*    */           } 
/*    */           if (paramAdvancementPredicate instanceof PlayerPredicate.AdvancementCriterionsPredicate) {
/*    */             PlayerPredicate.AdvancementCriterionsPredicate advancementCriterionsPredicate = (PlayerPredicate.AdvancementCriterionsPredicate)paramAdvancementPredicate;
/*    */             return Either.right(advancementCriterionsPredicate);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\PlayerPredicate$AdvancementPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */