/*    */ package net.minecraft.core.dispenser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 16 */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*    */   private final EntityType<? extends AbstractBoat> type;
/*    */   
/*    */   public BoatDispenseItemBehavior(EntityType<? extends AbstractBoat> paramEntityType) {
/* 20 */     this.type = paramEntityType;
/*    */   }
/*    */   
/*    */   public ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/*    */     double d5;
/* 25 */     Direction direction = (Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING);
/* 26 */     ServerLevel serverLevel = paramBlockSource.level();
/* 27 */     Vec3 vec3 = paramBlockSource.center();
/*    */     
/* 29 */     double d1 = 0.5625D + this.type.getWidth() / 2.0D;
/* 30 */     double d2 = vec3.x() + direction.getStepX() * d1;
/* 31 */     double d3 = vec3.y() + (direction.getStepY() * 1.125F);
/* 32 */     double d4 = vec3.z() + direction.getStepZ() * d1;
/*    */     
/* 34 */     BlockPos blockPos = paramBlockSource.pos().relative(direction);
/*    */ 
/*    */     
/* 37 */     if (serverLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 38 */       d5 = 1.0D;
/* 39 */     } else if (serverLevel.getBlockState(blockPos).isAir() && serverLevel.getFluidState(blockPos.below()).is(FluidTags.WATER)) {
/* 40 */       d5 = 0.0D;
/*    */     } else {
/* 42 */       return this.defaultDispenseItemBehavior.dispense(paramBlockSource, paramItemStack);
/*    */     } 
/*    */     
/* 45 */     AbstractBoat abstractBoat = (AbstractBoat)this.type.create((Level)serverLevel, EntitySpawnReason.DISPENSER);
/*    */     
/* 47 */     if (abstractBoat != null) {
/* 48 */       abstractBoat.setInitialPos(d2, d3 + d5, d4);
/* 49 */       EntityType.createDefaultStackConfig((Level)serverLevel, paramItemStack, null).accept(abstractBoat);
/* 50 */       abstractBoat.setYRot(direction.toYRot());
/* 51 */       serverLevel.addFreshEntity((Entity)abstractBoat);
/*    */       
/* 53 */       paramItemStack.shrink(1);
/*    */     } 
/* 55 */     return paramItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource paramBlockSource) {
/* 60 */     paramBlockSource.level().levelEvent(1000, paramBlockSource.pos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\BoatDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */