/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.MoverType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreathAirGoal
/*    */   extends Goal
/*    */ {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public BreathAirGoal(PathfinderMob paramPathfinderMob) {
/* 20 */     this.mob = paramPathfinderMob;
/* 21 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 26 */     return (this.mob.getAirSupply() < 140);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 31 */     return canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptable() {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 41 */     findAirPosition();
/*    */   }
/*    */   
/*    */   private void findAirPosition() {
/* 45 */     Iterable iterable = BlockPos.betweenClosed(
/* 46 */         Mth.floor(this.mob.getX() - 1.0D), this.mob
/* 47 */         .getBlockY(), 
/* 48 */         Mth.floor(this.mob.getZ() - 1.0D), 
/* 49 */         Mth.floor(this.mob.getX() + 1.0D), 
/* 50 */         Mth.floor(this.mob.getY() + 8.0D), 
/* 51 */         Mth.floor(this.mob.getZ() + 1.0D));
/*    */ 
/*    */     
/* 54 */     BlockPos blockPos = null;
/* 55 */     for (BlockPos blockPos1 : iterable) {
/* 56 */       if (givesAir((LevelReader)this.mob.level(), blockPos1)) {
/* 57 */         blockPos = blockPos1;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 62 */     if (blockPos == null) {
/* 63 */       blockPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 8.0D, this.mob.getZ());
/*    */     }
/*    */     
/* 66 */     this.mob.getNavigation().moveTo(blockPos.getX(), (blockPos.getY() + 1), blockPos.getZ(), 1.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 71 */     findAirPosition();
/*    */     
/* 73 */     this.mob.moveRelative(0.02F, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/* 74 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*    */   }
/*    */   
/*    */   private boolean givesAir(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 78 */     BlockState blockState = paramLevelReader.getBlockState(paramBlockPos);
/* 79 */     return ((paramLevelReader.getFluidState(paramBlockPos).isEmpty() || blockState.is(Blocks.BUBBLE_COLUMN)) && blockState.isPathfindable(PathComputationType.LAND));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\BreathAirGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */