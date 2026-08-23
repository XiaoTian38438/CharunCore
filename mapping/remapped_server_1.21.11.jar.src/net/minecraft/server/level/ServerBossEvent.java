/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.BossEvent;
/*     */ 
/*     */ public class ServerBossEvent extends BossEvent {
/*  17 */   private final Set<ServerPlayer> players = Sets.newHashSet();
/*  18 */   private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);
/*     */   private boolean visible = true;
/*     */   
/*     */   public ServerBossEvent(Component paramComponent, BossEvent.BossBarColor paramBossBarColor, BossEvent.BossBarOverlay paramBossBarOverlay) {
/*  22 */     super(Mth.createInsecureUUID(), paramComponent, paramBossBarColor, paramBossBarOverlay);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProgress(float paramFloat) {
/*  27 */     if (paramFloat != this.progress) {
/*  28 */       super.setProgress(paramFloat);
/*  29 */       broadcast(ClientboundBossEventPacket::createUpdateProgressPacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(BossEvent.BossBarColor paramBossBarColor) {
/*  35 */     if (paramBossBarColor != this.color) {
/*  36 */       super.setColor(paramBossBarColor);
/*  37 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOverlay(BossEvent.BossBarOverlay paramBossBarOverlay) {
/*  43 */     if (paramBossBarOverlay != this.overlay) {
/*  44 */       super.setOverlay(paramBossBarOverlay);
/*  45 */       broadcast(ClientboundBossEventPacket::createUpdateStylePacket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setDarkenScreen(boolean paramBoolean) {
/*  51 */     if (paramBoolean != this.darkenScreen) {
/*  52 */       super.setDarkenScreen(paramBoolean);
/*  53 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setPlayBossMusic(boolean paramBoolean) {
/*  60 */     if (paramBoolean != this.playBossMusic) {
/*  61 */       super.setPlayBossMusic(paramBoolean);
/*  62 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BossEvent setCreateWorldFog(boolean paramBoolean) {
/*  69 */     if (paramBoolean != this.createWorldFog) {
/*  70 */       super.setCreateWorldFog(paramBoolean);
/*  71 */       broadcast(ClientboundBossEventPacket::createUpdatePropertiesPacket);
/*     */     } 
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(Component paramComponent) {
/*  78 */     if (!Objects.equal(paramComponent, this.name)) {
/*  79 */       super.setName(paramComponent);
/*  80 */       broadcast(ClientboundBossEventPacket::createUpdateNamePacket);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(Function<BossEvent, ClientboundBossEventPacket> paramFunction) {
/*  85 */     if (this.visible) {
/*  86 */       ClientboundBossEventPacket clientboundBossEventPacket = paramFunction.apply(this);
/*  87 */       for (ServerPlayer serverPlayer : this.players) {
/*  88 */         serverPlayer.connection.send((Packet)clientboundBossEventPacket);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPlayer(ServerPlayer paramServerPlayer) {
/*  94 */     if (this.players.add(paramServerPlayer) && this.visible) {
/*  95 */       paramServerPlayer.connection.send((Packet)ClientboundBossEventPacket.createAddPacket(this));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removePlayer(ServerPlayer paramServerPlayer) {
/* 100 */     if (this.players.remove(paramServerPlayer) && this.visible) {
/* 101 */       paramServerPlayer.connection.send((Packet)ClientboundBossEventPacket.createRemovePacket(getId()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeAllPlayers() {
/* 106 */     if (!this.players.isEmpty()) {
/* 107 */       for (ServerPlayer serverPlayer : Lists.newArrayList(this.players)) {
/* 108 */         removePlayer(serverPlayer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 114 */     return this.visible;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean paramBoolean) {
/* 118 */     if (paramBoolean != this.visible) {
/* 119 */       this.visible = paramBoolean;
/*     */       
/* 121 */       for (ServerPlayer serverPlayer : this.players) {
/* 122 */         serverPlayer.connection.send(paramBoolean ? (Packet)ClientboundBossEventPacket.createAddPacket(this) : (Packet)ClientboundBossEventPacket.createRemovePacket(getId()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<ServerPlayer> getPlayers() {
/* 128 */     return this.unmodifiablePlayers;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ServerBossEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */