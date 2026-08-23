/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BlockDataLayerStorageMap
/*    */   extends DataLayerStorageMap<BlockLightSectionStorage.BlockDataLayerStorageMap>
/*    */ {
/*    */   public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> paramLong2ObjectOpenHashMap) {
/* 31 */     super(paramLong2ObjectOpenHashMap);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockDataLayerStorageMap copy() {
/* 36 */     return new BlockDataLayerStorageMap(this.map.clone());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\BlockLightSectionStorage$BlockDataLayerStorageMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */