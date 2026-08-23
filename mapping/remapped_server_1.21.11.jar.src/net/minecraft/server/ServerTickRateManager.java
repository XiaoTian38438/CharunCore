/*     */ package net.minecraft.server;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundTickingStepPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.TickRateManager;
/*     */ 
/*     */ public class ServerTickRateManager extends TickRateManager {
/*  13 */   private long remainingSprintTicks = 0L;
/*  14 */   private long sprintTickStartTime = 0L;
/*  15 */   private long sprintTimeSpend = 0L;
/*  16 */   private long scheduledCurrentSprintTicks = 0L;
/*     */   private boolean previousIsFrozen = false;
/*     */   private final MinecraftServer server;
/*     */   
/*     */   public ServerTickRateManager(MinecraftServer paramMinecraftServer) {
/*  21 */     this.server = paramMinecraftServer;
/*     */   }
/*     */   
/*     */   public boolean isSprinting() {
/*  25 */     return (this.scheduledCurrentSprintTicks > 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean paramBoolean) {
/*  30 */     super.setFrozen(paramBoolean);
/*  31 */     updateStateToClients();
/*     */   }
/*     */   
/*     */   private void updateStateToClients() {
/*  35 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundTickingStatePacket.from(this));
/*     */   }
/*     */   
/*     */   private void updateStepTicks() {
/*  39 */     this.server.getPlayerList().broadcastAll((Packet)ClientboundTickingStepPacket.from(this));
/*     */   }
/*     */   
/*     */   public boolean stepGameIfPaused(int paramInt) {
/*  43 */     if (!isFrozen()) {
/*  44 */       return false;
/*     */     }
/*  46 */     this.frozenTicksToRun = paramInt;
/*  47 */     updateStepTicks();
/*  48 */     return true;
/*     */   }
/*     */   
/*     */   public boolean stopStepping() {
/*  52 */     if (this.frozenTicksToRun > 0) {
/*  53 */       this.frozenTicksToRun = 0;
/*  54 */       updateStepTicks();
/*  55 */       return true;
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   public boolean stopSprinting() {
/*  61 */     if (this.remainingSprintTicks > 0L) {
/*  62 */       finishTickSprint();
/*  63 */       return true;
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requestGameToSprint(int paramInt) {
/*  70 */     boolean bool = (this.remainingSprintTicks > 0L) ? true : false;
/*  71 */     this.sprintTimeSpend = 0L;
/*  72 */     this.scheduledCurrentSprintTicks = paramInt;
/*  73 */     this.remainingSprintTicks = paramInt;
/*  74 */     this.previousIsFrozen = isFrozen();
/*  75 */     setFrozen(false);
/*  76 */     return bool;
/*     */   }
/*     */   
/*     */   private void finishTickSprint() {
/*  80 */     long l = this.scheduledCurrentSprintTicks - this.remainingSprintTicks;
/*  81 */     double d = Math.max(1.0D, this.sprintTimeSpend) / TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*  82 */     int i = (int)((TimeUtil.MILLISECONDS_PER_SECOND * l) / d);
/*  83 */     String str = String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf((l == 0L) ? millisecondsPerTick() : (d / l)) });
/*  84 */     this.scheduledCurrentSprintTicks = 0L;
/*  85 */     this.sprintTimeSpend = 0L;
/*  86 */     this.server.createCommandSourceStack().sendSuccess(() -> Component.translatable("commands.tick.sprint.report", new Object[] { Integer.valueOf(paramInt), paramString }), true);
/*  87 */     this.remainingSprintTicks = 0L;
/*  88 */     setFrozen(this.previousIsFrozen);
/*  89 */     this.server.onTickRateChanged();
/*     */   }
/*     */   
/*     */   public boolean checkShouldSprintThisTick() {
/*  93 */     if (!this.runGameElements) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (this.remainingSprintTicks > 0L) {
/*  97 */       this.sprintTickStartTime = System.nanoTime();
/*  98 */       this.remainingSprintTicks--;
/*  99 */       return true;
/*     */     } 
/* 101 */     finishTickSprint();
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTickWork() {
/* 107 */     this.sprintTimeSpend += System.nanoTime() - this.sprintTickStartTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTickRate(float paramFloat) {
/* 112 */     super.setTickRate(paramFloat);
/* 113 */     this.server.onTickRateChanged();
/* 114 */     updateStateToClients();
/*     */   }
/*     */   
/*     */   public void updateJoiningPlayer(ServerPlayer paramServerPlayer) {
/* 118 */     paramServerPlayer.connection.send((Packet)ClientboundTickingStatePacket.from(this));
/* 119 */     paramServerPlayer.connection.send((Packet)ClientboundTickingStepPacket.from(this));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\ServerTickRateManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */