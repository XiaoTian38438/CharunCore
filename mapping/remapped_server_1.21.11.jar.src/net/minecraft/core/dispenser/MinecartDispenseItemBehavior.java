/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MinecartDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 19 */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*    */   private final EntityType<? extends AbstractMinecart> entityType;
/*    */   
/*    */   public MinecartDispenseItemBehavior(EntityType<? extends AbstractMinecart> paramEntityType) {
/* 23 */     this.entityType = paramEntityType;
/*    */   }
/*    */   
/*    */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/*    */     double d4;
/* 28 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 29 */     ServerLevel serverLevel = paramBlockSource.level();
/* 30 */     Vec3 vec31 = paramBlockSource.center();
/*    */ 
/*    */ 
/*    */     
/* 34 */     double d1 = vec31.x() + direction.getStepX() * 1.125D;
/* 35 */     double d2 = Math.floor(vec31.y()) + direction.getStepY();
/* 36 */     double d3 = vec31.z() + direction.getStepZ() * 1.125D;
/*    */     
/* 38 */     BlockPos blockPos = paramBlockSource.pos().relative(direction);
/* 39 */     BlockState blockState = serverLevel.getBlockState(blockPos);
/*    */ 
/*    */     
/* 42 */     if (blockState.is(BlockTags.RAILS)) {
/* 43 */       if (getRailShape(blockState).isSlope()) {
/* 44 */         d4 = 0.6D;
/*    */       } else {
/* 46 */         d4 = 0.1D;
/*    */       } 
/* 48 */     } else if (blockState.isAir()) {
/* 49 */       BlockState blockState1 = serverLevel.getBlockState(blockPos.below());
/* 50 */       if (blockState1.is(BlockTags.RAILS)) {
/* 51 */         if (direction == Direction.DOWN || !getRailShape(blockState1).isSlope()) {
/* 52 */           d4 = -0.9D;
/*    */         } else {
/* 54 */           d4 = -0.4D;
/*    */         } 
/*    */       } else {
/* 57 */         return this.defaultDispenseItemBehavior.dispense(paramBlockSource, paramItemStack);
/*    */       } 
/*    */     } else {
/* 60 */       return this.defaultDispenseItemBehavior.dispense(paramBlockSource, paramItemStack);
/*    */     } 
/*    */     
/* 63 */     Vec3 vec32 = new Vec3(d1, d2 + d4, d3);
/* 64 */     AbstractMinecart abstractMinecart = AbstractMinecart.createMinecart((Level)serverLevel, vec32.x, vec32.y, vec32.z, this.entityType, EntitySpawnReason.DISPENSER, paramItemStack, null);
/* 65 */     if (abstractMinecart != null) {
/* 66 */       serverLevel.addFreshEntity((Entity)abstractMinecart);
/* 67 */       paramItemStack.shrink(1);
/*    */     } 
/* 69 */     return paramItemStack;
/*    */   }
/*    */   
/*    */   private static RailShape getRailShape(BlockState paramBlockState) {
/* 73 */     Block block = paramBlockState.getBlock(); BaseRailBlock baseRailBlock = (BaseRailBlock)block; return (block instanceof BaseRailBlock) ? (RailShape)paramBlockState.getValue(baseRailBlock.getShapeProperty()) : RailShape.NORTH_SOUTH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource paramBlockSource) {
/* 78 */     paramBlockSource.level().levelEvent(1000, paramBlockSource.pos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\MinecartDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */