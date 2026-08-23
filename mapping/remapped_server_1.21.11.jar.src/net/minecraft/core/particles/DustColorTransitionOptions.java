/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustColorTransitionOptions extends ScalableParticleOptionsBase {
/* 14 */   public static final DustColorTransitionOptions SCULK_TO_REDSTONE = new DustColorTransitionOptions(3790560, 16711680, 1.0F); public static final int SCULK_PARTICLE_COLOR = 3790560; public static final MapCodec<DustColorTransitionOptions> CODEC; public static final StreamCodec<RegistryFriendlyByteBuf, DustColorTransitionOptions> STREAM_CODEC;
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("from_color").forGetter(()), (App)ExtraCodecs.RGB_COLOR_CODEC.fieldOf("to_color").forGetter(()), (App)SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply((Applicative)paramInstance, DustColorTransitionOptions::new));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 22 */     STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, paramDustColorTransitionOptions -> Integer.valueOf(paramDustColorTransitionOptions.fromColor), ByteBufCodecs.INT, paramDustColorTransitionOptions -> Integer.valueOf(paramDustColorTransitionOptions.toColor), ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustColorTransitionOptions::new);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final int fromColor;
/*    */   
/*    */   private final int toColor;
/*    */ 
/*    */   
/*    */   public DustColorTransitionOptions(int paramInt1, int paramInt2, float paramFloat) {
/* 33 */     super(paramFloat);
/* 34 */     this.fromColor = paramInt1;
/* 35 */     this.toColor = paramInt2;
/*    */   }
/*    */   
/*    */   public Vector3f getFromColor() {
/* 39 */     return ARGB.vector3fFromRGB24(this.fromColor);
/*    */   }
/*    */   
/*    */   public Vector3f getToColor() {
/* 43 */     return ARGB.vector3fFromRGB24(this.toColor);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleType<DustColorTransitionOptions> getType() {
/* 48 */     return ParticleTypes.DUST_COLOR_TRANSITION;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\particles\DustColorTransitionOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */