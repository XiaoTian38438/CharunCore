/*    */ package net.minecraft.world.damagesource;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class CombatRules {
/*    */   public static final float MAX_ARMOR = 20.0F;
/*    */   public static final float ARMOR_PROTECTION_DIVIDER = 25.0F;
/*    */   public static final float BASE_ARMOR_TOUGHNESS = 2.0F;
/*    */   public static final float MIN_ARMOR_RATIO = 0.2F;
/*    */   private static final int NUM_ARMOR_ITEMS = 4;
/*    */   
/*    */   public static float getDamageAfterAbsorb(LivingEntity paramLivingEntity, float paramFloat1, DamageSource paramDamageSource, float paramFloat2, float paramFloat3) {
/* 17 */     float f1 = 2.0F + paramFloat3 / 4.0F;
/* 18 */     float f2 = Mth.clamp(paramFloat2 - paramFloat1 / f1, paramFloat2 * 0.2F, 20.0F);
/* 19 */     float f3 = f2 / 25.0F;
/*    */     
/* 21 */     ItemStack itemStack = paramDamageSource.getWeaponItem();
/* 22 */     if (itemStack != null) { Level level = paramLivingEntity.level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 23 */         float f6 = Mth.clamp(EnchantmentHelper.modifyArmorEffectiveness(serverLevel, itemStack, (Entity)paramLivingEntity, paramDamageSource, f3), 0.0F, 1.0F);
/*    */ 
/*    */ 
/*    */         
/* 27 */         float f7 = 1.0F - f6;
/* 28 */         return paramFloat1 * f7; }  }  float f4 = f3; float f5 = 1.0F - f4; return paramFloat1 * f5;
/*    */   }
/*    */   
/*    */   public static float getDamageAfterMagicAbsorb(float paramFloat1, float paramFloat2) {
/* 32 */     float f = Mth.clamp(paramFloat2, 0.0F, 20.0F);
/* 33 */     return paramFloat1 * (1.0F - f / 25.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\damagesource\CombatRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */