/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ class SaturationMobEffect
/*    */   extends InstantenousMobEffect {
/*    */   protected SaturationMobEffect(MobEffectCategory paramMobEffectCategory, int paramInt) {
/* 10 */     super(paramMobEffectCategory, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, int paramInt) {
/* 15 */     if (paramLivingEntity instanceof Player) { Player player = (Player)paramLivingEntity;
/* 16 */       player.getFoodData().eat(paramInt + 1, 1.0F); }
/*    */     
/* 18 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\effect\SaturationMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */