/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class ServerCodeOfConductConfigurationTask
/*    */   implements ConfigurationTask {
/* 11 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("server_code_of_conduct");
/*    */   
/*    */   private final Supplier<String> codeOfConduct;
/*    */   
/*    */   public ServerCodeOfConductConfigurationTask(Supplier<String> paramSupplier) {
/* 16 */     this.codeOfConduct = paramSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(Consumer<Packet<?>> paramConsumer) {
/* 21 */     paramConsumer.accept(new ClientboundCodeOfConductPacket(this.codeOfConduct.get()));
/*    */   }
/*    */ 
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 26 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\network\config\ServerCodeOfConductConfigurationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */