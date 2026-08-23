/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XoroshiroPositionalRandomFactory
/*     */   implements PositionalRandomFactory
/*     */ {
/*     */   private final long seedLo;
/*     */   private final long seedHi;
/*     */   
/*     */   public XoroshiroPositionalRandomFactory(long paramLong1, long paramLong2) {
/* 137 */     this.seedLo = paramLong1;
/* 138 */     this.seedHi = paramLong2;
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource at(int paramInt1, int paramInt2, int paramInt3) {
/* 143 */     long l1 = Mth.getSeed(paramInt1, paramInt2, paramInt3);
/* 144 */     long l2 = l1 ^ this.seedLo;
/* 145 */     return new XoroshiroRandomSource(l2, this.seedHi);
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource fromHashOf(String paramString) {
/* 150 */     RandomSupport.Seed128bit seed128bit = RandomSupport.seedFromHashOf(paramString);
/* 151 */     return new XoroshiroRandomSource(seed128bit.xor(this.seedLo, this.seedHi));
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource fromSeed(long paramLong) {
/* 156 */     return new XoroshiroRandomSource(paramLong ^ this.seedLo, paramLong ^ this.seedHi);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public void parityConfigString(StringBuilder paramStringBuilder) {
/* 162 */     paramStringBuilder.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource$XoroshiroPositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */