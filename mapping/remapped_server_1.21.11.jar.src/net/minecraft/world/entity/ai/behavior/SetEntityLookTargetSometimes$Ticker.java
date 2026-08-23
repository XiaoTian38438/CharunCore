/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
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
/*    */ public final class Ticker
/*    */ {
/*    */   private final UniformInt interval;
/*    */   private int ticksUntilNextStart;
/*    */   
/*    */   public Ticker(UniformInt paramUniformInt) {
/* 56 */     if (paramUniformInt.getMinValue() <= 1) {
/* 57 */       throw new IllegalArgumentException();
/*    */     }
/* 59 */     this.interval = paramUniformInt;
/*    */   }
/*    */   
/*    */   public boolean tickDownAndCheck(RandomSource paramRandomSource) {
/* 63 */     if (this.ticksUntilNextStart == 0) {
/* 64 */       this.ticksUntilNextStart = this.interval.sample(paramRandomSource) - 1;
/* 65 */       return false;
/*    */     } 
/*    */     
/* 68 */     return (--this.ticksUntilNextStart == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes$Ticker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */