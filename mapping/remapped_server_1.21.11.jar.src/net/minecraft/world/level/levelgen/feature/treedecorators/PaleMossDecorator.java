/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ 
/*    */ public class PaleMossDecorator extends TreeDecorator {
/*    */   public static final MapCodec<PaleMossDecorator> CODEC;
/*    */   private final float leavesProbability;
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 22 */     return TreeDecoratorType.PALE_MOSS;
/*    */   } private final float trunkProbability; private final float groundProbability;
/*    */   static {
/* 25 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(())).apply((Applicative)paramInstance, PaleMossDecorator::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PaleMossDecorator(float paramFloat1, float paramFloat2, float paramFloat3) {
/* 36 */     this.leavesProbability = paramFloat1;
/* 37 */     this.trunkProbability = paramFloat2;
/* 38 */     this.groundProbability = paramFloat3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 43 */     RandomSource randomSource = paramContext.random();
/*    */     
/* 45 */     WorldGenLevel worldGenLevel = (WorldGenLevel)paramContext.level();
/*    */     
/* 47 */     List<? extends BlockPos> list = Util.shuffledCopy(paramContext.logs(), randomSource);
/* 48 */     if (list.isEmpty()) {
/*    */       return;
/*    */     }
/* 51 */     BlockPos blockPos = Collections.<BlockPos>min(list, Comparator.comparingInt(Vec3i::getY));
/*    */     
/* 53 */     if (randomSource.nextFloat() < this.groundProbability) {
/* 54 */       worldGenLevel.registryAccess()
/* 55 */         .lookup(Registries.CONFIGURED_FEATURE)
/* 56 */         .flatMap(paramRegistry -> paramRegistry.get(VegetationFeatures.PALE_MOSS_PATCH))
/* 57 */         .ifPresent(paramReference -> ((ConfiguredFeature)paramReference.value()).place(paramWorldGenLevel, paramWorldGenLevel.getLevel().getChunkSource().getGenerator(), paramRandomSource, paramBlockPos.above()));
/*    */     }
/* 59 */     paramContext.logs().forEach(paramBlockPos -> {
/*    */           if (paramRandomSource.nextFloat() < this.trunkProbability) {
/*    */             BlockPos blockPos = paramBlockPos.below();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addMossHanger(blockPos, paramContext);
/*    */             }
/*    */           } 
/*    */         });
/* 67 */     paramContext.leaves().forEach(paramBlockPos -> {
/*    */           if (paramRandomSource.nextFloat() < this.leavesProbability) {
/*    */             BlockPos blockPos = paramBlockPos.below();
/*    */             if (paramContext.isAir(blockPos)) {
/*    */               addMossHanger(blockPos, paramContext);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void addMossHanger(BlockPos paramBlockPos, TreeDecorator.Context paramContext) {
/* 78 */     while (paramContext.isAir(paramBlockPos.below()) && 
/* 79 */       paramContext.random().nextFloat() >= 0.5D) {
/*    */ 
/*    */       
/* 82 */       paramContext.setBlock(paramBlockPos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue((Property)HangingMossBlock.TIP, Boolean.valueOf(false)));
/* 83 */       paramBlockPos = paramBlockPos.below();
/*    */     } 
/* 85 */     paramContext.setBlock(paramBlockPos, (BlockState)Blocks.PALE_HANGING_MOSS.defaultBlockState().setValue((Property)HangingMossBlock.TIP, Boolean.valueOf(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\PaleMossDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */