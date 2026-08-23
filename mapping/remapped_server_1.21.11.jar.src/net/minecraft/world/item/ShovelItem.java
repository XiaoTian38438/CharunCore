/*    */ package net.minecraft.world.item;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CampfireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class ShovelItem extends Item {
/* 24 */   protected static final Map<Block, BlockState> FLATTENABLES = Maps.newHashMap((Map)(new ImmutableMap.Builder())
/* 25 */       .put(Blocks.GRASS_BLOCK, Blocks.DIRT_PATH.defaultBlockState())
/* 26 */       .put(Blocks.DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 27 */       .put(Blocks.PODZOL, Blocks.DIRT_PATH.defaultBlockState())
/* 28 */       .put(Blocks.COARSE_DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 29 */       .put(Blocks.MYCELIUM, Blocks.DIRT_PATH.defaultBlockState())
/* 30 */       .put(Blocks.ROOTED_DIRT, Blocks.DIRT_PATH.defaultBlockState())
/* 31 */       .build());
/*    */   
/*    */   public ShovelItem(ToolMaterial paramToolMaterial, float paramFloat1, float paramFloat2, Item.Properties paramProperties) {
/* 34 */     super(paramProperties.shovel(paramToolMaterial, paramFloat1, paramFloat2));
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 39 */     Level level = paramUseOnContext.getLevel();
/* 40 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*    */     
/* 42 */     BlockState blockState = level.getBlockState(blockPos);
/* 43 */     if (paramUseOnContext.getClickedFace() != Direction.DOWN) {
/* 44 */       Player player = paramUseOnContext.getPlayer();
/* 45 */       BlockState blockState1 = FLATTENABLES.get(blockState.getBlock());
/* 46 */       BlockState blockState2 = null;
/*    */       
/* 48 */       if (blockState1 != null && level.getBlockState(blockPos.above()).isAir()) {
/* 49 */         level.playSound((Entity)player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 50 */         blockState2 = blockState1;
/* 51 */       } else if (blockState.getBlock() instanceof CampfireBlock && ((Boolean)blockState.getValue((Property)CampfireBlock.LIT)).booleanValue()) {
/* 52 */         if (!level.isClientSide()) {
/* 53 */           level.levelEvent(null, 1009, blockPos, 0);
/*    */         }
/* 55 */         CampfireBlock.dowse((Entity)paramUseOnContext.getPlayer(), (LevelAccessor)level, blockPos, blockState);
/* 56 */         blockState2 = (BlockState)blockState.setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false));
/*    */       } 
/*    */       
/* 59 */       if (blockState2 != null) {
/* 60 */         if (!level.isClientSide()) {
/* 61 */           level.setBlock(blockPos, blockState2, 11);
/* 62 */           level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)player, blockState2));
/* 63 */           if (player != null) {
/* 64 */             paramUseOnContext.getItemInHand().hurtAndBreak(1, (LivingEntity)player, paramUseOnContext.getHand().asEquipmentSlot());
/*    */           }
/*    */         } 
/* 67 */         return (InteractionResult)InteractionResult.SUCCESS;
/*    */       } 
/* 69 */       return (InteractionResult)InteractionResult.PASS;
/*    */     } 
/*    */     
/* 72 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ShovelItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */