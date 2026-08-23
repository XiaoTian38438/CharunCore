/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ParamInfo;
/*     */ import net.minecraft.server.jsonrpc.api.ResultInfo;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
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
/*     */ public class OutgoingRpcMethodBuilder<Params, Result>
/*     */ {
/*  95 */   public static final OutgoingRpcMethod.Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod.Attributes(true);
/*     */   private final OutgoingRpcMethod.Factory<Params, Result> method;
/*  97 */   private String description = "";
/*     */   private ParamInfo<Params> paramInfo;
/*     */   private ResultInfo<Result> resultInfo;
/*     */   
/*     */   public OutgoingRpcMethodBuilder(OutgoingRpcMethod.Factory<Params, Result> paramFactory) {
/* 102 */     this.method = paramFactory;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> description(String paramString) {
/* 106 */     this.description = paramString;
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> response(String paramString, Schema<Result> paramSchema) {
/* 111 */     this.resultInfo = new ResultInfo(paramString, paramSchema);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public OutgoingRpcMethodBuilder<Params, Result> param(String paramString, Schema<Params> paramSchema) {
/* 116 */     this.paramInfo = new ParamInfo(paramString, paramSchema);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   private OutgoingRpcMethod<Params, Result> build() {
/* 121 */     MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return this.method.create(methodInfo, DEFAULT_ATTRIBUTES);
/*     */   }
/*     */   
/*     */   public Holder.Reference<OutgoingRpcMethod<Params, Result>> register(String paramString) {
/* 130 */     return register(Identifier.withDefaultNamespace("notification/" + paramString));
/*     */   }
/*     */   
/*     */   private Holder.Reference<OutgoingRpcMethod<Params, Result>> register(Identifier paramIdentifier) {
/* 134 */     return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, paramIdentifier, build());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\OutgoingRpcMethod$OutgoingRpcMethodBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */