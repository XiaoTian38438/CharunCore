package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

@FunctionalInterface
public interface ParameterlessRpcMethodFunction<Result> {
  Result apply(MinecraftApi paramMinecraftApi, ClientInfo paramClientInfo);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$ParameterlessRpcMethodFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */