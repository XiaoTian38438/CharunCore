/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralClawFeature
/*    */   extends CoralFeature {
/*    */   public CoralClawFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 17 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 22 */     if (!placeCoralBlock(paramLevelAccessor, paramRandomSource, paramBlockPos, paramBlockState)) {
/* 23 */       return false;
/*    */     }
/*    */     
/* 26 */     Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/* 27 */     int i = paramRandomSource.nextInt(2) + 2;
/*    */     
/* 29 */     List list1 = Util.toShuffledList(Stream.of(new Direction[] { direction, direction.getClockWise(), direction.getCounterClockWise() }, ), paramRandomSource);
/* 30 */     List list2 = list1.subList(0, i);
/*    */     
/* 32 */     for (Direction direction1 : list2) {
/* 33 */       int k; Direction direction2; BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/* 34 */       int j = paramRandomSource.nextInt(2) + 1;
/*    */ 
/*    */ 
/*    */       
/* 38 */       mutableBlockPos.move(direction1);
/* 39 */       if (direction1 == direction) {
/* 40 */         direction2 = direction;
/* 41 */         k = paramRandomSource.nextInt(3) + 2;
/*    */       } else {
/* 43 */         mutableBlockPos.move(Direction.UP);
/*    */ 
/*    */         
/* 46 */         Direction[] arrayOfDirection = { direction1, Direction.UP };
/* 47 */         direction2 = (Direction)Util.getRandom((Object[])arrayOfDirection, paramRandomSource);
/* 48 */         k = paramRandomSource.nextInt(3) + 3;
/*    */       } 
/*    */       byte b;
/* 51 */       for (b = 0; b < j && 
/* 52 */         placeCoralBlock(paramLevelAccessor, paramRandomSource, (BlockPos)mutableBlockPos, paramBlockState); b++)
/*    */       {
/*    */         
/* 55 */         mutableBlockPos.move(direction2);
/*    */       }
/* 57 */       mutableBlockPos.move(direction2.getOpposite());
/* 58 */       mutableBlockPos.move(Direction.UP);
/*    */       
/* 60 */       for (b = 0; b < k; b++) {
/* 61 */         mutableBlockPos.move(direction);
/* 62 */         if (!placeCoralBlock(paramLevelAccessor, paramRandomSource, (BlockPos)mutableBlockPos, paramBlockState)) {
/*    */           break;
/*    */         }
/*    */         
/* 66 */         if (paramRandomSource.nextFloat() < 0.25F) {
/* 67 */           mutableBlockPos.move(Direction.UP);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\CoralClawFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */