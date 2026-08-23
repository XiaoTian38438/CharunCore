/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public abstract class DataLayerStorageMap<M extends DataLayerStorageMap<M>>
/*    */ {
/*    */   private static final int CACHE_SIZE = 2;
/*  9 */   private final long[] lastSectionKeys = new long[2];
/* 10 */   private final DataLayer[] lastSections = new DataLayer[2];
/*    */   private boolean cacheEnabled;
/*    */   protected final Long2ObjectOpenHashMap<DataLayer> map;
/*    */   
/*    */   protected DataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> paramLong2ObjectOpenHashMap) {
/* 15 */     this.map = paramLong2ObjectOpenHashMap;
/* 16 */     clearCache();
/* 17 */     this.cacheEnabled = true;
/*    */   }
/*    */   
/*    */   public abstract M copy();
/*    */   
/*    */   public DataLayer copyDataLayer(long paramLong) {
/* 23 */     DataLayer dataLayer = ((DataLayer)this.map.get(paramLong)).copy();
/* 24 */     this.map.put(paramLong, dataLayer);
/* 25 */     clearCache();
/* 26 */     return dataLayer;
/*    */   }
/*    */   
/*    */   public boolean hasLayer(long paramLong) {
/* 30 */     return this.map.containsKey(paramLong);
/*    */   }
/*    */   
/*    */   public DataLayer getLayer(long paramLong) {
/* 34 */     if (this.cacheEnabled) {
/* 35 */       for (byte b = 0; b < 2; b++) {
/* 36 */         if (paramLong == this.lastSectionKeys[b]) {
/* 37 */           return this.lastSections[b];
/*    */         }
/*    */       } 
/*    */     }
/* 41 */     DataLayer dataLayer = (DataLayer)this.map.get(paramLong);
/* 42 */     if (dataLayer != null) {
/* 43 */       if (this.cacheEnabled) {
/* 44 */         for (byte b = 1; b; b--) {
/* 45 */           this.lastSectionKeys[b] = this.lastSectionKeys[b - 1];
/* 46 */           this.lastSections[b] = this.lastSections[b - 1];
/*    */         } 
/* 48 */         this.lastSectionKeys[0] = paramLong;
/* 49 */         this.lastSections[0] = dataLayer;
/*    */       } 
/* 51 */       return dataLayer;
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public DataLayer removeLayer(long paramLong) {
/* 58 */     return (DataLayer)this.map.remove(paramLong);
/*    */   }
/*    */   
/*    */   public void setLayer(long paramLong, DataLayer paramDataLayer) {
/* 62 */     this.map.put(paramLong, paramDataLayer);
/*    */   }
/*    */   
/*    */   public void clearCache() {
/* 66 */     for (byte b = 0; b < 2; b++) {
/* 67 */       this.lastSectionKeys[b] = Long.MAX_VALUE;
/* 68 */       this.lastSections[b] = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void disableCache() {
/* 73 */     this.cacheEnabled = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\DataLayerStorageMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */