/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.resources.Identifier;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NetworkPayload
/*    */ {
/* 58 */   public static final NetworkPayload EMPTY = new NetworkPayload(Map.of());
/*    */   
/*    */   final Map<Identifier, IntList> tags;
/*    */   
/*    */   NetworkPayload(Map<Identifier, IntList> paramMap) {
/* 63 */     this.tags = paramMap;
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf paramFriendlyByteBuf) {
/* 67 */     paramFriendlyByteBuf.writeMap(this.tags, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList);
/*    */   }
/*    */   
/*    */   public static NetworkPayload read(FriendlyByteBuf paramFriendlyByteBuf) {
/* 71 */     return new NetworkPayload(paramFriendlyByteBuf.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList));
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 75 */     return this.tags.isEmpty();
/*    */   }
/*    */   
/*    */   public int size() {
/* 79 */     return this.tags.size();
/*    */   }
/*    */   
/*    */   public <T> TagLoader.LoadResult<T> resolve(Registry<T> paramRegistry) {
/* 83 */     return TagNetworkSerialization.deserializeTagsFromNetwork(paramRegistry, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\tags\TagNetworkSerialization$NetworkPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */