/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WalkTarget
/*    */ {
/*    */   private final PositionTracker target;
/*    */   
/*    */   public WalkTarget(BlockPos paramBlockPos, float paramFloat, int paramInt) {
/* 16 */     this((PositionTracker)new BlockPosTracker(paramBlockPos), paramFloat, paramInt);
/*    */   }
/*    */   private final float speedModifier; private final int closeEnoughDist;
/*    */   public WalkTarget(Vec3 paramVec3, float paramFloat, int paramInt) {
/* 20 */     this((PositionTracker)new BlockPosTracker(BlockPos.containing((Position)paramVec3)), paramFloat, paramInt);
/*    */   }
/*    */   
/*    */   public WalkTarget(Entity paramEntity, float paramFloat, int paramInt) {
/* 24 */     this((PositionTracker)new EntityTracker(paramEntity, false), paramFloat, paramInt);
/*    */   }
/*    */   
/*    */   public WalkTarget(PositionTracker paramPositionTracker, float paramFloat, int paramInt) {
/* 28 */     this.target = paramPositionTracker;
/* 29 */     this.speedModifier = paramFloat;
/* 30 */     this.closeEnoughDist = paramInt;
/*    */   }
/*    */   
/*    */   public PositionTracker getTarget() {
/* 34 */     return this.target;
/*    */   }
/*    */   
/*    */   public float getSpeedModifier() {
/* 38 */     return this.speedModifier;
/*    */   }
/*    */   
/*    */   public int getCloseEnoughDist() {
/* 42 */     return this.closeEnoughDist;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\memory\WalkTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */