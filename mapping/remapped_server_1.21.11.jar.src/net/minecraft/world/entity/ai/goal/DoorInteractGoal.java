/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public abstract class DoorInteractGoal extends Goal {
/*  13 */   protected BlockPos doorPos = BlockPos.ZERO; protected Mob mob;
/*     */   protected boolean hasDoor;
/*     */   private boolean passed;
/*     */   private float doorOpenDirX;
/*     */   private float doorOpenDirZ;
/*     */   
/*     */   public DoorInteractGoal(Mob paramMob) {
/*  20 */     this.mob = paramMob;
/*  21 */     if (!GoalUtils.hasGroundPathNavigation(paramMob)) {
/*  22 */       throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isOpen() {
/*  27 */     if (!this.hasDoor) {
/*  28 */       return false;
/*     */     }
/*  30 */     BlockState blockState = this.mob.level().getBlockState(this.doorPos);
/*  31 */     if (!(blockState.getBlock() instanceof DoorBlock)) {
/*  32 */       this.hasDoor = false;
/*  33 */       return false;
/*     */     } 
/*  35 */     return ((Boolean)blockState.getValue((Property)DoorBlock.OPEN)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void setOpen(boolean paramBoolean) {
/*  39 */     if (this.hasDoor) {
/*  40 */       BlockState blockState = this.mob.level().getBlockState(this.doorPos);
/*  41 */       if (blockState.getBlock() instanceof DoorBlock) {
/*  42 */         ((DoorBlock)blockState.getBlock()).setOpen((Entity)this.mob, this.mob.level(), blockState, this.doorPos, paramBoolean);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  49 */     if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
/*  50 */       return false;
/*     */     }
/*  52 */     if (!this.mob.horizontalCollision) {
/*  53 */       return false;
/*     */     }
/*  55 */     Path path = this.mob.getNavigation().getPath();
/*  56 */     if (path == null || path.isDone()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     for (byte b = 0; b < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); b++) {
/*  61 */       Node node = path.getNode(b);
/*  62 */       this.doorPos = new BlockPos(node.x, node.y + 1, node.z);
/*  63 */       if (this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) <= 2.25D) {
/*     */ 
/*     */         
/*  66 */         this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
/*  67 */         if (this.hasDoor) {
/*  68 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  72 */     this.doorPos = this.mob.blockPosition().above();
/*  73 */     this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level(), this.doorPos);
/*  74 */     return this.hasDoor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  79 */     return !this.passed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  84 */     this.passed = false;
/*  85 */     this.doorOpenDirX = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/*  86 */     this.doorOpenDirZ = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  96 */     float f1 = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/*  97 */     float f2 = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/*  98 */     float f3 = this.doorOpenDirX * f1 + this.doorOpenDirZ * f2;
/*  99 */     if (f3 < 0.0F)
/* 100 */       this.passed = true; 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\DoorInteractGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */