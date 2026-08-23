/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PositionalRandomFactory
/*    */ {
/*    */   default RandomSource at(BlockPos paramBlockPos) {
/* 20 */     return at(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*    */   }
/*    */   
/*    */   default RandomSource fromHashOf(Identifier paramIdentifier) {
/* 24 */     return fromHashOf(paramIdentifier.toString());
/*    */   }
/*    */   
/*    */   RandomSource fromHashOf(String paramString);
/*    */   
/*    */   RandomSource fromSeed(long paramLong);
/*    */   
/*    */   RandomSource at(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   @VisibleForTesting
/*    */   void parityConfigString(StringBuilder paramStringBuilder);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\PositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */