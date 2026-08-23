/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public class LevelLightEngine
/*     */   implements LightEventListener
/*     */ {
/*     */   public static final int LIGHT_SECTION_PADDING = 1;
/*  15 */   public static final LevelLightEngine EMPTY = new LevelLightEngine();
/*     */   
/*     */   protected final LevelHeightAccessor levelHeightAccessor;
/*     */   private final LightEngine<?, ?> blockEngine;
/*     */   private final LightEngine<?, ?> skyEngine;
/*     */   
/*     */   public LevelLightEngine(LightChunkGetter paramLightChunkGetter, boolean paramBoolean1, boolean paramBoolean2) {
/*  22 */     this.levelHeightAccessor = (LevelHeightAccessor)paramLightChunkGetter.getLevel();
/*  23 */     this.blockEngine = paramBoolean1 ? new BlockLightEngine(paramLightChunkGetter) : null;
/*  24 */     this.skyEngine = paramBoolean2 ? new SkyLightEngine(paramLightChunkGetter) : null;
/*     */   }
/*     */   
/*     */   private LevelLightEngine() {
/*  28 */     this.levelHeightAccessor = LevelHeightAccessor.create(0, 0);
/*  29 */     this.blockEngine = null;
/*  30 */     this.skyEngine = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos paramBlockPos) {
/*  36 */     if (this.blockEngine != null) {
/*  37 */       this.blockEngine.checkBlock(paramBlockPos);
/*     */     }
/*  39 */     if (this.skyEngine != null) {
/*  40 */       this.skyEngine.checkBlock(paramBlockPos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/*  47 */     if (this.skyEngine != null && this.skyEngine.hasLightWork()) {
/*  48 */       return true;
/*     */     }
/*  50 */     return (this.blockEngine != null && this.blockEngine.hasLightWork());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/*  56 */     int i = 0;
/*  57 */     if (this.blockEngine != null) {
/*  58 */       i += this.blockEngine.runLightUpdates();
/*     */     }
/*  60 */     if (this.skyEngine != null) {
/*  61 */       i += this.skyEngine.runLightUpdates();
/*     */     }
/*  63 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos paramSectionPos, boolean paramBoolean) {
/*  72 */     if (this.blockEngine != null) {
/*  73 */       this.blockEngine.updateSectionStatus(paramSectionPos, paramBoolean);
/*     */     }
/*  75 */     if (this.skyEngine != null) {
/*  76 */       this.skyEngine.updateSectionStatus(paramSectionPos, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos paramChunkPos, boolean paramBoolean) {
/*  83 */     if (this.blockEngine != null) {
/*  84 */       this.blockEngine.setLightEnabled(paramChunkPos, paramBoolean);
/*     */     }
/*  86 */     if (this.skyEngine != null) {
/*  87 */       this.skyEngine.setLightEnabled(paramChunkPos, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void propagateLightSources(ChunkPos paramChunkPos) {
/*  94 */     if (this.blockEngine != null) {
/*  95 */       this.blockEngine.propagateLightSources(paramChunkPos);
/*     */     }
/*  97 */     if (this.skyEngine != null) {
/*  98 */       this.skyEngine.propagateLightSources(paramChunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   public LayerLightEventListener getLayerListener(LightLayer paramLightLayer) {
/* 103 */     if (paramLightLayer == LightLayer.BLOCK) {
/* 104 */       if (this.blockEngine == null) {
/* 105 */         return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */       }
/* 107 */       return this.blockEngine;
/*     */     } 
/* 109 */     if (this.skyEngine == null) {
/* 110 */       return LayerLightEventListener.DummyLightLayerEventListener.INSTANCE;
/*     */     }
/* 112 */     return this.skyEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDebugData(LightLayer paramLightLayer, SectionPos paramSectionPos) {
/* 117 */     if (paramLightLayer == LightLayer.BLOCK) {
/* 118 */       if (this.blockEngine != null) {
/* 119 */         return this.blockEngine.getDebugData(paramSectionPos.asLong());
/*     */       }
/*     */     }
/* 122 */     else if (this.skyEngine != null) {
/* 123 */       return this.skyEngine.getDebugData(paramSectionPos.asLong());
/*     */     } 
/*     */     
/* 126 */     return "n/a";
/*     */   }
/*     */   
/*     */   public LayerLightSectionStorage.SectionType getDebugSectionType(LightLayer paramLightLayer, SectionPos paramSectionPos) {
/* 130 */     if (paramLightLayer == LightLayer.BLOCK) {
/* 131 */       if (this.blockEngine != null) {
/* 132 */         return this.blockEngine.getDebugSectionType(paramSectionPos.asLong());
/*     */       }
/*     */     }
/* 135 */     else if (this.skyEngine != null) {
/* 136 */       return this.skyEngine.getDebugSectionType(paramSectionPos.asLong());
/*     */     } 
/*     */     
/* 139 */     return LayerLightSectionStorage.SectionType.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueSectionData(LightLayer paramLightLayer, SectionPos paramSectionPos, DataLayer paramDataLayer) {
/* 147 */     if (paramLightLayer == LightLayer.BLOCK) {
/* 148 */       if (this.blockEngine != null) {
/* 149 */         this.blockEngine.queueSectionData(paramSectionPos.asLong(), paramDataLayer);
/*     */       }
/*     */     }
/* 152 */     else if (this.skyEngine != null) {
/* 153 */       this.skyEngine.queueSectionData(paramSectionPos.asLong(), paramDataLayer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void retainData(ChunkPos paramChunkPos, boolean paramBoolean) {
/* 159 */     if (this.blockEngine != null) {
/* 160 */       this.blockEngine.retainData(paramChunkPos, paramBoolean);
/*     */     }
/* 162 */     if (this.skyEngine != null) {
/* 163 */       this.skyEngine.retainData(paramChunkPos, paramBoolean);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRawBrightness(BlockPos paramBlockPos, int paramInt) {
/* 168 */     boolean bool1 = (this.skyEngine == null) ? false : (this.skyEngine.getLightValue(paramBlockPos) - paramInt);
/* 169 */     boolean bool2 = (this.blockEngine == null) ? false : this.blockEngine.getLightValue(paramBlockPos);
/*     */     
/* 171 */     return Math.max(bool2, bool1);
/*     */   }
/*     */   
/*     */   public boolean lightOnInColumn(long paramLong) {
/* 175 */     return (this.blockEngine == null || (this.blockEngine.storage.lightOnInColumn(paramLong) && (this.skyEngine == null || this.skyEngine.storage
/* 176 */       .lightOnInColumn(paramLong))));
/*     */   }
/*     */   
/*     */   public int getLightSectionCount() {
/* 180 */     return this.levelHeightAccessor.getSectionsCount() + 2;
/*     */   }
/*     */   
/*     */   public int getMinLightSection() {
/* 184 */     return this.levelHeightAccessor.getMinSectionY() - 1;
/*     */   }
/*     */   
/*     */   public int getMaxLightSection() {
/* 188 */     return getMinLightSection() + getLightSectionCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\LevelLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */