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
/*    */ import net.minecraft.world.inventory.CraftingMenu;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CraftingTableBlock extends Block {
/* 18 */   public static final MapCodec<CraftingTableBlock> CODEC = simpleCodec(CraftingTableBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<? extends CraftingTableBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.crafting");
/*    */   
/*    */   protected CraftingTableBlock(BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 33 */     if (!paramLevel.isClientSide()) {
/* 34 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/* 35 */       paramPlayer.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
/*    */     } 
/* 37 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 42 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new CraftingMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CraftingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */