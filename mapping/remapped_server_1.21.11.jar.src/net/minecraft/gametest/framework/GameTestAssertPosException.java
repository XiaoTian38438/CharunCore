/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertPosException
/*    */   extends GameTestAssertException {
/*    */   private final BlockPos absolutePos;
/*    */   private final BlockPos relativePos;
/*    */   
/*    */   public GameTestAssertPosException(Component paramComponent, BlockPos paramBlockPos1, BlockPos paramBlockPos2, int paramInt) {
/* 12 */     super(paramComponent, paramInt);
/* 13 */     this.absolutePos = paramBlockPos1;
/* 14 */     this.relativePos = paramBlockPos2;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 19 */     return (Component)Component.translatable("test.error.position", new Object[] { this.message, Integer.valueOf(this.absolutePos.getX()), Integer.valueOf(this.absolutePos.getY()), Integer.valueOf(this.absolutePos.getZ()), Integer.valueOf(this.relativePos.getX()), Integer.valueOf(this.relativePos.getY()), Integer.valueOf(this.relativePos.getZ()), Integer.valueOf(this.tick) });
/*    */   }
/*    */   
/*    */   public Component getMessageToShowAtBlock() {
/* 23 */     return this.message;
/*    */   }
/*    */   
/*    */   public BlockPos getRelativePos() {
/* 27 */     return this.relativePos;
/*    */   }
/*    */   
/*    */   public BlockPos getAbsolutePos() {
/* 31 */     return this.absolutePos;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestAssertPosException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */