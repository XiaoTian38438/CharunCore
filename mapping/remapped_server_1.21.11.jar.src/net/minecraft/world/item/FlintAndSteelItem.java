/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BaseFireBlock;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.CandleCakeBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class FlintAndSteelItem extends Item {
/*    */   public FlintAndSteelItem(Item.Properties paramProperties) {
/* 24 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 29 */     Player player = paramUseOnContext.getPlayer();
/* 30 */     Level level = paramUseOnContext.getLevel();
/* 31 */     BlockPos blockPos1 = paramUseOnContext.getClickedPos();
/*    */     
/* 33 */     BlockState blockState = level.getBlockState(blockPos1);
/* 34 */     if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
/* 35 */       level.playSound((Entity)player, blockPos1, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/* 36 */       level.setBlock(blockPos1, (BlockState)blockState.setValue((Property)BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
/* 37 */       level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_CHANGE, blockPos1);
/* 38 */       if (player != null) {
/* 39 */         paramUseOnContext.getItemInHand().hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*    */       }
/* 41 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 44 */     BlockPos blockPos2 = blockPos1.relative(paramUseOnContext.getClickedFace());
/* 45 */     if (BaseFireBlock.canBePlacedAt(level, blockPos2, paramUseOnContext.getHorizontalDirection())) {
/* 46 */       level.playSound((Entity)player, blockPos2, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
/*    */       
/* 48 */       BlockState blockState1 = BaseFireBlock.getState((BlockGetter)level, blockPos2);
/* 49 */       level.setBlock(blockPos2, blockState1, 11);
/* 50 */       level.gameEvent((Entity)player, (Holder)GameEvent.BLOCK_PLACE, blockPos1);
/*    */       
/* 52 */       ItemStack itemStack = paramUseOnContext.getItemInHand();
/* 53 */       if (player instanceof ServerPlayer) {
/* 54 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos2, itemStack);
/* 55 */         itemStack.hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*    */       } 
/*    */       
/* 58 */       return (InteractionResult)InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 61 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\FlintAndSteelItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */