/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BoundingBox {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static final Codec<BoundingBox> CODEC;
/*     */   public static final StreamCodec<ByteBuf, BoundingBox> STREAM_CODEC;
/*     */   
/*     */   static {
/*  29 */     CODEC = Codec.INT_STREAM.comapFlatMap(paramIntStream -> Util.fixedSize(paramIntStream, 6).map(()), paramBoundingBox -> IntStream.of(new int[] { paramBoundingBox.minX, paramBoundingBox.minY, paramBoundingBox.minZ, paramBoundingBox.maxX, paramBoundingBox.maxY, paramBoundingBox.maxZ })).stable();
/*     */     
/*  31 */     STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, paramBoundingBox -> new BlockPos(paramBoundingBox.minX, paramBoundingBox.minY, paramBoundingBox.minZ), BlockPos.STREAM_CODEC, paramBoundingBox -> new BlockPos(paramBoundingBox.maxX, paramBoundingBox.maxY, paramBoundingBox.maxZ), (paramBlockPos1, paramBlockPos2) -> new BoundingBox(paramBlockPos1.getX(), paramBlockPos1.getY(), paramBlockPos1.getZ(), paramBlockPos2.getX(), paramBlockPos2.getY(), paramBlockPos2.getZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   private int minX;
/*     */   
/*     */   private int minY;
/*     */   
/*     */   private int minZ;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */   private int maxZ;
/*     */   
/*     */   public BoundingBox(BlockPos paramBlockPos) {
/*  45 */     this(paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ(), paramBlockPos.getX(), paramBlockPos.getY(), paramBlockPos.getZ());
/*     */   }
/*     */   
/*     */   public BoundingBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/*  49 */     this.minX = paramInt1;
/*  50 */     this.minY = paramInt2;
/*  51 */     this.minZ = paramInt3;
/*  52 */     this.maxX = paramInt4;
/*  53 */     this.maxY = paramInt5;
/*  54 */     this.maxZ = paramInt6;
/*     */     
/*  56 */     if (paramInt4 < paramInt1 || paramInt5 < paramInt2 || paramInt6 < paramInt3) {
/*  57 */       Util.logAndPauseIfInIde("Invalid bounding box data, inverted bounds for: " + String.valueOf(this));
/*     */       
/*  59 */       this.minX = Math.min(paramInt1, paramInt4);
/*  60 */       this.minY = Math.min(paramInt2, paramInt5);
/*  61 */       this.minZ = Math.min(paramInt3, paramInt6);
/*  62 */       this.maxX = Math.max(paramInt1, paramInt4);
/*  63 */       this.maxY = Math.max(paramInt2, paramInt5);
/*  64 */       this.maxZ = Math.max(paramInt3, paramInt6);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BoundingBox fromCorners(Vec3i paramVec3i1, Vec3i paramVec3i2) {
/*  69 */     return new BoundingBox(Math.min(paramVec3i1.getX(), paramVec3i2.getX()), Math.min(paramVec3i1.getY(), paramVec3i2.getY()), Math.min(paramVec3i1.getZ(), paramVec3i2.getZ()), Math.max(paramVec3i1.getX(), paramVec3i2.getX()), Math.max(paramVec3i1.getY(), paramVec3i2.getY()), Math.max(paramVec3i1.getZ(), paramVec3i2.getZ()));
/*     */   }
/*     */   
/*     */   public static BoundingBox infinite() {
/*  73 */     return new BoundingBox(-2147483648, -2147483648, -2147483648, 2147483647, 2147483647, 2147483647);
/*     */   }
/*     */   
/*     */   public static BoundingBox orientBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, Direction paramDirection) {
/*  77 */     switch (paramDirection) {
/*     */ 
/*     */       
/*     */       default:
/*  81 */         return new BoundingBox(paramInt1 + paramInt4, paramInt2 + paramInt5, paramInt3 + paramInt6, paramInt1 + paramInt7 - 1 + paramInt4, paramInt2 + paramInt8 - 1 + paramInt5, paramInt3 + paramInt9 - 1 + paramInt6);
/*     */       
/*     */       case NORTH:
/*  84 */         return new BoundingBox(paramInt1 + paramInt4, paramInt2 + paramInt5, paramInt3 - paramInt9 + 1 + paramInt6, paramInt1 + paramInt7 - 1 + paramInt4, paramInt2 + paramInt8 - 1 + paramInt5, paramInt3 + paramInt6);
/*     */       
/*     */       case WEST:
/*  87 */         return new BoundingBox(paramInt1 - paramInt9 + 1 + paramInt6, paramInt2 + paramInt5, paramInt3 + paramInt4, paramInt1 + paramInt6, paramInt2 + paramInt8 - 1 + paramInt5, paramInt3 + paramInt7 - 1 + paramInt4);
/*     */       case EAST:
/*     */         break;
/*  90 */     }  return new BoundingBox(paramInt1 + paramInt6, paramInt2 + paramInt5, paramInt3 + paramInt4, paramInt1 + paramInt9 - 1 + paramInt6, paramInt2 + paramInt8 - 1 + paramInt5, paramInt3 + paramInt7 - 1 + paramInt4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<ChunkPos> intersectingChunks() {
/*  95 */     int i = SectionPos.blockToSectionCoord(minX());
/*  96 */     int j = SectionPos.blockToSectionCoord(minZ());
/*  97 */     int k = SectionPos.blockToSectionCoord(maxX());
/*  98 */     int m = SectionPos.blockToSectionCoord(maxZ());
/*  99 */     return ChunkPos.rangeClosed(new ChunkPos(i, j), new ChunkPos(k, m));
/*     */   }
/*     */   
/*     */   public boolean intersects(BoundingBox paramBoundingBox) {
/* 103 */     return (this.maxX >= paramBoundingBox.minX && this.minX <= paramBoundingBox.maxX && this.maxZ >= paramBoundingBox.minZ && this.minZ <= paramBoundingBox.maxZ && this.maxY >= paramBoundingBox.minY && this.minY <= paramBoundingBox.maxY);
/*     */   }
/*     */   
/*     */   public boolean intersects(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 107 */     return (this.maxX >= paramInt1 && this.minX <= paramInt3 && this.maxZ >= paramInt2 && this.minZ <= paramInt4);
/*     */   }
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingPositions(Iterable<BlockPos> paramIterable) {
/* 111 */     Iterator<BlockPos> iterator = paramIterable.iterator();
/* 112 */     if (!iterator.hasNext()) {
/* 113 */       return Optional.empty();
/*     */     }
/*     */     
/* 116 */     BoundingBox boundingBox = new BoundingBox(iterator.next());
/* 117 */     Objects.requireNonNull(boundingBox); iterator.forEachRemaining(boundingBox::encapsulate);
/* 118 */     return Optional.of(boundingBox);
/*     */   }
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingBoxes(Iterable<BoundingBox> paramIterable) {
/* 122 */     Iterator<BoundingBox> iterator = paramIterable.iterator();
/* 123 */     if (!iterator.hasNext()) {
/* 124 */       return Optional.empty();
/*     */     }
/*     */     
/* 127 */     BoundingBox boundingBox1 = iterator.next();
/* 128 */     BoundingBox boundingBox2 = new BoundingBox(boundingBox1.minX, boundingBox1.minY, boundingBox1.minZ, boundingBox1.maxX, boundingBox1.maxY, boundingBox1.maxZ);
/* 129 */     Objects.requireNonNull(boundingBox2); iterator.forEachRemaining(boundingBox2::encapsulate);
/* 130 */     return Optional.of(boundingBox2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BoundingBox paramBoundingBox) {
/* 138 */     this.minX = Math.min(this.minX, paramBoundingBox.minX);
/* 139 */     this.minY = Math.min(this.minY, paramBoundingBox.minY);
/* 140 */     this.minZ = Math.min(this.minZ, paramBoundingBox.minZ);
/* 141 */     this.maxX = Math.max(this.maxX, paramBoundingBox.maxX);
/* 142 */     this.maxY = Math.max(this.maxY, paramBoundingBox.maxY);
/* 143 */     this.maxZ = Math.max(this.maxZ, paramBoundingBox.maxZ);
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public static BoundingBox encapsulating(BoundingBox paramBoundingBox1, BoundingBox paramBoundingBox2) {
/* 148 */     return new BoundingBox(
/* 149 */         Math.min(paramBoundingBox1.minX, paramBoundingBox2.minX), 
/* 150 */         Math.min(paramBoundingBox1.minY, paramBoundingBox2.minY), 
/* 151 */         Math.min(paramBoundingBox1.minZ, paramBoundingBox2.minZ), 
/* 152 */         Math.max(paramBoundingBox1.maxX, paramBoundingBox2.maxX), 
/* 153 */         Math.max(paramBoundingBox1.maxY, paramBoundingBox2.maxY), 
/* 154 */         Math.max(paramBoundingBox1.maxZ, paramBoundingBox2.maxZ));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BlockPos paramBlockPos) {
/* 163 */     this.minX = Math.min(this.minX, paramBlockPos.getX());
/* 164 */     this.minY = Math.min(this.minY, paramBlockPos.getY());
/* 165 */     this.minZ = Math.min(this.minZ, paramBlockPos.getZ());
/* 166 */     this.maxX = Math.max(this.maxX, paramBlockPos.getX());
/* 167 */     this.maxY = Math.max(this.maxY, paramBlockPos.getY());
/* 168 */     this.maxZ = Math.max(this.maxZ, paramBlockPos.getZ());
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox move(int paramInt1, int paramInt2, int paramInt3) {
/* 177 */     this.minX += paramInt1;
/* 178 */     this.minY += paramInt2;
/* 179 */     this.minZ += paramInt3;
/* 180 */     this.maxX += paramInt1;
/* 181 */     this.maxY += paramInt2;
/* 182 */     this.maxZ += paramInt3;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox move(Vec3i paramVec3i) {
/* 191 */     return move(paramVec3i.getX(), paramVec3i.getY(), paramVec3i.getZ());
/*     */   }
/*     */   
/*     */   public BoundingBox moved(int paramInt1, int paramInt2, int paramInt3) {
/* 195 */     return new BoundingBox(this.minX + paramInt1, this.minY + paramInt2, this.minZ + paramInt3, this.maxX + paramInt1, this.maxY + paramInt2, this.maxZ + paramInt3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox inflatedBy(int paramInt) {
/* 206 */     return inflatedBy(paramInt, paramInt, paramInt);
/*     */   }
/*     */   
/*     */   public BoundingBox inflatedBy(int paramInt1, int paramInt2, int paramInt3) {
/* 210 */     return new BoundingBox(
/* 211 */         minX() - paramInt1, 
/* 212 */         minY() - paramInt2, 
/* 213 */         minZ() - paramInt3, 
/* 214 */         maxX() + paramInt1, 
/* 215 */         maxY() + paramInt2, 
/* 216 */         maxZ() + paramInt3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInside(Vec3i paramVec3i) {
/* 221 */     return isInside(paramVec3i.getX(), paramVec3i.getY(), paramVec3i.getZ());
/*     */   }
/*     */   
/*     */   public boolean isInside(int paramInt1, int paramInt2, int paramInt3) {
/* 225 */     return (paramInt1 >= this.minX && paramInt1 <= this.maxX && paramInt3 >= this.minZ && paramInt3 <= this.maxZ && paramInt2 >= this.minY && paramInt2 <= this.maxY);
/*     */   }
/*     */   
/*     */   public Vec3i getLength() {
/* 229 */     return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
/*     */   }
/*     */   
/*     */   public int getXSpan() {
/* 233 */     return this.maxX - this.minX + 1;
/*     */   }
/*     */   
/*     */   public int getYSpan() {
/* 237 */     return this.maxY - this.minY + 1;
/*     */   }
/*     */   
/*     */   public int getZSpan() {
/* 241 */     return this.maxZ - this.minZ + 1;
/*     */   }
/*     */   
/*     */   public BlockPos getCenter() {
/* 245 */     return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
/*     */   }
/*     */   
/*     */   public void forAllCorners(Consumer<BlockPos> paramConsumer) {
/* 249 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 250 */     paramConsumer.accept(mutableBlockPos.set(this.maxX, this.maxY, this.maxZ));
/* 251 */     paramConsumer.accept(mutableBlockPos.set(this.minX, this.maxY, this.maxZ));
/* 252 */     paramConsumer.accept(mutableBlockPos.set(this.maxX, this.minY, this.maxZ));
/* 253 */     paramConsumer.accept(mutableBlockPos.set(this.minX, this.minY, this.maxZ));
/* 254 */     paramConsumer.accept(mutableBlockPos.set(this.maxX, this.maxY, this.minZ));
/* 255 */     paramConsumer.accept(mutableBlockPos.set(this.minX, this.maxY, this.minZ));
/* 256 */     paramConsumer.accept(mutableBlockPos.set(this.maxX, this.minY, this.minZ));
/* 257 */     paramConsumer.accept(mutableBlockPos.set(this.minX, this.minY, this.minZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 262 */     return MoreObjects.toStringHelper(this)
/* 263 */       .add("minX", this.minX)
/* 264 */       .add("minY", this.minY)
/* 265 */       .add("minZ", this.minZ)
/* 266 */       .add("maxX", this.maxX)
/* 267 */       .add("maxY", this.maxY)
/* 268 */       .add("maxZ", this.maxZ)
/* 269 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 274 */     if (this == paramObject) {
/* 275 */       return true;
/*     */     }
/* 277 */     if (paramObject instanceof BoundingBox) { BoundingBox boundingBox = (BoundingBox)paramObject;
/* 278 */       return (this.minX == boundingBox.minX && this.minY == boundingBox.minY && this.minZ == boundingBox.minZ && this.maxX == boundingBox.maxX && this.maxY == boundingBox.maxY && this.maxZ == boundingBox.maxZ); }
/*     */     
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 285 */     return Objects.hash(new Object[] { Integer.valueOf(this.minX), Integer.valueOf(this.minY), Integer.valueOf(this.minZ), Integer.valueOf(this.maxX), Integer.valueOf(this.maxY), Integer.valueOf(this.maxZ) });
/*     */   }
/*     */   
/*     */   public int minX() {
/* 289 */     return this.minX;
/*     */   }
/*     */   
/*     */   public int minY() {
/* 293 */     return this.minY;
/*     */   }
/*     */   
/*     */   public int minZ() {
/* 297 */     return this.minZ;
/*     */   }
/*     */   
/*     */   public int maxX() {
/* 301 */     return this.maxX;
/*     */   }
/*     */   
/*     */   public int maxY() {
/* 305 */     return this.maxY;
/*     */   }
/*     */   
/*     */   public int maxZ() {
/* 309 */     return this.maxZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\BoundingBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */