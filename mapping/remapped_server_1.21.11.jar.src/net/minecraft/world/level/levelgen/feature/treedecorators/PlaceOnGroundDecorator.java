/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class PlaceOnGroundDecorator extends TreeDecorator {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(Integer.valueOf(128)).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(Integer.valueOf(2)).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(Integer.valueOf(1)).forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(())).apply((Applicative)paramInstance, PlaceOnGroundDecorator::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<PlaceOnGroundDecorator> CODEC;
/*    */   
/*    */   private final int tries;
/*    */   
/*    */   private final int radius;
/*    */   private final int height;
/*    */   private final BlockStateProvider blockStateProvider;
/*    */   
/*    */   public PlaceOnGroundDecorator(int paramInt1, int paramInt2, int paramInt3, BlockStateProvider paramBlockStateProvider) {
/* 32 */     this.tries = paramInt1;
/* 33 */     this.radius = paramInt2;
/* 34 */     this.height = paramInt3;
/* 35 */     this.blockStateProvider = paramBlockStateProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 40 */     return TreeDecoratorType.PLACE_ON_GROUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 45 */     List<BlockPos> list = TreeFeature.getLowestTrunkOrRootOfTree(paramContext);
/*    */     
/* 47 */     if (list.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     BlockPos blockPos = list.getFirst();
/* 52 */     int i = blockPos.getY();
/* 53 */     int j = blockPos.getX();
/* 54 */     int k = blockPos.getX();
/* 55 */     int m = blockPos.getZ();
/* 56 */     int n = blockPos.getZ();
/* 57 */     for (BlockPos blockPos1 : list) {
/* 58 */       if (blockPos1.getY() == i) {
/* 59 */         j = Math.min(j, blockPos1.getX());
/* 60 */         k = Math.max(k, blockPos1.getX());
/* 61 */         m = Math.min(m, blockPos1.getZ());
/* 62 */         n = Math.max(n, blockPos1.getZ());
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     RandomSource randomSource = paramContext.random();
/* 67 */     BoundingBox boundingBox = (new BoundingBox(j, i, m, k, i, n)).inflatedBy(this.radius, this.height, this.radius);
/* 68 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/*    */     
/* 70 */     for (byte b = 0; b < this.tries; b++) {
/* 71 */       mutableBlockPos.set(randomSource.nextIntBetweenInclusive(boundingBox.minX(), boundingBox.maxX()), randomSource.nextIntBetweenInclusive(boundingBox.minY(), boundingBox.maxY()), randomSource.nextIntBetweenInclusive(boundingBox.minZ(), boundingBox.maxZ()));
/* 72 */       attemptToPlaceBlockAbove(paramContext, (BlockPos)mutableBlockPos);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void attemptToPlaceBlockAbove(TreeDecorator.Context paramContext, BlockPos paramBlockPos) {
/* 77 */     BlockPos blockPos = paramBlockPos.above();
/* 78 */     if (paramContext.level().isStateAtPosition(blockPos, paramBlockState -> (paramBlockState.isAir() || paramBlockState.is(Blocks.VINE))) && paramContext
/* 79 */       .checkBlock(paramBlockPos, BlockBehaviour.BlockStateBase::isSolidRender) && paramContext
/* 80 */       .level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, paramBlockPos).getY() <= blockPos.getY())
/* 81 */       paramContext.setBlock(blockPos, this.blockStateProvider.getState(paramContext.random(), blockPos)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\PlaceOnGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */