/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityCollisionContext
/*    */   implements CollisionContext {
/*    */   private final boolean descending;
/*    */   private final double entityBottom;
/*    */   private final boolean placement;
/*    */   private final ItemStack heldItem;
/*    */   private final boolean alwaysCollideWithFluid;
/*    */   private final Entity entity;
/*    */   
/*    */   protected EntityCollisionContext(boolean paramBoolean1, boolean paramBoolean2, double paramDouble, ItemStack paramItemStack, boolean paramBoolean3, Entity paramEntity) {
/* 24 */     this.descending = paramBoolean1;
/* 25 */     this.placement = paramBoolean2;
/* 26 */     this.entityBottom = paramDouble;
/* 27 */     this.heldItem = paramItemStack;
/* 28 */     this.alwaysCollideWithFluid = paramBoolean3;
/* 29 */     this.entity = paramEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   protected EntityCollisionContext(Entity paramEntity, boolean paramBoolean1, boolean paramBoolean2) {
/* 35 */     this(paramEntity
/* 36 */         .isDescending(), paramBoolean2, paramEntity
/*    */         
/* 38 */         .getY(), 
/* 39 */         (paramEntity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY, paramBoolean1, paramEntity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isHoldingItem(Item paramItem) {
/* 47 */     return this.heldItem.is(paramItem);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean alwaysCollideWithFluid() {
/* 52 */     return this.alwaysCollideWithFluid;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canStandOnFluid(FluidState paramFluidState1, FluidState paramFluidState2) {
/* 57 */     Entity entity = this.entity; if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 58 */       return (livingEntity.canStandOnFluid(paramFluidState2) && !paramFluidState1.getType().isSame(paramFluidState2.getType())); }
/*    */     
/* 60 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getCollisionShape(BlockState paramBlockState, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos) {
/* 65 */     return paramBlockState.getCollisionShape((BlockGetter)paramCollisionGetter, paramBlockPos, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDescending() {
/* 70 */     return this.descending;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean) {
/* 75 */     return (this.entityBottom > paramBlockPos.getY() + paramVoxelShape.max(Direction.Axis.Y) - 9.999999747378752E-6D);
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 79 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPlacement() {
/* 84 */     return this.placement;
/*    */   }
/*    */   
/*    */   protected static class Empty extends EntityCollisionContext {
/* 88 */     protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new Empty(false);
/* 89 */     protected static final CollisionContext WITH_FLUID_COLLISIONS = new Empty(true);
/*    */     
/*    */     public Empty(boolean param1Boolean) {
/* 92 */       super(false, false, -1.7976931348623157E308D, ItemStack.EMPTY, param1Boolean, null);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isAbove(VoxelShape param1VoxelShape, BlockPos param1BlockPos, boolean param1Boolean) {
/* 97 */       return param1Boolean;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */