/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.inventory.LoomMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class LoomBlock extends HorizontalDirectionalBlock {
/* 20 */   public static final MapCodec<LoomBlock> CODEC = simpleCodec(LoomBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<LoomBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/* 27 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.loom");
/*    */   
/*    */   protected LoomBlock(BlockBehaviour.Properties paramProperties) {
/* 30 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 35 */     if (!paramLevel.isClientSide()) {
/* 36 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/* 37 */       paramPlayer.awardStat(Stats.INTERACT_WITH_LOOM);
/*    */     } 
/* 39 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 44 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new LoomMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 49 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 54 */     paramBuilder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LoomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */