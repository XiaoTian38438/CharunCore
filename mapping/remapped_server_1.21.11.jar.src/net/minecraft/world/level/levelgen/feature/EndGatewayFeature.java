/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ 
/*    */ public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {
/*    */   public EndGatewayFeature(Codec<EndGatewayConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<EndGatewayConfiguration> paramFeaturePlaceContext) {
/* 18 */     BlockPos blockPos = paramFeaturePlaceContext.origin();
/* 19 */     WorldGenLevel worldGenLevel = paramFeaturePlaceContext.level();
/* 20 */     EndGatewayConfiguration endGatewayConfiguration = paramFeaturePlaceContext.config();
/* 21 */     for (Iterator<BlockPos> iterator = BlockPos.betweenClosed(blockPos.offset(-1, -2, -1), blockPos.offset(1, 2, 1)).iterator(); iterator.hasNext(); ) { BlockPos blockPos1 = iterator.next();
/* 22 */       boolean bool1 = (blockPos1.getX() == blockPos.getX()) ? true : false;
/* 23 */       boolean bool2 = (blockPos1.getY() == blockPos.getY()) ? true : false;
/* 24 */       boolean bool3 = (blockPos1.getZ() == blockPos.getZ()) ? true : false;
/* 25 */       boolean bool4 = (Math.abs(blockPos1.getY() - blockPos.getY()) == 2) ? true : false;
/*    */       
/* 27 */       if (bool1 && bool2 && bool3) {
/* 28 */         BlockPos blockPos2 = blockPos1.immutable();
/* 29 */         setBlock((LevelWriter)worldGenLevel, blockPos2, Blocks.END_GATEWAY.defaultBlockState());
/* 30 */         endGatewayConfiguration.getExit().ifPresent(paramBlockPos2 -> {
/*    */               BlockEntity blockEntity = paramWorldGenLevel.getBlockEntity(paramBlockPos1); if (blockEntity instanceof TheEndGatewayBlockEntity) {
/*    */                 TheEndGatewayBlockEntity theEndGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity; theEndGatewayBlockEntity.setExitPosition(paramBlockPos2, paramEndGatewayConfiguration.isExitExact());
/*    */               } 
/*    */             }); continue;
/*    */       } 
/* 36 */       if (bool2) {
/* 37 */         setBlock((LevelWriter)worldGenLevel, blockPos1, Blocks.AIR.defaultBlockState()); continue;
/* 38 */       }  if (bool4 && bool1 && bool3) {
/* 39 */         setBlock((LevelWriter)worldGenLevel, blockPos1, Blocks.BEDROCK.defaultBlockState()); continue;
/* 40 */       }  if ((!bool1 && !bool3) || bool4) {
/* 41 */         setBlock((LevelWriter)worldGenLevel, blockPos1, Blocks.AIR.defaultBlockState()); continue;
/*    */       } 
/* 43 */       setBlock((LevelWriter)worldGenLevel, blockPos1, Blocks.BEDROCK.defaultBlockState()); }
/*    */ 
/*    */     
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\EndGatewayFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */