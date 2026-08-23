/*    */ package net.minecraft.world.entity.boss.enderdragon;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class EnderDragonPart
/*    */   extends Entity
/*    */ {
/*    */   public final EnderDragon parentMob;
/*    */   public final String name;
/*    */   private final EntityDimensions size;
/*    */   
/*    */   public EnderDragonPart(EnderDragon paramEnderDragon, String paramString, float paramFloat1, float paramFloat2) {
/* 24 */     super(paramEnderDragon.getType(), paramEnderDragon.level());
/* 25 */     this.size = EntityDimensions.scalable(paramFloat1, paramFloat2);
/* 26 */     refreshDimensions();
/* 27 */     this.parentMob = paramEnderDragon;
/* 28 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput paramValueInput) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {}
/*    */ 
/*    */   
/*    */   public boolean isPickable() {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getPickResult() {
/* 50 */     return this.parentMob.getPickResult();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 55 */     if (isInvulnerableToBase(paramDamageSource)) {
/* 56 */       return false;
/*    */     }
/* 58 */     return this.parentMob.hurt(paramServerLevel, this, paramDamageSource, paramFloat);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Entity paramEntity) {
/* 63 */     return (this == paramEntity || this.parentMob == paramEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 69 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDimensions getDimensions(Pose paramPose) {
/* 74 */     return this.size;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldBeSaved() {
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\boss\enderdragon\EnderDragonPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */