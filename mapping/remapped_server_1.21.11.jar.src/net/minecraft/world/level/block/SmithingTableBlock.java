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
/*    */ import net.minecraft.world.inventory.SmithingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SmithingTableBlock extends CraftingTableBlock {
/* 18 */   public static final MapCodec<SmithingTableBlock> CODEC = simpleCodec(SmithingTableBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SmithingTableBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SmithingTableBlock(BlockBehaviour.Properties paramProperties) {
/* 26 */     super(paramProperties);
/*    */   }
/*    */   
/* 29 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.upgrade");
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 33 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new SmithingMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 38 */     if (!paramLevel.isClientSide()) {
/* 39 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/* 40 */       paramPlayer.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
/*    */     } 
/* 42 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SmithingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */