/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
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
/*     */ class Position
/*     */ {
/*     */   double x;
/*     */   double z;
/*     */   
/*     */   double dist(Position paramPosition) {
/* 252 */     double d1 = this.x - paramPosition.x;
/* 253 */     double d2 = this.z - paramPosition.z;
/*     */     
/* 255 */     return Math.sqrt(d1 * d1 + d2 * d2);
/*     */   }
/*     */   
/*     */   void normalize() {
/* 259 */     double d = getLength();
/* 260 */     this.x /= d;
/* 261 */     this.z /= d;
/*     */   }
/*     */   
/*     */   double getLength() {
/* 265 */     return Math.sqrt(this.x * this.x + this.z * this.z);
/*     */   }
/*     */   
/*     */   public void moveAway(Position paramPosition) {
/* 269 */     this.x -= paramPosition.x;
/* 270 */     this.z -= paramPosition.z;
/*     */   }
/*     */   
/*     */   public boolean clamp(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 274 */     boolean bool = false;
/*     */     
/* 276 */     if (this.x < paramDouble1) {
/* 277 */       this.x = paramDouble1;
/* 278 */       bool = true;
/* 279 */     } else if (this.x > paramDouble3) {
/* 280 */       this.x = paramDouble3;
/* 281 */       bool = true;
/*     */     } 
/*     */     
/* 284 */     if (this.z < paramDouble2) {
/* 285 */       this.z = paramDouble2;
/* 286 */       bool = true;
/* 287 */     } else if (this.z > paramDouble4) {
/* 288 */       this.z = paramDouble4;
/* 289 */       bool = true;
/*     */     } 
/*     */     
/* 292 */     return bool;
/*     */   }
/*     */   
/*     */   public int getSpawnY(BlockGetter paramBlockGetter, int paramInt) {
/* 296 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(this.x, (paramInt + 1), this.z);
/* 297 */     boolean bool1 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/* 298 */     mutableBlockPos.move(Direction.DOWN);
/* 299 */     boolean bool2 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/* 300 */     while (mutableBlockPos.getY() > paramBlockGetter.getMinY()) {
/* 301 */       mutableBlockPos.move(Direction.DOWN);
/* 302 */       boolean bool = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos).isAir();
/*     */       
/* 304 */       if (!bool && bool2 && bool1) {
/* 305 */         return mutableBlockPos.getY() + 1;
/*     */       }
/* 307 */       bool1 = bool2;
/* 308 */       bool2 = bool;
/*     */     } 
/*     */     
/* 311 */     return paramInt + 1;
/*     */   }
/*     */   
/*     */   public boolean isSafe(BlockGetter paramBlockGetter, int paramInt) {
/* 315 */     BlockPos blockPos = BlockPos.containing(this.x, (getSpawnY(paramBlockGetter, paramInt) - 1), this.z);
/* 316 */     BlockState blockState = paramBlockGetter.getBlockState(blockPos);
/* 317 */     return (blockPos.getY() < paramInt && !blockState.liquid() && !blockState.is(BlockTags.FIRE));
/*     */   }
/*     */   
/*     */   public void randomize(RandomSource paramRandomSource, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 321 */     this.x = Mth.nextDouble(paramRandomSource, paramDouble1, paramDouble3);
/* 322 */     this.z = Mth.nextDouble(paramRandomSource, paramDouble2, paramDouble4);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SpreadPlayersCommand$Position.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */