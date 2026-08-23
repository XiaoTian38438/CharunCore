/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IncomingRpcMethodBuilder<Params, Result>
/*     */ {
/* 107 */   private String description = "";
/*     */   private ParamInfo<Params> paramInfo;
/*     */   private ResultInfo<Result> resultInfo;
/*     */   private boolean discoverable = true;
/*     */   private boolean runOnMainThread = true;
/*     */   private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction;
/*     */   private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction;
/*     */   
/*     */   public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> paramParameterlessRpcMethodFunction) {
/* 116 */     this.parameterlessFunction = paramParameterlessRpcMethodFunction;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> paramRpcMethodFunction) {
/* 120 */     this.parameterFunction = paramRpcMethodFunction;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> paramFunction) {
/* 124 */     this.parameterlessFunction = ((paramMinecraftApi, paramClientInfo) -> paramFunction.apply(paramMinecraftApi));
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> description(String paramString) {
/* 128 */     this.description = paramString;
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> response(String paramString, Schema<Result> paramSchema) {
/* 133 */     this.resultInfo = new ResultInfo(paramString, paramSchema.info());
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> param(String paramString, Schema<Params> paramSchema) {
/* 138 */     this.paramInfo = new ParamInfo(paramString, paramSchema.info());
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 143 */     this.discoverable = false;
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 148 */     this.runOnMainThread = false;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public IncomingRpcMethod<Params, Result> build() {
/* 153 */     if (this.resultInfo == null) {
/* 154 */       throw new IllegalStateException("No response defined");
/*     */     }
/*     */     
/* 157 */     IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable);
/* 158 */     MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     if (this.parameterlessFunction != null) {
/* 165 */       return new IncomingRpcMethod.ParameterlessMethod<>(methodInfo, attributes, this.parameterlessFunction);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 170 */     if (this.parameterFunction != null) {
/* 171 */       if (this.paramInfo == null) {
/* 172 */         throw new IllegalStateException("No param schema defined");
/*     */       }
/* 174 */       return new IncomingRpcMethod.Method<>(methodInfo, attributes, this.parameterFunction);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     throw new IllegalStateException("No method defined");
/*     */   }
/*     */ 
/*     */   
/*     */   public IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> paramRegistry, String paramString) {
/* 185 */     return register(paramRegistry, Identifier.withDefaultNamespace(paramString));
/*     */   }
/*     */   
/*     */   private IncomingRpcMethod<?, ?> register(Registry<IncomingRpcMethod<?, ?>> paramRegistry, Identifier paramIdentifier) {
/* 189 */     return (IncomingRpcMethod<?, ?>)Registry.register(paramRegistry, paramIdentifier, build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethod$IncomingRpcMethodBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */