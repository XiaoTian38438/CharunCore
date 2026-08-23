/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public class PowerParticleOption implements ParticleOptions {
/*    */   private final ParticleType<PowerParticleOption> type;
/*    */   
/*    */   public static MapCodec<PowerParticleOption> codec(ParticleType<PowerParticleOption> paramParticleType) {
/* 11 */     return Codec.FLOAT.xmap(paramFloat -> new PowerParticleOption(paramParticleType, paramFloat.floatValue()), paramPowerParticleOption -> Float.valueOf(paramPowerParticleOption.power)).optionalFieldOf("power", create(paramParticleType, 1.0F));
/*    */   }
/*    */   private final float power;
/*    */   public static StreamCodec<? super ByteBuf, PowerParticleOption> streamCodec(ParticleType<PowerParticleOption> paramParticleType) {
/* 15 */     return ByteBufCodecs.FLOAT.map(paramFloat -> new PowerParticleOption(paramParticleType, paramFloat.floatValue()), paramPowerParticleOption -> Float.valueOf(paramPowerParticleOption.power));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private PowerParticleOption(ParticleType<PowerParticleOption> paramParticleType, float paramFloat) {
/* 22 */     this.type = paramParticleType;
/* 23 */     this.power = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<PowerParticleOption> getType() {
/* 28 */     return this.type;
/*    */   }
/*    */   
/*    */   public float getPower() {
/* 32 */     return this.power;
/*    */   }
/*    */   
/*    */   public static PowerParticleOption create(ParticleType<PowerParticleOption> paramParticleType, float paramFloat) {
/* 36 */     return new PowerParticleOption(paramParticleType, paramFloat);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\PowerParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */