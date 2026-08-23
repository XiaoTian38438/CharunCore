/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.CocoaBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CocoaDecorator extends TreeDecorator {
/*    */   public static final MapCodec<CocoaDecorator> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CocoaDecorator::new, paramCocoaDecorator -> Float.valueOf(paramCocoaDecorator.probability));
/*    */   }
/*    */   private final float probability;
/*    */   
/*    */   public CocoaDecorator(float paramFloat) {
/* 19 */     this.probability = paramFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 24 */     return TreeDecoratorType.COCOA;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 29 */     RandomSource randomSource = paramContext.random();
/* 30 */     if (randomSource.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     ObjectArrayList<BlockPos> objectArrayList = paramContext.logs();
/* 35 */     if (objectArrayList.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     int i = ((BlockPos)objectArrayList.getFirst()).getY();
/* 40 */     objectArrayList.stream()
/* 41 */       .filter(paramBlockPos -> (paramBlockPos.getY() - paramInt <= 2))
/* 42 */       .forEach(paramBlockPos -> {
/*    */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/*    */             if (paramRandomSource.nextFloat() <= 0.25F) {
/*    */               Direction direction1 = direction.getOpposite();
/*    */               BlockPos blockPos = paramBlockPos.offset(direction1.getStepX(), 0, direction1.getStepZ());
/*    */               if (paramContext.isAir(blockPos))
/*    */                 paramContext.setBlock(blockPos, (BlockState)((BlockState)Blocks.COCOA.defaultBlockState().setValue((Property)CocoaBlock.AGE, Integer.valueOf(paramRandomSource.nextInt(3)))).setValue((Property)CocoaBlock.FACING, (Comparable)direction)); 
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CocoaDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */