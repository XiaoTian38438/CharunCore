/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ 
/*    */ public class StandingAndWallBlockItem
/*    */   extends BlockItem {
/*    */   protected final Block wallBlock;
/*    */   private final Direction attachmentDirection;
/*    */   
/*    */   public StandingAndWallBlockItem(Block paramBlock1, Block paramBlock2, Direction paramDirection, Item.Properties paramProperties) {
/* 19 */     super(paramBlock1, paramProperties);
/* 20 */     this.wallBlock = paramBlock2;
/* 21 */     this.attachmentDirection = paramDirection;
/*    */   }
/*    */   
/*    */   protected boolean canPlace(LevelReader paramLevelReader, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 25 */     return paramBlockState.canSurvive(paramLevelReader, paramBlockPos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState getPlacementState(BlockPlaceContext paramBlockPlaceContext) {
/* 30 */     BlockState blockState1 = this.wallBlock.getStateForPlacement(paramBlockPlaceContext);
/*    */     
/* 32 */     BlockState blockState2 = null;
/*    */     
/* 34 */     Level level = paramBlockPlaceContext.getLevel();
/* 35 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 36 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/* 37 */       if (direction != this.attachmentDirection.getOpposite()) {
/*    */ 
/*    */ 
/*    */         
/* 41 */         BlockState blockState = (direction == this.attachmentDirection) ? getBlock().getStateForPlacement(paramBlockPlaceContext) : blockState1;
/* 42 */         if (blockState != null && canPlace((LevelReader)level, blockState, blockPos)) {
/* 43 */           blockState2 = blockState;
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 48 */     return (blockState2 != null && level.isUnobstructed(blockState2, blockPos, CollisionContext.empty())) ? blockState2 : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerBlocks(Map<Block, Item> paramMap, Item paramItem) {
/* 53 */     super.registerBlocks(paramMap, paramItem);
/*    */     
/* 55 */     paramMap.put(this.wallBlock, paramItem);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\StandingAndWallBlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */