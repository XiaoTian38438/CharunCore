/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ public interface CollisionContext
/*    */ {
/*    */   static CollisionContext empty() {
/* 17 */     return EntityCollisionContext.Empty.WITHOUT_FLUID_COLLISIONS;
/*    */   }
/*    */   
/*    */   static CollisionContext emptyWithFluidCollisions() {
/* 21 */     return EntityCollisionContext.Empty.WITH_FLUID_COLLISIONS;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity paramEntity) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: dup
/*    */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   5: pop
/*    */     //   6: astore_1
/*    */     //   7: iconst_0
/*    */     //   8: istore_2
/*    */     //   9: aload_1
/*    */     //   10: iload_2
/*    */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   16: lookupswitch default -> 76, 0 -> 36
/*    */     //   36: aload_1
/*    */     //   37: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*    */     //   40: astore_3
/*    */     //   41: aload_3
/*    */     //   42: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*    */     //   45: invokestatic useExperimentalMovement : (Lnet/minecraft/world/level/Level;)Z
/*    */     //   48: ifeq -> 63
/*    */     //   51: new net/minecraft/world/phys/shapes/MinecartCollisionContext
/*    */     //   54: dup
/*    */     //   55: aload_3
/*    */     //   56: iconst_0
/*    */     //   57: invokespecial <init> : (Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;Z)V
/*    */     //   60: goto -> 86
/*    */     //   63: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   66: dup
/*    */     //   67: aload_0
/*    */     //   68: iconst_0
/*    */     //   69: iconst_0
/*    */     //   70: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   73: goto -> 86
/*    */     //   76: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   79: dup
/*    */     //   80: aload_0
/*    */     //   81: iconst_0
/*    */     //   82: iconst_0
/*    */     //   83: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   86: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     //   #27	-> 36
/*    */     //   #28	-> 41
/*    */     //   #29	-> 51
/*    */     //   #31	-> 63
/*    */     //   #33	-> 76
/*    */     //   #26	-> 86
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity paramEntity, boolean paramBoolean) {
/* 39 */     return new EntityCollisionContext(paramEntity, paramBoolean, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext placementContext(Player paramPlayer) {
/* 47 */     Player player = paramPlayer; return new EntityCollisionContext((paramPlayer != null) ? paramPlayer.isDescending() : false, true, (paramPlayer != null) ? paramPlayer.getY() : -1.7976931348623157E308D, (paramPlayer instanceof LivingEntity) ? player.getMainHandItem() : ItemStack.EMPTY, false, (Entity)paramPlayer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext withPosition(Entity paramEntity, double paramDouble) {
/* 58 */     LivingEntity livingEntity = (LivingEntity)paramEntity; return new EntityCollisionContext((paramEntity != null) ? paramEntity.isDescending() : false, true, (paramEntity != null) ? paramDouble : -1.7976931348623157E308D, (paramEntity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY, false, paramEntity);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isDescending();
/*    */ 
/*    */   
/*    */   boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   boolean isHoldingItem(Item paramItem);
/*    */   
/*    */   boolean alwaysCollideWithFluid();
/*    */   
/*    */   boolean canStandOnFluid(FluidState paramFluidState1, FluidState paramFluidState2);
/*    */   
/*    */   VoxelShape getCollisionShape(BlockState paramBlockState, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos);
/*    */   
/*    */   default boolean isPlacement() {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\CollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */