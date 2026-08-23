/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.util.Mth;
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
/*    */ 
/*    */ 
/*    */ public class LegacyPositionalRandomFactory
/*    */   implements PositionalRandomFactory
/*    */ {
/*    */   private final long seed;
/*    */   
/*    */   public LegacyPositionalRandomFactory(long paramLong) {
/* 64 */     this.seed = paramLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource at(int paramInt1, int paramInt2, int paramInt3) {
/* 69 */     long l1 = Mth.getSeed(paramInt1, paramInt2, paramInt3);
/* 70 */     long l2 = l1 ^ this.seed;
/* 71 */     return new LegacyRandomSource(l2);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fromHashOf(String paramString) {
/* 76 */     int i = paramString.hashCode();
/* 77 */     return new LegacyRandomSource(i ^ this.seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fromSeed(long paramLong) {
/* 82 */     return new LegacyRandomSource(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   public void parityConfigString(StringBuilder paramStringBuilder) {
/* 88 */     paramStringBuilder.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource$LegacyPositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */