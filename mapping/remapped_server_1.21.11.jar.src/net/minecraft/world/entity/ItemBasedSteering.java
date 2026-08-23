/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.network.syncher.EntityDataAccessor;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemBasedSteering
/*    */ {
/*    */   private static final int MIN_BOOST_TIME = 140;
/*    */   private static final int MAX_BOOST_TIME = 700;
/*    */   private final SynchedEntityData entityData;
/*    */   private final EntityDataAccessor<Integer> boostTimeAccessor;
/*    */   private boolean boosting;
/*    */   private int boostTime;
/*    */   
/*    */   public ItemBasedSteering(SynchedEntityData paramSynchedEntityData, EntityDataAccessor<Integer> paramEntityDataAccessor) {
/* 20 */     this.entityData = paramSynchedEntityData;
/* 21 */     this.boostTimeAccessor = paramEntityDataAccessor;
/*    */   }
/*    */   
/*    */   public void onSynced() {
/* 25 */     this.boosting = true;
/* 26 */     this.boostTime = 0;
/*    */   }
/*    */   
/*    */   public boolean boost(RandomSource paramRandomSource) {
/* 30 */     if (this.boosting) {
/* 31 */       return false;
/*    */     }
/* 33 */     this.boosting = true;
/* 34 */     this.boostTime = 0;
/* 35 */     this.entityData.set(this.boostTimeAccessor, Integer.valueOf(paramRandomSource.nextInt(841) + 140));
/* 36 */     return true;
/*    */   }
/*    */   
/*    */   public void tickBoost() {
/* 40 */     if (this.boosting && this.boostTime++ > boostTimeTotal()) {
/* 41 */       this.boosting = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public float boostFactor() {
/* 46 */     if (this.boosting) {
/* 47 */       return 1.0F + 1.15F * Mth.sin((this.boostTime / boostTimeTotal() * 3.1415927F));
/*    */     }
/* 49 */     return 1.0F;
/*    */   }
/*    */   
/*    */   private int boostTimeTotal() {
/* 53 */     return ((Integer)this.entityData.get(this.boostTimeAccessor)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ItemBasedSteering.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */