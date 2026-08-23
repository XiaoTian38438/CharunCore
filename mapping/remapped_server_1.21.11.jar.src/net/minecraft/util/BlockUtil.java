/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class BlockUtil
/*     */ {
/*     */   public static class IntBounds {
/*     */     public final int min;
/*     */     public final int max;
/*     */     
/*     */     public IntBounds(int param1Int1, int param1Int2) {
/*  22 */       this.min = param1Int1;
/*  23 */       this.max = param1Int2;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  28 */       return "IntBounds{min=" + this.min + ", max=" + this.max + "}";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FoundRectangle
/*     */   {
/*     */     public final BlockPos minCorner;
/*     */     
/*     */     public final int axis1Size;
/*     */     public final int axis2Size;
/*     */     
/*     */     public FoundRectangle(BlockPos param1BlockPos, int param1Int1, int param1Int2) {
/*  41 */       this.minCorner = param1BlockPos;
/*  42 */       this.axis1Size = param1Int1;
/*  43 */       this.axis2Size = param1Int2;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FoundRectangle getLargestRectangleAround(BlockPos paramBlockPos, Direction.Axis paramAxis1, int paramInt1, Direction.Axis paramAxis2, int paramInt2, Predicate<BlockPos> paramPredicate) {
/*  61 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/*  63 */     Direction direction1 = Direction.get(Direction.AxisDirection.NEGATIVE, paramAxis1);
/*  64 */     Direction direction2 = direction1.getOpposite();
/*     */     
/*  66 */     Direction direction3 = Direction.get(Direction.AxisDirection.NEGATIVE, paramAxis2);
/*  67 */     Direction direction4 = direction3.getOpposite();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     int i = getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos), direction1, paramInt1);
/*  84 */     int j = getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos), direction2, paramInt1);
/*     */     
/*  86 */     int k = i;
/*  87 */     IntBounds[] arrayOfIntBounds = new IntBounds[k + 1 + j];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     arrayOfIntBounds[k] = new IntBounds(
/* 104 */         getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos), direction3, paramInt2), 
/* 105 */         getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos), direction4, paramInt2));
/*     */ 
/*     */     
/* 108 */     int m = (arrayOfIntBounds[k]).min;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     for (n = 1; n <= i; n++) {
/* 129 */       IntBounds intBounds = arrayOfIntBounds[k - n - 1];
/* 130 */       arrayOfIntBounds[k - n] = new IntBounds(
/* 131 */           getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos).move(direction1, n), direction3, intBounds.min), 
/* 132 */           getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos).move(direction1, n), direction4, intBounds.max));
/*     */     } 
/*     */ 
/*     */     
/* 136 */     for (n = 1; n <= j; n++) {
/* 137 */       IntBounds intBounds = arrayOfIntBounds[k + n - 1];
/* 138 */       arrayOfIntBounds[k + n] = new IntBounds(
/* 139 */           getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos).move(direction2, n), direction3, intBounds.min), 
/* 140 */           getLimit(paramPredicate, mutableBlockPos.set((Vec3i)paramBlockPos).move(direction2, n), direction4, intBounds.max));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     n = 0;
/* 159 */     int i1 = 0;
/* 160 */     int i2 = 0;
/* 161 */     int i3 = 0;
/*     */     
/* 163 */     int[] arrayOfInt = new int[arrayOfIntBounds.length];
/*     */     
/* 165 */     for (int i4 = m; i4 >= 0; i4--) {
/* 166 */       for (byte b = 0; b < arrayOfIntBounds.length; b++) {
/* 167 */         IntBounds intBounds1 = arrayOfIntBounds[b];
/* 168 */         int i7 = m - intBounds1.min;
/* 169 */         int i8 = m + intBounds1.max;
/*     */         
/* 171 */         arrayOfInt[b] = (i4 >= i7 && i4 <= i8) ? (i8 + 1 - i4) : 0;
/*     */       } 
/*     */       
/* 174 */       Pair<IntBounds, Integer> pair = getMaxRectangleLocation(arrayOfInt);
/* 175 */       IntBounds intBounds = (IntBounds)pair.getFirst();
/* 176 */       int i5 = 1 + intBounds.max - intBounds.min;
/* 177 */       int i6 = ((Integer)pair.getSecond()).intValue();
/*     */       
/* 179 */       if (i5 * i6 > i2 * i3) {
/* 180 */         n = intBounds.min;
/* 181 */         i1 = i4;
/* 182 */         i2 = i5;
/* 183 */         i3 = i6;
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     return new FoundRectangle(paramBlockPos
/* 188 */         .relative(paramAxis1, n - k).relative(paramAxis2, i1 - m), i2, i3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLimit(Predicate<BlockPos> paramPredicate, BlockPos.MutableBlockPos paramMutableBlockPos, Direction paramDirection, int paramInt) {
/* 195 */     byte b = 0;
/* 196 */     while (b < paramInt && paramPredicate.test(paramMutableBlockPos.move(paramDirection))) {
/* 197 */       b++;
/*     */     }
/* 199 */     return b;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Pair<IntBounds, Integer> getMaxRectangleLocation(int[] paramArrayOfint) {
/* 204 */     byte b1 = 0;
/* 205 */     byte b2 = 0;
/* 206 */     int i = 0;
/*     */     
/* 208 */     IntArrayList intArrayList = new IntArrayList();
/* 209 */     intArrayList.push(0);
/* 210 */     for (byte b3 = 1; b3 <= paramArrayOfint.length; b3++) {
/* 211 */       byte b = (b3 == paramArrayOfint.length) ? 0 : paramArrayOfint[b3];
/* 212 */       while (!intArrayList.isEmpty()) {
/* 213 */         int j = paramArrayOfint[intArrayList.topInt()];
/* 214 */         if (b >= j) {
/* 215 */           intArrayList.push(b3);
/*     */           
/*     */           break;
/*     */         } 
/* 219 */         intArrayList.popInt();
/* 220 */         byte b4 = intArrayList.isEmpty() ? 0 : (intArrayList.topInt() + 1);
/*     */         
/* 222 */         if (j * (b3 - b4) > i * (b2 - b1)) {
/* 223 */           b2 = b3;
/* 224 */           b1 = b4;
/* 225 */           i = j;
/*     */         } 
/*     */       } 
/*     */       
/* 229 */       if (intArrayList.isEmpty()) {
/* 230 */         intArrayList.push(b3);
/*     */       }
/*     */     } 
/*     */     
/* 234 */     return new Pair(new IntBounds(b1, b2 - 1), Integer.valueOf(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<BlockPos> getTopConnectedBlock(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Block paramBlock1, Direction paramDirection, Block paramBlock2) {
/*     */     BlockState blockState;
/* 243 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable();
/*     */     
/*     */     do {
/* 246 */       mutableBlockPos.move(paramDirection);
/* 247 */       blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/* 248 */     } while (blockState.is(paramBlock1));
/*     */     
/* 250 */     if (blockState.is(paramBlock2)) {
/* 251 */       return (Optional)Optional.of(mutableBlockPos);
/*     */     }
/* 253 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\BlockUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */