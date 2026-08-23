/*    */ package net.minecraft.world.item;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Leashable;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class LeadItem extends Item {
/*    */   public LeadItem(Item.Properties paramProperties) {
/* 19 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext paramUseOnContext) {
/* 24 */     Level level = paramUseOnContext.getLevel();
/* 25 */     BlockPos blockPos = paramUseOnContext.getClickedPos();
/*    */     
/* 27 */     BlockState blockState = level.getBlockState(blockPos);
/* 28 */     if (blockState.is(BlockTags.FENCES)) {
/* 29 */       Player player = paramUseOnContext.getPlayer();
/* 30 */       if (!level.isClientSide() && player != null) {
/* 31 */         return bindPlayerMobs(player, level, blockPos);
/*    */       }
/*    */     } 
/*    */     
/* 35 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public static InteractionResult bindPlayerMobs(Player paramPlayer, Level paramLevel, BlockPos paramBlockPos) {
/* 39 */     LeashFenceKnotEntity leashFenceKnotEntity = null;
/*    */     
/* 41 */     List list = Leashable.leashableInArea(paramLevel, Vec3.atCenterOf((Vec3i)paramBlockPos), paramLeashable -> (paramLeashable.getLeashHolder() == paramPlayer));
/* 42 */     boolean bool = false;
/* 43 */     for (Leashable leashable : list) {
/* 44 */       if (leashFenceKnotEntity == null) {
/* 45 */         leashFenceKnotEntity = LeashFenceKnotEntity.getOrCreateKnot(paramLevel, paramBlockPos);
/* 46 */         leashFenceKnotEntity.playPlacementSound();
/*    */       } 
/* 48 */       if (leashable.canHaveALeashAttachedTo((Entity)leashFenceKnotEntity)) {
/* 49 */         leashable.setLeashedTo((Entity)leashFenceKnotEntity, true);
/* 50 */         bool = true;
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     if (bool) {
/* 55 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_ATTACH, paramBlockPos, GameEvent.Context.of((Entity)paramPlayer));
/* 56 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*    */     } 
/*    */     
/* 59 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\LeadItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */