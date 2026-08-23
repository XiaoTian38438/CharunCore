/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralTreeFeature extends CoralFeature {
/*    */   public CoralTreeFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 15 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 20 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*    */     
/* 22 */     int i = paramRandomSource.nextInt(3) + 1;
/* 23 */     for (byte b = 0; b < i; b++) {
/* 24 */       if (!placeCoralBlock(paramLevelAccessor, paramRandomSource, (BlockPos)mutableBlockPos, paramBlockState)) {
/* 25 */         return true;
/*    */       }
/* 27 */       mutableBlockPos.move(Direction.UP);
/*    */     } 
/* 29 */     BlockPos blockPos = mutableBlockPos.immutable();
/*    */     
/* 31 */     int j = paramRandomSource.nextInt(3) + 2;
/* 32 */     List list1 = Direction.Plane.HORIZONTAL.shuffledCopy(paramRandomSource);
/* 33 */     List list2 = list1.subList(0, j);
/*    */     
/* 35 */     for (Direction direction : list2) {
/* 36 */       mutableBlockPos.set((Vec3i)blockPos);
/* 37 */       mutableBlockPos.move(direction);
/*    */       
/* 39 */       int k = paramRandomSource.nextInt(5) + 2;
/* 40 */       byte b1 = 0;
/* 41 */       for (byte b2 = 0; b2 < k && 
/* 42 */         placeCoralBlock(paramLevelAccessor, paramRandomSource, (BlockPos)mutableBlockPos, paramBlockState); b2++) {
/*    */ 
/*    */         
/* 45 */         b1++;
/* 46 */         mutableBlockPos.move(Direction.UP);
/*    */         
/* 48 */         if (b2 == 0 || (b1 >= 2 && paramRandomSource.nextFloat() < 0.25F)) {
/* 49 */           mutableBlockPos.move(direction);
/* 50 */           b1 = 0;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\CoralTreeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */