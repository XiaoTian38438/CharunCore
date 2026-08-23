/*    */ package net.minecraft.world.entity.animal.fish;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Cod extends AbstractSchoolingFish {
/*    */   public Cod(EntityType<? extends Cod> paramEntityType, Level paramLevel) {
/* 13 */     super((EntityType)paramEntityType, paramLevel);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getBucketItemStack() {
/* 18 */     return new ItemStack((ItemLike)Items.COD_BUCKET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 23 */     return SoundEvents.COD_AMBIENT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 28 */     return SoundEvents.COD_DEATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 33 */     return SoundEvents.COD_HURT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 38 */     return SoundEvents.COD_FLOP;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\fish\Cod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */