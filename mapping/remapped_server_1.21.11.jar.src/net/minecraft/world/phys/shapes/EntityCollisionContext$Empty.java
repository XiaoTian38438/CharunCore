/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ public class Empty
/*    */   extends EntityCollisionContext
/*    */ {
/* 88 */   protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new Empty(false);
/* 89 */   protected static final CollisionContext WITH_FLUID_COLLISIONS = new Empty(true);
/*    */   
/*    */   public Empty(boolean paramBoolean) {
/* 92 */     super(false, false, -1.7976931348623157E308D, ItemStack.EMPTY, paramBoolean, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean) {
/* 97 */     return paramBoolean;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext$Empty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */