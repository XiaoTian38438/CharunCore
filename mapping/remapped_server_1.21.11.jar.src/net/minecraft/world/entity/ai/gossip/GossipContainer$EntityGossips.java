/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
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
/*    */ class EntityGossips
/*    */ {
/* 50 */   final Object2IntMap<GossipType> entries = (Object2IntMap<GossipType>)new Object2IntOpenHashMap();
/*    */   
/*    */   public int weightedValue(Predicate<GossipType> paramPredicate) {
/* 53 */     return this.entries.object2IntEntrySet()
/* 54 */       .stream()
/* 55 */       .filter(paramEntry -> paramPredicate.test((GossipType)paramEntry.getKey()))
/* 56 */       .mapToInt(paramEntry -> paramEntry.getIntValue() * ((GossipType)paramEntry.getKey()).weight)
/* 57 */       .sum();
/*    */   }
/*    */   
/*    */   public Stream<GossipContainer.GossipEntry> unpack(UUID paramUUID) {
/* 61 */     return this.entries.object2IntEntrySet().stream().map(paramEntry -> new GossipContainer.GossipEntry(paramUUID, (GossipType)paramEntry.getKey(), paramEntry.getIntValue()));
/*    */   }
/*    */   
/*    */   public void decay() {
/* 65 */     ObjectIterator objectIterator = this.entries.object2IntEntrySet().iterator();
/* 66 */     while (objectIterator.hasNext()) {
/* 67 */       Object2IntMap.Entry entry = (Object2IntMap.Entry)objectIterator.next();
/* 68 */       int i = entry.getIntValue() - ((GossipType)entry.getKey()).decayPerDay;
/* 69 */       if (i < 2) {
/* 70 */         objectIterator.remove(); continue;
/*    */       } 
/* 72 */       entry.setValue(i);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 78 */     return this.entries.isEmpty();
/*    */   }
/*    */   
/*    */   public void makeSureValueIsntTooLowOrTooHigh(GossipType paramGossipType) {
/* 82 */     int i = this.entries.getInt(paramGossipType);
/* 83 */     if (i > paramGossipType.max) {
/* 84 */       this.entries.put(paramGossipType, paramGossipType.max);
/*    */     }
/* 86 */     if (i < 2) {
/* 87 */       remove(paramGossipType);
/*    */     }
/*    */   }
/*    */   
/*    */   public void remove(GossipType paramGossipType) {
/* 92 */     this.entries.removeInt(paramGossipType);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer$EntityGossips.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */