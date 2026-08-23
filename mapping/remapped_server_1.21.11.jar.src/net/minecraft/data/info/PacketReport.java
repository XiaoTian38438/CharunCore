/*    */ package net.minecraft.data.info;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.data.CachedOutput;
/*    */ import net.minecraft.data.DataProvider;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*    */ import net.minecraft.network.protocol.game.GameProtocols;
/*    */ import net.minecraft.network.protocol.handshake.HandshakeProtocols;
/*    */ import net.minecraft.network.protocol.login.LoginProtocols;
/*    */ import net.minecraft.network.protocol.status.StatusProtocols;
/*    */ 
/*    */ public class PacketReport implements DataProvider {
/*    */   public PacketReport(PackOutput paramPackOutput) {
/* 24 */     this.output = paramPackOutput;
/*    */   }
/*    */   private final PackOutput output;
/*    */   
/*    */   public CompletableFuture<?> run(CachedOutput paramCachedOutput) {
/* 29 */     Path path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("packets.json");
/* 30 */     return DataProvider.saveStable(paramCachedOutput, serializePackets(), path);
/*    */   }
/*    */   
/*    */   private JsonElement serializePackets() {
/* 34 */     JsonObject jsonObject = new JsonObject();
/*    */     
/* 36 */     ((Map)Stream.<ProtocolInfo.DetailsProvider>of(new ProtocolInfo.DetailsProvider[] { (ProtocolInfo.DetailsProvider)HandshakeProtocols.SERVERBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)StatusProtocols.CLIENTBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)StatusProtocols.SERVERBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)LoginProtocols.CLIENTBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)LoginProtocols.SERVERBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)ConfigurationProtocols.CLIENTBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)ConfigurationProtocols.SERVERBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)GameProtocols.CLIENTBOUND_TEMPLATE, (ProtocolInfo.DetailsProvider)GameProtocols.SERVERBOUND_TEMPLATE
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 51 */         }).map(ProtocolInfo.DetailsProvider::details)
/* 52 */       .collect(Collectors.groupingBy(ProtocolInfo.Details::id)))
/* 53 */       .forEach((paramConnectionProtocol, paramList) -> {
/*    */           JsonObject jsonObject = new JsonObject();
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           paramJsonObject.add(paramConnectionProtocol.id(), (JsonElement)jsonObject);
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           paramList.forEach(());
/*    */         });
/*    */ 
/*    */ 
/*    */     
/* 69 */     return (JsonElement)jsonObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 74 */     return "Packet Report";
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\info\PacketReport.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */