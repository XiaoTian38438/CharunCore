/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AttachedToLeavesDecorator extends TreeDecorator {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(paramInstance -> paramInstance.group((App)Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()), (App)Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter(()), (App)Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(()), (App)Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter(()), (App)ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(())).apply((Applicative)paramInstance, AttachedToLeavesDecorator::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<AttachedToLeavesDecorator> CODEC;
/*    */   
/*    */   protected final float probability;
/*    */   
/*    */   protected final int exclusionRadiusXZ;
/*    */   
/*    */   protected final int exclusionRadiusY;
/*    */   
/*    */   protected final BlockStateProvider blockProvider;
/*    */   protected final int requiredEmptyBlocks;
/*    */   protected final List<Direction> directions;
/*    */   
/*    */   public AttachedToLeavesDecorator(float paramFloat, int paramInt1, int paramInt2, BlockStateProvider paramBlockStateProvider, int paramInt3, List<Direction> paramList) {
/* 35 */     this.probability = paramFloat;
/* 36 */     this.exclusionRadiusXZ = paramInt1;
/* 37 */     this.exclusionRadiusY = paramInt2;
/* 38 */     this.blockProvider = paramBlockStateProvider;
/* 39 */     this.requiredEmptyBlocks = paramInt3;
/* 40 */     this.directions = paramList;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context paramContext) {
/* 45 */     HashSet<BlockPos> hashSet = new HashSet();
/*    */     
/* 47 */     RandomSource randomSource = paramContext.random();
/* 48 */     for (BlockPos blockPos1 : Util.shuffledCopy(paramContext.leaves(), randomSource)) {
/* 49 */       Direction direction = (Direction)Util.getRandom(this.directions, randomSource);
/* 50 */       BlockPos blockPos2 = blockPos1.relative(direction);
/* 51 */       if (hashSet.contains(blockPos2)) {
/*    */         continue;
/*    */       }
/* 54 */       if (randomSource.nextFloat() < this.probability && 
/* 55 */         hasRequiredEmptyBlocks(paramContext, blockPos1, direction)) {
/*    */         
/* 57 */         BlockPos blockPos3 = blockPos2.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
/* 58 */         BlockPos blockPos4 = blockPos2.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);
/* 59 */         for (BlockPos blockPos : BlockPos.betweenClosed(blockPos3, blockPos4)) {
/* 60 */           hashSet.add(blockPos.immutable());
/*    */         }
/*    */         
/* 63 */         paramContext.setBlock(blockPos2, this.blockProvider.getState(randomSource, blockPos2));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean hasRequiredEmptyBlocks(TreeDecorator.Context paramContext, BlockPos paramBlockPos, Direction paramDirection) {
/* 70 */     for (byte b = 1; b <= this.requiredEmptyBlocks; b++) {
/* 71 */       BlockPos blockPos = paramBlockPos.relative(paramDirection, b);
/* 72 */       if (!paramContext.isAir(blockPos)) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 81 */     return TreeDecoratorType.ATTACHED_TO_LEAVES;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AttachedToLeavesDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */