package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.api.MethodInfo;

@FunctionalInterface
public interface Factory<Params, Result> {
  OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> paramMethodInfo, OutgoingRpcMethod.Attributes paramAttributes);
}


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */