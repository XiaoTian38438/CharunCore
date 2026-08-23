/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPlaceContext
/*    */   extends UseOnContext
/*    */ {
/*    */   private final BlockPos relativePos;
/*    */   protected boolean replaceClicked = true;
/*    */   
/*    */   public BlockPlaceContext(Player paramPlayer, InteractionHand paramInteractionHand, ItemStack paramItemStack, BlockHitResult paramBlockHitResult) {
/* 20 */     this(paramPlayer.level(), paramPlayer, paramInteractionHand, paramItemStack, paramBlockHitResult);
/*    */   }
/*    */   
/*    */   public BlockPlaceContext(UseOnContext paramUseOnContext) {
/* 24 */     this(paramUseOnContext.getLevel(), paramUseOnContext.getPlayer(), paramUseOnContext.getHand(), paramUseOnContext.getItemInHand(), paramUseOnContext.getHitResult());
/*    */   }
/*    */   
/*    */   protected BlockPlaceContext(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand, ItemStack paramItemStack, BlockHitResult paramBlockHitResult) {
/* 28 */     super(paramLevel, paramPlayer, paramInteractionHand, paramItemStack, paramBlockHitResult);
/*    */     
/* 30 */     this.relativePos = paramBlockHitResult.getBlockPos().relative(paramBlockHitResult.getDirection());
/* 31 */     this.replaceClicked = paramLevel.getBlockState(paramBlockHitResult.getBlockPos()).canBeReplaced(this);
/*    */   }
/*    */   
/*    */   public static BlockPlaceContext at(BlockPlaceContext paramBlockPlaceContext, BlockPos paramBlockPos, Direction paramDirection) {
/* 35 */     return new BlockPlaceContext(paramBlockPlaceContext
/* 36 */         .getLevel(), paramBlockPlaceContext
/* 37 */         .getPlayer(), paramBlockPlaceContext
/* 38 */         .getHand(), paramBlockPlaceContext
/* 39 */         .getItemInHand(), new BlockHitResult(new Vec3(paramBlockPos
/*    */ 
/*    */             
/* 42 */             .getX() + 0.5D + paramDirection.getStepX() * 0.5D, paramBlockPos
/* 43 */             .getY() + 0.5D + paramDirection.getStepY() * 0.5D, paramBlockPos
/* 44 */             .getZ() + 0.5D + paramDirection.getStepZ() * 0.5D), paramDirection, paramBlockPos, false));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 55 */     return this.replaceClicked ? super.getClickedPos() : this.relativePos;
/*    */   }
/*    */   
/*    */   public boolean canPlace() {
/* 59 */     return (this.replaceClicked || getLevel().getBlockState(getClickedPos()).canBeReplaced(this));
/*    */   }
/*    */   
/*    */   public boolean replacingClickedOnBlock() {
/* 63 */     return this.replaceClicked;
/*    */   }
/*    */   
/*    */   public Direction getNearestLookingDirection() {
/* 67 */     return Direction.orderedByNearest((Entity)getPlayer())[0];
/*    */   }
/*    */   
/*    */   public Direction getNearestLookingVerticalDirection() {
/* 71 */     return Direction.getFacingAxis((Entity)getPlayer(), Direction.Axis.Y);
/*    */   }
/*    */   
/*    */   public Direction[] getNearestLookingDirections() {
/* 75 */     Direction[] arrayOfDirection = Direction.orderedByNearest((Entity)getPlayer());
/*    */     
/* 77 */     if (this.replaceClicked) {
/* 78 */       return arrayOfDirection;
/*    */     }
/*    */     
/* 81 */     Direction direction = getClickedFace();
/*    */ 
/*    */     
/* 84 */     byte b = 0;
/* 85 */     for (; b < arrayOfDirection.length && 
/* 86 */       arrayOfDirection[b] != direction.getOpposite(); b++);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 91 */     if (b > 0) {
/* 92 */       System.arraycopy(arrayOfDirection, 0, arrayOfDirection, 1, b);
/* 93 */       arrayOfDirection[0] = direction.getOpposite();
/*    */     } 
/* 95 */     return arrayOfDirection;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\context\BlockPlaceContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */