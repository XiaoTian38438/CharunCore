/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.Tool;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.GrowingPlantHeadBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class ShearsItem extends Item {
/*    */   public ShearsItem(Item.Properties paramProperties) {
/* 30 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Tool createToolProperties() {
/* 35 */     HolderGetter holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.BLOCK);
/*    */     
/* 37 */     return new Tool(
/* 38 */         List.of(
/* 39 */           Tool.Rule.minesAndDrops((HolderSet)HolderSet.direct(new Holder[] { (Holder)Blocks.COBWEB.builtInRegistryHolder() }, ), 15.0F), 
/* 40 */           Tool.Rule.overrideSpeed((HolderSet)holderGetter.getOrThrow(BlockTags.LEAVES), 15.0F), 
/* 41 */           Tool.Rule.overrideSpeed((HolderSet)holderGetter.getOrThrow(BlockTags.WOOL), 5.0F), 
/* 42 */           Tool.Rule.overrideSpeed((HolderSet)HolderSet.direct(new Holder[] { (Holder)Blocks.VINE.builtInRegistryHolder(), (Holder)Blocks.GLOW_LICHEN.builtInRegistryHolder() }, ), 2.0F)), 1.0F, 1, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mineBlock(ItemStack paramItemStack, Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, LivingEntity paramLivingEntity) {
/* 52 */     Tool tool = (Tool)paramItemStack.get(DataComponents.TOOL);
/* 53 */     if (tool == null) {
/* 54 */       return false;
/*    */     }
/*    */     
/* 57 */     if (!paramLevel.isClientSide() && !paramBlockState.is(BlockTags.FIRE) && tool.damagePerBlock() > 0) {
/* 58 */       paramItemStack.hurtAndBreak(tool.damagePerBlock(), paramLivingEntity, EquipmentSlot.MAINHAND);
/*    */     }
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 65 */     Level level = paramUseOnContext.getLevel();
/* 66 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/* 67 */     BlockState blockState = level.getBlockState(blockPos);
/* 68 */     Block block = blockState.getBlock();
/* 69 */     if (block instanceof GrowingPlantHeadBlock) { GrowingPlantHeadBlock growingPlantHeadBlock = (GrowingPlantHeadBlock)block;
/* 70 */       if (!growingPlantHeadBlock.isMaxAge(blockState)) {
/* 71 */         Player player = paramUseOnContext.getPlayer();
/* 72 */         ItemStack itemStack = paramUseOnContext.getItemInHand();
/* 73 */         if (player instanceof ServerPlayer) {
/* 74 */           CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
/*    */         }
/* 76 */         level.playSound((Entity)player, blockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 77 */         BlockState blockState1 = growingPlantHeadBlock.getMaxAgeState(blockState);
/* 78 */         level.setBlockAndUpdate(blockPos, blockState1);
/* 79 */         level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)paramUseOnContext.getPlayer(), blockState1));
/* 80 */         if (player != null) {
/* 81 */           itemStack.hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*    */         }
/*    */         
/* 84 */         return (InteractionResult)InteractionResult.SUCCESS;
/*    */       }  }
/*    */     
/* 87 */     return super.useOn(paramUseOnContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ShearsItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */