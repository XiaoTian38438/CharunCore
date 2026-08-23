/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WeatheringCopperGolemStatueBlock extends CopperGolemStatueBlock implements WeatheringCopper {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(ChangeOverTimeBlock::getAge), (App)propertiesCodec()).apply((Applicative)paramInstance, WeatheringCopperGolemStatueBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<WeatheringCopperGolemStatueBlock> CODEC;
/*    */   
/*    */   public MapCodec<WeatheringCopperGolemStatueBlock> codec() {
/* 28 */     return CODEC;
/*    */   }
/*    */   
/*    */   public WeatheringCopperGolemStatueBlock(WeatheringCopper.WeatherState paramWeatherState, BlockBehaviour.Properties paramProperties) {
/* 32 */     super(paramWeatherState, paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 37 */     return WeatheringCopper.getNext(paramBlockState.getBlock()).isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 42 */     changeOverTime(paramBlockState, paramServerLevel, paramBlockPos, paramRandomSource);
/*    */   }
/*    */ 
/*    */   
/*    */   public WeatheringCopper.WeatherState getAge() {
/* 47 */     return getWeatheringState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected InteractionResult useItemOn(ItemStack paramItemStack, BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, InteractionHand paramInteractionHand, BlockHitResult paramBlockHitResult) {
/* 52 */     BlockEntity blockEntity = paramLevel.getBlockEntity(paramBlockPos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 53 */       if (paramItemStack.is(ItemTags.AXES))
/* 54 */       { if (getAge().equals(WeatheringCopper.WeatherState.UNAFFECTED)) {
/* 55 */           CopperGolem copperGolem = copperGolemStatueBlockEntity.removeStatue(paramBlockState);
/* 56 */           paramItemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/* 57 */           if (copperGolem != null) {
/* 58 */             paramLevel.addFreshEntity((Entity)copperGolem);
/* 59 */             paramLevel.removeBlock(paramBlockPos, false);
/* 60 */             return (InteractionResult)InteractionResult.SUCCESS;
/*    */           } 
/*    */         }  }
/* 63 */       else { if (paramItemStack.is(Items.HONEYCOMB)) {
/* 64 */           return (InteractionResult)InteractionResult.PASS;
/*    */         }
/* 66 */         updatePose(paramLevel, paramBlockState, paramBlockPos, paramPlayer);
/* 67 */         return (InteractionResult)InteractionResult.SUCCESS; }
/*    */        }
/*    */     
/* 70 */     return (InteractionResult)InteractionResult.PASS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WeatheringCopperGolemStatueBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */