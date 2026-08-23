/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class UseOnContext
/*    */ {
/*    */   private final Player player;
/*    */   private final InteractionHand hand;
/*    */   private final BlockHitResult hitResult;
/*    */   private final Level level;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public UseOnContext(Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 22 */     this(paramPlayer.level(), paramPlayer, paramInteractionHand, paramPlayer.getItemInHand(paramInteractionHand), paramBlockHitResult);
/*    */   }
/*    */   
/*    */   protected UseOnContext(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand, ItemStack paramItemStack, BlockHitResult paramBlockHitResult) {
/* 26 */     this.player = paramPlayer;
/* 27 */     this.hand = paramInteractionHand;
/* 28 */     this.hitResult = paramBlockHitResult;
/*    */     
/* 30 */     this.itemStack = paramItemStack;
/* 31 */     this.level = paramLevel;
/*    */   }
/*    */   
/*    */   protected final BlockHitResult getHitResult() {
/* 35 */     return this.hitResult;
/*    */   }
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 39 */     return this.hitResult.getBlockPos();
/*    */   }
/*    */   
/*    */   public Direction getClickedFace() {
/* 43 */     return this.hitResult.getDirection();
/*    */   }
/*    */   
/*    */   public Vec3 getClickLocation() {
/* 47 */     return this.hitResult.getLocation();
/*    */   }
/*    */   
/*    */   public boolean isInside() {
/* 51 */     return this.hitResult.isInside();
/*    */   }
/*    */   
/*    */   public ItemStack getItemInHand() {
/* 55 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 59 */     return this.player;
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 63 */     return this.hand;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 67 */     return this.level;
/*    */   }
/*    */   
/*    */   public Direction getHorizontalDirection() {
/* 71 */     return (this.player == null) ? Direction.NORTH : this.player.getDirection();
/*    */   }
/*    */   
/*    */   public boolean isSecondaryUseActive() {
/* 75 */     return (this.player != null && this.player.isSecondaryUseActive());
/*    */   }
/*    */   
/*    */   public float getRotation() {
/* 79 */     return (this.player == null) ? 0.0F : this.player.getYRot();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\context\UseOnContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */