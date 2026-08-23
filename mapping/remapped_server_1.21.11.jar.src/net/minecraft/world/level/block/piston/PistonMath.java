/*    */ package net.minecraft.world.level.block.piston;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PistonMath
/*    */ {
/*    */   public static AABB getMovementArea(AABB paramAABB, Direction paramDirection, double paramDouble) {
/* 15 */     double d1 = paramDouble * paramDirection.getAxisDirection().getStep();
/* 16 */     double d2 = Math.min(d1, 0.0D);
/* 17 */     double d3 = Math.max(d1, 0.0D);
/* 18 */     switch (paramDirection)
/*    */     { case WEST:
/* 20 */         return new AABB(paramAABB.minX + d2, paramAABB.minY, paramAABB.minZ, paramAABB.minX + d3, paramAABB.maxY, paramAABB.maxZ);
/*    */       case EAST:
/* 22 */         return new AABB(paramAABB.maxX + d2, paramAABB.minY, paramAABB.minZ, paramAABB.maxX + d3, paramAABB.maxY, paramAABB.maxZ);
/*    */       case DOWN:
/* 24 */         return new AABB(paramAABB.minX, paramAABB.minY + d2, paramAABB.minZ, paramAABB.maxX, paramAABB.minY + d3, paramAABB.maxZ);
/*    */       
/*    */       default:
/* 27 */         return new AABB(paramAABB.minX, paramAABB.maxY + d2, paramAABB.minZ, paramAABB.maxX, paramAABB.maxY + d3, paramAABB.maxZ);
/*    */       case NORTH:
/* 29 */         return new AABB(paramAABB.minX, paramAABB.minY, paramAABB.minZ + d2, paramAABB.maxX, paramAABB.maxY, paramAABB.minZ + d3);
/*    */       case SOUTH:
/* 31 */         break; }  return new AABB(paramAABB.minX, paramAABB.minY, paramAABB.maxZ + d2, paramAABB.maxX, paramAABB.maxY, paramAABB.maxZ + d3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\piston\PistonMath.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */