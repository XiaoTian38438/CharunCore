/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.cauldron.CauldronInteraction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.InsideBlockEffectType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LayeredCauldronBlock extends AbstractCauldronBlock {
/*     */   static {
/*  28 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Biome.Precipitation.CODEC.fieldOf("precipitation").forGetter(()), (App)CauldronInteraction.CODEC.fieldOf("interactions").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, LayeredCauldronBlock::new));
/*     */   }
/*     */   
/*     */   public static final MapCodec<LayeredCauldronBlock> CODEC;
/*     */   public static final int MIN_FILL_LEVEL = 1;
/*     */   public static final int MAX_FILL_LEVEL = 3;
/*     */   
/*     */   public MapCodec<LayeredCauldronBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
/*     */   
/*     */   private static final int BASE_CONTENT_HEIGHT = 6;
/*     */   
/*     */   private static final double HEIGHT_PER_LEVEL = 3.0D;
/*  46 */   private static final VoxelShape[] FILLED_SHAPES = (VoxelShape[])Util.make(() -> Block.boxes(2, ()));
/*     */   
/*     */   private final Biome.Precipitation precipitationType;
/*     */   
/*     */   public LayeredCauldronBlock(Biome.Precipitation paramPrecipitation, CauldronInteraction.InteractionMap paramInteractionMap, BlockBehaviour.Properties paramProperties) {
/*  51 */     super(paramProperties, paramInteractionMap);
/*  52 */     this.precipitationType = paramPrecipitation;
/*  53 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull(BlockState paramBlockState) {
/*  58 */     return (((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() == 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReceiveStalactiteDrip(Fluid paramFluid) {
/*  63 */     return (paramFluid == Fluids.WATER && this.precipitationType == Biome.Precipitation.RAIN);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getContentHeight(BlockState paramBlockState) {
/*  68 */     return getPixelContentHeight(((Integer)paramBlockState.getValue((Property)LEVEL)).intValue()) / 16.0D;
/*     */   }
/*     */   
/*     */   private static double getPixelContentHeight(int paramInt) {
/*  72 */     return 6.0D + paramInt * 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getEntityInsideCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Entity paramEntity) {
/*  77 */     return FILLED_SHAPES[((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  82 */     if (paramLevel instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)paramLevel;
/*  83 */       BlockPos blockPos = paramBlockPos.immutable();
/*  84 */       paramInsideBlockEffectApplier.runBefore(InsideBlockEffectType.EXTINGUISH, paramEntity -> {
/*     */             if (paramEntity.isOnFire() && paramEntity.mayInteract(paramServerLevel, paramBlockPos)) {
/*     */               handleEntityOnFireInside(paramBlockState, paramLevel, paramBlockPos);
/*     */             }
/*     */           }); }
/*     */     
/*  90 */     paramInsideBlockEffectApplier.apply(InsideBlockEffectType.EXTINGUISH);
/*     */   }
/*     */   
/*     */   private void handleEntityOnFireInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  94 */     if (this.precipitationType == Biome.Precipitation.SNOW) {
/*  95 */       lowerFillLevel((BlockState)Blocks.WATER_CAULDRON.defaultBlockState().setValue((Property)LEVEL, paramBlockState.getValue((Property)LEVEL)), paramLevel, paramBlockPos);
/*     */     } else {
/*  97 */       lowerFillLevel(paramBlockState, paramLevel, paramBlockPos);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void lowerFillLevel(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/* 102 */     int i = ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() - 1;
/* 103 */     BlockState blockState = (i == 0) ? Blocks.CAULDRON.defaultBlockState() : (BlockState)paramBlockState.setValue((Property)LEVEL, Integer.valueOf(i));
/* 104 */     paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 105 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlePrecipitation(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Biome.Precipitation paramPrecipitation) {
/* 110 */     if (!CauldronBlock.shouldHandlePrecipitation(paramLevel, paramPrecipitation) || ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() == 3 || paramPrecipitation != this.precipitationType) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     BlockState blockState = (BlockState)paramBlockState.cycle((Property)LEVEL);
/* 115 */     paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 116 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Direction paramDirection) {
/* 121 */     return ((Integer)paramBlockState.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 126 */     paramBuilder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void receiveStalactiteDrip(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Fluid paramFluid) {
/* 131 */     if (isFull(paramBlockState)) {
/*     */       return;
/*     */     }
/* 134 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)LEVEL, Integer.valueOf(((Integer)paramBlockState.getValue((Property)LEVEL)).intValue() + 1));
/* 135 */     paramLevel.setBlockAndUpdate(paramBlockPos, blockState);
/* 136 */     paramLevel.gameEvent((Holder)GameEvent.BLOCK_CHANGE, paramBlockPos, GameEvent.Context.of(blockState));
/* 137 */     paramLevel.levelEvent(1047, paramBlockPos, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\LayeredCauldronBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */