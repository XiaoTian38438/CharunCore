/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ChunkMap;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalMobCapCalculator
/*    */ {
/* 19 */   private final Long2ObjectMap<List<ServerPlayer>> playersNearChunk = (Long2ObjectMap<List<ServerPlayer>>)new Long2ObjectOpenHashMap();
/* 20 */   private final Map<ServerPlayer, MobCounts> playerMobCounts = Maps.newHashMap();
/*    */   private final ChunkMap chunkMap;
/*    */   
/*    */   public LocalMobCapCalculator(ChunkMap paramChunkMap) {
/* 24 */     this.chunkMap = paramChunkMap;
/*    */   }
/*    */   
/*    */   private List<ServerPlayer> getPlayersNear(ChunkPos paramChunkPos) {
/* 28 */     return (List<ServerPlayer>)this.playersNearChunk.computeIfAbsent(paramChunkPos.toLong(), paramLong -> this.chunkMap.getPlayersCloseForSpawning(paramChunkPos));
/*    */   }
/*    */   
/*    */   public void addMob(ChunkPos paramChunkPos, MobCategory paramMobCategory) {
/* 32 */     for (ServerPlayer serverPlayer : getPlayersNear(paramChunkPos)) {
/* 33 */       ((MobCounts)this.playerMobCounts.computeIfAbsent(serverPlayer, paramServerPlayer -> new MobCounts())).add(paramMobCategory);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canSpawn(MobCategory paramMobCategory, ChunkPos paramChunkPos) {
/* 38 */     for (ServerPlayer serverPlayer : getPlayersNear(paramChunkPos)) {
/* 39 */       MobCounts mobCounts = this.playerMobCounts.get(serverPlayer);
/* 40 */       if (mobCounts == null || mobCounts.canSpawn(paramMobCategory)) {
/* 41 */         return true;
/*    */       }
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   private static class MobCounts {
/* 48 */     private final Object2IntMap<MobCategory> counts = (Object2IntMap<MobCategory>)new Object2IntOpenHashMap((MobCategory.values()).length);
/*    */     
/*    */     public void add(MobCategory param1MobCategory) {
/* 51 */       this.counts.computeInt(param1MobCategory, (param1MobCategory, param1Integer) -> Integer.valueOf((param1Integer == null) ? 1 : (param1Integer.intValue() + 1)));
/*    */     }
/*    */     
/*    */     public boolean canSpawn(MobCategory param1MobCategory) {
/* 55 */       return (this.counts.getOrDefault(param1MobCategory, 0) < param1MobCategory.getMaxInstancesPerChunk());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LocalMobCapCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */