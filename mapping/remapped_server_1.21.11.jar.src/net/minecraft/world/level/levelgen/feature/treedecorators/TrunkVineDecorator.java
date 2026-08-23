/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ 
/*    */ public class TrunkVineDecorator
/*    */   extends TreeDecorator {
/*    */   protected TreeDecoratorType<?> type() {
/* 11 */     return TreeDecoratorType.TRUNK_VINE;
/*    */   }
/*    */   
/* 14 */   public static final MapCodec<TrunkVineDecorator> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 16 */   public static final TrunkVineDecorator INSTANCE = new TrunkVineDecorator();
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 20 */     RandomSource randomSource = paramContext.random();
/* 21 */     paramContext.logs().forEach(paramBlockPos -> {
/*    */           if (paramRandomSource.nextInt(3) > 0) {
/*    */             BlockPos blockPos = paramBlockPos.west();
/*    */             if (paramContext.isAir(blockPos))
/*    */               paramContext.placeVine(blockPos, VineBlock.EAST); 
/*    */           } 
/*    */           if (paramRandomSource.nextInt(3) > 0) {
/*    */             BlockPos blockPos = paramBlockPos.east();
/*    */             if (paramContext.isAir(blockPos))
/*    */               paramContext.placeVine(blockPos, VineBlock.WEST); 
/*    */           } 
/*    */           if (paramRandomSource.nextInt(3) > 0) {
/*    */             BlockPos blockPos = paramBlockPos.north();
/*    */             if (paramContext.isAir(blockPos))
/*    */               paramContext.placeVine(blockPos, VineBlock.SOUTH); 
/*    */           } 
/*    */           if (paramRandomSource.nextInt(3) > 0) {
/*    */             BlockPos blockPos = paramBlockPos.south();
/*    */             if (paramContext.isAir(blockPos))
/*    */               paramContext.placeVine(blockPos, VineBlock.NORTH); 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TrunkVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */