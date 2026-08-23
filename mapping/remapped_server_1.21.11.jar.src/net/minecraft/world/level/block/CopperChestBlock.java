/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class CopperChestBlock extends ChestBlock {
/*     */   static {
/*  25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperChestBlock::getState), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), (App)BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), (App)propertiesCodec()).apply((Applicative)paramInstance, CopperChestBlock::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MapCodec<CopperChestBlock> CODEC;
/*     */   
/*  32 */   private static final Map<Block, Supplier<Block>> COPPER_TO_COPPER_CHEST_MAPPING = Map.of(Blocks.COPPER_BLOCK, () -> Blocks.COPPER_CHEST, Blocks.EXPOSED_COPPER, () -> Blocks.EXPOSED_COPPER_CHEST, Blocks.WEATHERED_COPPER, () -> Blocks.WEATHERED_COPPER_CHEST, Blocks.OXIDIZED_COPPER, () -> Blocks.OXIDIZED_COPPER_CHEST, Blocks.WAXED_COPPER_BLOCK, () -> Blocks.COPPER_CHEST, Blocks.WAXED_EXPOSED_COPPER, () -> Blocks.EXPOSED_COPPER_CHEST, Blocks.WAXED_WEATHERED_COPPER, () -> Blocks.WEATHERED_COPPER_CHEST, Blocks.WAXED_OXIDIZED_COPPER, () -> Blocks.OXIDIZED_COPPER_CHEST);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WeatheringCopper.WeatherState weatherState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<? extends CopperChestBlock> codec() {
/*  44 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CopperChestBlock(WeatheringCopper.WeatherState paramWeatherState, SoundEvent paramSoundEvent1, SoundEvent paramSoundEvent2, BlockBehaviour.Properties paramProperties) {
/*  50 */     super(() -> BlockEntityType.CHEST, paramSoundEvent1, paramSoundEvent2, paramProperties);
/*  51 */     this.weatherState = paramWeatherState;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chestCanConnectTo(BlockState paramBlockState) {
/*  56 */     return (paramBlockState.is(BlockTags.COPPER_CHESTS) && paramBlockState.hasProperty((Property)ChestBlock.TYPE));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  61 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*  62 */     return getLeastOxidizedChestOfConnectedBlocks(blockState, paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */   
/*     */   private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos) {
/*  66 */     BlockState blockState = paramLevel.getBlockState(paramBlockPos.relative(getConnectedDirection(paramBlockState)));
/*  67 */     if (!((ChestType)paramBlockState.getValue((Property)ChestBlock.TYPE)).equals(ChestType.SINGLE)) { Block block = paramBlockState.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock copperChestBlock = (CopperChestBlock)block; block = blockState.getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock copperChestBlock1 = (CopperChestBlock)block;
/*  68 */           BlockState blockState1 = paramBlockState;
/*  69 */           BlockState blockState2 = blockState;
/*     */           
/*  71 */           if (copperChestBlock.isWaxed() != copperChestBlock1.isWaxed()) {
/*  72 */             blockState1 = unwaxBlock(copperChestBlock, paramBlockState).orElse(blockState1);
/*  73 */             blockState2 = unwaxBlock(copperChestBlock1, blockState).orElse(blockState2);
/*     */           } 
/*     */           
/*  76 */           Block block1 = (copperChestBlock.weatherState.ordinal() <= copperChestBlock1.weatherState.ordinal()) ? blockState1.getBlock() : blockState2.getBlock();
/*  77 */           return block1.withPropertiesOf(blockState1); }  }
/*     */        }
/*  79 */      return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  84 */     BlockState blockState = super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*  85 */     if (chestCanConnectTo(paramBlockState2)) {
/*  86 */       ChestType chestType = (ChestType)blockState.getValue((Property)ChestBlock.TYPE);
/*  87 */       if (!chestType.equals(ChestType.SINGLE) && getConnectedDirection(blockState) == paramDirection) {
/*  88 */         return paramBlockState2.getBlock().withPropertiesOf(blockState);
/*     */       }
/*     */     } 
/*  91 */     return blockState;
/*     */   }
/*     */   
/*     */   private static Optional<BlockState> unwaxBlock(CopperChestBlock paramCopperChestBlock, BlockState paramBlockState) {
/*  95 */     if (!paramCopperChestBlock.isWaxed()) {
/*  96 */       return Optional.of(paramBlockState);
/*     */     }
/*  98 */     return Optional.<Block>ofNullable((Block)((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(paramBlockState.getBlock())).map(paramBlock -> paramBlock.withPropertiesOf(paramBlockState));
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getState() {
/* 102 */     return this.weatherState;
/*     */   }
/*     */   
/*     */   public static BlockState getFromCopperBlock(Block paramBlock, Direction paramDirection, Level paramLevel, BlockPos paramBlockPos) {
/* 106 */     Objects.requireNonNull(Blocks.COPPER_CHEST); CopperChestBlock copperChestBlock = ((Supplier<CopperChestBlock>)COPPER_TO_COPPER_CHEST_MAPPING.getOrDefault(paramBlock, Blocks.COPPER_CHEST::asBlock)).get();
/* 107 */     ChestType chestType = copperChestBlock.getChestType(paramLevel, paramBlockPos, paramDirection);
/* 108 */     BlockState blockState = (BlockState)((BlockState)copperChestBlock.defaultBlockState().setValue((Property)FACING, (Comparable)paramDirection)).setValue((Property)TYPE, (Comparable)chestType);
/* 109 */     return getLeastOxidizedChestOfConnectedBlocks(blockState, paramLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   public boolean isWaxed() {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldChangedStateKeepBlockEntity(BlockState paramBlockState) {
/* 118 */     return paramBlockState.is(BlockTags.COPPER_CHESTS);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\CopperChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */