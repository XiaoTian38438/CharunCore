/*    */ package net.minecraft.server.rcon;
/*    */ import net.minecraft.commands.CommandSource;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.world.phys.Vec2;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RconConsoleSource implements CommandSource {
/* 14 */   private static final Component RCON_COMPONENT = (Component)Component.literal("Rcon"); private static final String RCON = "Rcon";
/* 15 */   private final StringBuffer buffer = new StringBuffer();
/*    */   private final MinecraftServer server;
/*    */   
/*    */   public RconConsoleSource(MinecraftServer paramMinecraftServer) {
/* 19 */     this.server = paramMinecraftServer;
/*    */   }
/*    */   
/*    */   public void prepareForCommand() {
/* 23 */     this.buffer.setLength(0);
/*    */   }
/*    */   
/*    */   public String getCommandResponse() {
/* 27 */     return this.buffer.toString();
/*    */   }
/*    */   
/*    */   public CommandSourceStack createCommandSourceStack() {
/* 31 */     ServerLevel serverLevel = this.server.overworld();
/* 32 */     return new CommandSourceStack(this, Vec3.atLowerCornerOf((Vec3i)serverLevel.getRespawnData().pos()), Vec2.ZERO, serverLevel, (PermissionSet)LevelBasedPermissionSet.OWNER, "Rcon", RCON_COMPONENT, this.server, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void sendSystemMessage(Component paramComponent) {
/* 37 */     this.buffer.append(paramComponent.getString());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptsSuccess() {
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptsFailure() {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldInformAdmins() {
/* 52 */     return this.server.shouldRconBroadcast();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\rcon\RconConsoleSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */