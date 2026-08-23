/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public final class BitSetDiscreteVoxelShape
/*     */   extends DiscreteVoxelShape {
/*     */   private final BitSet storage;
/*     */   private int xMin;
/*     */   private int yMin;
/*     */   private int zMin;
/*     */   private int xMax;
/*     */   private int yMax;
/*     */   private int zMax;
/*     */   
/*     */   public BitSetDiscreteVoxelShape(int paramInt1, int paramInt2, int paramInt3) {
/*  17 */     super(paramInt1, paramInt2, paramInt3);
/*  18 */     this.storage = new BitSet(paramInt1 * paramInt2 * paramInt3);
/*  19 */     this.xMin = paramInt1;
/*  20 */     this.yMin = paramInt2;
/*  21 */     this.zMin = paramInt3;
/*     */   }
/*     */   
/*     */   public static BitSetDiscreteVoxelShape withFilledBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9) {
/*  25 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(paramInt1, paramInt2, paramInt3);
/*     */     
/*  27 */     bitSetDiscreteVoxelShape.xMin = paramInt4;
/*  28 */     bitSetDiscreteVoxelShape.yMin = paramInt5;
/*  29 */     bitSetDiscreteVoxelShape.zMin = paramInt6;
/*  30 */     bitSetDiscreteVoxelShape.xMax = paramInt7;
/*  31 */     bitSetDiscreteVoxelShape.yMax = paramInt8;
/*  32 */     bitSetDiscreteVoxelShape.zMax = paramInt9;
/*     */     
/*  34 */     for (int i = paramInt4; i < paramInt7; i++) {
/*  35 */       for (int j = paramInt5; j < paramInt8; j++) {
/*  36 */         for (int k = paramInt6; k < paramInt9; k++) {
/*  37 */           bitSetDiscreteVoxelShape.fillUpdateBounds(i, j, k, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     return bitSetDiscreteVoxelShape;
/*     */   }
/*     */   
/*     */   public BitSetDiscreteVoxelShape(DiscreteVoxelShape paramDiscreteVoxelShape) {
/*  46 */     super(paramDiscreteVoxelShape.xSize, paramDiscreteVoxelShape.ySize, paramDiscreteVoxelShape.zSize);
/*  47 */     if (paramDiscreteVoxelShape instanceof BitSetDiscreteVoxelShape) {
/*  48 */       this.storage = (BitSet)((BitSetDiscreteVoxelShape)paramDiscreteVoxelShape).storage.clone();
/*     */     } else {
/*  50 */       this.storage = new BitSet(this.xSize * this.ySize * this.zSize);
/*  51 */       for (byte b = 0; b < this.xSize; b++) {
/*  52 */         for (byte b1 = 0; b1 < this.ySize; b1++) {
/*  53 */           for (byte b2 = 0; b2 < this.zSize; b2++) {
/*  54 */             if (paramDiscreteVoxelShape.isFull(b, b1, b2)) {
/*  55 */               this.storage.set(getIndex(b, b1, b2));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  62 */     this.xMin = paramDiscreteVoxelShape.firstFull(Direction.Axis.X);
/*  63 */     this.yMin = paramDiscreteVoxelShape.firstFull(Direction.Axis.Y);
/*  64 */     this.zMin = paramDiscreteVoxelShape.firstFull(Direction.Axis.Z);
/*     */     
/*  66 */     this.xMax = paramDiscreteVoxelShape.lastFull(Direction.Axis.X);
/*  67 */     this.yMax = paramDiscreteVoxelShape.lastFull(Direction.Axis.Y);
/*  68 */     this.zMax = paramDiscreteVoxelShape.lastFull(Direction.Axis.Z);
/*     */   }
/*     */   
/*     */   protected int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/*  72 */     return (paramInt1 * this.ySize + paramInt2) * this.zSize + paramInt3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull(int paramInt1, int paramInt2, int paramInt3) {
/*  77 */     return this.storage.get(getIndex(paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */   
/*     */   private void fillUpdateBounds(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/*  81 */     this.storage.set(getIndex(paramInt1, paramInt2, paramInt3));
/*     */     
/*  83 */     if (paramBoolean) {
/*  84 */       this.xMin = Math.min(this.xMin, paramInt1);
/*  85 */       this.yMin = Math.min(this.yMin, paramInt2);
/*  86 */       this.zMin = Math.min(this.zMin, paramInt3);
/*     */       
/*  88 */       this.xMax = Math.max(this.xMax, paramInt1 + 1);
/*  89 */       this.yMax = Math.max(this.yMax, paramInt2 + 1);
/*  90 */       this.zMax = Math.max(this.zMax, paramInt3 + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill(int paramInt1, int paramInt2, int paramInt3) {
/*  96 */     fillUpdateBounds(paramInt1, paramInt2, paramInt3, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 101 */     return this.storage.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int firstFull(Direction.Axis paramAxis) {
/* 106 */     return paramAxis.choose(this.xMin, this.yMin, this.zMin);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastFull(Direction.Axis paramAxis) {
/* 111 */     return paramAxis.choose(this.xMax, this.yMax, this.zMax);
/*     */   }
/*     */   
/*     */   static BitSetDiscreteVoxelShape join(DiscreteVoxelShape paramDiscreteVoxelShape1, DiscreteVoxelShape paramDiscreteVoxelShape2, IndexMerger paramIndexMerger1, IndexMerger paramIndexMerger2, IndexMerger paramIndexMerger3, BooleanOp paramBooleanOp) {
/* 115 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(paramIndexMerger1.size() - 1, paramIndexMerger2.size() - 1, paramIndexMerger3.size() - 1);
/* 116 */     int[] arrayOfInt = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     paramIndexMerger1.forMergedIndexes((paramInt1, paramInt2, paramInt3) -> {
/*     */           boolean[] arrayOfBoolean = { false };
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           paramIndexMerger1.forMergedIndexes(());
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           if (arrayOfBoolean[0]) {
/*     */             paramArrayOfint[0] = Math.min(paramArrayOfint[0], paramInt3);
/*     */ 
/*     */ 
/*     */             
/*     */             paramArrayOfint[3] = Math.max(paramArrayOfint[3], paramInt3);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*     */           return true;
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 151 */     bitSetDiscreteVoxelShape.xMin = arrayOfInt[0];
/* 152 */     bitSetDiscreteVoxelShape.yMin = arrayOfInt[1];
/* 153 */     bitSetDiscreteVoxelShape.zMin = arrayOfInt[2];
/* 154 */     bitSetDiscreteVoxelShape.xMax = arrayOfInt[3] + 1;
/* 155 */     bitSetDiscreteVoxelShape.yMax = arrayOfInt[4] + 1;
/* 156 */     bitSetDiscreteVoxelShape.zMax = arrayOfInt[5] + 1;
/* 157 */     return bitSetDiscreteVoxelShape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void forAllBoxes(DiscreteVoxelShape paramDiscreteVoxelShape, DiscreteVoxelShape.IntLineConsumer paramIntLineConsumer, boolean paramBoolean) {
/* 165 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(paramDiscreteVoxelShape);
/* 166 */     for (byte b = 0; b < bitSetDiscreteVoxelShape.ySize; b++) {
/* 167 */       for (byte b1 = 0; b1 < bitSetDiscreteVoxelShape.xSize; b1++) {
/* 168 */         byte b2 = -1;
/* 169 */         for (byte b3 = 0; b3 <= bitSetDiscreteVoxelShape.zSize; b3++) {
/* 170 */           if (bitSetDiscreteVoxelShape.isFullWide(b1, b, b3)) {
/* 171 */             if (paramBoolean) {
/*     */               
/* 173 */               if (b2 == -1) {
/* 174 */                 b2 = b3;
/*     */               }
/*     */             } else {
/* 177 */               paramIntLineConsumer.consume(b1, b, b3, b1 + 1, b + 1, b3 + 1);
/*     */             } 
/* 179 */           } else if (b2 != -1) {
/*     */ 
/*     */             
/* 182 */             byte b4 = b1;
/* 183 */             byte b5 = b;
/*     */ 
/*     */             
/* 186 */             bitSetDiscreteVoxelShape.clearZStrip(b2, b3, b1, b);
/*     */ 
/*     */             
/* 189 */             while (bitSetDiscreteVoxelShape.isZStripFull(b2, b3, b4 + 1, b)) {
/* 190 */               bitSetDiscreteVoxelShape.clearZStrip(b2, b3, b4 + 1, b);
/* 191 */               b4++;
/*     */             } 
/*     */ 
/*     */             
/* 195 */             while (bitSetDiscreteVoxelShape.isXZRectangleFull(b1, b4 + 1, b2, b3, b5 + 1)) {
/* 196 */               for (byte b6 = b1; b6 <= b4; b6++) {
/* 197 */                 bitSetDiscreteVoxelShape.clearZStrip(b2, b3, b6, b5 + 1);
/*     */               }
/* 199 */               b5++;
/*     */             } 
/*     */             
/* 202 */             paramIntLineConsumer.consume(b1, b, b2, b4 + 1, b5 + 1, b3);
/* 203 */             b2 = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isZStripFull(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 212 */     if (paramInt3 >= this.xSize || paramInt4 >= this.ySize) {
/* 213 */       return false;
/*     */     }
/* 215 */     return (this.storage.nextClearBit(getIndex(paramInt3, paramInt4, paramInt1)) >= getIndex(paramInt3, paramInt4, paramInt2));
/*     */   }
/*     */   
/*     */   private boolean isXZRectangleFull(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 219 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 220 */       if (!isZStripFull(paramInt3, paramInt4, i, paramInt5)) {
/* 221 */         return false;
/*     */       }
/*     */     } 
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearZStrip(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 229 */     this.storage.clear(getIndex(paramInt3, paramInt4, paramInt1), getIndex(paramInt3, paramInt4, paramInt2));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterior(int paramInt1, int paramInt2, int paramInt3) {
/* 234 */     boolean bool = (paramInt1 > 0 && paramInt1 < this.xSize - 1 && paramInt2 > 0 && paramInt2 < this.ySize - 1 && paramInt3 > 0 && paramInt3 < this.zSize - 1) ? true : false;
/*     */     
/* 236 */     return (bool && isFull(paramInt1, paramInt2, paramInt3) && 
/* 237 */       isFull(paramInt1 - 1, paramInt2, paramInt3) && 
/* 238 */       isFull(paramInt1 + 1, paramInt2, paramInt3) && 
/* 239 */       isFull(paramInt1, paramInt2 - 1, paramInt3) && 
/* 240 */       isFull(paramInt1, paramInt2 + 1, paramInt3) && 
/* 241 */       isFull(paramInt1, paramInt2, paramInt3 - 1) && 
/* 242 */       isFull(paramInt1, paramInt2, paramInt3 + 1));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\BitSetDiscreteVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */