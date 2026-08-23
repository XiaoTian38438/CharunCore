/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ final class ArrayBuilder
/*     */   implements ListBuilder<JsonElement>
/*     */ {
/* 343 */   private DataResult<JsonArray> builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/*     */ 
/*     */   
/*     */   public DynamicOps<JsonElement> ops() {
/* 347 */     return JsonOps.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> add(JsonElement paramJsonElement) {
/* 352 */     this.builder = this.builder.map(paramJsonArray -> {
/*     */           paramJsonArray.add(paramJsonElement);
/*     */           return paramJsonArray;
/*     */         });
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> add(DataResult<JsonElement> paramDataResult) {
/* 361 */     this.builder = this.builder.apply2stable((paramJsonArray, paramJsonElement) -> { paramJsonArray.add(paramJsonElement); return paramJsonArray; }paramDataResult);
/*     */ 
/*     */ 
/*     */     
/* 365 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> withErrorsFrom(DataResult<?> paramDataResult) {
/* 370 */     this.builder = this.builder.flatMap(paramJsonArray -> paramDataResult.map(()));
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<JsonElement> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 376 */     this.builder = this.builder.mapError(paramUnaryOperator);
/* 377 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<JsonElement> build(JsonElement paramJsonElement) {
/* 382 */     DataResult<?> dataResult = this.builder.flatMap(paramJsonArray -> {
/*     */           if (!(paramJsonElement instanceof JsonArray) && paramJsonElement != ops().empty()) {
/*     */             return DataResult.error((), paramJsonElement);
/*     */           }
/*     */           
/*     */           JsonArray jsonArray = new JsonArray();
/*     */           
/*     */           if (paramJsonElement != ops().empty()) {
/*     */             jsonArray.addAll(paramJsonElement.getAsJsonArray());
/*     */           }
/*     */           jsonArray.addAll(paramJsonArray);
/*     */           return DataResult.success(jsonArray, Lifecycle.stable());
/*     */         });
/* 395 */     this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
/* 396 */     return (DataResult)dataResult;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\JsonOps$ArrayBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */