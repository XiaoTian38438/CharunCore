/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.entity.ContainerUser;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.AABB;
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
/*    */ public abstract class ContainerOpenersCounter
/*    */ {
/*    */   private static final int CHECK_TICK_DELAY = 5;
/*    */   private int openCount;
/*    */   private double maxInteractionRange;
/*    */   
/*    */   public void incrementOpeners(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, double paramDouble) {
/* 32 */     int i = this.openCount++;
/* 33 */     if (i == 0) {
/* 34 */       onOpen(paramLevel, paramBlockPos, paramBlockState);
/* 35 */       paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.CONTAINER_OPEN, paramBlockPos);
/* 36 */       scheduleRecheck(paramLevel, paramBlockPos, paramBlockState);
/*    */     } 
/* 38 */     openerCountChanged(paramLevel, paramBlockPos, paramBlockState, i, this.openCount);
/* 39 */     this.maxInteractionRange = Math.max(paramDouble, this.maxInteractionRange);
/*    */   }
/*    */   
/*    */   public void decrementOpeners(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 43 */     int i = this.openCount--;
/* 44 */     if (this.openCount == 0) {
/* 45 */       onClose(paramLevel, paramBlockPos, paramBlockState);
/* 46 */       paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.CONTAINER_CLOSE, paramBlockPos);
/* 47 */       this.maxInteractionRange = 0.0D;
/*    */     } 
/* 49 */     openerCountChanged(paramLevel, paramBlockPos, paramBlockState, i, this.openCount);
/*    */   }
/*    */   
/*    */   public List<ContainerUser> getEntitiesWithContainerOpen(Level paramLevel, BlockPos paramBlockPos) {
/* 53 */     double d = this.maxInteractionRange + 4.0D;
/* 54 */     AABB aABB = (new AABB(paramBlockPos)).inflate(d);
/* 55 */     return (List<ContainerUser>)paramLevel.getEntities((Entity)null, aABB, paramEntity -> hasContainerOpen(paramEntity, paramBlockPos)).stream()
/* 56 */       .map(paramEntity -> (ContainerUser)paramEntity)
/* 57 */       .collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   private boolean hasContainerOpen(Entity paramEntity, BlockPos paramBlockPos) {
/* 61 */     if (paramEntity instanceof ContainerUser) { ContainerUser containerUser = (ContainerUser)paramEntity; if (!containerUser.getLivingEntity().isSpectator())
/* 62 */         return containerUser.hasContainerOpen(this, paramBlockPos);  }
/*    */     
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public void recheckOpeners(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 68 */     List<ContainerUser> list = getEntitiesWithContainerOpen(paramLevel, paramBlockPos);
/*    */     
/* 70 */     this.maxInteractionRange = 0.0D;
/* 71 */     for (ContainerUser containerUser : list) {
/* 72 */       this.maxInteractionRange = Math.max(containerUser.getContainerInteractionRange(), this.maxInteractionRange);
/*    */     }
/* 74 */     int i = list.size();
/* 75 */     int j = this.openCount;
/* 76 */     if (j != i) {
/* 77 */       boolean bool1 = (i != 0) ? true : false;
/* 78 */       boolean bool2 = (j != 0) ? true : false;
/* 79 */       if (bool1 && !bool2) {
/* 80 */         onOpen(paramLevel, paramBlockPos, paramBlockState);
/* 81 */         paramLevel.gameEvent(null, (Holder)GameEvent.CONTAINER_OPEN, paramBlockPos);
/* 82 */       } else if (!bool1) {
/* 83 */         onClose(paramLevel, paramBlockPos, paramBlockState);
/* 84 */         paramLevel.gameEvent(null, (Holder)GameEvent.CONTAINER_CLOSE, paramBlockPos);
/*    */       } 
/* 86 */       this.openCount = i;
/*    */     } 
/* 88 */     openerCountChanged(paramLevel, paramBlockPos, paramBlockState, j, i);
/* 89 */     if (i > 0) {
/* 90 */       scheduleRecheck(paramLevel, paramBlockPos, paramBlockState);
/*    */     }
/*    */   }
/*    */   
/*    */   public int getOpenerCount() {
/* 95 */     return this.openCount;
/*    */   }
/*    */   
/*    */   private static void scheduleRecheck(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 99 */     paramLevel.scheduleTick(paramBlockPos, paramBlockState.getBlock(), 5);
/*    */   }
/*    */   
/*    */   protected abstract void onOpen(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   protected abstract void onClose(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   protected abstract void openerCountChanged(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract boolean isOwnContainer(Player paramPlayer);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\ContainerOpenersCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */