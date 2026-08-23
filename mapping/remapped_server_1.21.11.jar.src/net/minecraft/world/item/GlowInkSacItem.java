/*    */ package net.minecraft.world.item;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class GlowInkSacItem extends Item implements SignApplicator {
/*    */   public GlowInkSacItem(Item.Properties paramProperties) {
/* 11 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, Player paramPlayer) {
/* 16 */     if (paramSignBlockEntity.updateText(paramSignText -> paramSignText.setHasGlowingText(true), paramBoolean)) {
/* 17 */       paramLevel.playSound(null, paramSignBlockEntity.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\GlowInkSacItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */