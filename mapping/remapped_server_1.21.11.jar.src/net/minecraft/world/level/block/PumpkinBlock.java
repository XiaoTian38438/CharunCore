/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PumpkinBlock extends Block {
/* 23 */   public static final MapCodec<PumpkinBlock> CODEC = simpleCodec(PumpkinBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<PumpkinBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected PumpkinBlock(BlockBehaviour.Properties paramProperties) {
/* 31 */     super(paramProperties);
/*    */   }
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/*    */     ServerLevel serverLevel;
/* 36 */     if (!paramItemStack.is(Items.SHEARS)) {
/* 37 */       return super.useItemOn(paramItemStack, paramBlockState, paramLevel, paramBlockPos, paramPlayer, paramInteractionHand, paramBlockHitResult);
/*    */     }
/* 39 */     if (paramLevel instanceof ServerLevel) { serverLevel = (ServerLevel)paramLevel; }
/* 40 */     else { return (InteractionResult)InteractionResult.SUCCESS; }
/*    */     
/* 42 */     Direction direction1 = paramBlockHitResult.getDirection();
/* 43 */     Direction direction2 = (direction1.getAxis() == Direction.Axis.Y) ? paramPlayer.getDirection().getOpposite() : direction1;
/*    */     
/* 45 */     dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.CARVE_PUMPKIN, paramBlockState, paramLevel.getBlockEntity(paramBlockPos), paramItemStack, (Entity)paramPlayer, (paramServerLevel, paramItemStack) -> {
/*    */           ItemEntity itemEntity = new ItemEntity(paramLevel, paramBlockPos.getX() + 0.5D + paramDirection.getStepX() * 0.65D, paramBlockPos.getY() + 0.1D, paramBlockPos.getZ() + 0.5D + paramDirection.getStepZ() * 0.65D, paramItemStack);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           itemEntity.setDeltaMovement(0.05D * paramDirection.getStepX() + paramLevel.random.nextDouble() * 0.02D, 0.05D, 0.05D * paramDirection.getStepZ() + paramLevel.random.nextDouble() * 0.02D);
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           paramLevel.addFreshEntity((Entity)itemEntity);
/*    */         });
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     paramLevel.playSound(null, paramBlockPos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 64 */     paramLevel.setBlock(paramBlockPos, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue((Property)CarvedPumpkinBlock.FACING, (Comparable)direction2), 11);
/* 65 */     paramItemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/* 66 */     paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.SHEAR, paramBlockPos);
/* 67 */     paramPlayer.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
/*    */     
/* 69 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\PumpkinBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */