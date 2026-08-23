/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class ServerResourcePackConfigurationTask
/*    */   implements ConfigurationTask {
/* 12 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_resource_pack");
/*    */   
/*    */   private final MinecraftServer.ServerResourcePackInfo info;
/*    */   
/*    */   public ServerResourcePackConfigurationTask(MinecraftServer.ServerResourcePackInfo paramServerResourcePackInfo) {
/* 17 */     this.info = paramServerResourcePackInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> paramConsumer) {
/* 22 */     paramConsumer.accept(new ClientboundResourcePackPushPacket(this.info.id(), this.info.url(), this.info.hash(), this.info.isRequired(), Optional.ofNullable(this.info.prompt())));
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 27 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\config\ServerResourcePackConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */