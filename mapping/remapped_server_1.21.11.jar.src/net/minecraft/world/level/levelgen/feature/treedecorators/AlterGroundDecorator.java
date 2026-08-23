/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AlterGroundDecorator extends TreeDecorator {
/*    */   public static final MapCodec<AlterGroundDecorator> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, paramAlterGroundDecorator -> paramAlterGroundDecorator.provider);
/*    */   }
/*    */   private final BlockStateProvider provider;
/*    */   
/*    */   public AlterGroundDecorator(BlockStateProvider paramBlockStateProvider) {
/* 17 */     this.provider = paramBlockStateProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 22 */     return TreeDecoratorType.ALTER_GROUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 27 */     List<BlockPos> list = TreeFeature.getLowestTrunkOrRootOfTree(paramContext);
/*    */     
/* 29 */     if (list.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     int i = ((BlockPos)list.get(0)).getY();
/* 34 */     list.stream().filter(paramBlockPos -> (paramBlockPos.getY() == paramInt)).forEach(paramBlockPos -> {
/*    */           placeCircle(paramContext, paramBlockPos.west().north());
/*    */           placeCircle(paramContext, paramBlockPos.east(2).north());
/*    */           placeCircle(paramContext, paramBlockPos.west().south(2));
/*    */           placeCircle(paramContext, paramBlockPos.east(2).south(2));
/*    */           for (byte b = 0; b < 5; b++) {
/*    */             int i = paramContext.random().nextInt(64);
/*    */             int j = i % 8;
/*    */             int k = i / 8;
/*    */             if (j == 0 || j == 7 || k == 0 || k == 7) {
/*    */               placeCircle(paramContext, paramBlockPos.offset(-3 + j, 0, -3 + k));
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private void placeCircle(TreeDecorator.Context paramContext, BlockPos paramBlockPos) {
/* 52 */     for (byte b = -2; b <= 2; b++) {
/* 53 */       for (byte b1 = -2; b1 <= 2; b1++) {
/* 54 */         if (Math.abs(b) != 2 || Math.abs(b1) != 2) {
/* 55 */           placeBlockAt(paramContext, paramBlockPos.offset(b, 0, b1));
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void placeBlockAt(TreeDecorator.Context paramContext, BlockPos paramBlockPos) {
/* 62 */     for (byte b = 2; b >= -3; b--) {
/* 63 */       BlockPos blockPos = paramBlockPos.above(b);
/* 64 */       if (Feature.isGrassOrDirt(paramContext.level(), blockPos)) {
/* 65 */         paramContext.setBlock(blockPos, this.provider.getState(paramContext.random(), paramBlockPos)); break;
/*    */       } 
/* 67 */       if (!paramContext.isAir(blockPos) && b < 0)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AlterGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */