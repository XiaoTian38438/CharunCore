/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperChestBlock extends CopperChestBlock implements WeatheringCopper {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperChestBlock::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeatheringCopperChestBlock> CODEC;
/*    */ 
/*    */   
/*    */   public MapCodec<WeatheringCopperChestBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperChestBlock(WeatheringCopper.WeatherState paramWeatherState, SoundEvent paramSoundEvent1, SoundEvent paramSoundEvent2, BlockBehaviour.Properties paramProperties) {
/* 28 */     super(paramWeatherState, paramSoundEvent1, paramSoundEvent2, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 33 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 38 */     if (!((ChestType)paramBlockState.getValue((Property)ChestBlock.TYPE)).equals(ChestType.RIGHT)) { BlockEntity blockEntity = paramServerLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof ChestBlockEntity) { ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity; if (chestBlockEntity.getEntitiesWithContainerOpen().isEmpty())
/* 39 */           changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);  }
/*    */        }
/*    */   
/*    */   }
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 45 */     return getState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWaxed() {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */