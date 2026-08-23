/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class BlockHitResult extends HitResult {
/*    */   private final Direction direction;
/*    */   private final BlockPos blockPos;
/*    */   private final boolean miss;
/*    */   private final boolean inside;
/*    */   private final boolean worldBorderHit;
/*    */   
/*    */   public static BlockHitResult miss(Vec3 paramVec3, Direction paramDirection, BlockPos paramBlockPos) {
/* 14 */     return new BlockHitResult(true, paramVec3, paramDirection, paramBlockPos, false, false);
/*    */   }
/*    */   
/*    */   public BlockHitResult(Vec3 paramVec3, Direction paramDirection, BlockPos paramBlockPos, boolean paramBoolean) {
/* 18 */     this(false, paramVec3, paramDirection, paramBlockPos, paramBoolean, false);
/*    */   }
/*    */   
/*    */   public BlockHitResult(Vec3 paramVec3, Direction paramDirection, BlockPos paramBlockPos, boolean paramBoolean1, boolean paramBoolean2) {
/* 22 */     this(false, paramVec3, paramDirection, paramBlockPos, paramBoolean1, paramBoolean2);
/*    */   }
/*    */   
/*    */   private BlockHitResult(boolean paramBoolean1, Vec3 paramVec3, Direction paramDirection, BlockPos paramBlockPos, boolean paramBoolean2, boolean paramBoolean3) {
/* 26 */     super(paramVec3);
/* 27 */     this.miss = paramBoolean1;
/* 28 */     this.direction = paramDirection;
/* 29 */     this.blockPos = paramBlockPos;
/* 30 */     this.inside = paramBoolean2;
/* 31 */     this.worldBorderHit = paramBoolean3;
/*    */   }
/*    */   
/*    */   public BlockHitResult withDirection(Direction paramDirection) {
/* 35 */     return new BlockHitResult(this.miss, this.location, paramDirection, this.blockPos, this.inside, this.worldBorderHit);
/*    */   }
/*    */   
/*    */   public BlockHitResult withPosition(BlockPos paramBlockPos) {
/* 39 */     return new BlockHitResult(this.miss, this.location, this.direction, paramBlockPos, this.inside, this.worldBorderHit);
/*    */   }
/*    */   
/*    */   public BlockHitResult hitBorder() {
/* 43 */     return new BlockHitResult(this.miss, this.location, this.direction, this.blockPos, this.inside, true);
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 47 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 51 */     return this.direction;
/*    */   }
/*    */ 
/*    */   
/*    */   public HitResult.Type getType() {
/* 56 */     return this.miss ? HitResult.Type.MISS : HitResult.Type.BLOCK;
/*    */   }
/*    */   
/*    */   public boolean isInside() {
/* 60 */     return this.inside;
/*    */   }
/*    */   
/*    */   public boolean isWorldBorderHit() {
/* 64 */     return this.worldBorderHit;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\BlockHitResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */