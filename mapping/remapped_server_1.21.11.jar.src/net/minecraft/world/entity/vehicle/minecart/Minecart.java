/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class Minecart extends AbstractMinecart {
/*    */   private float rotationOffset;
/*    */   
/*    */   public Minecart(EntityType<?> paramEntityType, Level paramLevel) {
/* 21 */     super(paramEntityType, paramLevel);
/*    */   }
/*    */   private float playerRotationOffset;
/*    */   
/*    */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 26 */     if (!paramPlayer.isSecondaryUseActive() && !isVehicle() && (level().isClientSide() || paramPlayer.startRiding((Entity)this))) {
/* 27 */       this.playerRotationOffset = this.rotationOffset;
/* 28 */       if (!level().isClientSide()) {
/* 29 */         return paramPlayer.startRiding((Entity)this) ? (InteractionResult)InteractionResult.CONSUME : (InteractionResult)InteractionResult.PASS;
/*    */       }
/* 31 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/* 33 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Item getDropItem() {
/* 38 */     return Items.MINECART;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack getPickResult() {
/* 43 */     return new ItemStack((ItemLike)Items.MINECART);
/*    */   }
/*    */ 
/*    */   
/*    */   public void activateMinecart(ServerLevel paramServerLevel, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/* 48 */     if (paramBoolean) {
/* 49 */       if (isVehicle()) {
/* 50 */         ejectPassengers();
/*    */       }
/* 52 */       if (getHurtTime() == 0) {
/* 53 */         setHurtDir(-getHurtDir());
/* 54 */         setHurtTime(10);
/* 55 */         setDamage(50.0F);
/* 56 */         markHurt();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRideable() {
/* 63 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 68 */     double d1 = getYRot();
/* 69 */     Vec3 vec3 = position();
/* 70 */     super.tick();
/* 71 */     double d2 = (getYRot() - d1) % 360.0D;
/* 72 */     if (level().isClientSide() && vec3.distanceTo(position()) > 0.01D) {
/* 73 */       this.rotationOffset += (float)d2;
/* 74 */       this.rotationOffset %= 360.0F;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void positionRider(Entity paramEntity, Entity.MoveFunction paramMoveFunction) {
/* 80 */     super.positionRider(paramEntity, paramMoveFunction);
/* 81 */     if (level().isClientSide() && paramEntity instanceof Player) { Player player = (Player)paramEntity; if (player.shouldRotateWithMinecart() && useExperimentalMovement(level())) {
/* 82 */         float f = (float)Mth.rotLerp(0.5D, this.playerRotationOffset, this.rotationOffset);
/* 83 */         player.setYRot(player.getYRot() - f - this.playerRotationOffset);
/* 84 */         this.playerRotationOffset = f;
/*    */       }  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\vehicle\minecart\Minecart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */