/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SteepMaterialCondition
/*     */   extends SurfaceRules.LazyXZCondition
/*     */ {
/*     */   SteepMaterialCondition(SurfaceRules.Context paramContext) {
/* 191 */     super(paramContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean compute() {
/* 196 */     int i = this.context.blockX & 0xF;
/* 197 */     int j = this.context.blockZ & 0xF;
/*     */     
/* 199 */     int k = Math.max(j - 1, 0);
/* 200 */     int m = Math.min(j + 1, 15);
/*     */     
/* 202 */     ChunkAccess chunkAccess = this.context.chunk;
/* 203 */     int n = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, k);
/* 204 */     int i1 = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, m);
/*     */     
/* 206 */     if (i1 >= n + 4) {
/* 207 */       return true;
/*     */     }
/*     */     
/* 210 */     int i2 = Math.max(i - 1, 0);
/* 211 */     int i3 = Math.min(i + 1, 15);
/* 212 */     int i4 = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i2, j);
/* 213 */     int i5 = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i3, j);
/*     */     
/* 215 */     return (i4 >= i5 + 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$SteepMaterialCondition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */