/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class NoiseThresholdCountPlacement
/*    */   extends RepeatingPlacement {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.DOUBLE.fieldOf("noise_level").forGetter(()), (App)Codec.INT.fieldOf("below_noise").forGetter(()), (App)Codec.INT.fieldOf("above_noise").forGetter(())).apply((Applicative)paramInstance, NoiseThresholdCountPlacement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<NoiseThresholdCountPlacement> CODEC;
/*    */   
/*    */   private final double noiseLevel;
/*    */   
/*    */   private final int belowNoise;
/*    */   private final int aboveNoise;
/*    */   
/*    */   private NoiseThresholdCountPlacement(double paramDouble, int paramInt1, int paramInt2) {
/* 28 */     this.noiseLevel = paramDouble;
/* 29 */     this.belowNoise = paramInt1;
/* 30 */     this.aboveNoise = paramInt2;
/*    */   }
/*    */   
/*    */   public static NoiseThresholdCountPlacement of(double paramDouble, int paramInt1, int paramInt2) {
/* 34 */     return new NoiseThresholdCountPlacement(paramDouble, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int count(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 40 */     double d = Biome.BIOME_INFO_NOISE.getValue(paramBlockPos.getX() / 200.0D, paramBlockPos.getZ() / 200.0D, false);
/* 41 */     return (d < this.noiseLevel) ? this.belowNoise : this.aboveNoise;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 46 */     return PlacementModifierType.NOISE_THRESHOLD_COUNT;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\placement\NoiseThresholdCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */