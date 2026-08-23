/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MushroomBlock extends VegetationBlock implements BonemealableBlock {
/*     */   static {
/*  23 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(()), (App)propertiesCodec()).apply((Applicative)paramInstance, MushroomBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<MushroomBlock> CODEC;
/*     */   
/*     */   public MapCodec<MushroomBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   private static final VoxelShape SHAPE = Block.column(6.0D, 0.0D, 6.0D);
/*     */   
/*     */   private final ResourceKey<ConfiguredFeature<?, ?>> feature;
/*     */   
/*     */   public MushroomBlock(ResourceKey<ConfiguredFeature<?, ?>> paramResourceKey, BlockBehaviour.Properties paramProperties) {
/*  38 */     super(paramProperties);
/*  39 */     this.feature = paramResourceKey;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  44 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  49 */     if (paramRandomSource.nextInt(25) == 0) {
/*  50 */       byte b1 = 5;
/*  51 */       byte b2 = 4;
/*  52 */       for (BlockPos blockPos1 : BlockPos.betweenClosed(paramBlockPos.offset(-4, -1, -4), paramBlockPos.offset(4, 1, 4))) {
/*  53 */         if (paramServerLevel.getBlockState(blockPos1).is(this) && --b1 <= 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*  59 */       BlockPos blockPos = paramBlockPos.offset(paramRandomSource.nextInt(3) - 1, paramRandomSource.nextInt(2) - paramRandomSource.nextInt(2), paramRandomSource.nextInt(3) - 1);
/*  60 */       for (byte b3 = 0; b3 < 4; b3++) {
/*  61 */         if (paramServerLevel.isEmptyBlock(blockPos) && paramBlockState.canSurvive((LevelReader)paramServerLevel, blockPos)) {
/*  62 */           paramBlockPos = blockPos;
/*     */         }
/*  64 */         blockPos = paramBlockPos.offset(paramRandomSource.nextInt(3) - 1, paramRandomSource.nextInt(2) - paramRandomSource.nextInt(2), paramRandomSource.nextInt(3) - 1);
/*     */       } 
/*     */       
/*  67 */       if (paramServerLevel.isEmptyBlock(blockPos) && paramBlockState.canSurvive((LevelReader)paramServerLevel, blockPos)) {
/*  68 */         paramServerLevel.setBlock(blockPos, paramBlockState, 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  75 */     return paramBlockState.isSolidRender();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  80 */     BlockPos blockPos = paramBlockPos.below();
/*  81 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*  82 */     if (blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
/*  83 */       return true;
/*     */     }
/*     */     
/*  86 */     return (paramLevelReader.getRawBrightness(paramBlockPos, 0) < 13 && mayPlaceOn(blockState, (BlockGetter)paramLevelReader, blockPos));
/*     */   }
/*     */   
/*     */   public boolean growMushroom(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, RandomSource paramRandomSource) {
/*  90 */     Optional<Holder> optional = paramServerLevel.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.feature);
/*  91 */     if (optional.isEmpty()) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     paramServerLevel.removeBlock(paramBlockPos, false);
/*     */     
/*  97 */     if (((ConfiguredFeature)((Holder)optional.get()).value()).place((WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramRandomSource, paramBlockPos)) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     paramServerLevel.setBlock(paramBlockPos, paramBlockState, 3);
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 112 */     return (paramRandomSource.nextFloat() < 0.4D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 117 */     growMushroom(paramServerLevel, paramBlockPos, paramBlockState, paramRandomSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MushroomBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */