/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public interface Hopper extends Container {
/*  8 */   public static final AABB SUCK_AABB = Block.column(16.0D, 11.0D, 32.0D).toAabbs().get(0);
/*    */   
/*    */   default AABB getSuckAabb() {
/* 11 */     return SUCK_AABB;
/*    */   }
/*    */   
/*    */   double getLevelX();
/*    */   
/*    */   double getLevelY();
/*    */   
/*    */   double getLevelZ();
/*    */   
/*    */   boolean isGridAligned();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\Hopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */