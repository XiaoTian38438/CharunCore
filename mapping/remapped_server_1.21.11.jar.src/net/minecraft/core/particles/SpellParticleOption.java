/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpellParticleOption implements ParticleOptions {
/*    */   public static MapCodec<SpellParticleOption> codec(ParticleType<SpellParticleOption> paramParticleType) {
/* 15 */     return RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Integer.valueOf(-1)).forGetter(()), (App)Codec.FLOAT.optionalFieldOf("power", Float.valueOf(1.0F)).forGetter(())).apply((Applicative)paramInstance, ()));
/*    */   }
/*    */   private final ParticleType<SpellParticleOption> type;
/*    */   private final int color;
/*    */   private final float power;
/*    */   
/*    */   public static StreamCodec<? super ByteBuf, SpellParticleOption> streamCodec(ParticleType<SpellParticleOption> paramParticleType) {
/* 22 */     return StreamCodec.composite(ByteBufCodecs.INT, paramSpellParticleOption -> Integer.valueOf(paramSpellParticleOption.color), ByteBufCodecs.FLOAT, paramSpellParticleOption -> Float.valueOf(paramSpellParticleOption.power), (paramInteger, paramFloat) -> new SpellParticleOption(paramParticleType, paramInteger.intValue(), paramFloat.floatValue()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SpellParticleOption(ParticleType<SpellParticleOption> paramParticleType, int paramInt, float paramFloat) {
/* 34 */     this.type = paramParticleType;
/* 35 */     this.color = paramInt;
/* 36 */     this.power = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<SpellParticleOption> getType() {
/* 41 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getRed() {
/* 45 */     return ARGB.red(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getGreen() {
/* 49 */     return ARGB.green(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getBlue() {
/* 53 */     return ARGB.blue(this.color) / 255.0F;
/*    */   }
/*    */   
/*    */   public float getPower() {
/* 57 */     return this.power;
/*    */   }
/*    */   
/*    */   public static SpellParticleOption create(ParticleType<SpellParticleOption> paramParticleType, int paramInt, float paramFloat) {
/* 61 */     return new SpellParticleOption(paramParticleType, paramInt, paramFloat);
/*    */   }
/*    */   
/*    */   public static SpellParticleOption create(ParticleType<SpellParticleOption> paramParticleType, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
/* 65 */     return create(paramParticleType, ARGB.colorFromFloat(1.0F, paramFloat1, paramFloat2, paramFloat3), paramFloat4);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\SpellParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */