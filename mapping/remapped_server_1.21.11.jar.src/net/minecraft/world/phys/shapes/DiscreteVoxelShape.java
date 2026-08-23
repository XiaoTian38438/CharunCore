/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ public abstract class DiscreteVoxelShape {
/*   9 */   private static final Direction.Axis[] AXIS_VALUES = Direction.Axis.values();
/*     */   
/*     */   protected final int xSize;
/*     */   protected final int ySize;
/*     */   protected final int zSize;
/*     */   
/*     */   protected DiscreteVoxelShape(int paramInt1, int paramInt2, int paramInt3) {
/*  16 */     if (paramInt1 < 0 || paramInt2 < 0 || paramInt3 < 0) {
/*  17 */       throw new IllegalArgumentException("Need all positive sizes: x: " + paramInt1 + ", y: " + paramInt2 + ", z: " + paramInt3);
/*     */     }
/*  19 */     this.xSize = paramInt1;
/*  20 */     this.ySize = paramInt2;
/*  21 */     this.zSize = paramInt3;
/*     */   }
/*     */   
/*     */   public DiscreteVoxelShape rotate(OctahedralGroup paramOctahedralGroup) {
/*  25 */     if (paramOctahedralGroup == OctahedralGroup.IDENTITY) {
/*  26 */       return this;
/*     */     }
/*     */     
/*  29 */     Vector3i vector3i = paramOctahedralGroup.rotate(new Vector3i(this.xSize, this.ySize, this.zSize));
/*     */ 
/*     */     
/*  32 */     int i = fixupCoordinate(vector3i, 0);
/*  33 */     int j = fixupCoordinate(vector3i, 1);
/*  34 */     int k = fixupCoordinate(vector3i, 2);
/*     */     
/*  36 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(vector3i.x, vector3i.y, vector3i.z);
/*  37 */     for (byte b = 0; b < this.xSize; b++) {
/*  38 */       for (byte b1 = 0; b1 < this.ySize; b1++) {
/*  39 */         for (byte b2 = 0; b2 < this.zSize; b2++) {
/*  40 */           if (isFull(b, b1, b2)) {
/*  41 */             Vector3i vector3i1 = paramOctahedralGroup.rotate(vector3i.set(b, b1, b2));
/*  42 */             int m = i + vector3i1.x;
/*  43 */             int n = j + vector3i1.y;
/*  44 */             int i1 = k + vector3i1.z;
/*  45 */             bitSetDiscreteVoxelShape.fill(m, n, i1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  50 */     return bitSetDiscreteVoxelShape;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int fixupCoordinate(Vector3i paramVector3i, int paramInt) {
/*  55 */     int i = paramVector3i.get(paramInt);
/*  56 */     if (i < 0) {
/*  57 */       paramVector3i.setComponent(paramInt, -i);
/*  58 */       return -i - 1;
/*     */     } 
/*  60 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isFullWide(AxisCycle paramAxisCycle, int paramInt1, int paramInt2, int paramInt3) {
/*  64 */     return isFullWide(paramAxisCycle
/*  65 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.X), paramAxisCycle
/*  66 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.Y), paramAxisCycle
/*  67 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullWide(int paramInt1, int paramInt2, int paramInt3) {
/*  72 */     if (paramInt1 < 0 || paramInt2 < 0 || paramInt3 < 0) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (paramInt1 >= this.xSize || paramInt2 >= this.ySize || paramInt3 >= this.zSize) {
/*  76 */       return false;
/*     */     }
/*  78 */     return isFull(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   public boolean isFull(AxisCycle paramAxisCycle, int paramInt1, int paramInt2, int paramInt3) {
/*  82 */     return isFull(paramAxisCycle
/*  83 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.X), paramAxisCycle
/*  84 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.Y), paramAxisCycle
/*  85 */         .cycle(paramInt1, paramInt2, paramInt3, Direction.Axis.Z));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isFull(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract void fill(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     for (Direction.Axis axis : AXIS_VALUES) {
/*  95 */       if (firstFull(axis) >= lastFull(axis)) {
/*  96 */         return true;
/*     */       }
/*     */     } 
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int firstFull(Direction.Axis paramAxis);
/*     */   
/*     */   public abstract int lastFull(Direction.Axis paramAxis);
/*     */   
/*     */   public int firstFull(Direction.Axis paramAxis, int paramInt1, int paramInt2) {
/* 108 */     int i = getSize(paramAxis);
/* 109 */     if (paramInt1 < 0 || paramInt2 < 0) {
/* 110 */       return i;
/*     */     }
/* 112 */     Direction.Axis axis1 = AxisCycle.FORWARD.cycle(paramAxis);
/* 113 */     Direction.Axis axis2 = AxisCycle.BACKWARD.cycle(paramAxis);
/* 114 */     if (paramInt1 >= getSize(axis1) || paramInt2 >= getSize(axis2)) {
/* 115 */       return i;
/*     */     }
/* 117 */     AxisCycle axisCycle = AxisCycle.between(Direction.Axis.X, paramAxis);
/* 118 */     for (byte b = 0; b < i; b++) {
/* 119 */       if (isFull(axisCycle, b, paramInt1, paramInt2)) {
/* 120 */         return b;
/*     */       }
/*     */     } 
/* 123 */     return i;
/*     */   }
/*     */   
/*     */   public int lastFull(Direction.Axis paramAxis, int paramInt1, int paramInt2) {
/* 127 */     if (paramInt1 < 0 || paramInt2 < 0) {
/* 128 */       return 0;
/*     */     }
/* 130 */     Direction.Axis axis1 = AxisCycle.FORWARD.cycle(paramAxis);
/* 131 */     Direction.Axis axis2 = AxisCycle.BACKWARD.cycle(paramAxis);
/* 132 */     if (paramInt1 >= getSize(axis1) || paramInt2 >= getSize(axis2)) {
/* 133 */       return 0;
/*     */     }
/* 135 */     int i = getSize(paramAxis);
/* 136 */     AxisCycle axisCycle = AxisCycle.between(Direction.Axis.X, paramAxis);
/* 137 */     for (int j = i - 1; j >= 0; j--) {
/* 138 */       if (isFull(axisCycle, j, paramInt1, paramInt2)) {
/* 139 */         return j + 1;
/*     */       }
/*     */     } 
/* 142 */     return 0;
/*     */   }
/*     */   
/*     */   public int getSize(Direction.Axis paramAxis) {
/* 146 */     return paramAxis.choose(this.xSize, this.ySize, this.zSize);
/*     */   }
/*     */   
/*     */   public int getXSize() {
/* 150 */     return getSize(Direction.Axis.X);
/*     */   }
/*     */   
/*     */   public int getYSize() {
/* 154 */     return getSize(Direction.Axis.Y);
/*     */   }
/*     */   
/*     */   public int getZSize() {
/* 158 */     return getSize(Direction.Axis.Z);
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
/*     */   public void forAllEdges(IntLineConsumer paramIntLineConsumer, boolean paramBoolean) {
/* 170 */     forAllAxisEdges(paramIntLineConsumer, AxisCycle.NONE, paramBoolean);
/* 171 */     forAllAxisEdges(paramIntLineConsumer, AxisCycle.FORWARD, paramBoolean);
/* 172 */     forAllAxisEdges(paramIntLineConsumer, AxisCycle.BACKWARD, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void forAllAxisEdges(IntLineConsumer paramIntLineConsumer, AxisCycle paramAxisCycle, boolean paramBoolean) {
/* 180 */     AxisCycle axisCycle = paramAxisCycle.inverse();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     int i = getSize(axisCycle.cycle(Direction.Axis.X));
/* 186 */     int j = getSize(axisCycle.cycle(Direction.Axis.Y));
/* 187 */     int k = getSize(axisCycle.cycle(Direction.Axis.Z));
/*     */     
/* 189 */     for (byte b = 0; b <= i; b++) {
/* 190 */       for (byte b1 = 0; b1 <= j; b1++) {
/* 191 */         byte b2 = -1;
/* 192 */         for (byte b3 = 0; b3 <= k; b3++) {
/* 193 */           byte b4 = 0;
/*     */           
/* 195 */           int m = 0;
/* 196 */           for (byte b5 = 0; b5 <= 1; b5++) {
/* 197 */             for (byte b6 = 0; b6 <= 1; b6++) {
/* 198 */               if (isFullWide(axisCycle, b + b5 - 1, b1 + b6 - 1, b3)) {
/* 199 */                 b4++;
/* 200 */                 m ^= b5 ^ b6;
/*     */               } 
/*     */             } 
/*     */           } 
/* 204 */           if (b4 == 1 || b4 == 3 || (b4 == 2 && (m & 0x1) == 0)) {
/* 205 */             if (paramBoolean) {
/*     */               
/* 207 */               if (b2 == -1) {
/* 208 */                 b2 = b3;
/*     */               }
/*     */             } else {
/* 211 */               paramIntLineConsumer.consume(axisCycle
/* 212 */                   .cycle(b, b1, b3, Direction.Axis.X), axisCycle
/* 213 */                   .cycle(b, b1, b3, Direction.Axis.Y), axisCycle
/* 214 */                   .cycle(b, b1, b3, Direction.Axis.Z), axisCycle
/* 215 */                   .cycle(b, b1, b3 + 1, Direction.Axis.X), axisCycle
/* 216 */                   .cycle(b, b1, b3 + 1, Direction.Axis.Y), axisCycle
/* 217 */                   .cycle(b, b1, b3 + 1, Direction.Axis.Z));
/*     */             }
/*     */           
/* 220 */           } else if (b2 != -1) {
/*     */             
/* 222 */             paramIntLineConsumer.consume(axisCycle
/* 223 */                 .cycle(b, b1, b2, Direction.Axis.X), axisCycle
/* 224 */                 .cycle(b, b1, b2, Direction.Axis.Y), axisCycle
/* 225 */                 .cycle(b, b1, b2, Direction.Axis.Z), axisCycle
/* 226 */                 .cycle(b, b1, b3, Direction.Axis.X), axisCycle
/* 227 */                 .cycle(b, b1, b3, Direction.Axis.Y), axisCycle
/* 228 */                 .cycle(b, b1, b3, Direction.Axis.Z));
/*     */             
/* 230 */             b2 = -1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forAllBoxes(IntLineConsumer paramIntLineConsumer, boolean paramBoolean) {
/* 238 */     BitSetDiscreteVoxelShape.forAllBoxes(this, paramIntLineConsumer, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forAllFaces(IntFaceConsumer paramIntFaceConsumer) {
/* 243 */     forAllAxisFaces(paramIntFaceConsumer, AxisCycle.NONE);
/* 244 */     forAllAxisFaces(paramIntFaceConsumer, AxisCycle.FORWARD);
/* 245 */     forAllAxisFaces(paramIntFaceConsumer, AxisCycle.BACKWARD);
/*     */   }
/*     */   
/*     */   private void forAllAxisFaces(IntFaceConsumer paramIntFaceConsumer, AxisCycle paramAxisCycle) {
/* 249 */     AxisCycle axisCycle = paramAxisCycle.inverse();
/*     */     
/* 251 */     Direction.Axis axis = axisCycle.cycle(Direction.Axis.Z);
/*     */     
/* 253 */     int i = getSize(axisCycle.cycle(Direction.Axis.X));
/* 254 */     int j = getSize(axisCycle.cycle(Direction.Axis.Y));
/* 255 */     int k = getSize(axis);
/*     */     
/* 257 */     Direction direction1 = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE);
/* 258 */     Direction direction2 = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
/*     */     
/* 260 */     for (byte b = 0; b < i; b++) {
/* 261 */       for (byte b1 = 0; b1 < j; b1++) {
/* 262 */         boolean bool = false;
/* 263 */         for (byte b2 = 0; b2 <= k; b2++) {
/* 264 */           boolean bool1 = (b2 != k && isFull(axisCycle, b, b1, b2)) ? true : false;
/* 265 */           if (!bool && bool1) {
/* 266 */             paramIntFaceConsumer.consume(direction1, axisCycle
/*     */                 
/* 268 */                 .cycle(b, b1, b2, Direction.Axis.X), axisCycle
/* 269 */                 .cycle(b, b1, b2, Direction.Axis.Y), axisCycle
/* 270 */                 .cycle(b, b1, b2, Direction.Axis.Z));
/*     */           }
/*     */           
/* 273 */           if (bool && !bool1) {
/* 274 */             paramIntFaceConsumer.consume(direction2, axisCycle
/*     */                 
/* 276 */                 .cycle(b, b1, b2 - 1, Direction.Axis.X), axisCycle
/* 277 */                 .cycle(b, b1, b2 - 1, Direction.Axis.Y), axisCycle
/* 278 */                 .cycle(b, b1, b2 - 1, Direction.Axis.Z));
/*     */           }
/*     */           
/* 281 */           bool = bool1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface IntLineConsumer {
/*     */     void consume(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
/*     */   }
/*     */   
/*     */   public static interface IntFaceConsumer {
/*     */     void consume(Direction param1Direction, int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\DiscreteVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */