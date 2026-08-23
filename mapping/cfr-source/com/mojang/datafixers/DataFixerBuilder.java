/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
 *  it.unimi.dsi.fastutil.ints.IntAVLTreeSet
 *  it.unimi.dsi.fastutil.ints.IntBidirectionalIterator
 *  it.unimi.dsi.fastutil.ints.IntSortedSet
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.datafixers;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerUpper;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFixerBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFixerBuilder.class);
    private final int dataVersion;
    private final Int2ObjectSortedMap<Schema> schemas = new Int2ObjectAVLTreeMap();
    private final List<DataFix> globalList = new ArrayList<DataFix>();
    private final IntSortedSet fixerVersions = new IntAVLTreeSet();

    public DataFixerBuilder(int n) {
        this.dataVersion = n;
    }

    public Schema addSchema(int n, BiFunction<Integer, Schema, Schema> biFunction) {
        return this.addSchema(n, 0, biFunction);
    }

    public Schema addSchema(int n, int n2, BiFunction<Integer, Schema, Schema> biFunction) {
        int n3 = DataFixUtils.makeKey(n, n2);
        Schema schema = this.schemas.isEmpty() ? null : (Schema)this.schemas.get(DataFixerUpper.getLowestSchemaSameVersion(this.schemas, n3 - 1));
        Schema schema2 = biFunction.apply(DataFixUtils.makeKey(n, n2), schema);
        this.addSchema(schema2);
        return schema2;
    }

    public void addSchema(Schema schema) {
        this.schemas.put(schema.getVersionKey(), (Object)schema);
    }

    public void addFixer(DataFix dataFix) {
        int n = DataFixUtils.getVersion(dataFix.getVersionKey());
        if (n > this.dataVersion) {
            LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", (Object)n, (Object)this.dataVersion);
            return;
        }
        this.globalList.add(dataFix);
        this.fixerVersions.add(dataFix.getVersionKey());
    }

    public Result build() {
        DataFixerUpper dataFixerUpper = new DataFixerUpper((Int2ObjectSortedMap<Schema>)new Int2ObjectAVLTreeMap(this.schemas), new ArrayList<DataFix>(this.globalList), (IntSortedSet)new IntAVLTreeSet(this.fixerVersions));
        return new Result(dataFixerUpper);
    }

    public class Result {
        private final DataFixerUpper fixerUpper;

        public Result(DataFixerUpper dataFixerUpper) {
            this.fixerUpper = dataFixerUpper;
        }

        public DataFixer fixer() {
            return this.fixerUpper;
        }

        public CompletableFuture<?> optimize(Set<DSL.TypeReference> set, Executor executor) {
            Object object;
            Instant instant = Instant.now();
            ArrayList<CompletableFuture<Void>> arrayList = new ArrayList<CompletableFuture<Void>>();
            ArrayList arrayList2 = new ArrayList();
            Set set2 = set.stream().map(DSL.TypeReference::typeName).collect(Collectors.toSet());
            IntBidirectionalIterator intBidirectionalIterator = this.fixerUpper.fixerVersions().iterator();
            while (intBidirectionalIterator.hasNext()) {
                int n = intBidirectionalIterator.nextInt();
                object = (Schema)DataFixerBuilder.this.schemas.get(n);
                for (String string : ((Schema)object).types()) {
                    if (!set2.contains(string)) continue;
                    CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> this.lambda$optimize$1((Schema)object, string, n), executor);
                    arrayList.add(completableFuture);
                    CompletableFuture completableFuture2 = new CompletableFuture();
                    completableFuture.exceptionally(throwable -> {
                        completableFuture2.completeExceptionally((Throwable)throwable);
                        return null;
                    });
                    arrayList2.add(completableFuture2);
                }
            }
            CompletionStage completionStage = CompletableFuture.allOf((CompletableFuture[])arrayList.toArray(CompletableFuture[]::new)).thenAccept(void_ -> LOGGER.info("{} Datafixer optimizations took {} milliseconds", (Object)arrayList.size(), (Object)Duration.between(instant, Instant.now()).toMillis()));
            object = CompletableFuture.anyOf((CompletableFuture[])arrayList2.toArray(CompletableFuture[]::new));
            return CompletableFuture.anyOf(new CompletableFuture[]{completionStage, object});
        }

        private /* synthetic */ void lambda$optimize$1(Schema schema, String string, int n) {
            Type<?> type = schema.getType(() -> string);
            TypeRewriteRule typeRewriteRule = this.fixerUpper.getRule(DataFixUtils.getVersion(n), DataFixerBuilder.this.dataVersion);
            type.rewrite(typeRewriteRule, DataFixerUpper.OPTIMIZATION_RULE);
        }
    }
}

