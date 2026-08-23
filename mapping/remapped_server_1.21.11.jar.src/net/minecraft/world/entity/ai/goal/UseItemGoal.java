/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class UseItemGoal<T extends Mob>
/*    */   extends Goal
/*    */ {
/*    */   private final T mob;
/*    */   private final ItemStack item;
/*    */   private final Predicate<? super T> canUseSelector;
/*    */   private final SoundEvent finishUsingSound;
/*    */   
/*    */   public UseItemGoal(T paramT, ItemStack paramItemStack, SoundEvent paramSoundEvent, Predicate<? super T> paramPredicate) {
/* 19 */     this.mob = paramT;
/* 20 */     this.item = paramItemStack;
/* 21 */     this.finishUsingSound = paramSoundEvent;
/* 22 */     this.canUseSelector = paramPredicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     return this.canUseSelector.test(this.mob);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 32 */     return this.mob.isUsingItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, this.item.copy());
/* 38 */     this.mob.startUsingItem(InteractionHand.MAIN_HAND);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 43 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*    */     
/* 45 */     if (this.finishUsingSound != null)
/* 46 */       this.mob.playSound(this.finishUsingSound, 1.0F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\UseItemGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */