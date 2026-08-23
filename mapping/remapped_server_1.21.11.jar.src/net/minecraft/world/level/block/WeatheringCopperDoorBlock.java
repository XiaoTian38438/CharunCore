/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*    */ 
/*    */ public class WeatheringCopperDoorBlock extends DoorBlock implements WeatheringCopper {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(DoorBlock::type), (App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopperDoorBlock::getAge), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperDoorBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeatheringCopperDoorBlock> CODEC;
/*    */   private final WeatheringCopper.WeatherState weatherState;
/*    */   
/*    */   public MapCodec<WeatheringCopperDoorBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected WeatheringCopperDoorBlock(BlockSetType paramBlockSetType, WeatheringCopper.WeatherState paramWeatherState, BlockBehaviour.Properties paramProperties) {
/* 27 */     super(paramBlockSetType, paramProperties);
/* 28 */     this.weatherState = paramWeatherState;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 33 */     if (paramBlockState.getValue((Property)DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
/* 34 */       changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 40 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return this.weatherState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperDoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */