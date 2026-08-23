/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*    */ 
/*    */ public class BlockLightSectionStorage extends LayerLightSectionStorage<BlockLightSectionStorage.BlockDataLayerStorageMap> {
/*    */   protected BlockLightSectionStorage(LightChunkGetter paramLightChunkGetter) {
/* 12 */     super(LightLayer.BLOCK, paramLightChunkGetter, new BlockDataLayerStorageMap(new Long2ObjectOpenHashMap()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLightValue(long paramLong) {
/* 17 */     long l = SectionPos.blockToSection(paramLong);
/* 18 */     DataLayer dataLayer = getDataLayer(l, false);
/* 19 */     if (dataLayer == null) {
/* 20 */       return 0;
/*    */     }
/* 22 */     return dataLayer.get(
/* 23 */         SectionPos.sectionRelative(BlockPos.getX(paramLong)), 
/* 24 */         SectionPos.sectionRelative(BlockPos.getY(paramLong)), 
/* 25 */         SectionPos.sectionRelative(BlockPos.getZ(paramLong)));
/*    */   }
/*    */   
/*    */   protected static final class BlockDataLayerStorageMap
/*    */     extends DataLayerStorageMap<BlockDataLayerStorageMap> {
/*    */     public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> param1Long2ObjectOpenHashMap) {
/* 31 */       super(param1Long2ObjectOpenHashMap);
/*    */     }
/*    */ 
/*    */     
/*    */     public BlockDataLayerStorageMap copy() {
/* 36 */       return new BlockDataLayerStorageMap(this.map.clone());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */