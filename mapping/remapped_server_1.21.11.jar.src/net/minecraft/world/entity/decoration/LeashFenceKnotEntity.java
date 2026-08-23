/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LeashFenceKnotEntity extends BlockAttachedEntity {
/*     */   public static final double OFFSET_Y = 0.375D;
/*     */   
/*     */   public LeashFenceKnotEntity(EntityType<? extends LeashFenceKnotEntity> paramEntityType, Level paramLevel) {
/*  34 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public LeashFenceKnotEntity(Level paramLevel, BlockPos paramBlockPos) {
/*  38 */     super(EntityType.LEASH_KNOT, paramLevel, paramBlockPos);
/*  39 */     setPos(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {}
/*     */ 
/*     */   
/*     */   protected void recalculateBoundingBox() {
/*  48 */     setPosRaw(this.pos.getX() + 0.5D, this.pos.getY() + 0.375D, this.pos.getZ() + 0.5D);
/*  49 */     double d1 = getType().getWidth() / 2.0D;
/*  50 */     double d2 = getType().getHeight();
/*  51 */     setBoundingBox(new AABB(getX() - d1, getY(), getZ() - d1, getX() + d1, getY() + d2, getZ() + d1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double paramDouble) {
/*  56 */     return (paramDouble < 1024.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropItem(ServerLevel paramServerLevel, Entity paramEntity) {
/*  61 */     playSound(SoundEvents.LEAD_UNTIED, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {}
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/*  74 */     if (level().isClientSide()) {
/*  75 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     }
/*  77 */     if (paramPlayer.getItemInHand(paramInteractionHand).is(Items.SHEARS)) {
/*     */       
/*  79 */       InteractionResult interactionResult = super.interact(paramPlayer, paramInteractionHand);
/*  80 */       if (interactionResult instanceof InteractionResult.Success) { InteractionResult.Success success = (InteractionResult.Success)interactionResult; if (success.wasItemInteraction()) {
/*  81 */           return interactionResult;
/*     */         } }
/*     */     
/*     */     } 
/*  85 */     boolean bool1 = false;
/*     */     
/*  87 */     List list = Leashable.leashableLeashedTo((Entity)paramPlayer);
/*     */     
/*  89 */     for (Leashable leashable : list) {
/*  90 */       if (leashable.canHaveALeashAttachedTo(this)) {
/*  91 */         leashable.setLeashedTo(this, true);
/*  92 */         bool1 = true;
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     boolean bool2 = false;
/*  97 */     if (!bool1 && !paramPlayer.isSecondaryUseActive()) {
/*  98 */       List list1 = Leashable.leashableLeashedTo(this);
/*  99 */       for (Leashable leashable : list1) {
/* 100 */         if (leashable.canHaveALeashAttachedTo((Entity)paramPlayer)) {
/* 101 */           leashable.setLeashedTo((Entity)paramPlayer, true);
/* 102 */           bool2 = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     if (bool1 || bool2) {
/* 108 */       gameEvent((Holder)GameEvent.BLOCK_ATTACH, (Entity)paramPlayer);
/* 109 */       playSound(SoundEvents.LEAD_TIED);
/* 110 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 113 */     return super.interact(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyLeasheeRemoved(Leashable paramLeashable) {
/* 118 */     if (Leashable.leashableLeashedTo(this).isEmpty()) {
/* 119 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean survives() {
/* 126 */     return level().getBlockState(this.pos).is(BlockTags.FENCES);
/*     */   }
/*     */   
/*     */   public static LeashFenceKnotEntity getOrCreateKnot(Level paramLevel, BlockPos paramBlockPos) {
/* 130 */     int i = paramBlockPos.getX();
/* 131 */     int j = paramBlockPos.getY();
/* 132 */     int k = paramBlockPos.getZ();
/*     */     
/* 134 */     List list = paramLevel.getEntitiesOfClass(LeashFenceKnotEntity.class, new AABB(i - 1.0D, j - 1.0D, k - 1.0D, i + 1.0D, j + 1.0D, k + 1.0D));
/* 135 */     for (LeashFenceKnotEntity leashFenceKnotEntity1 : list) {
/* 136 */       if (leashFenceKnotEntity1.getPos().equals(paramBlockPos)) {
/* 137 */         return leashFenceKnotEntity1;
/*     */       }
/*     */     } 
/*     */     
/* 141 */     LeashFenceKnotEntity leashFenceKnotEntity = new LeashFenceKnotEntity(paramLevel, paramBlockPos);
/* 142 */     paramLevel.addFreshEntity(leashFenceKnotEntity);
/* 143 */     return leashFenceKnotEntity;
/*     */   }
/*     */   
/*     */   public void playPlacementSound() {
/* 147 */     playSound(SoundEvents.LEAD_TIED, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity paramServerEntity) {
/* 152 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, 0, getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getRopeHoldPosition(float paramFloat) {
/* 157 */     return getPosition(paramFloat).add(0.0D, 0.2D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getPickResult() {
/* 162 */     return new ItemStack((ItemLike)Items.LEAD);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\decoration\LeashFenceKnotEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */