/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralMushroomFeature extends CoralFeature {
/*    */   public CoralMushroomFeature(Codec<NoneFeatureConfiguration> paramCodec) {
/* 13 */     super(paramCodec);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor paramLevelAccessor, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 18 */     int i = paramRandomSource.nextInt(3) + 3;
/* 19 */     int j = paramRandomSource.nextInt(3) + 3;
/* 20 */     int k = paramRandomSource.nextInt(3) + 3;
/*    */     
/* 22 */     int m = paramRandomSource.nextInt(3) + 1;
/*    */     
/* 24 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*    */ 
/*    */ 
/*    */     
/* 28 */     for (byte b = 0; b <= j; b++) {
/* 29 */       for (byte b1 = 0; b1 <= i; b1++) {
/* 30 */         for (byte b2 = 0; b2 <= k; b2++) {
/* 31 */           mutableBlockPos.set(b + paramBlockPos.getX(), b1 + paramBlockPos.getY(), b2 + paramBlockPos.getZ());
/* 32 */           mutableBlockPos.move(Direction.DOWN, m);
/*    */ 
/*    */           
/* 35 */           if ((b != 0 && b != j) || (b1 != 0 && b1 != i))
/*    */           {
/*    */ 
/*    */             
/* 39 */             if ((b2 != 0 && b2 != k) || (b1 != 0 && b1 != i))
/*    */             {
/*    */ 
/*    */               
/* 43 */               if ((b != 0 && b != j) || (b2 != 0 && b2 != k))
/*    */               {
/*    */ 
/*    */ 
/*    */                 
/* 48 */                 if (b == 0 || b == j || b1 == 0 || b1 == i || b2 == 0 || b2 == k)
/*    */                 {
/*    */ 
/*    */ 
/*    */                   
/* 53 */                   if (paramRandomSource.nextFloat() >= 0.1F)
/*    */                   {
/*    */ 
/*    */                     
/* 57 */                     if (!placeCoralBlock(paramLevelAccessor, paramRandomSource, (BlockPos)mutableBlockPos, paramBlockState)); }  }  } 
/*    */             }
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\feature\CoralMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */