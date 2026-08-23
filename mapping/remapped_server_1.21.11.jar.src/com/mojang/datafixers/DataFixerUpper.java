/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntSortedSet;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFixerUpper
/*     */   implements DataFixer
/*     */ {
/*     */   public static boolean ERRORS_ARE_FATAL = false;
/*  43 */   private static final Logger LOGGER = LoggerFactory.getLogger(DataFixerUpper.class);
/*     */   
/*  45 */   protected static final PointFreeRule OPTIMIZATION_RULE = DataFixUtils.<PointFreeRule>make(() -> PointFreeRule.everywhere(PointFreeRule.seq(new PointFreeRule[] { (PointFreeRule)PointFreeRule.CataFuseSame.INSTANCE, (PointFreeRule)PointFreeRule.CataFuseDifferent.INSTANCE, (PointFreeRule)PointFreeRule.CompRewrite.together(new PointFreeRule.CompRewrite[] { (PointFreeRule.CompRewrite)PointFreeRule.LensComp.INSTANCE, (PointFreeRule.CompRewrite)PointFreeRule.SortProj.INSTANCE, (PointFreeRule.CompRewrite)PointFreeRule.SortInj.INSTANCE }, ) }, ), (PointFreeRule)PointFreeRule.AppNest.INSTANCE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Int2ObjectSortedMap<Schema> schemas;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<DataFix> globalList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IntSortedSet fixerVersions;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final Long2ObjectMap<TypeRewriteRule> rules = Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());
/*     */   
/*     */   protected DataFixerUpper(Int2ObjectSortedMap<Schema> paramInt2ObjectSortedMap, List<DataFix> paramList, IntSortedSet paramIntSortedSet) {
/*  69 */     this.schemas = paramInt2ObjectSortedMap;
/*  70 */     this.globalList = paramList;
/*  71 */     this.fixerVersions = paramIntSortedSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Dynamic<T> update(DSL.TypeReference paramTypeReference, Dynamic<T> paramDynamic, int paramInt1, int paramInt2) {
/*  76 */     if (paramInt1 < paramInt2) {
/*  77 */       Type<?> type = getType(paramTypeReference, paramInt1);
/*  78 */       DataResult dataResult = type.readAndWrite(paramDynamic.getOps(), getType(paramTypeReference, paramInt2), getRule(paramInt1, paramInt2), OPTIMIZATION_RULE, paramDynamic.getValue());
/*  79 */       Objects.requireNonNull(LOGGER); Object object = dataResult.resultOrPartial(LOGGER::error).orElse(paramDynamic.getValue());
/*  80 */       return new Dynamic(paramDynamic.getOps(), object);
/*     */     } 
/*  82 */     return paramDynamic;
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema getSchema(int paramInt) {
/*  87 */     return (Schema)this.schemas.get(getLowestSchemaSameVersion(this.schemas, paramInt));
/*     */   }
/*     */   
/*     */   protected Type<?> getType(DSL.TypeReference paramTypeReference, int paramInt) {
/*  91 */     return getSchema(DataFixUtils.makeKey(paramInt)).getTypeRaw(paramTypeReference);
/*     */   }
/*     */   
/*     */   protected static int getLowestSchemaSameVersion(Int2ObjectSortedMap<Schema> paramInt2ObjectSortedMap, int paramInt) {
/*  95 */     if (paramInt < paramInt2ObjectSortedMap.firstIntKey())
/*     */     {
/*  97 */       return paramInt2ObjectSortedMap.firstIntKey();
/*     */     }
/*  99 */     return paramInt2ObjectSortedMap.subMap(0, paramInt + 1).lastIntKey();
/*     */   }
/*     */   
/*     */   private int getLowestFixSameVersion(int paramInt) {
/* 103 */     if (paramInt < this.fixerVersions.firstInt())
/*     */     {
/* 105 */       return this.fixerVersions.firstInt() - 1;
/*     */     }
/* 107 */     return this.fixerVersions.subSet(0, paramInt + 1).lastInt();
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule getRule(int paramInt1, int paramInt2) {
/* 111 */     if (paramInt1 >= paramInt2) {
/* 112 */       return TypeRewriteRule.nop();
/*     */     }
/*     */     
/* 115 */     long l = paramInt1 << 32L | paramInt2;
/* 116 */     return (TypeRewriteRule)this.rules.computeIfAbsent(l, paramLong -> {
/*     */           int i = getLowestFixSameVersion(DataFixUtils.makeKey(paramInt1));
/*     */           ArrayList<TypeRewriteRule> arrayList = Lists.newArrayList();
/*     */           for (DataFix dataFix : this.globalList) {
/*     */             int j = dataFix.getVersionKey();
/*     */             int k = DataFixUtils.getVersion(j);
/*     */             if (j > i && k <= paramInt2) {
/*     */               TypeRewriteRule typeRewriteRule = dataFix.getRule();
/*     */               if (typeRewriteRule == TypeRewriteRule.nop()) {
/*     */                 continue;
/*     */               }
/*     */               arrayList.add(typeRewriteRule);
/*     */             } 
/*     */           } 
/*     */           return TypeRewriteRule.seq(arrayList);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected IntSortedSet fixerVersions() {
/* 137 */     return this.fixerVersions;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFixerUpper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */