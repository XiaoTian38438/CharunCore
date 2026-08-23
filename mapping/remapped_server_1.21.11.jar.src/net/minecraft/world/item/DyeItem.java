/*    */ package net.minecraft.world.item;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class DyeItem extends Item implements SignApplicator {
/* 17 */   private static final Map<DyeColor, DyeItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);
/*    */   
/*    */   private final DyeColor dyeColor;
/*    */   
/*    */   public DyeItem(DyeColor paramDyeColor, Item.Properties paramProperties) {
/* 22 */     super(paramProperties);
/* 23 */     this.dyeColor = paramDyeColor;
/* 24 */     ITEM_BY_COLOR.put(paramDyeColor, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack paramItemStack, Player paramPlayer, LivingEntity paramLivingEntity, InteractionHand paramInteractionHand) {
/* 29 */     if (paramLivingEntity instanceof Sheep) { Sheep sheep = (Sheep)paramLivingEntity;
/* 30 */       if (sheep.isAlive() && !sheep.isSheared() && sheep.getColor() != this.dyeColor) {
/* 31 */         sheep.level().playSound((Entity)paramPlayer, (Entity)sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
/* 32 */         if (!paramPlayer.level().isClientSide()) {
/* 33 */           sheep.setColor(this.dyeColor);
/* 34 */           paramItemStack.shrink(1);
/*    */         } 
/* 36 */         return (InteractionResult)InteractionResult.SUCCESS;
/*    */       }  }
/*    */     
/* 39 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public DyeColor getDyeColor() {
/* 43 */     return this.dyeColor;
/*    */   }
/*    */   
/*    */   public static DyeItem byColor(DyeColor paramDyeColor) {
/* 47 */     return ITEM_BY_COLOR.get(paramDyeColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, Player paramPlayer) {
/* 52 */     if (paramSignBlockEntity.updateText(paramSignText -> paramSignText.setColor(getDyeColor()), paramBoolean)) {
/* 53 */       paramLevel.playSound(null, paramSignBlockEntity.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 54 */       return true;
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\DyeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */