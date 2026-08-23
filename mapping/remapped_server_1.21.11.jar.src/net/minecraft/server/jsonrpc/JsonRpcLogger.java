/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class JsonRpcLogger
/*    */ {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private static final String PREFIX = "RPC Connection #{}: ";
/*    */   
/*    */   public void log(ClientInfo paramClientInfo, String paramString, Object... paramVarArgs) {
/* 16 */     if (paramVarArgs.length == 0) {
/* 17 */       LOGGER.info("RPC Connection #{}: " + paramString, paramClientInfo.connectionId());
/*    */     } else {
/* 19 */       ArrayList<Integer> arrayList = new ArrayList(Arrays.asList(paramVarArgs));
/* 20 */       arrayList.addFirst(paramClientInfo.connectionId());
/* 21 */       LOGGER.info("RPC Connection #{}: " + paramString, arrayList.toArray());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\JsonRpcLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */