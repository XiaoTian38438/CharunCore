/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AttachedToLogsDecorator extends TreeDecorator {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(()), (App)ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(())).apply((Applicative)paramInstance, AttachedToLogsDecorator::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<AttachedToLogsDecorator> CODEC;
/*    */   
/*    */   private final float probability;
/*    */   private final BlockStateProvider blockProvider;
/*    */   private final List<Direction> directions;
/*    */   
/*    */   public AttachedToLogsDecorator(float paramFloat, BlockStateProvider paramBlockStateProvider, List<Direction> paramList) {
/* 27 */     this.probability = paramFloat;
/* 28 */     this.blockProvider = paramBlockStateProvider;
/* 29 */     this.directions = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 34 */     RandomSource randomSource = paramContext.random();
/* 35 */     for (BlockPos blockPos1 : Util.shuffledCopy(paramContext.logs(), randomSource)) {
/* 36 */       Direction direction = (Direction)Util.getRandom(this.directions, randomSource);
/* 37 */       BlockPos blockPos2 = blockPos1.relative(direction);
/* 38 */       if (randomSource.nextFloat() <= this.probability && paramContext.isAir(blockPos2)) {
/* 39 */         paramContext.setBlock(blockPos2, this.blockProvider.getState(randomSource, blockPos2));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 46 */     return TreeDecoratorType.ATTACHED_TO_LOGS;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AttachedToLogsDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */