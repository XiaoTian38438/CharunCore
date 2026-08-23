/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PathfindingContext
/*    */ {
/*    */   private final CollisionGetter level;
/* 14 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(); private final PathTypeCache cache; private final BlockPos mobPosition;
/*    */   
/*    */   public PathfindingContext(CollisionGetter paramCollisionGetter, Mob paramMob) {
/* 17 */     this.level = paramCollisionGetter;
/* 18 */     Level level = paramMob.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 19 */       this.cache = serverLevel.getPathTypeCache(); }
/*    */     else
/* 21 */     { this.cache = null; }
/*    */     
/* 23 */     this.mobPosition = paramMob.blockPosition();
/*    */   }
/*    */   
/*    */   public PathType getPathTypeFromState(int paramInt1, int paramInt2, int paramInt3) {
/* 27 */     BlockPos.MutableBlockPos mutableBlockPos = this.mutablePos.set(paramInt1, paramInt2, paramInt3);
/* 28 */     if (this.cache == null) {
/* 29 */       return WalkNodeEvaluator.getPathTypeFromState((BlockGetter)this.level, (BlockPos)mutableBlockPos);
/*    */     }
/* 31 */     return this.cache.getOrCompute((BlockGetter)this.level, (BlockPos)mutableBlockPos);
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos paramBlockPos) {
/* 35 */     return this.level.getBlockState(paramBlockPos);
/*    */   }
/*    */   
/*    */   public CollisionGetter level() {
/* 39 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos mobPosition() {
/* 43 */     return this.mobPosition;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\pathfinder\PathfindingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */