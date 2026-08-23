/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperBarsBlock extends IronBarsBlock implements WeatheringCopper {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperBarsBlock::getAge), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperBarsBlock::new));
/*    */   }
/*    */   public static final MapCodec<WeatheringCopperBarsBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public MapCodec<WeatheringCopperBarsBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperBarsBlock(WeatheringCopper.WeatherState paramWeatherState, BlockBehaviour.Properties paramProperties) {
/* 24 */     super(paramProperties);
/* 25 */     this.weatherState = paramWeatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 30 */     changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 35 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 40 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperBarsBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */