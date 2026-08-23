/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.scores.DisplaySlot;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerScoreEntry;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.Score;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.ScoreboardSaveData;
/*     */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*     */ 
/*     */ public class ServerScoreboard extends Scoreboard {
/*     */   private final MinecraftServer server;
/*  30 */   private final Set<Objective> trackedObjectives = Sets.newHashSet();
/*     */   private boolean dirty;
/*     */   
/*     */   public ServerScoreboard(MinecraftServer paramMinecraftServer) {
/*  34 */     this.server = paramMinecraftServer;
/*     */   }
/*     */   
/*     */   public void load(ScoreboardSaveData.Packed paramPacked) {
/*  38 */     paramPacked.objectives().forEach(paramPacked -> paramServerScoreboard.loadObjective(paramPacked));
/*  39 */     paramPacked.scores().forEach(paramPackedScore -> paramServerScoreboard.loadPlayerScore(paramPackedScore));
/*  40 */     paramPacked.displaySlots().forEach((paramDisplaySlot, paramString) -> {
/*     */           Objective objective = getObjective(paramString);
/*     */           setDisplayObjective(paramDisplaySlot, objective);
/*     */         });
/*  44 */     paramPacked.teams().forEach(paramPacked -> paramServerScoreboard.loadPlayerTeam(paramPacked));
/*     */   }
/*     */   
/*     */   private ScoreboardSaveData.Packed store() {
/*  48 */     return new ScoreboardSaveData.Packed(packObjectives(), packPlayerScores(), packDisplaySlots(), packPlayerTeams());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onScoreChanged(ScoreHolder paramScoreHolder, Objective paramObjective, Score paramScore) {
/*  53 */     super.onScoreChanged(paramScoreHolder, paramObjective, paramScore);
/*     */     
/*  55 */     if (this.trackedObjectives.contains(paramObjective)) {
/*  56 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetScorePacket(paramScoreHolder.getScoreboardName(), paramObjective.getName(), paramScore.value(), Optional.ofNullable(paramScore.display()), Optional.ofNullable(paramScore.numberFormat())));
/*     */     }
/*     */     
/*  59 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onScoreLockChanged(ScoreHolder paramScoreHolder, Objective paramObjective) {
/*  64 */     super.onScoreLockChanged(paramScoreHolder, paramObjective);
/*     */     
/*  66 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerRemoved(ScoreHolder paramScoreHolder) {
/*  71 */     super.onPlayerRemoved(paramScoreHolder);
/*  72 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundResetScorePacket(paramScoreHolder.getScoreboardName(), null));
/*  73 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerScoreRemoved(ScoreHolder paramScoreHolder, Objective paramObjective) {
/*  78 */     super.onPlayerScoreRemoved(paramScoreHolder, paramObjective);
/*  79 */     if (this.trackedObjectives.contains(paramObjective)) {
/*  80 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundResetScorePacket(paramScoreHolder.getScoreboardName(), paramObjective.getName()));
/*     */     }
/*  82 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisplayObjective(DisplaySlot paramDisplaySlot, Objective paramObjective) {
/*  87 */     Objective objective = getDisplayObjective(paramDisplaySlot);
/*     */     
/*  89 */     super.setDisplayObjective(paramDisplaySlot, paramObjective);
/*     */     
/*  91 */     if (objective != paramObjective && objective != null) {
/*  92 */       if (getObjectiveDisplaySlotCount(objective) > 0) {
/*  93 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetDisplayObjectivePacket(paramDisplaySlot, paramObjective));
/*     */       } else {
/*  95 */         stopTrackingObjective(objective);
/*     */       } 
/*     */     }
/*     */     
/*  99 */     if (paramObjective != null) {
/* 100 */       if (this.trackedObjectives.contains(paramObjective)) {
/* 101 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetDisplayObjectivePacket(paramDisplaySlot, paramObjective));
/*     */       } else {
/* 103 */         startTrackingObjective(paramObjective);
/*     */       } 
/*     */     }
/*     */     
/* 107 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPlayerToTeam(String paramString, PlayerTeam paramPlayerTeam) {
/* 112 */     if (super.addPlayerToTeam(paramString, paramPlayerTeam)) {
/* 113 */       this.server.getPlayerList().broadcastAll((Packet)ClientboundSetPlayerTeamPacket.createPlayerPacket(paramPlayerTeam, paramString, ClientboundSetPlayerTeamPacket.Action.ADD));
/* 114 */       updatePlayerWaypoint(paramString);
/* 115 */       setDirty();
/*     */       
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String paramString, PlayerTeam paramPlayerTeam) {
/* 125 */     super.removePlayerFromTeam(paramString, paramPlayerTeam);
/*     */     
/* 127 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundSetPlayerTeamPacket.createPlayerPacket(paramPlayerTeam, paramString, ClientboundSetPlayerTeamPacket.Action.REMOVE));
/* 128 */     updatePlayerWaypoint(paramString);
/* 129 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveAdded(Objective paramObjective) {
/* 134 */     super.onObjectiveAdded(paramObjective);
/* 135 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveChanged(Objective paramObjective) {
/* 140 */     super.onObjectiveChanged(paramObjective);
/*     */     
/* 142 */     if (this.trackedObjectives.contains(paramObjective)) {
/* 143 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetObjectivePacket(paramObjective, 2));
/*     */     }
/*     */     
/* 146 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onObjectiveRemoved(Objective paramObjective) {
/* 151 */     super.onObjectiveRemoved(paramObjective);
/*     */     
/* 153 */     if (this.trackedObjectives.contains(paramObjective)) {
/* 154 */       stopTrackingObjective(paramObjective);
/*     */     }
/*     */     
/* 157 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamAdded(PlayerTeam paramPlayerTeam) {
/* 162 */     super.onTeamAdded(paramPlayerTeam);
/*     */     
/* 164 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(paramPlayerTeam, true));
/*     */     
/* 166 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamChanged(PlayerTeam paramPlayerTeam) {
/* 171 */     super.onTeamChanged(paramPlayerTeam);
/*     */     
/* 173 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(paramPlayerTeam, false));
/* 174 */     updateTeamWaypoints(paramPlayerTeam);
/* 175 */     setDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onTeamRemoved(PlayerTeam paramPlayerTeam) {
/* 180 */     super.onTeamRemoved(paramPlayerTeam);
/*     */     
/* 182 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundSetPlayerTeamPacket.createRemovePacket(paramPlayerTeam));
/* 183 */     updateTeamWaypoints(paramPlayerTeam);
/* 184 */     setDirty();
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/* 188 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void storeToSaveDataIfDirty(ScoreboardSaveData paramScoreboardSaveData) {
/* 192 */     if (this.dirty) {
/* 193 */       this.dirty = false;
/* 194 */       paramScoreboardSaveData.setData(store());
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStartTrackingPackets(Objective paramObjective) {
/* 199 */     ArrayList<ClientboundSetObjectivePacket> arrayList = Lists.newArrayList();
/* 200 */     arrayList.add(new ClientboundSetObjectivePacket(paramObjective, 0));
/*     */     
/* 202 */     for (DisplaySlot displaySlot : DisplaySlot.values()) {
/* 203 */       if (getDisplayObjective(displaySlot) == paramObjective) {
/* 204 */         arrayList.add(new ClientboundSetDisplayObjectivePacket(displaySlot, paramObjective));
/*     */       }
/*     */     } 
/*     */     
/* 208 */     for (PlayerScoreEntry playerScoreEntry : listPlayerScores(paramObjective)) {
/* 209 */       arrayList.add(new ClientboundSetScorePacket(playerScoreEntry.owner(), paramObjective.getName(), playerScoreEntry.value(), Optional.ofNullable(playerScoreEntry.display()), Optional.ofNullable(playerScoreEntry.numberFormatOverride())));
/*     */     }
/*     */     
/* 212 */     return (List)arrayList;
/*     */   }
/*     */   
/*     */   public void startTrackingObjective(Objective paramObjective) {
/* 216 */     List<Packet<?>> list = getStartTrackingPackets(paramObjective);
/*     */     
/* 218 */     for (ServerPlayer serverPlayer : this.server.getPlayerList().getPlayers()) {
/* 219 */       for (Packet<?> packet : list) {
/* 220 */         serverPlayer.connection.send(packet);
/*     */       }
/*     */     } 
/*     */     
/* 224 */     this.trackedObjectives.add(paramObjective);
/*     */   }
/*     */   
/*     */   public List<Packet<?>> getStopTrackingPackets(Objective paramObjective) {
/* 228 */     ArrayList<ClientboundSetObjectivePacket> arrayList = Lists.newArrayList();
/* 229 */     arrayList.add(new ClientboundSetObjectivePacket(paramObjective, 1));
/*     */     
/* 231 */     for (DisplaySlot displaySlot : DisplaySlot.values()) {
/* 232 */       if (getDisplayObjective(displaySlot) == paramObjective) {
/* 233 */         arrayList.add(new ClientboundSetDisplayObjectivePacket(displaySlot, paramObjective));
/*     */       }
/*     */     } 
/*     */     
/* 237 */     return (List)arrayList;
/*     */   }
/*     */   
/*     */   public void stopTrackingObjective(Objective paramObjective) {
/* 241 */     List<Packet<?>> list = getStopTrackingPackets(paramObjective);
/*     */     
/* 243 */     for (ServerPlayer serverPlayer : this.server.getPlayerList().getPlayers()) {
/* 244 */       for (Packet<?> packet : list) {
/* 245 */         serverPlayer.connection.send(packet);
/*     */       }
/*     */     } 
/*     */     
/* 249 */     this.trackedObjectives.remove(paramObjective);
/*     */   }
/*     */   
/*     */   public int getObjectiveDisplaySlotCount(Objective paramObjective) {
/* 253 */     byte b = 0;
/*     */     
/* 255 */     for (DisplaySlot displaySlot : DisplaySlot.values()) {
/* 256 */       if (getDisplayObjective(displaySlot) == paramObjective) {
/* 257 */         b++;
/*     */       }
/*     */     } 
/*     */     
/* 261 */     return b;
/*     */   }
/*     */   
/*     */   private void updatePlayerWaypoint(String paramString) {
/* 265 */     ServerPlayer serverPlayer = this.server.getPlayerList().getPlayerByName(paramString);
/* 266 */     if (serverPlayer != null) {
/* 267 */       serverPlayer.level().getWaypointManager().remakeConnections((WaypointTransmitter)serverPlayer);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateTeamWaypoints(PlayerTeam paramPlayerTeam) {
/* 272 */     for (ServerLevel serverLevel : this.server.getAllLevels())
/* 273 */       paramPlayerTeam.getPlayers().stream()
/* 274 */         .map(paramString -> this.server.getPlayerList().getPlayerByName(paramString))
/* 275 */         .filter(Objects::nonNull)
/* 276 */         .forEach(paramServerPlayer -> paramServerLevel.getWaypointManager().remakeConnections((WaypointTransmitter)paramServerPlayer)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ServerScoreboard.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */