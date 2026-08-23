/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SelectableSlotContainer
/*    */ {
/*    */   default OptionalInt getHitSlot(BlockHitResult paramBlockHitResult, Direction paramDirection) {
/* 19 */     return getRelativeHitCoordinatesForBlockFace(paramBlockHitResult, paramDirection)
/* 20 */       .<OptionalInt>map(paramVec2 -> {
/*    */           int i = getSection(1.0F - paramVec2.y, getRows());
/*    */           int j = getSection(paramVec2.x, getColumns());
/*    */           return OptionalInt.of(j + i * getColumns());
/* 24 */         }).orElseGet(OptionalInt::empty);
/*    */   }
/*    */   
/*    */   private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult paramBlockHitResult, Direction paramDirection) {
/* 28 */     Direction direction = paramBlockHitResult.getDirection();
/*    */     
/* 30 */     if (paramDirection != direction) {
/* 31 */       return Optional.empty();
/*    */     }
/*    */     
/* 34 */     BlockPos blockPos = paramBlockHitResult.getBlockPos().relative(direction);
/* 35 */     Vec3 vec3 = paramBlockHitResult.getLocation().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/*    */     
/* 37 */     double d1 = vec3.x();
/* 38 */     double d2 = vec3.y();
/* 39 */     double d3 = vec3.z();
/*    */     
/* 41 */     switch (direction) { default: throw new MatchException(null, null);case NORTH: case SOUTH: case WEST: case EAST: case DOWN: case UP: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 46 */       Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   private static int getSection(float paramFloat, int paramInt) {
/* 51 */     float f1 = paramFloat * 16.0F;
/* 52 */     float f2 = 16.0F / paramInt;
/* 53 */     return Mth.clamp(Mth.floor(f1 / f2), 0, paramInt - 1);
/*    */   }
/*    */   
/*    */   int getRows();
/*    */   
/*    */   int getColumns();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SelectableSlotContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */