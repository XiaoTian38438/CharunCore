/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPosTracker implements PositionTracker {
/*    */   private final BlockPos blockPos;
/*    */   
/*    */   public BlockPosTracker(BlockPos paramBlockPos) {
/* 12 */     this.blockPos = paramBlockPos.immutable();
/* 13 */     this.centerPosition = Vec3.atCenterOf((Vec3i)paramBlockPos);
/*    */   }
/*    */   private final Vec3 centerPosition;
/*    */   public BlockPosTracker(Vec3 paramVec3) {
/* 17 */     this.blockPos = BlockPos.containing((Position)paramVec3);
/* 18 */     this.centerPosition = paramVec3;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 currentPosition() {
/* 23 */     return this.centerPosition;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 28 */     return this.blockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity paramLivingEntity) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "BlockPosTracker{blockPos=" + String.valueOf(this.blockPos) + ", centerPosition=" + String.valueOf(this.centerPosition) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\BlockPosTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */