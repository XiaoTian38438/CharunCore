/*     */ package net.minecraft.server.network;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ClientInformation;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ServerCommonPacketListenerImpl implements ServerCommonPacketListener {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public static final int LATENCY_CHECK_INTERVAL = 15000;
/*     */   private static final int CLOSED_LISTENER_TIMEOUT = 15000;
/*  37 */   private static final Component TIMEOUT_DISCONNECTION_MESSAGE = (Component)Component.translatable("disconnect.timeout");
/*  38 */   static final Component DISCONNECT_UNEXPECTED_QUERY = (Component)Component.translatable("multiplayer.disconnect.unexpected_query_response");
/*     */   
/*     */   protected final MinecraftServer server;
/*     */   
/*     */   protected final Connection connection;
/*     */   private final boolean transferred;
/*     */   private long keepAliveTime;
/*     */   private boolean keepAlivePending;
/*     */   private long keepAliveChallenge;
/*     */   private long closedListenerTime;
/*     */   private boolean closed = false;
/*     */   private int latency;
/*     */   private volatile boolean suspendFlushingOnServerThread = false;
/*     */   
/*     */   public ServerCommonPacketListenerImpl(MinecraftServer paramMinecraftServer, Connection paramConnection, CommonListenerCookie paramCommonListenerCookie) {
/*  53 */     this.server = paramMinecraftServer;
/*  54 */     this.connection = paramConnection;
/*  55 */     this.keepAliveTime = Util.getMillis();
/*  56 */     this.latency = paramCommonListenerCookie.latency();
/*  57 */     this.transferred = paramCommonListenerCookie.transferred();
/*     */   }
/*     */   
/*     */   private void close() {
/*  61 */     if (!this.closed) {
/*  62 */       this.closedListenerTime = Util.getMillis();
/*  63 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails paramDisconnectionDetails) {
/*  69 */     if (isSingleplayerOwner()) {
/*  70 */       LOGGER.info("Stopping singleplayer server as player logged out");
/*  71 */       this.server.halt(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPacketError(Packet paramPacket, Exception paramException) throws ReportedException {
/*  77 */     super.onPacketError(paramPacket, paramException);
/*  78 */     this.server.reportPacketHandlingException(paramException, paramPacket.type());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKeepAlive(ServerboundKeepAlivePacket paramServerboundKeepAlivePacket) {
/*  83 */     if (this.keepAlivePending && paramServerboundKeepAlivePacket.getId() == this.keepAliveChallenge) {
/*  84 */       int i = (int)(Util.getMillis() - this.keepAliveTime);
/*  85 */       this.latency = (this.latency * 3 + i) / 4;
/*  86 */       this.keepAlivePending = false;
/*     */     }
/*  88 */     else if (!isSingleplayerOwner()) {
/*  89 */       disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePong(ServerboundPongPacket paramServerboundPongPacket) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleCustomPayload(ServerboundCustomPayloadPacket paramServerboundCustomPayloadPacket) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleCustomClickAction(ServerboundCustomClickActionPacket paramServerboundCustomClickActionPacket) {
/* 104 */     PacketUtils.ensureRunningOnSameThread((Packet)paramServerboundCustomClickActionPacket, (PacketListener)this, this.server.packetProcessor());
/* 105 */     this.server.handleCustomClickAction(paramServerboundCustomClickActionPacket.id(), paramServerboundCustomClickActionPacket.payload());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket) {
/* 110 */     PacketUtils.ensureRunningOnSameThread((Packet)paramServerboundResourcePackPacket, (PacketListener)this, this.server.packetProcessor());
/* 111 */     if (paramServerboundResourcePackPacket.action() == ServerboundResourcePackPacket.Action.DECLINED && this.server.isResourcePackRequired()) {
/* 112 */       LOGGER.info("Disconnecting {} due to resource pack {} rejection", playerProfile().name(), paramServerboundResourcePackPacket.id());
/* 113 */       disconnect((Component)Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCookieResponse(ServerboundCookieResponsePacket paramServerboundCookieResponsePacket) {
/* 119 */     disconnect(DISCONNECT_UNEXPECTED_QUERY);
/*     */   }
/*     */   
/*     */   protected void keepConnectionAlive() {
/* 123 */     Profiler.get().push("keepAlive");
/* 124 */     long l = Util.getMillis();
/* 125 */     if (!isSingleplayerOwner() && l - this.keepAliveTime >= 15000L) {
/* 126 */       if (this.keepAlivePending) {
/* 127 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/* 128 */       } else if (checkIfClosed(l)) {
/* 129 */         this.keepAlivePending = true;
/* 130 */         this.keepAliveTime = l;
/* 131 */         this.keepAliveChallenge = l;
/* 132 */         send((Packet<?>)new ClientboundKeepAlivePacket(this.keepAliveChallenge));
/*     */       } 
/*     */     }
/* 135 */     Profiler.get().pop();
/*     */   }
/*     */   
/*     */   private boolean checkIfClosed(long paramLong) {
/* 139 */     if (this.closed) {
/* 140 */       if (paramLong - this.closedListenerTime >= 15000L) {
/* 141 */         disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
/*     */       }
/* 143 */       return false;
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public void suspendFlushing() {
/* 149 */     this.suspendFlushingOnServerThread = true;
/*     */   }
/*     */   
/*     */   public void resumeFlushing() {
/* 153 */     this.suspendFlushingOnServerThread = false;
/* 154 */     this.connection.flushChannel();
/*     */   }
/*     */   
/*     */   public void send(Packet<?> paramPacket) {
/* 158 */     send(paramPacket, null);
/*     */   }
/*     */   
/*     */   public void send(Packet<?> paramPacket, ChannelFutureListener paramChannelFutureListener) {
/* 162 */     if (paramPacket.isTerminal()) {
/* 163 */       close();
/*     */     }
/* 165 */     boolean bool = (!this.suspendFlushingOnServerThread || !this.server.isSameThread()) ? true : false;
/*     */     try {
/* 167 */       this.connection.send(paramPacket, paramChannelFutureListener, bool);
/* 168 */     } catch (Throwable throwable) {
/* 169 */       CrashReport crashReport = CrashReport.forThrowable(throwable, "Sending packet");
/* 170 */       CrashReportCategory crashReportCategory = crashReport.addCategory("Packet being sent");
/*     */       
/* 172 */       crashReportCategory.setDetail("Packet class", () -> paramPacket.getClass().getCanonicalName());
/*     */       
/* 174 */       throw new ReportedException(crashReport);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnect(Component paramComponent) {
/* 179 */     disconnect(new DisconnectionDetails(paramComponent));
/*     */   }
/*     */   
/*     */   public void disconnect(DisconnectionDetails paramDisconnectionDetails) {
/* 183 */     this.connection.send((Packet)new ClientboundDisconnectPacket(paramDisconnectionDetails.reason()), PacketSendListener.thenRun(() -> this.connection.disconnect(paramDisconnectionDetails)));
/* 184 */     this.connection.setReadOnly();
/* 185 */     Objects.requireNonNull(this.connection); this.server.executeBlocking(this.connection::handleDisconnection);
/*     */   }
/*     */   
/*     */   protected boolean isSingleplayerOwner() {
/* 189 */     return this.server.isSingleplayerOwner(new NameAndId(playerProfile()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public GameProfile getOwner() {
/* 196 */     return playerProfile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int latency() {
/* 203 */     return this.latency;
/*     */   }
/*     */   
/*     */   protected CommonListenerCookie createCookie(ClientInformation paramClientInformation) {
/* 207 */     return new CommonListenerCookie(
/* 208 */         playerProfile(), this.latency, paramClientInformation, this.transferred);
/*     */   }
/*     */   
/*     */   protected abstract GameProfile playerProfile();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\ServerCommonPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */