/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.placement.VegetationPlacements;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class GrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
/* 21 */   public static final MapCodec<GrassBlock> CODEC = simpleCodec(GrassBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<GrassBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   public GrassBlock(BlockBehaviour.Properties paramProperties) {
/* 29 */     super(paramProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 34 */     return paramLevelReader.getBlockState(paramBlockPos.above()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 44 */     BlockPos blockPos = paramBlockPos.above();
/*    */     
/* 46 */     BlockState blockState = Blocks.SHORT_GRASS.defaultBlockState();
/*    */     
/* 48 */     Optional<Holder> optional = paramServerLevel.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).get(VegetationPlacements.GRASS_BONEMEAL);
/*    */     
/*    */     byte b;
/* 51 */     label34: for (b = 0; b < ''; b++) {
/* 52 */       Holder holder; BlockPos blockPos1 = blockPos;
/* 53 */       for (byte b1 = 0; b1 < b / 16; ) {
/* 54 */         blockPos1 = blockPos1.offset(paramRandomSource.nextInt(3) - 1, (paramRandomSource.nextInt(3) - 1) * paramRandomSource.nextInt(3) / 2, paramRandomSource.nextInt(3) - 1);
/* 55 */         if (paramServerLevel.getBlockState(blockPos1.below()).is(this)) { if (paramServerLevel.getBlockState(blockPos1).isCollisionShapeFullBlock((BlockGetter)paramServerLevel, blockPos1))
/*    */             continue label34; 
/*    */           b1++; }
/*    */         
/*    */         continue label34;
/*    */       } 
/* 61 */       BlockState blockState1 = paramServerLevel.getBlockState(blockPos1);
/* 62 */       if (blockState1.is(blockState.getBlock()) && paramRandomSource.nextInt(10) == 0) {
/* 63 */         BonemealableBlock bonemealableBlock = (BonemealableBlock)blockState.getBlock();
/* 64 */         if (bonemealableBlock.isValidBonemealTarget((LevelReader)paramServerLevel, blockPos1, blockState1)) {
/* 65 */           bonemealableBlock.performBonemeal(paramServerLevel, paramRandomSource, blockPos1, blockState1);
/*    */         }
/*    */       } 
/*    */       
/* 69 */       if (!blockState1.isAir()) {
/*    */         continue;
/*    */       }
/*    */ 
/*    */       
/* 74 */       if (paramRandomSource.nextInt(8) == 0) {
/* 75 */         List<ConfiguredFeature> list = ((Biome)paramServerLevel.getBiome(blockPos1).value()).getGenerationSettings().getFlowerFeatures();
/* 76 */         if (list.isEmpty()) {
/*    */           continue;
/*    */         }
/*    */         
/* 80 */         int i = paramRandomSource.nextInt(list.size());
/* 81 */         holder = ((RandomPatchConfiguration)((ConfiguredFeature)list.get(i)).config()).feature();
/* 82 */       } else if (optional.isPresent()) {
/* 83 */         holder = optional.get();
/*    */       } else {
/*    */         continue;
/*    */       } 
/*    */       
/* 88 */       ((PlacedFeature)holder.value()).place((WorldGenLevel)paramServerLevel, paramServerLevel.getChunkSource().getGenerator(), paramRandomSource, blockPos1);
/*    */       continue;
/*    */     } 
/*    */   }
/*    */   
/*    */   public BonemealableBlock.Type getType() {
/* 94 */     return BonemealableBlock.Type.NEIGHBOR_SPREADER;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GrassBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */