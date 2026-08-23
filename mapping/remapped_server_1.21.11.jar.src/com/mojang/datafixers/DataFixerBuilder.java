/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFixerBuilder
/*     */ {
/*  26 */   private static final Logger LOGGER = LoggerFactory.getLogger(DataFixerBuilder.class);
/*     */   
/*     */   private final int dataVersion;
/*  29 */   private final Int2ObjectSortedMap<Schema> schemas = (Int2ObjectSortedMap<Schema>)new Int2ObjectAVLTreeMap();
/*  30 */   private final List<DataFix> globalList = new ArrayList<>();
/*  31 */   private final IntSortedSet fixerVersions = (IntSortedSet)new IntAVLTreeSet();
/*     */   
/*     */   public DataFixerBuilder(int paramInt) {
/*  34 */     this.dataVersion = paramInt;
/*     */   }
/*     */   
/*     */   public Schema addSchema(int paramInt, BiFunction<Integer, Schema, Schema> paramBiFunction) {
/*  38 */     return addSchema(paramInt, 0, paramBiFunction);
/*     */   }
/*     */   
/*     */   public Schema addSchema(int paramInt1, int paramInt2, BiFunction<Integer, Schema, Schema> paramBiFunction) {
/*  42 */     int i = DataFixUtils.makeKey(paramInt1, paramInt2);
/*  43 */     Schema schema1 = this.schemas.isEmpty() ? null : (Schema)this.schemas.get(DataFixerUpper.getLowestSchemaSameVersion(this.schemas, i - 1));
/*  44 */     Schema schema2 = paramBiFunction.apply(Integer.valueOf(DataFixUtils.makeKey(paramInt1, paramInt2)), schema1);
/*  45 */     addSchema(schema2);
/*  46 */     return schema2;
/*     */   }
/*     */   
/*     */   public void addSchema(Schema paramSchema) {
/*  50 */     this.schemas.put(paramSchema.getVersionKey(), paramSchema);
/*     */   }
/*     */   
/*     */   public void addFixer(DataFix paramDataFix) {
/*  54 */     int i = DataFixUtils.getVersion(paramDataFix.getVersionKey());
/*     */     
/*  56 */     if (i > this.dataVersion) {
/*  57 */       LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", Integer.valueOf(i), Integer.valueOf(this.dataVersion));
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     this.globalList.add(paramDataFix);
/*  62 */     this.fixerVersions.add(paramDataFix.getVersionKey());
/*     */   }
/*     */   
/*     */   public Result build() {
/*  66 */     DataFixerUpper dataFixerUpper = new DataFixerUpper((Int2ObjectSortedMap<Schema>)new Int2ObjectAVLTreeMap(this.schemas), new ArrayList<>(this.globalList), (IntSortedSet)new IntAVLTreeSet(this.fixerVersions));
/*  67 */     return new Result(dataFixerUpper);
/*     */   }
/*     */   
/*     */   public class Result {
/*     */     private final DataFixerUpper fixerUpper;
/*     */     
/*     */     public Result(DataFixerUpper param1DataFixerUpper) {
/*  74 */       this.fixerUpper = param1DataFixerUpper;
/*     */     }
/*     */     
/*     */     public DataFixer fixer() {
/*  78 */       return this.fixerUpper;
/*     */     }
/*     */     
/*     */     public CompletableFuture<?> optimize(Set<DSL.TypeReference> param1Set, Executor param1Executor) {
/*  82 */       Instant instant = Instant.now();
/*  83 */       ArrayList<CompletableFuture<Void>> arrayList = new ArrayList();
/*  84 */       ArrayList<CompletableFuture> arrayList1 = new ArrayList();
/*     */       
/*  86 */       Set set = (Set)param1Set.stream().map(DSL.TypeReference::typeName).collect(Collectors.toSet());
/*     */       
/*  88 */       IntBidirectionalIterator intBidirectionalIterator = this.fixerUpper.fixerVersions().iterator();
/*  89 */       while (intBidirectionalIterator.hasNext()) {
/*  90 */         int i = intBidirectionalIterator.nextInt();
/*  91 */         Schema schema = (Schema)DataFixerBuilder.this.schemas.get(i);
/*  92 */         for (String str : schema.types()) {
/*  93 */           if (!set.contains(str)) {
/*     */             continue;
/*     */           }
/*  96 */           CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> { Type type = param1Schema.getType(()); TypeRewriteRule typeRewriteRule = this.fixerUpper.getRule(DataFixUtils.getVersion(param1Int), DataFixerBuilder.this.dataVersion); type.rewrite(typeRewriteRule, DataFixerUpper.OPTIMIZATION_RULE); }param1Executor);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 101 */           arrayList.add(completableFuture2);
/*     */           
/* 103 */           CompletableFuture completableFuture3 = new CompletableFuture();
/* 104 */           completableFuture2.exceptionally(param1Throwable -> {
/*     */                 param1CompletableFuture.completeExceptionally(param1Throwable);
/*     */                 return null;
/*     */               });
/* 108 */           arrayList1.add(completableFuture3);
/*     */         } 
/*     */       } 
/*     */       
/* 112 */       CompletableFuture<Void> completableFuture = CompletableFuture.allOf((CompletableFuture<?>[])arrayList.toArray(param1Int -> new CompletableFuture[param1Int])).thenAccept(param1Void -> DataFixerBuilder.LOGGER.info("{} Datafixer optimizations took {} milliseconds", Integer.valueOf(param1List.size()), Long.valueOf(Duration.between(param1Instant, Instant.now()).toMillis())));
/*     */ 
/*     */ 
/*     */       
/* 116 */       CompletableFuture<Object> completableFuture1 = CompletableFuture.anyOf((CompletableFuture<?>[])arrayList1.toArray(param1Int -> new CompletableFuture[param1Int]));
/*     */       
/* 118 */       return CompletableFuture.anyOf((CompletableFuture<?>[])new CompletableFuture[] { completableFuture, completableFuture1 });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFixerBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */