/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public abstract class NoiseBasedStateProvider extends BlockStateProvider {
/*    */   protected static <P extends NoiseBasedStateProvider> Products.P3<RecordCodecBuilder.Mu<P>, Long, NormalNoise.NoiseParameters, Float> noiseCodec(RecordCodecBuilder.Instance<P> paramInstance) {
/* 14 */     return paramInstance.group((App)Codec.LONG
/* 15 */         .fieldOf("seed").forGetter(paramNoiseBasedStateProvider -> Long.valueOf(paramNoiseBasedStateProvider.seed)), (App)NormalNoise.NoiseParameters.DIRECT_CODEC
/* 16 */         .fieldOf("noise").forGetter(paramNoiseBasedStateProvider -> paramNoiseBasedStateProvider.parameters), (App)ExtraCodecs.POSITIVE_FLOAT
/* 17 */         .fieldOf("scale").forGetter(paramNoiseBasedStateProvider -> Float.valueOf(paramNoiseBasedStateProvider.scale)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected final long seed;
/*    */   protected final NormalNoise.NoiseParameters parameters;
/*    */   protected final float scale;
/*    */   protected final NormalNoise noise;
/*    */   
/*    */   protected NoiseBasedStateProvider(long paramLong, NormalNoise.NoiseParameters paramNoiseParameters, float paramFloat) {
/* 27 */     this.seed = paramLong;
/* 28 */     this.parameters = paramNoiseParameters;
/* 29 */     this.scale = paramFloat;
/* 30 */     this.noise = NormalNoise.create((RandomSource)new WorldgenRandom((RandomSource)new LegacyRandomSource(paramLong)), paramNoiseParameters);
/*    */   }
/*    */   
/*    */   protected double getNoiseValue(BlockPos paramBlockPos, double paramDouble) {
/* 34 */     return this.noise.getValue(paramBlockPos.getX() * paramDouble, paramBlockPos.getY() * paramDouble, paramBlockPos.getZ() * paramDouble);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseBasedStateProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */