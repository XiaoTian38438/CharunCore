/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CreakingHeartDecorator extends TreeDecorator {
/*    */   public static final MapCodec<CreakingHeartDecorator> CODEC;
/*    */   
/*    */   static {
/* 19 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CreakingHeartDecorator::new, paramCreakingHeartDecorator -> Float.valueOf(paramCreakingHeartDecorator.probability));
/*    */   }
/*    */   private final float probability;
/*    */   
/*    */   public CreakingHeartDecorator(float paramFloat) {
/* 24 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 29 */     return TreeDecoratorType.CREAKING_HEART;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 34 */     RandomSource randomSource = paramContext.random();
/*    */     
/* 36 */     ObjectArrayList<BlockPos> objectArrayList = paramContext.logs();
/* 37 */     if (objectArrayList.isEmpty()) {
/*    */       return;
/*    */     }
/* 40 */     if (randomSource.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     ArrayList<BlockPos> arrayList = new ArrayList<>((Collection<? extends BlockPos>)objectArrayList);
/* 45 */     Util.shuffle(arrayList, randomSource);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     Optional<BlockPos> optional = arrayList.stream().filter(paramBlockPos -> { for (Direction direction : Direction.values()) { if (!paramContext.checkBlock(paramBlockPos.relative(direction), ())) return false;  }  return true; }).findFirst();
/* 56 */     if (optional.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 60 */     paramContext.setBlock(optional.get(), (BlockState)((BlockState)Blocks.CREAKING_HEART.defaultBlockState().setValue((Property)CreakingHeartBlock.STATE, (Comparable)CreakingHeartState.DORMANT)).setValue((Property)CreakingHeartBlock.NATURAL, Boolean.valueOf(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CreakingHeartDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */