/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Result
/*     */ {
/*     */   private final DataFixerUpper fixerUpper;
/*     */   
/*     */   public Result(DataFixerUpper paramDataFixerUpper) {
/*  74 */     this.fixerUpper = paramDataFixerUpper;
/*     */   }
/*     */   
/*     */   public DataFixer fixer() {
/*  78 */     return this.fixerUpper;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> optimize(Set<DSL.TypeReference> paramSet, Executor paramExecutor) {
/*  82 */     Instant instant = Instant.now();
/*  83 */     ArrayList<CompletableFuture<Void>> arrayList = new ArrayList();
/*  84 */     ArrayList<CompletableFuture> arrayList1 = new ArrayList();
/*     */     
/*  86 */     Set set = (Set)paramSet.stream().map(DSL.TypeReference::typeName).collect(Collectors.toSet());
/*     */     
/*  88 */     IntBidirectionalIterator intBidirectionalIterator = this.fixerUpper.fixerVersions().iterator();
/*  89 */     while (intBidirectionalIterator.hasNext()) {
/*  90 */       int i = intBidirectionalIterator.nextInt();
/*  91 */       Schema schema = (Schema)DataFixerBuilder.this.schemas.get(i);
/*  92 */       for (String str : schema.types()) {
/*  93 */         if (!set.contains(str)) {
/*     */           continue;
/*     */         }
/*  96 */         CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> { Type type = paramSchema.getType(()); TypeRewriteRule typeRewriteRule = this.fixerUpper.getRule(DataFixUtils.getVersion(paramInt), DataFixerBuilder.this.dataVersion); type.rewrite(typeRewriteRule, DataFixerUpper.OPTIMIZATION_RULE); }paramExecutor);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 101 */         arrayList.add(completableFuture2);
/*     */         
/* 103 */         CompletableFuture completableFuture3 = new CompletableFuture();
/* 104 */         completableFuture2.exceptionally(paramThrowable -> {
/*     */               paramCompletableFuture.completeExceptionally(paramThrowable);
/*     */               return null;
/*     */             });
/* 108 */         arrayList1.add(completableFuture3);
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     CompletableFuture<Void> completableFuture = CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(paramInt -> new CompletableFuture[paramInt])).thenAccept(paramVoid -> DataFixerBuilder.LOGGER.info("{} Datafixer optimizations took {} milliseconds", Integer.valueOf(paramList.size()), Long.valueOf(Duration.between(paramInstant, Instant.now()).toMillis())));
/*     */ 
/*     */ 
/*     */     
/* 116 */     CompletableFuture<Object> completableFuture1 = CompletableFuture.anyOf((CompletableFuture<?>[])arrayList1.toArray(paramInt -> new CompletableFuture[paramInt]));
/*     */     
/* 118 */     return CompletableFuture.anyOf((CompletableFuture<?>[])new CompletableFuture[] { completableFuture, completableFuture1 });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFixerBuilder$Result.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */