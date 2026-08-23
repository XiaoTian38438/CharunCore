/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class BottleItem extends Item {
/*    */   public BottleItem(Item.Properties paramProperties) {
/* 27 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level paramLevel, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 32 */     List<AreaEffectCloud> list = paramLevel.getEntitiesOfClass(AreaEffectCloud.class, paramPlayer.getBoundingBox().inflate(2.0D), paramAreaEffectCloud -> (paramAreaEffectCloud.isAlive() && paramAreaEffectCloud.getOwner() instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon));
/*    */     
/* 34 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*    */     
/* 36 */     if (!list.isEmpty()) {
/* 37 */       AreaEffectCloud areaEffectCloud = list.get(0);
/* 38 */       areaEffectCloud.setRadius(areaEffectCloud.getRadius() - 0.5F);
/*    */       
/* 40 */       paramLevel.playSound(null, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 41 */       paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.FLUID_PICKUP, paramPlayer.position());
/* 42 */       if (paramPlayer instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)paramPlayer;
/* 43 */         CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, (Entity)areaEffectCloud); }
/*    */ 
/*    */       
/* 46 */       return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(turnBottleIntoItem(itemStack, paramPlayer, new ItemStack(Items.DRAGON_BREATH)));
/*    */     } 
/*    */     
/* 49 */     BlockHitResult blockHitResult = getPlayerPOVHitResult(paramLevel, paramPlayer, ClipContext.Fluid.SOURCE_ONLY);
/* 50 */     if (blockHitResult.getType() == HitResult.Type.MISS) {
/* 51 */       return (InteractionResult)InteractionResult.PASS;
/*    */     }
/*    */     
/* 54 */     if (blockHitResult.getType() == HitResult.Type.BLOCK) {
/* 55 */       BlockPos blockPos = blockHitResult.getBlockPos();
/*    */       
/* 57 */       if (!paramLevel.mayInteract((Entity)paramPlayer, blockPos)) {
/* 58 */         return (InteractionResult)InteractionResult.PASS;
/*    */       }
/* 60 */       if (paramLevel.getFluidState(blockPos).is(FluidTags.WATER)) {
/* 61 */         paramLevel.playSound((Entity)paramPlayer, paramPlayer.getX(), paramPlayer.getY(), paramPlayer.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 62 */         paramLevel.gameEvent((Entity)paramPlayer, (Holder)GameEvent.FLUID_PICKUP, blockPos);
/* 63 */         return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(turnBottleIntoItem(itemStack, paramPlayer, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
/*    */       } 
/*    */     } 
/*    */     
/* 67 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   protected ItemStack turnBottleIntoItem(ItemStack paramItemStack1, Player paramPlayer, ItemStack paramItemStack2) {
/* 71 */     paramPlayer.awardStat(Stats.ITEM_USED.get(this));
/* 72 */     return ItemUtils.createFilledResult(paramItemStack1, paramPlayer, paramItemStack2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\BottleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */