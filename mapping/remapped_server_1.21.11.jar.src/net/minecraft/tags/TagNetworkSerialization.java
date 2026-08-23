/*    */ package net.minecraft.tags;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ 
/*    */ public class TagNetworkSerialization {
/*    */   public static Map<ResourceKey<? extends Registry<?>>, NetworkPayload> serializeTagsToNetwork(LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess) {
/* 23 */     return (Map<ResourceKey<? extends Registry<?>>, NetworkPayload>)RegistrySynchronization.networkSafeRegistries(paramLayeredRegistryAccess)
/* 24 */       .map(paramRegistryEntry -> Pair.of(paramRegistryEntry.key(), serializeToNetwork(paramRegistryEntry.value())))
/* 25 */       .filter(paramPair -> !((NetworkPayload)paramPair.getSecond()).isEmpty())
/* 26 */       .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
/*    */   }
/*    */   
/*    */   private static <T> NetworkPayload serializeToNetwork(Registry<T> paramRegistry) {
/* 30 */     HashMap<Object, Object> hashMap = new HashMap<>();
/* 31 */     paramRegistry.getTags().forEach(paramNamed -> {
/*    */           IntArrayList intArrayList = new IntArrayList(paramNamed.size());
/*    */           for (Holder holder : paramNamed) {
/*    */             if (holder.kind() != Holder.Kind.REFERENCE) {
/*    */               throw new IllegalStateException("Can't serialize unregistered value " + String.valueOf(holder));
/*    */             }
/*    */             intArrayList.add(paramRegistry.getId(holder.value()));
/*    */           } 
/*    */           paramMap.put(paramNamed.key().location(), intArrayList);
/*    */         });
/* 41 */     return new NetworkPayload((Map)hashMap);
/*    */   }
/*    */   
/*    */   static <T> TagLoader.LoadResult<T> deserializeTagsFromNetwork(Registry<T> paramRegistry, NetworkPayload paramNetworkPayload) {
/* 45 */     ResourceKey<? extends Registry<T>> resourceKey = paramRegistry.key();
/* 46 */     HashMap<Object, Object> hashMap = new HashMap<>();
/*    */     
/* 48 */     paramNetworkPayload.tags.forEach((paramIdentifier, paramIntList) -> {
/*    */           TagKey<?> tagKey = TagKey.create(paramResourceKey, paramIdentifier);
/*    */           Objects.requireNonNull(paramRegistry);
/*    */           List list = (List)paramIntList.intStream().mapToObj(paramRegistry::get).flatMap(Optional::stream).collect(Collectors.toUnmodifiableList());
/*    */           paramMap.put(tagKey, list);
/*    */         });
/* 54 */     return new TagLoader.LoadResult<>(resourceKey, (Map)hashMap);
/*    */   }
/*    */   
/*    */   public static final class NetworkPayload {
/* 58 */     public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */     
/*    */     final Map<Identifier, IntList> tags;
/*    */     
/*    */     NetworkPayload(Map<Identifier, IntList> param1Map) {
/* 63 */       this.tags = param1Map;
/*    */     }
/*    */     
/*    */     public void write(FriendlyByteBuf param1FriendlyByteBuf) {
/* 67 */       param1FriendlyByteBuf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList);
/*    */     }
/*    */     
/*    */     public static NetworkPayload read(FriendlyByteBuf param1FriendlyByteBuf) {
/* 71 */       return new NetworkPayload(param1FriendlyByteBuf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList));
/*    */     }
/*    */     
/*    */     public boolean isEmpty() {
/* 75 */       return this.tags.isEmpty();
/*    */     }
/*    */     
/*    */     public int size() {
/* 79 */       return this.tags.size();
/*    */     }
/*    */     
/*    */     public <T> TagLoader.LoadResult<T> resolve(Registry<T> param1Registry) {
/* 83 */       return TagNetworkSerialization.deserializeTagsFromNetwork(param1Registry, this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\TagNetworkSerialization.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */