/*    */ package net.minecraft.core.dispenser;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.Shearable;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BeehiveBlock;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class ShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {
/*    */   protected ItemStack execute(BlockSource paramBlockSource, ItemStack paramItemStack) {
/* 24 */     ServerLevel serverLevel = paramBlockSource.level();
/* 25 */     if (!serverLevel.isClientSide()) {
/* 26 */       BlockPos blockPos = paramBlockSource.pos().relative((Direction)paramBlockSource.state().getValue((Property)DispenserBlock.FACING));
/*    */       
/* 28 */       setSuccess((tryShearBeehive(serverLevel, paramItemStack, blockPos) || tryShearEntity(serverLevel, blockPos, paramItemStack)));
/* 29 */       if (isSuccess())
/* 30 */         paramItemStack.hurtAndBreak(1, serverLevel, null, paramItem -> {
/*    */             
/*    */             }); 
/* 33 */     }  return paramItemStack;
/*    */   }
/*    */   
/*    */   private static boolean tryShearBeehive(ServerLevel paramServerLevel, ItemStack paramItemStack, BlockPos paramBlockPos) {
/* 37 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos);
/* 38 */     if (blockState.is(BlockTags.BEEHIVES, paramBlockStateBase -> (paramBlockStateBase.hasProperty((Property)BeehiveBlock.HONEY_LEVEL) && paramBlockStateBase.getBlock() instanceof BeehiveBlock))) {
/* 39 */       int i = ((Integer)blockState.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue();
/*    */       
/* 41 */       if (i >= 5) {
/* 42 */         paramServerLevel.playSound(null, paramBlockPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */         
/* 44 */         BeehiveBlock.dropHoneycomb(paramServerLevel, paramItemStack, blockState, paramServerLevel.getBlockEntity(paramBlockPos), null, paramBlockPos);
/* 45 */         ((BeehiveBlock)blockState.getBlock()).releaseBeesAndResetHoneyLevel((Level)paramServerLevel, blockState, paramBlockPos, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 46 */         paramServerLevel.gameEvent(null, (Holder)GameEvent.SHEAR, paramBlockPos);
/* 47 */         return true;
/*    */       } 
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean tryShearEntity(ServerLevel paramServerLevel, BlockPos paramBlockPos, ItemStack paramItemStack) {
/* 54 */     List list = paramServerLevel.getEntitiesOfClass(Entity.class, new AABB(paramBlockPos), EntitySelector.NO_SPECTATORS);
/* 55 */     for (Entity entity : list) {
/* 56 */       if (entity.shearOffAllLeashConnections(null)) {
/* 57 */         return true;
/*    */       }
/* 59 */       if (entity instanceof Shearable) { Shearable shearable = (Shearable)entity;
/* 60 */         if (shearable.readyForShearing()) {
/* 61 */           shearable.shear(paramServerLevel, SoundSource.BLOCKS, paramItemStack);
/* 62 */           paramServerLevel.gameEvent(null, (Holder)GameEvent.SHEAR, paramBlockPos);
/* 63 */           return true;
/*    */         }  }
/*    */     
/*    */     } 
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\ShearsDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */