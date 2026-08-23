/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ import net.minecraft.server.level.ServerEntity;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class Marker
/*    */   extends Entity {
/*    */   public Marker(EntityType<?> paramEntityType, Level paramLevel) {
/* 17 */     super(paramEntityType, paramLevel);
/* 18 */     this.noPhysics = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {}
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
/*    */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 39 */     throw new IllegalStateException("Markers should never be sent");
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canAddPassenger(Entity paramEntity) {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean couldAcceptPassenger() {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addPassenger(Entity paramEntity) {
/* 54 */     throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
/*    */   }
/*    */ 
/*    */   
/*    */   public PushReaction getPistonPushReaction() {
/* 59 */     return PushReaction.IGNORE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isIgnoringBlockTriggers() {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\Marker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */