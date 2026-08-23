/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SleepStatus
/*    */ {
/*    */   private int activePlayers;
/*    */   private int sleepingPlayers;
/*    */   
/*    */   public boolean areEnoughSleeping(int paramInt) {
/* 16 */     return (this.sleepingPlayers >= sleepersNeeded(paramInt));
/*    */   }
/*    */   
/*    */   public boolean areEnoughDeepSleeping(int paramInt, List<ServerPlayer> paramList) {
/* 20 */     int i = (int)paramList.stream().filter(Player::isSleepingLongEnough).count();
/* 21 */     return (i >= sleepersNeeded(paramInt));
/*    */   }
/*    */   
/*    */   public int sleepersNeeded(int paramInt) {
/* 25 */     return Math.max(1, Mth.ceil((this.activePlayers * paramInt) / 100.0F));
/*    */   }
/*    */   
/*    */   public void removeAllSleepers() {
/* 29 */     this.sleepingPlayers = 0;
/*    */   }
/*    */   
/*    */   public int amountSleeping() {
/* 33 */     return this.sleepingPlayers;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean update(List<ServerPlayer> paramList) {
/* 38 */     int i = this.activePlayers;
/* 39 */     int j = this.sleepingPlayers;
/* 40 */     this.activePlayers = 0;
/* 41 */     this.sleepingPlayers = 0;
/*    */     
/* 43 */     for (ServerPlayer serverPlayer : paramList) {
/* 44 */       if (!serverPlayer.isSpectator()) {
/* 45 */         this.activePlayers++;
/* 46 */         if (serverPlayer.isSleeping()) {
/* 47 */           this.sleepingPlayers++;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     return ((j > 0 || this.sleepingPlayers > 0) && (i != this.activePlayers || j != this.sleepingPlayers));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\players\SleepStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */