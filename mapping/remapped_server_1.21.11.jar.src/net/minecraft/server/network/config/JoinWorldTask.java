/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class JoinWorldTask
/*    */   implements ConfigurationTask {
/* 10 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("join_world");
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> paramConsumer) {
/* 14 */     paramConsumer.accept(ClientboundFinishConfigurationPacket.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 19 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\config\JoinWorldTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */