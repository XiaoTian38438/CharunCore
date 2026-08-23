/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public class SkyLightSectionStorage extends LayerLightSectionStorage<SkyLightSectionStorage.SkyDataLayerStorageMap> {
/*     */   protected SkyLightSectionStorage(LightChunkGetter paramLightChunkGetter) {
/*  14 */     super(LightLayer.SKY, paramLightChunkGetter, new SkyDataLayerStorageMap(new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), 2147483647));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLightValue(long paramLong) {
/*  19 */     return getLightValue(paramLong, false);
/*     */   }
/*     */   
/*     */   protected int getLightValue(long paramLong, boolean paramBoolean) {
/*  23 */     long l = SectionPos.blockToSection(paramLong);
/*  24 */     int i = SectionPos.y(l);
/*  25 */     SkyDataLayerStorageMap skyDataLayerStorageMap = paramBoolean ? this.updatingSectionData : this.visibleSectionData;
/*  26 */     int j = skyDataLayerStorageMap.topSections.get(SectionPos.getZeroNode(l));
/*  27 */     if (j == skyDataLayerStorageMap.currentLowestY || i >= j) {
/*  28 */       if (paramBoolean && !lightOnInSection(l)) {
/*  29 */         return 0;
/*     */       }
/*  31 */       return 15;
/*     */     } 
/*  33 */     DataLayer dataLayer = getDataLayer(skyDataLayerStorageMap, l);
/*  34 */     if (dataLayer == null) {
/*  35 */       paramLong = BlockPos.getFlatIndex(paramLong);
/*  36 */       while (dataLayer == null) {
/*  37 */         i++;
/*  38 */         if (i >= j) {
/*  39 */           return 15;
/*     */         }
/*  41 */         l = SectionPos.offset(l, Direction.UP);
/*  42 */         dataLayer = getDataLayer(skyDataLayerStorageMap, l);
/*     */       } 
/*     */     } 
/*  45 */     return dataLayer.get(
/*  46 */         SectionPos.sectionRelative(BlockPos.getX(paramLong)), 
/*  47 */         SectionPos.sectionRelative(BlockPos.getY(paramLong)), 
/*  48 */         SectionPos.sectionRelative(BlockPos.getZ(paramLong)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNodeAdded(long paramLong) {
/*  54 */     int i = SectionPos.y(paramLong);
/*  55 */     if (this.updatingSectionData.currentLowestY > i) {
/*  56 */       this.updatingSectionData.currentLowestY = i;
/*  57 */       this.updatingSectionData.topSections.defaultReturnValue(this.updatingSectionData.currentLowestY);
/*     */     } 
/*  59 */     long l = SectionPos.getZeroNode(paramLong);
/*  60 */     int j = this.updatingSectionData.topSections.get(l);
/*  61 */     if (j < i + 1) {
/*  62 */       this.updatingSectionData.topSections.put(l, i + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNodeRemoved(long paramLong) {
/*  68 */     long l = SectionPos.getZeroNode(paramLong);
/*  69 */     int i = SectionPos.y(paramLong);
/*  70 */     if (this.updatingSectionData.topSections.get(l) == i + 1) {
/*  71 */       long l1 = paramLong;
/*  72 */       while (!storingLightForSection(l1) && hasLightDataAtOrBelow(i)) {
/*  73 */         i--;
/*  74 */         l1 = SectionPos.offset(l1, Direction.DOWN);
/*     */       } 
/*  76 */       if (storingLightForSection(l1)) {
/*  77 */         this.updatingSectionData.topSections.put(l, i + 1);
/*     */       } else {
/*  79 */         this.updatingSectionData.topSections.remove(l);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataLayer createDataLayer(long paramLong) {
/*  86 */     DataLayer dataLayer1 = (DataLayer)this.queuedSections.get(paramLong);
/*  87 */     if (dataLayer1 != null) {
/*  88 */       return dataLayer1;
/*     */     }
/*     */     
/*  91 */     int i = this.updatingSectionData.topSections.get(SectionPos.getZeroNode(paramLong));
/*     */     
/*  93 */     if (i == this.updatingSectionData.currentLowestY || SectionPos.y(paramLong) >= i) {
/*     */       
/*  95 */       if (lightOnInSection(paramLong)) {
/*  96 */         return new DataLayer(15);
/*     */       }
/*  98 */       return new DataLayer();
/*     */     } 
/*     */ 
/*     */     
/* 102 */     long l = SectionPos.offset(paramLong, Direction.UP);
/*     */     DataLayer dataLayer2;
/* 104 */     while ((dataLayer2 = getDataLayer(l, true)) == null) {
/* 105 */       l = SectionPos.offset(l, Direction.UP);
/*     */     }
/*     */     
/* 108 */     return repeatFirstLayer(dataLayer2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DataLayer repeatFirstLayer(DataLayer paramDataLayer) {
/* 113 */     if (paramDataLayer.isDefinitelyHomogenous()) {
/* 114 */       return paramDataLayer.copy();
/*     */     }
/* 116 */     byte[] arrayOfByte1 = paramDataLayer.getData();
/* 117 */     byte[] arrayOfByte2 = new byte[2048];
/*     */     
/* 119 */     for (byte b = 0; b < 16; b++) {
/* 120 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte2, b * 128, 128);
/*     */     }
/*     */     
/* 123 */     return new DataLayer(arrayOfByte2);
/*     */   }
/*     */   
/*     */   protected boolean hasLightDataAtOrBelow(int paramInt) {
/* 127 */     return (paramInt >= this.updatingSectionData.currentLowestY);
/*     */   }
/*     */   
/*     */   protected boolean isAboveData(long paramLong) {
/* 131 */     long l = SectionPos.getZeroNode(paramLong);
/* 132 */     int i = this.updatingSectionData.topSections.get(l);
/* 133 */     return (i == this.updatingSectionData.currentLowestY || SectionPos.y(paramLong) >= i);
/*     */   }
/*     */   
/*     */   protected int getTopSectionY(long paramLong) {
/* 137 */     return this.updatingSectionData.topSections.get(paramLong);
/*     */   }
/*     */   
/*     */   protected int getBottomSectionY() {
/* 141 */     return this.updatingSectionData.currentLowestY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class SkyDataLayerStorageMap
/*     */     extends DataLayerStorageMap<SkyDataLayerStorageMap>
/*     */   {
/*     */     int currentLowestY;
/*     */ 
/*     */     
/*     */     final Long2IntOpenHashMap topSections;
/*     */ 
/*     */     
/*     */     public SkyDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> param1Long2ObjectOpenHashMap, Long2IntOpenHashMap param1Long2IntOpenHashMap, int param1Int) {
/* 156 */       super(param1Long2ObjectOpenHashMap);
/* 157 */       this.topSections = param1Long2IntOpenHashMap;
/* 158 */       param1Long2IntOpenHashMap.defaultReturnValue(param1Int);
/* 159 */       this.currentLowestY = param1Int;
/*     */     }
/*     */ 
/*     */     
/*     */     public SkyDataLayerStorageMap copy() {
/* 164 */       return new SkyDataLayerStorageMap(this.map.clone(), this.topSections.clone(), this.currentLowestY);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\SkyLightSectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */