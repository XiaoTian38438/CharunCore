/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.WallTorchBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndPodiumFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public static final int PODIUM_RADIUS = 4;
/*    */   public static final int PODIUM_PILLAR_HEIGHT = 4;
/* 17 */   private static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO; public static final int RIM_RADIUS = 1; public static final float CORNER_ROUNDING = 0.5F; private final boolean active;
/*    */   
/*    */   public static BlockPos getLocation(BlockPos paramBlockPos) {
/* 20 */     return END_PODIUM_LOCATION.offset((Vec3i)paramBlockPos);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EndPodiumFeature(boolean paramBoolean) {
/* 26 */     super(NoneFeatureConfiguration.CODEC);
/* 27 */     this.active = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> paramFeaturePlaceContext) {
/* 32 */     BlockPos blockPos1 = paramFeaturePlaceContext.origin();
/* 33 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 34 */     for (BlockPos blockPos : BlockPos.betweenClosed(new BlockPos(blockPos1.getX() - 4, blockPos1.getY() - 1, blockPos1.getZ() - 4), new BlockPos(blockPos1.getX() + 4, blockPos1.getY() + 32, blockPos1.getZ() + 4))) {
/* 35 */       boolean bool = blockPos.closerThan((Vec3i)blockPos1, 2.5D);
/*    */       
/* 37 */       if (bool || blockPos.closerThan((Vec3i)blockPos1, 3.5D)) {
/* 38 */         if (blockPos.getY() < blockPos1.getY()) {
/* 39 */           if (bool) {
/*    */             
/* 41 */             setBlock((LevelWriter)worldGenLevel, blockPos, Blocks.BEDROCK.defaultBlockState()); continue;
/* 42 */           }  if (blockPos.getY() < blockPos1.getY()) {
/*    */             
/* 44 */             if (this.active) {
/* 45 */               dropPreviousAndSetBlock(worldGenLevel, blockPos, Blocks.END_STONE); continue;
/*    */             } 
/* 47 */             setBlock((LevelWriter)worldGenLevel, blockPos, Blocks.END_STONE.defaultBlockState());
/*    */           }  continue;
/*    */         } 
/* 50 */         if (blockPos.getY() > blockPos1.getY()) {
/*    */           
/* 52 */           if (this.active) {
/* 53 */             dropPreviousAndSetBlock(worldGenLevel, blockPos, Blocks.AIR); continue;
/*    */           } 
/* 55 */           setBlock((LevelWriter)worldGenLevel, blockPos, Blocks.AIR.defaultBlockState()); continue;
/*    */         } 
/* 57 */         if (!bool) {
/*    */           
/* 59 */           setBlock((LevelWriter)worldGenLevel, blockPos, Blocks.BEDROCK.defaultBlockState()); continue;
/* 60 */         }  if (this.active) {
/*    */           
/* 62 */           dropPreviousAndSetBlock(worldGenLevel, new BlockPos((Vec3i)blockPos), Blocks.END_PORTAL); continue;
/*    */         } 
/* 64 */         setBlock((LevelWriter)worldGenLevel, new BlockPos((Vec3i)blockPos), Blocks.AIR.defaultBlockState());
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 70 */     for (byte b = 0; b < 4; b++) {
/* 71 */       setBlock((LevelWriter)worldGenLevel, blockPos1.above(b), Blocks.BEDROCK.defaultBlockState());
/*    */     }
/*    */     
/* 74 */     BlockPos blockPos2 = blockPos1.above(2);
/* 75 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 76 */       setBlock((LevelWriter)worldGenLevel, blockPos2.relative(direction), (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)direction));
/*    */     }
/*    */     
/* 79 */     return true;
/*    */   }
/*    */   
/*    */   private void dropPreviousAndSetBlock(WorldGenLevel paramWorldGenLevel, BlockPos paramBlockPos, Block paramBlock) {
/* 83 */     if (!paramWorldGenLevel.getBlockState(paramBlockPos).is(paramBlock)) {
/* 84 */       paramWorldGenLevel.destroyBlock(paramBlockPos, true, null);
/* 85 */       setBlock((LevelWriter)paramWorldGenLevel, paramBlockPos, paramBlock.defaultBlockState());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\EndPodiumFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */