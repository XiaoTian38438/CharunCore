/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ public class InstantenousMobEffect extends MobEffect {
/*    */   public InstantenousMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/*  5 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInstantenous() {
/* 10 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int paramInt1, int paramInt2) {
/* 15 */     return (paramInt1 >= 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\InstantenousMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */