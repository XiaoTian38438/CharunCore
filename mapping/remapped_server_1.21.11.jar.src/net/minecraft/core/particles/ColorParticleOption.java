/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ColorParticleOption implements ParticleOptions {
/*    */   private final ParticleType<ColorParticleOption> type;
/*    */   
/*    */   public static MapCodec<ColorParticleOption> codec(ParticleType<ColorParticleOption> paramParticleType) {
/* 13 */     return ExtraCodecs.ARGB_COLOR_CODEC.xmap(paramInteger -> new ColorParticleOption(paramParticleType, paramInteger.intValue()), paramColorParticleOption -> Integer.valueOf(paramColorParticleOption.color)).fieldOf("color");
/*    */   }
/*    */   private final int color;
/*    */   public static StreamCodec<? super ByteBuf, ColorParticleOption> streamCodec(ParticleType<ColorParticleOption> paramParticleType) {
/* 17 */     return ByteBufCodecs.INT.map(paramInteger -> new ColorParticleOption(paramParticleType, paramInteger.intValue()), paramColorParticleOption -> Integer.valueOf(paramColorParticleOption.color));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ColorParticleOption(ParticleType<ColorParticleOption> paramParticleType, int paramInt) {
/* 24 */     this.type = paramParticleType;
/* 25 */     this.color = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ColorParticleOption> getType() {
/* 30 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getRed() {
/* 34 */     return ARGB.red(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getGreen() {
/* 38 */     return ARGB.green(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getBlue() {
/* 42 */     return ARGB.blue(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getAlpha() {
/* 46 */     return ARGB.alpha(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public static ColorParticleOption create(ParticleType<ColorParticleOption> paramParticleType, int paramInt) {
/* 50 */     return new ColorParticleOption(paramParticleType, paramInt);
/*    */   }
/*    */   
/*    */   public static ColorParticleOption create(ParticleType<ColorParticleOption> paramParticleType, float paramFloat1, float paramFloat2, float paramFloat3) {
/* 54 */     return create(paramParticleType, ARGB.colorFromFloat(1.0F, paramFloat1, paramFloat2, paramFloat3));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\ColorParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */