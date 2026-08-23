/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class StemBlock extends VegetationBlock implements BonemealableBlock {
/*     */   static {
/*  30 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.BLOCK).fieldOf("fruit").forGetter(()), (App)ResourceKey.codec(Registries.BLOCK).fieldOf("attached_stem").forGetter(()), (App)ResourceKey.codec(Registries.ITEM).fieldOf("seed").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, StemBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<StemBlock> CODEC;
/*     */   
/*     */   public static final int MAX_AGE = 7;
/*     */   
/*     */   public MapCodec<StemBlock> codec() {
/*  39 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  43 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_7; private static final VoxelShape[] SHAPES; private final ResourceKey<Block> fruit; private final ResourceKey<Block> attachedStem; private final ResourceKey<Item> seed;
/*     */   static {
/*  45 */     SHAPES = Block.boxes(7, paramInt -> Block.column(2.0D, 0.0D, (2 + paramInt * 2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StemBlock(ResourceKey<Block> paramResourceKey1, ResourceKey<Block> paramResourceKey2, ResourceKey<Item> paramResourceKey, BlockBehaviour.Properties paramProperties) {
/*  53 */     super(paramProperties);
/*  54 */     this.fruit = paramResourceKey1;
/*  55 */     this.attachedStem = paramResourceKey2;
/*  56 */     this.seed = paramResourceKey;
/*  57 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  62 */     return SHAPES[((Integer)paramBlockState.getValue((Property)AGE)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  67 */     return paramBlockState.is(Blocks.FARMLAND);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  72 */     if (paramServerLevel.getRawBrightness(paramBlockPos, 0) < 9) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     float f = CropBlock.getGrowthSpeed(this, (BlockGetter)paramServerLevel, paramBlockPos);
/*  77 */     if (paramRandomSource.nextInt((int)(25.0F / f) + 1) == 0) {
/*  78 */       int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/*  79 */       if (i < 7) {
/*  80 */         paramBlockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i + 1));
/*  81 */         paramServerLevel.setBlock(paramBlockPos, paramBlockState, 2);
/*     */       } else {
/*  83 */         Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/*  84 */         BlockPos blockPos = paramBlockPos.relative(direction);
/*     */         
/*  86 */         BlockState blockState = paramServerLevel.getBlockState(blockPos.below());
/*  87 */         if (paramServerLevel.getBlockState(blockPos).isAir() && (blockState.is(Blocks.FARMLAND) || blockState.is(BlockTags.DIRT))) {
/*  88 */           Registry registry = paramServerLevel.registryAccess().lookupOrThrow(Registries.BLOCK);
/*  89 */           Optional<Block> optional1 = registry.getOptional(this.fruit);
/*  90 */           Optional<Block> optional2 = registry.getOptional(this.attachedStem);
/*  91 */           if (optional1.isPresent() && optional2.isPresent()) {
/*  92 */             paramServerLevel.setBlockAndUpdate(blockPos, ((Block)optional1.get()).defaultBlockState());
/*  93 */             paramServerLevel.setBlockAndUpdate(paramBlockPos, (BlockState)((Block)optional2.get()).defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 102 */     return new ItemStack((ItemLike)DataFixUtils.orElse(paramLevelReader.registryAccess().lookupOrThrow(Registries.ITEM).getOptional(this.seed), this));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 107 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() != 7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 117 */     int i = Math.min(7, ((Integer)paramBlockState.getValue((Property)AGE)).intValue() + Mth.nextInt(paramServerLevel.random, 2, 5));
/* 118 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i));
/* 119 */     paramServerLevel.setBlock(paramBlockPos, blockState, 2);
/* 120 */     if (i == 7) {
/* 121 */       blockState.randomTick(paramServerLevel, paramBlockPos, paramServerLevel.random);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 127 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\StemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */