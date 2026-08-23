/*    */ package net.minecraft.world.entity.animal;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemUtils;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.Level;
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
/*    */ public interface Bucketable
/*    */ {
/*    */   @Deprecated
/*    */   static void saveDefaultDataToBucketTag(Mob paramMob, ItemStack paramItemStack) {
/* 41 */     paramItemStack.copyFrom(DataComponents.CUSTOM_NAME, (DataComponentGetter)paramMob);
/* 42 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, paramItemStack, paramCompoundTag -> {
/*    */           if (paramMob.isNoAi()) {
/*    */             paramCompoundTag.putBoolean("NoAI", paramMob.isNoAi());
/*    */           }
/*    */           if (paramMob.isSilent()) {
/*    */             paramCompoundTag.putBoolean("Silent", paramMob.isSilent());
/*    */           }
/*    */           if (paramMob.isNoGravity()) {
/*    */             paramCompoundTag.putBoolean("NoGravity", paramMob.isNoGravity());
/*    */           }
/*    */           if (paramMob.hasGlowingTag()) {
/*    */             paramCompoundTag.putBoolean("Glowing", paramMob.hasGlowingTag());
/*    */           }
/*    */           if (paramMob.isInvulnerable()) {
/*    */             paramCompoundTag.putBoolean("Invulnerable", paramMob.isInvulnerable());
/*    */           }
/*    */           paramCompoundTag.putFloat("Health", paramMob.getHealth());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   static void loadDefaultDataFromBucketTag(Mob paramMob, CompoundTag paramCompoundTag) {
/* 68 */     Objects.requireNonNull(paramMob); paramCompoundTag.getBoolean("NoAI").ifPresent(paramMob::setNoAi);
/* 69 */     Objects.requireNonNull(paramMob); paramCompoundTag.getBoolean("Silent").ifPresent(paramMob::setSilent);
/* 70 */     Objects.requireNonNull(paramMob); paramCompoundTag.getBoolean("NoGravity").ifPresent(paramMob::setNoGravity);
/* 71 */     Objects.requireNonNull(paramMob); paramCompoundTag.getBoolean("Glowing").ifPresent(paramMob::setGlowingTag);
/* 72 */     Objects.requireNonNull(paramMob); paramCompoundTag.getBoolean("Invulnerable").ifPresent(paramMob::setInvulnerable);
/* 73 */     Objects.requireNonNull(paramMob); paramCompoundTag.getFloat("Health").ifPresent(paramMob::setHealth);
/*    */   }
/*    */   
/*    */   static <T extends net.minecraft.world.entity.LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player paramPlayer, InteractionHand paramInteractionHand, T paramT) {
/* 77 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 79 */     if (itemStack.getItem() == Items.WATER_BUCKET && paramT.isAlive()) {
/* 80 */       paramT.playSound(((Bucketable)paramT).getPickupSound(), 1.0F, 1.0F);
/*    */       
/* 82 */       ItemStack itemStack1 = ((Bucketable)paramT).getBucketItemStack();
/* 83 */       ((Bucketable)paramT).saveToBucketTag(itemStack1);
/*    */       
/* 85 */       ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, paramPlayer, itemStack1, false);
/* 86 */       paramPlayer.setItemInHand(paramInteractionHand, itemStack2);
/*    */       
/* 88 */       Level level = paramT.level();
/*    */       
/* 90 */       if (!level.isClientSide()) {
/* 91 */         CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)paramPlayer, itemStack1);
/*    */       }
/*    */       
/* 94 */       paramT.discard();
/*    */       
/* 96 */       return (Optional)Optional.of(InteractionResult.SUCCESS);
/*    */     } 
/* 98 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   boolean fromBucket();
/*    */   
/*    */   void setFromBucket(boolean paramBoolean);
/*    */   
/*    */   void saveToBucketTag(ItemStack paramItemStack);
/*    */   
/*    */   void loadFromBucketTag(CompoundTag paramCompoundTag);
/*    */   
/*    */   ItemStack getBucketItemStack();
/*    */   
/*    */   SoundEvent getPickupSound();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\Bucketable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */