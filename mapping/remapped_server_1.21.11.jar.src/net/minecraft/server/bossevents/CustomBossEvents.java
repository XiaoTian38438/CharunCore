/*    */ package net.minecraft.server.bossevents;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class CustomBossEvents
/*    */ {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 22 */   private static final Codec<Map<Identifier, CustomBossEvent.Packed>> EVENTS_CODEC = (Codec<Map<Identifier, CustomBossEvent.Packed>>)Codec.unboundedMap(Identifier.CODEC, CustomBossEvent.Packed.CODEC);
/*    */   
/* 24 */   private final Map<Identifier, CustomBossEvent> events = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomBossEvent get(Identifier paramIdentifier) {
/* 30 */     return this.events.get(paramIdentifier);
/*    */   }
/*    */   
/*    */   public CustomBossEvent create(Identifier paramIdentifier, Component paramComponent) {
/* 34 */     CustomBossEvent customBossEvent = new CustomBossEvent(paramIdentifier, paramComponent);
/* 35 */     this.events.put(paramIdentifier, customBossEvent);
/* 36 */     return customBossEvent;
/*    */   }
/*    */   
/*    */   public void remove(CustomBossEvent paramCustomBossEvent) {
/* 40 */     this.events.remove(paramCustomBossEvent.getTextId());
/*    */   }
/*    */   
/*    */   public Collection<Identifier> getIds() {
/* 44 */     return this.events.keySet();
/*    */   }
/*    */   
/*    */   public Collection<CustomBossEvent> getEvents() {
/* 48 */     return this.events.values();
/*    */   }
/*    */   
/*    */   public CompoundTag save(HolderLookup.Provider paramProvider) {
/* 52 */     Map map = Util.mapValues(this.events, CustomBossEvent::pack);
/* 53 */     return (CompoundTag)EVENTS_CODEC.encodeStart((DynamicOps)paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE), map).getOrThrow();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void load(CompoundTag paramCompoundTag, HolderLookup.Provider paramProvider) {
/* 59 */     Map map = EVENTS_CODEC.parse((DynamicOps)paramProvider.createSerializationContext((DynamicOps)NbtOps.INSTANCE), paramCompoundTag).resultOrPartial(paramString -> LOGGER.error("Failed to parse boss bar events: {}", paramString)).orElse(Map.of());
/* 60 */     map.forEach((paramIdentifier, paramPacked) -> this.events.put(paramIdentifier, CustomBossEvent.load(paramIdentifier, paramPacked)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onPlayerConnect(ServerPlayer paramServerPlayer) {
/* 66 */     for (CustomBossEvent customBossEvent : this.events.values()) {
/* 67 */       customBossEvent.onPlayerConnect(paramServerPlayer);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onPlayerDisconnect(ServerPlayer paramServerPlayer) {
/* 72 */     for (CustomBossEvent customBossEvent : this.events.values())
/* 73 */       customBossEvent.onPlayerDisconnect(paramServerPlayer); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\bossevents\CustomBossEvents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */