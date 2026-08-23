/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public interface SignApplicator
/*    */ {
/*    */   boolean tryApplyToSign(Level paramLevel, SignBlockEntity paramSignBlockEntity, boolean paramBoolean, Player paramPlayer);
/*    */   
/*    */   default boolean canApplyToSign(SignText paramSignText, Player paramPlayer) {
/* 13 */     return paramSignText.hasMessage(paramPlayer);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\SignApplicator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */