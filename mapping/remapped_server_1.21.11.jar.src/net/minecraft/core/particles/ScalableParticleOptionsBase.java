/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ScalableParticleOptionsBase implements ParticleOptions {
/*    */   public static final float MIN_SCALE = 0.01F;
/*    */   public static final float MAX_SCALE = 4.0F;
/*    */   
/*    */   static {
/* 12 */     SCALE = Codec.FLOAT.validate(paramFloat -> (paramFloat.floatValue() >= 0.01F && paramFloat.floatValue() <= 4.0F) ? DataResult.success(paramFloat) : DataResult.error(()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected static final Codec<Float> SCALE;
/*    */   private final float scale;
/*    */   
/*    */   public ScalableParticleOptionsBase(float paramFloat) {
/* 20 */     this.scale = Mth.clamp(paramFloat, 0.01F, 4.0F);
/*    */   }
/*    */   
/*    */   public float getScale() {
/* 24 */     return this.scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\ScalableParticleOptionsBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */