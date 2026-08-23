/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.ScaffoldingBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ScaffoldingBlockItem extends BlockItem {
/*    */   public ScaffoldingBlockItem(Block paramBlock, Item.Properties paramProperties) {
/* 18 */     super(paramBlock, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext paramBlockPlaceContext) {
/* 23 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 24 */     Level level = paramBlockPlaceContext.getLevel();
/*    */     
/* 26 */     BlockState blockState = level.getBlockState(blockPos);
/* 27 */     Block block = getBlock();
/* 28 */     if (blockState.is(block)) {
/*    */       Direction direction;
/* 30 */       if (paramBlockPlaceContext.isSecondaryUseActive()) {
/* 31 */         direction = paramBlockPlaceContext.isInside() ? paramBlockPlaceContext.getClickedFace().getOpposite() : paramBlockPlaceContext.getClickedFace();
/*    */       } else {
/* 33 */         direction = (paramBlockPlaceContext.getClickedFace() == Direction.UP) ? paramBlockPlaceContext.getHorizontalDirection() : Direction.UP;
/*    */       } 
/*    */       
/* 36 */       byte b = 0;
/* 37 */       BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable().move(direction);
/* 38 */       while (b < 7) {
/* 39 */         if (!level.isClientSide() && !level.isInWorldBounds((BlockPos)mutableBlockPos)) {
/*    */           
/* 41 */           Player player = paramBlockPlaceContext.getPlayer();
/* 42 */           int i = level.getMaxY();
/* 43 */           if (player instanceof ServerPlayer && mutableBlockPos.getY() > i) {
/* 44 */             ((ServerPlayer)player).sendSystemMessage((Component)Component.translatable("build.tooHigh", new Object[] { Integer.valueOf(i) }).withStyle(ChatFormatting.RED), true);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/* 49 */         blockState = level.getBlockState((BlockPos)mutableBlockPos);
/*    */         
/* 51 */         if (!blockState.is(getBlock())) {
/* 52 */           if (blockState.canBeReplaced(paramBlockPlaceContext)) {
/* 53 */             return BlockPlaceContext.at(paramBlockPlaceContext, (BlockPos)mutableBlockPos, direction);
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/* 58 */         mutableBlockPos.move(direction);
/* 59 */         if (direction.getAxis().isHorizontal()) {
/* 60 */           b++;
/*    */         }
/*    */       } 
/*    */       
/* 64 */       return null;
/*    */     } 
/*    */     
/* 67 */     if (ScaffoldingBlock.getDistance((BlockGetter)level, blockPos) == 7) {
/* 68 */       return null;
/*    */     }
/*    */     
/* 71 */     return paramBlockPlaceContext;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mustSurvive() {
/* 76 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ScaffoldingBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */