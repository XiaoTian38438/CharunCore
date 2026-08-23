/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class PlaceOnWaterBlockItem extends BlockItem {
/*    */   public PlaceOnWaterBlockItem(Block paramBlock, Item.Properties paramProperties) {
/* 14 */     super(paramBlock, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 19 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 24 */     BlockHitResult blockHitResult1 = getPlayerPOVHitResult(paramLevel, paramPlayer, ClipContext.Fluid.SOURCE_ONLY);
/* 25 */     BlockHitResult blockHitResult2 = blockHitResult1.withPosition(blockHitResult1.getBlockPos().above());
/* 26 */     return super.useOn(new UseOnContext(paramPlayer, paramInteractionHand, blockHitResult2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\PlaceOnWaterBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */