/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TheEndPortalBlockEntity extends BlockEntity {
/*    */   protected TheEndPortalBlockEntity(BlockEntityType<?> paramBlockEntityType, BlockPos paramBlockPos, BlockState paramBlockState) {
/*  9 */     super(paramBlockEntityType, paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   public TheEndPortalBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 13 */     this(BlockEntityType.END_PORTAL, paramBlockPos, paramBlockState);
/*    */   }
/*    */   
/*    */   public boolean shouldRenderFace(Direction paramDirection) {
/* 17 */     return (paramDirection.getAxis() == Direction.Axis.Y);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\TheEndPortalBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */