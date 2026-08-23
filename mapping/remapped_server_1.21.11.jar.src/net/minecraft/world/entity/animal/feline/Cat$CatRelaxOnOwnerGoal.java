/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CatRelaxOnOwnerGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Cat cat;
/*     */   private Player ownerPlayer;
/*     */   private BlockPos goalPos;
/*     */   private int onBedTicks;
/*     */   
/*     */   public CatRelaxOnOwnerGoal(Cat paramCat) {
/* 552 */     this.cat = paramCat;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 557 */     if (!this.cat.isTame()) {
/* 558 */       return false;
/*     */     }
/*     */     
/* 561 */     if (this.cat.isOrderedToSit()) {
/* 562 */       return false;
/*     */     }
/*     */     
/* 565 */     LivingEntity livingEntity = this.cat.getOwner();
/* 566 */     if (livingEntity instanceof Player) { Player player = (Player)livingEntity;
/* 567 */       this.ownerPlayer = player;
/*     */       
/* 569 */       if (!livingEntity.isSleeping()) {
/* 570 */         return false;
/*     */       }
/*     */       
/* 573 */       if (this.cat.distanceToSqr((Entity)this.ownerPlayer) > 100.0D) {
/* 574 */         return false;
/*     */       }
/*     */       
/* 577 */       BlockPos blockPos = this.ownerPlayer.blockPosition();
/* 578 */       BlockState blockState = this.cat.level().getBlockState(blockPos);
/* 579 */       if (blockState.is(BlockTags.BEDS)) {
/* 580 */         this.goalPos = blockState.getOptionalValue((Property)BedBlock.FACING).map(paramDirection -> paramBlockPos.relative(paramDirection.getOpposite())).orElseGet(() -> new BlockPos((Vec3i)paramBlockPos));
/* 581 */         return !spaceIsOccupied();
/*     */       }  }
/*     */     
/* 584 */     return false;
/*     */   }
/*     */   
/*     */   private boolean spaceIsOccupied() {
/* 588 */     List list = this.cat.level().getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 589 */     for (Cat cat : list) {
/* 590 */       if (cat != this.cat && (cat.isLying() || cat.isRelaxStateOne())) {
/* 591 */         return true;
/*     */       }
/*     */     } 
/* 594 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 599 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 604 */     if (this.goalPos != null) {
/* 605 */       this.cat.setInSittingPose(false);
/* 606 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 612 */     this.cat.setLying(false);
/*     */     
/* 614 */     if (this.ownerPlayer.getSleepTimer() >= 100 && this.cat
/* 615 */       .level().getRandom().nextFloat() < ((Float)this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())).floatValue()) {
/* 616 */       giveMorningGift();
/*     */     }
/*     */     
/* 619 */     this.onBedTicks = 0;
/* 620 */     this.cat.setRelaxStateOne(false);
/* 621 */     this.cat.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private void giveMorningGift() {
/* 625 */     RandomSource randomSource = this.cat.getRandom();
/* 626 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 627 */     mutableBlockPos.set(this.cat.isLeashed() ? (Vec3i)this.cat.getLeashHolder().blockPosition() : (Vec3i)this.cat.blockPosition());
/* 628 */     this.cat.randomTeleport((mutableBlockPos.getX() + randomSource.nextInt(11) - 5), (mutableBlockPos.getY() + randomSource.nextInt(5) - 2), (mutableBlockPos.getZ() + randomSource.nextInt(11) - 5), false);
/*     */     
/* 630 */     mutableBlockPos.set((Vec3i)this.cat.blockPosition());
/* 631 */     this.cat.dropFromGiftLootTable(getServerLevel((Entity)this.cat), BuiltInLootTables.CAT_MORNING_GIFT, (paramServerLevel, paramItemStack) -> paramServerLevel.addFreshEntity((Entity)new ItemEntity((Level)paramServerLevel, paramMutableBlockPos.getX() - Mth.sin((this.cat.yBodyRot * 0.017453292F)), paramMutableBlockPos.getY(), paramMutableBlockPos.getZ() + Mth.cos((this.cat.yBodyRot * 0.017453292F)), paramItemStack)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 638 */     if (this.ownerPlayer != null && this.goalPos != null) {
/* 639 */       this.cat.setInSittingPose(false);
/* 640 */       this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 641 */       if (this.cat.distanceToSqr((Entity)this.ownerPlayer) < 2.5D) {
/* 642 */         this.onBedTicks++;
/* 643 */         if (this.onBedTicks > adjustedTickDelay(16)) {
/* 644 */           this.cat.setLying(true);
/* 645 */           this.cat.setRelaxStateOne(false);
/*     */         } else {
/* 647 */           this.cat.lookAt((Entity)this.ownerPlayer, 45.0F, 45.0F);
/* 648 */           this.cat.setRelaxStateOne(true);
/*     */         } 
/*     */       } else {
/* 651 */         this.cat.setLying(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\feline\Cat$CatRelaxOnOwnerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */