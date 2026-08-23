/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ public class NoiseThresholdProvider extends NoiseBasedStateProvider {
/*    */   public static final MapCodec<NoiseThresholdProvider> CODEC;
/*    */   private final float threshold;
/*    */   private final float highChance;
/*    */   private final BlockState defaultState;
/*    */   private final List<BlockState> lowStates;
/*    */   private final List<BlockState> highStates;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> noiseCodec(paramInstance).and(paramInstance.group((App)Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("high_chance").forGetter(()), (App)BlockState.CODEC.fieldOf("default_state").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("low_states").forGetter(()), (App)ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("high_states").forGetter(()))).apply((Applicative)paramInstance, NoiseThresholdProvider::new));
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoiseThresholdProvider(long paramLong, NormalNoise.NoiseParameters paramNoiseParameters, float paramFloat1, float paramFloat2, float paramFloat3, BlockState paramBlockState, List<BlockState> paramList1, List<BlockState> paramList2) {
/* 43 */     super(paramLong, paramNoiseParameters, paramFloat1);
/* 44 */     this.threshold = paramFloat2;
/* 45 */     this.highChance = paramFloat3;
/* 46 */     this.defaultState = paramBlockState;
/* 47 */     this.lowStates = paramList1;
/* 48 */     this.highStates = paramList2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockStateProviderType<?> type() {
/* 53 */     return BlockStateProviderType.NOISE_THRESHOLD_PROVIDER;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource paramRandomSource, BlockPos paramBlockPos) {
/* 59 */     double d = getNoiseValue(paramBlockPos, this.scale);
/* 60 */     if (d < this.threshold) {
/* 61 */       return (BlockState)Util.getRandom(this.lowStates, paramRandomSource);
/*    */     }
/*    */     
/* 64 */     if (paramRandomSource.nextFloat() < this.highChance) {
/* 65 */       return (BlockState)Util.getRandom(this.highStates, paramRandomSource);
/*    */     }
/*    */     
/* 68 */     return this.defaultState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseThresholdProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */