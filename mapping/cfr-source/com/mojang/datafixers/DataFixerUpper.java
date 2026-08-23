/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap
 *  it.unimi.dsi.fastutil.ints.IntSortedSet
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMaps
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.mojang.datafixers;

import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.functions.PointFreeRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataFixerUpper
implements DataFixer {
    public static boolean ERRORS_ARE_FATAL = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataFixerUpper.class);
    protected static final PointFreeRule OPTIMIZATION_RULE = DataFixUtils.make(() -> PointFreeRule.everywhere(PointFreeRule.seq(PointFreeRule.CataFuseSame.INSTANCE, PointFreeRule.CataFuseDifferent.INSTANCE, PointFreeRule.CompRewrite.together(PointFreeRule.LensComp.INSTANCE, PointFreeRule.SortProj.INSTANCE, PointFreeRule.SortInj.INSTANCE)), PointFreeRule.AppNest.INSTANCE));
    private final Int2ObjectSortedMap<Schema> schemas;
    private final List<DataFix> globalList;
    private final IntSortedSet fixerVersions;
    private final Long2ObjectMap<TypeRewriteRule> rules = Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());

    protected DataFixerUpper(Int2ObjectSortedMap<Schema> int2ObjectSortedMap, List<DataFix> list, IntSortedSet intSortedSet) {
        this.schemas = int2ObjectSortedMap;
        this.globalList = list;
        this.fixerVersions = intSortedSet;
    }

    @Override
    public <T> Dynamic<T> update(DSL.TypeReference typeReference, Dynamic<T> dynamic, int n, int n2) {
        if (n < n2) {
            Type<?> type = this.getType(typeReference, n);
            DataResult dataResult = type.readAndWrite(dynamic.getOps(), this.getType(typeReference, n2), this.getRule(n, n2), OPTIMIZATION_RULE, dynamic.getValue());
            Object t = dataResult.resultOrPartial(arg_0 -> ((Logger)LOGGER).error(arg_0)).orElse(dynamic.getValue());
            return new Dynamic(dynamic.getOps(), t);
        }
        return dynamic;
    }

    @Override
    public Schema getSchema(int n) {
        return (Schema)this.schemas.get(DataFixerUpper.getLowestSchemaSameVersion(this.schemas, n));
    }

    protected Type<?> getType(DSL.TypeReference typeReference, int n) {
        return this.getSchema(DataFixUtils.makeKey(n)).getTypeRaw(typeReference);
    }

    protected static int getLowestSchemaSameVersion(Int2ObjectSortedMap<Schema> int2ObjectSortedMap, int n) {
        if (n < int2ObjectSortedMap.firstIntKey()) {
            return int2ObjectSortedMap.firstIntKey();
        }
        return int2ObjectSortedMap.subMap(0, n + 1).lastIntKey();
    }

    private int getLowestFixSameVersion(int n) {
        if (n < this.fixerVersions.firstInt()) {
            return this.fixerVersions.firstInt() - 1;
        }
        return this.fixerVersions.subSet(0, n + 1).lastInt();
    }

    protected TypeRewriteRule getRule(int n, int n2) {
        if (n >= n2) {
            return TypeRewriteRule.nop();
        }
        long l2 = (long)n << 32 | (long)n2;
        return (TypeRewriteRule)this.rules.computeIfAbsent(l2, l -> {
            int n3 = this.getLowestFixSameVersion(DataFixUtils.makeKey(n));
            ArrayList arrayList = Lists.newArrayList();
            for (DataFix dataFix : this.globalList) {
                TypeRewriteRule typeRewriteRule;
                int n4 = dataFix.getVersionKey();
                int n5 = DataFixUtils.getVersion(n4);
                if (n4 <= n3 || n5 > n2 || (typeRewriteRule = dataFix.getRule()) == TypeRewriteRule.nop()) continue;
                arrayList.add(typeRewriteRule);
            }
            return TypeRewriteRule.seq(arrayList);
        });
    }

    protected IntSortedSet fixerVersions() {
        return this.fixerVersions;
    }
}

