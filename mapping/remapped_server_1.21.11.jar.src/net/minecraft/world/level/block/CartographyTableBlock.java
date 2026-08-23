/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.CartographyTableMenu;
/*    */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class CartographyTableBlock extends Block {
/* 19 */   public static final MapCodec<CartographyTableBlock> CODEC = simpleCodec(CartographyTableBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<CartographyTableBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   private static final Component CONTAINER_TITLE = (Component)Component.translatable("container.cartography_table");
/*    */   
/*    */   protected CartographyTableBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 34 */     if (!paramLevel.isClientSide()) {
/* 35 */       paramPlayer.openMenu(paramBlockState.getMenuProvider(paramLevel, paramBlockPos));
/* 36 */       paramPlayer.awardStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
/*    */     } 
/* 38 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected MenuProvider getMenuProvider(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 43 */     return (MenuProvider)new SimpleMenuProvider((paramInt, paramInventory, paramPlayer) -> new CartographyTableMenu(paramInt, paramInventory, ContainerLevelAccess.create(paramLevel, paramBlockPos)), CONTAINER_TITLE);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CartographyTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */