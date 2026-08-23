/*    */ package net.minecraft.server.network.config;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.RegistrySynchronization;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundSelectKnownPacks;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.RegistryLayer;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ import net.minecraft.tags.TagNetworkSerialization;
/*    */ 
/*    */ public class SynchronizeRegistriesTask implements ConfigurationTask {
/* 22 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("synchronize_registries");
/*    */   
/*    */   private final List<KnownPack> requestedPacks;
/*    */   private final LayeredRegistryAccess<RegistryLayer> registries;
/*    */   
/*    */   public SynchronizeRegistriesTask(List<KnownPack> paramList, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess) {
/* 28 */     this.requestedPacks = paramList;
/* 29 */     this.registries = paramLayeredRegistryAccess;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> paramConsumer) {
/* 34 */     paramConsumer.accept(new ClientboundSelectKnownPacks(this.requestedPacks));
/*    */   }
/*    */   
/*    */   private void sendRegistries(Consumer<Packet<?>> paramConsumer, Set<KnownPack> paramSet) {
/* 38 */     RegistryOps registryOps = this.registries.compositeAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 39 */     RegistrySynchronization.packRegistries((DynamicOps)registryOps, (RegistryAccess)this.registries.getAccessFrom(RegistryLayer.WORLDGEN), paramSet, (paramResourceKey, paramList) -> paramConsumer.accept(new ClientboundRegistryDataPacket(paramResourceKey, paramList)));
/*    */ 
/*    */     
/* 42 */     paramConsumer.accept(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(this.registries)));
/*    */   }
/*    */   
/*    */   public void handleResponse(List<KnownPack> paramList, Consumer<Packet<?>> paramConsumer) {
/* 46 */     if (paramList.equals(this.requestedPacks)) {
/* 47 */       sendRegistries(paramConsumer, Set.copyOf(this.requestedPacks));
/*    */     } else {
/*    */       
/* 50 */       sendRegistries(paramConsumer, Set.of());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 56 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\config\SynchronizeRegistriesTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */