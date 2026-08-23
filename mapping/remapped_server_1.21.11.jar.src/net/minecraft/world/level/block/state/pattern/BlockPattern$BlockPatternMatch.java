/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ public class BlockPatternMatch
/*     */ {
/*     */   private final BlockPos frontTopLeft;
/*     */   private final Direction forwards;
/*     */   private final Direction up;
/*     */   private final LoadingCache<BlockPos, BlockInWorld> cache;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final int depth;
/*     */   
/*     */   public BlockPatternMatch(BlockPos paramBlockPos, Direction paramDirection1, Direction paramDirection2, LoadingCache<BlockPos, BlockInWorld> paramLoadingCache, int paramInt1, int paramInt2, int paramInt3) {
/* 146 */     this.frontTopLeft = paramBlockPos;
/* 147 */     this.forwards = paramDirection1;
/* 148 */     this.up = paramDirection2;
/* 149 */     this.cache = paramLoadingCache;
/* 150 */     this.width = paramInt1;
/* 151 */     this.height = paramInt2;
/* 152 */     this.depth = paramInt3;
/*     */   }
/*     */   
/*     */   public BlockPos getFrontTopLeft() {
/* 156 */     return this.frontTopLeft;
/*     */   }
/*     */   
/*     */   public Direction getForwards() {
/* 160 */     return this.forwards;
/*     */   }
/*     */   
/*     */   public Direction getUp() {
/* 164 */     return this.up;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 168 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 172 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 176 */     return this.depth;
/*     */   }
/*     */   
/*     */   public BlockInWorld getBlock(int paramInt1, int paramInt2, int paramInt3) {
/* 180 */     return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, getForwards(), getUp(), paramInt1, paramInt2, paramInt3));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return MoreObjects.toStringHelper(this)
/* 186 */       .add("up", this.up)
/* 187 */       .add("forwards", this.forwards)
/* 188 */       .add("frontTopLeft", this.frontTopLeft)
/* 189 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern$BlockPatternMatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */