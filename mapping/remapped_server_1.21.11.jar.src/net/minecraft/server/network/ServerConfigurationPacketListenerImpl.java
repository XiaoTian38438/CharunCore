/*     */ package net.minecraft.server.network;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*     */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.game.GameProtocols;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.network.config.JoinWorldTask;
/*     */ import net.minecraft.server.network.config.PrepareSpawnTask;
/*     */ import net.minecraft.server.network.config.ServerCodeOfConductConfigurationTask;
/*     */ import net.minecraft.server.network.config.ServerResourcePackConfigurationTask;
/*     */ import net.minecraft.server.network.config.SynchronizeRegistriesTask;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerConfigurationPacketListenerImpl extends ServerCommonPacketListenerImpl implements ServerConfigurationPacketListener, TickablePacketListener {
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  48 */   private static final Component DISCONNECT_REASON_INVALID_DATA = (Component)Component.translatable("multiplayer.disconnect.invalid_player_data");
/*  49 */   private static final Component DISCONNECT_REASON_CONFIGURATION_ERROR = (Component)Component.translatable("multiplayer.disconnect.configuration_error");
/*     */   
/*     */   private final GameProfile gameProfile;
/*  52 */   private final Queue<ConfigurationTask> configurationTasks = new ConcurrentLinkedQueue<>();
/*     */   private ConfigurationTask currentTask;
/*     */   private ClientInformation clientInformation;
/*     */   private SynchronizeRegistriesTask synchronizeRegistriesTask;
/*     */   private PrepareSpawnTask prepareSpawnTask;
/*     */   
/*     */   public ServerConfigurationPacketListenerImpl(MinecraftServer paramMinecraftServer, Connection paramConnection, CommonListenerCookie paramCommonListenerCookie) {
/*  59 */     super(paramMinecraftServer, paramConnection, paramCommonListenerCookie);
/*  60 */     this.gameProfile = paramCommonListenerCookie.gameProfile();
/*  61 */     this.clientInformation = paramCommonListenerCookie.clientInformation();
/*     */   }
/*     */ 
/*     */   
/*     */   protected GameProfile playerProfile() {
/*  66 */     return this.gameProfile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails paramDisconnectionDetails) {
/*  71 */     LOGGER.info("{} ({}) lost connection: {}", new Object[] { this.gameProfile.name(), this.gameProfile.id(), paramDisconnectionDetails.reason().getString() });
/*  72 */     if (this.prepareSpawnTask != null) {
/*  73 */       this.prepareSpawnTask.close();
/*  74 */       this.prepareSpawnTask = null;
/*     */     } 
/*  76 */     super.onDisconnect(paramDisconnectionDetails);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAcceptingMessages() {
/*  81 */     return this.connection.isConnected();
/*     */   }
/*     */   
/*     */   public void startConfiguration() {
/*  85 */     send((Packet<?>)new ClientboundCustomPayloadPacket((CustomPacketPayload)new BrandPayload(this.server.getServerModName())));
/*     */     
/*  87 */     ServerLinks serverLinks = this.server.serverLinks();
/*  88 */     if (!serverLinks.isEmpty()) {
/*  89 */       send((Packet<?>)new ClientboundServerLinksPacket(serverLinks.untrust()));
/*     */     }
/*     */     
/*  92 */     LayeredRegistryAccess layeredRegistryAccess = this.server.registries();
/*  93 */     List list = this.server.getResourceManager().listPacks().flatMap(paramPackResources -> paramPackResources.location().knownPackInfo().stream()).toList();
/*  94 */     send((Packet<?>)new ClientboundUpdateEnabledFeaturesPacket(FeatureFlags.REGISTRY.toNames(this.server.getWorldData().enabledFeatures())));
/*     */     
/*  96 */     this.synchronizeRegistriesTask = new SynchronizeRegistriesTask(list, layeredRegistryAccess);
/*  97 */     this.configurationTasks.add(this.synchronizeRegistriesTask);
/*     */     
/*  99 */     addOptionalTasks();
/*     */ 
/*     */     
/* 102 */     returnToWorld();
/*     */   }
/*     */   
/*     */   public void returnToWorld() {
/* 106 */     this.prepareSpawnTask = new PrepareSpawnTask(this.server, new NameAndId(this.gameProfile));
/* 107 */     this.configurationTasks.add(this.prepareSpawnTask);
/* 108 */     this.configurationTasks.add(new JoinWorldTask());
/* 109 */     startNextTask();
/*     */   }
/*     */   
/*     */   private void addOptionalTasks() {
/* 113 */     Map map = this.server.getCodeOfConducts();
/* 114 */     if (!map.isEmpty()) {
/* 115 */       this.configurationTasks.add(new ServerCodeOfConductConfigurationTask(() -> {
/*     */               String str = (String)paramMap.get(this.clientInformation.language().toLowerCase(Locale.ROOT));
/*     */               if (str == null) {
/*     */                 str = (String)paramMap.get("en_us");
/*     */               }
/*     */               if (str == null) {
/*     */                 str = paramMap.values().iterator().next();
/*     */               }
/*     */               return str;
/*     */             }));
/*     */     }
/* 126 */     this.server.getServerResourcePack().ifPresent(paramServerResourcePackInfo -> this.configurationTasks.add(new ServerResourcePackConfigurationTask(paramServerResourcePackInfo)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleClientInformation(ServerboundClientInformationPacket paramServerboundClientInformationPacket) {
/* 131 */     this.clientInformation = paramServerboundClientInformationPacket.information();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket) {
/* 136 */     super.handleResourcePackResponse(paramServerboundResourcePackPacket);
/*     */     
/* 138 */     if (paramServerboundResourcePackPacket.action().isTerminal()) {
/* 139 */       finishCurrentTask(ServerResourcePackConfigurationTask.TYPE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleSelectKnownPacks(ServerboundSelectKnownPacks paramServerboundSelectKnownPacks) {
/* 145 */     PacketUtils.ensureRunningOnSameThread((Packet)paramServerboundSelectKnownPacks, (PacketListener)this, this.server.packetProcessor());
/* 146 */     if (this.synchronizeRegistriesTask == null) {
/* 147 */       throw new IllegalStateException("Unexpected response from client: received pack selection, but no negotiation ongoing");
/*     */     }
/* 149 */     this.synchronizeRegistriesTask.handleResponse(paramServerboundSelectKnownPacks.knownPacks(), this::send);
/* 150 */     finishCurrentTask(SynchronizeRegistriesTask.TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket paramServerboundAcceptCodeOfConductPacket) {
/* 155 */     finishCurrentTask(ServerCodeOfConductConfigurationTask.TYPE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleConfigurationFinished(ServerboundFinishConfigurationPacket paramServerboundFinishConfigurationPacket) {
/* 160 */     PacketUtils.ensureRunningOnSameThread((Packet)paramServerboundFinishConfigurationPacket, (PacketListener)this, this.server.packetProcessor());
/* 161 */     finishCurrentTask(JoinWorldTask.TYPE);
/*     */     
/* 163 */     this.connection.setupOutboundProtocol(GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator((RegistryAccess)this.server.registryAccess())));
/*     */     try {
/* 165 */       PlayerList playerList = this.server.getPlayerList();
/* 166 */       if (playerList.getPlayer(this.gameProfile.id()) != null) {
/*     */ 
/*     */         
/* 169 */         disconnect(PlayerList.DUPLICATE_LOGIN_DISCONNECT_MESSAGE);
/*     */         return;
/*     */       } 
/* 172 */       Component component = playerList.canPlayerLogin(this.connection.getRemoteAddress(), new NameAndId(this.gameProfile));
/* 173 */       if (component != null) {
/* 174 */         disconnect(component);
/*     */         
/*     */         return;
/*     */       } 
/* 178 */       ((PrepareSpawnTask)Objects.<PrepareSpawnTask>requireNonNull(this.prepareSpawnTask)).spawnPlayer(this.connection, createCookie(this.clientInformation));
/* 179 */     } catch (Exception exception) {
/* 180 */       LOGGER.error("Couldn't place player in world", exception);
/* 181 */       disconnect(DISCONNECT_REASON_INVALID_DATA);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 187 */     keepConnectionAlive();
/*     */     
/* 189 */     ConfigurationTask configurationTask = this.currentTask;
/* 190 */     if (configurationTask != null) {
/*     */       try {
/* 192 */         if (configurationTask.tick()) {
/* 193 */           finishCurrentTask(configurationTask.type());
/*     */         }
/* 195 */       } catch (Exception exception) {
/* 196 */         LOGGER.error("Failed to tick configuration task {}", configurationTask.type(), exception);
/* 197 */         disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
/*     */       } 
/*     */     }
/*     */     
/* 201 */     if (this.prepareSpawnTask != null) {
/* 202 */       this.prepareSpawnTask.keepAlive();
/*     */     }
/*     */   }
/*     */   
/*     */   private void startNextTask() {
/* 207 */     if (this.currentTask != null) {
/* 208 */       throw new IllegalStateException("Task " + this.currentTask.type().id() + " has not finished yet");
/*     */     }
/*     */     
/* 211 */     if (!isAcceptingMessages()) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     ConfigurationTask configurationTask = this.configurationTasks.poll();
/* 216 */     if (configurationTask != null) {
/* 217 */       this.currentTask = configurationTask;
/*     */       try {
/* 219 */         configurationTask.start(this::send);
/* 220 */       } catch (Exception exception) {
/* 221 */         LOGGER.error("Failed to start configuration task {}", configurationTask.type(), exception);
/* 222 */         disconnect(DISCONNECT_REASON_CONFIGURATION_ERROR);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishCurrentTask(ConfigurationTask.Type paramType) {
/* 228 */     ConfigurationTask.Type type = (this.currentTask != null) ? this.currentTask.type() : null;
/* 229 */     if (!paramType.equals(type)) {
/* 230 */       throw new IllegalStateException("Unexpected request for task finish, current task: " + String.valueOf(type) + ", requested: " + String.valueOf(paramType));
/*     */     }
/* 232 */     this.currentTask = null;
/* 233 */     startNextTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerConfigurationPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */