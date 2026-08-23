/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class SolidBucketItem
/*    */   extends BlockItem implements DispensibleContainerItem {
/*    */   public SolidBucketItem(Block paramBlock, SoundEvent paramSoundEvent, Item.Properties paramProperties) {
/* 21 */     super(paramBlock, paramProperties);
/* 22 */     this.placeSound = paramSoundEvent;
/*    */   }
/*    */   private final SoundEvent placeSound;
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 27 */     InteractionResult interactionResult = super.useOn(paramUseOnContext);
/* 28 */     Player player = paramUseOnContext.getPlayer();
/*    */     
/* 30 */     if (interactionResult.consumesAction() && player != null) {
/* 31 */       player.setItemInHand(paramUseOnContext.getHand(), BucketItem.getEmptySuccessItem(paramUseOnContext.getItemInHand(), player));
/*    */     }
/*    */     
/* 34 */     return interactionResult;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SoundEvent getPlaceSound(BlockState paramBlockState) {
/* 39 */     return this.placeSound;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean emptyContents(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockHitResult paramBlockHitResult) {
/* 44 */     if (paramLevel.isInWorldBounds(paramBlockPos) && paramLevel.isEmptyBlock(paramBlockPos)) {
/* 45 */       if (!paramLevel.isClientSide()) {
/* 46 */         paramLevel.setBlock(paramBlockPos, getBlock().defaultBlockState(), 3);
/*    */       }
/* 48 */       paramLevel.gameEvent((Entity)paramLivingEntity, (Holder)GameEvent.FLUID_PLACE, paramBlockPos);
/* 49 */       paramLevel.playSound((Entity)paramLivingEntity, paramBlockPos, this.placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 50 */       return true;
/*    */     } 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SolidBucketItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */