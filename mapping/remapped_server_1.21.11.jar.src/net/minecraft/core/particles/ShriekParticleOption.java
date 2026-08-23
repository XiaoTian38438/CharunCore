/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ShriekParticleOption implements ParticleOptions {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.INT.fieldOf("delay").forGetter(())).apply((Applicative)paramInstance, ShriekParticleOption::new));
/*    */ 
/*    */ 
/*    */     
/* 15 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, paramShriekParticleOption -> Integer.valueOf(paramShriekParticleOption.delay), ShriekParticleOption::new);
/*    */   }
/*    */   
/*    */   public static final MapCodec<ShriekParticleOption> CODEC;
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, ShriekParticleOption> STREAM_CODEC;
/*    */   private final int delay;
/*    */   
/*    */   public ShriekParticleOption(int paramInt) {
/* 23 */     this.delay = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<ShriekParticleOption> getType() {
/* 28 */     return ParticleTypes.SHRIEK;
/*    */   }
/*    */   
/*    */   public int getDelay() {
/* 32 */     return this.delay;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\ShriekParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */