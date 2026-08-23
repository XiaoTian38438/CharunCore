/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CauldronBlock extends AbstractCauldronBlock {
/* 14 */   public static final MapCodec<CauldronBlock> CODEC = simpleCodec(CauldronBlock::new); private static final float RAIN_FILL_CHANCE = 0.05F;
/*    */   private static final float POWDER_SNOW_FILL_CHANCE = 0.1F;
/*    */   
/*    */   public MapCodec<CauldronBlock> codec() {
/* 18 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CauldronBlock(BlockBehaviour.Properties paramProperties) {
/* 25 */     super(paramProperties, CauldronInteraction.EMPTY);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull(BlockState paramBlockState) {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   protected static boolean shouldHandlePrecipitation(Level paramLevel, Biome.Precipitation paramPrecipitation) {
/* 34 */     if (paramPrecipitation == Biome.Precipitation.RAIN)
/* 35 */       return (paramLevel.getRandom().nextFloat() < 0.05F); 
/* 36 */     if (paramPrecipitation == Biome.Precipitation.SNOW) {
/* 37 */       return (paramLevel.getRandom().nextFloat() < 0.1F);
/*    */     }
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePrecipitation(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Biome.Precipitation paramPrecipitation) {
/* 44 */     if (!shouldHandlePrecipitation(paramLevel, paramPrecipitation)) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     if (paramPrecipitation == Biome.Precipitation.RAIN) {
/* 49 */       paramLevel.setBlockAndUpdate(paramBlockPos, Blocks.WATER_CAULDRON.defaultBlockState());
/* 50 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/* 51 */     } else if (paramPrecipitation == Biome.Precipitation.SNOW) {
/* 52 */       paramLevel.setBlockAndUpdate(paramBlockPos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
/* 53 */       paramLevel.gameEvent(null, (Holder)GameEvent.BLOCK_CHANGE, paramBlockPos);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canReceiveStalactiteDrip(Fluid paramFluid) {
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void receiveStalactiteDrip(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Fluid paramFluid) {
/* 64 */     if (paramFluid == Fluids.WATER) {
/* 65 */       BlockState blockState = Blocks.WATER_CAULDRON.defaultBlockState();
/* 66 */       paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 67 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/* 68 */       paramLevel.levelEvent(1047, paramBlockPos, 0);
/* 69 */     } else if (paramFluid == Fluids.LAVA) {
/* 70 */       BlockState blockState = Blocks.LAVA_CAULDRON.defaultBlockState();
/* 71 */       paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 72 */       paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/* 73 */       paramLevel.levelEvent(1046, paramBlockPos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */