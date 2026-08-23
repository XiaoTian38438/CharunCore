/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class Containers
/*    */ {
/*    */   public static void dropContents(Level paramLevel, BlockPos paramBlockPos, Container paramContainer) {
/* 15 */     dropContents(paramLevel, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), paramContainer);
/*    */   }
/*    */   
/*    */   public static void dropContents(Level paramLevel, Entity paramEntity, Container paramContainer) {
/* 19 */     dropContents(paramLevel, paramEntity.getX(), paramEntity.getY(), paramEntity.getZ(), paramContainer);
/*    */   }
/*    */   
/*    */   private static void dropContents(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, Container paramContainer) {
/* 23 */     for (byte b = 0; b < paramContainer.getContainerSize(); b++) {
/* 24 */       dropItemStack(paramLevel, paramDouble1, paramDouble2, paramDouble3, paramContainer.getItem(b));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void dropContents(Level paramLevel, BlockPos paramBlockPos, NonNullList<ItemStack> paramNonNullList) {
/* 29 */     paramNonNullList.forEach(paramItemStack -> dropItemStack(paramLevel, paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), paramItemStack));
/*    */   }
/*    */   
/*    */   public static void dropItemStack(Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/* 33 */     double d1 = EntityType.ITEM.getWidth();
/* 34 */     double d2 = 1.0D - d1;
/* 35 */     double d3 = d1 / 2.0D;
/* 36 */     double d4 = Math.floor(paramDouble1) + paramLevel.random.nextDouble() * d2 + d3;
/* 37 */     double d5 = Math.floor(paramDouble2) + paramLevel.random.nextDouble() * d2;
/* 38 */     double d6 = Math.floor(paramDouble3) + paramLevel.random.nextDouble() * d2 + d3;
/*    */     
/* 40 */     while (!paramItemStack.isEmpty()) {
/* 41 */       ItemEntity itemEntity = new ItemEntity(paramLevel, d4, d5, d6, paramItemStack.split(paramLevel.random.nextInt(21) + 10));
/*    */       
/* 43 */       float f = 0.05F;
/* 44 */       itemEntity.setDeltaMovement(paramLevel.random
/* 45 */           .triangle(0.0D, 0.11485000171139836D), paramLevel.random
/* 46 */           .triangle(0.2D, 0.11485000171139836D), paramLevel.random
/* 47 */           .triangle(0.0D, 0.11485000171139836D));
/*    */ 
/*    */       
/* 50 */       paramLevel.addFreshEntity((Entity)itemEntity);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void updateNeighboursAfterDestroy(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 55 */     paramLevel.updateNeighbourForOutputSignal(paramBlockPos, paramBlockState.getBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\Containers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */