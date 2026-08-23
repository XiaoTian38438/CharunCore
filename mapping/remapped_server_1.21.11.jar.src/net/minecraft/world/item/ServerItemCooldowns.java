/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundCooldownPacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class ServerItemCooldowns
/*    */   extends ItemCooldowns {
/*    */   public ServerItemCooldowns(ServerPlayer paramServerPlayer) {
/* 11 */     this.player = paramServerPlayer;
/*    */   }
/*    */   private final ServerPlayer player;
/*    */   
/*    */   protected void onCooldownStarted(Identifier paramIdentifier, int paramInt) {
/* 16 */     super.onCooldownStarted(paramIdentifier, paramInt);
/* 17 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(paramIdentifier, paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onCooldownEnded(Identifier paramIdentifier) {
/* 22 */     super.onCooldownEnded(paramIdentifier);
/* 23 */     this.player.connection.send((Packet)new ClientboundCooldownPacket(paramIdentifier, 0));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\item\ServerItemCooldowns.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */