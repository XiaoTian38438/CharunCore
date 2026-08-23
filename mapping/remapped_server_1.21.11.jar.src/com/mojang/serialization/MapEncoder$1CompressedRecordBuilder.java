/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CompressedRecordBuilder
/*     */   extends RecordBuilder.AbstractUniversalBuilder<T, List<T>>
/*     */ {
/*     */   private CompressedRecordBuilder() {
/* 101 */     super(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<T> initBuilder() {
/* 106 */     ArrayList<T> arrayList = new ArrayList(compressor.size());
/* 107 */     for (byte b = 0; b < compressor.size(); b++) {
/* 108 */       arrayList.add(null);
/*     */     }
/* 110 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<T> append(T paramT1, T paramT2, List<T> paramList) {
/* 115 */     paramList.set(compressor.compress(paramT1), paramT2);
/* 116 */     return paramList;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataResult<T> build(List<T> paramList, T paramT) {
/* 121 */     return ops().mergeToList(paramT, paramList);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\MapEncoder$1CompressedRecordBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */