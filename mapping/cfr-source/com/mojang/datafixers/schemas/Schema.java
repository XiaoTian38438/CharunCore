/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 */
package com.mojang.datafixers.schemas;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.types.families.TypeFamily;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.TypeTemplate;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class Schema {
    private final Object2IntMap<String> recursiveTypes = new Object2IntOpenHashMap();
    private final Map<String, Supplier<TypeTemplate>> typeTemplates = Maps.newHashMap();
    private final Map<String, Type<?>> types;
    private final int versionKey;
    private final String name;
    private final Schema parent;

    public Schema(int n, Schema schema) {
        this.versionKey = n;
        int n2 = DataFixUtils.getSubVersion(n);
        this.name = "V" + DataFixUtils.getVersion(n) + (String)(n2 == 0 ? "" : "." + n2);
        this.parent = schema;
        this.registerTypes(this, this.registerEntities(this), this.registerBlockEntities(this));
        this.types = this.buildTypes();
    }

    protected Map<String, Type<?>> buildTypes() {
        Object object2;
        HashMap hashMap = Maps.newHashMap();
        ArrayList arrayList = Lists.newArrayList();
        for (Object object2 : this.recursiveTypes.object2IntEntrySet()) {
            arrayList.add(DSL.check((String)object2.getKey(), object2.getIntValue(), this.getTemplate((String)object2.getKey())));
        }
        Object object3 = (TypeTemplate)arrayList.stream().reduce(DSL::or).get();
        object2 = new RecursiveTypeFamily(this.name, (TypeTemplate)object3);
        for (String string : this.typeTemplates.keySet()) {
            int n = this.recursiveTypes.getOrDefault((Object)string, -1);
            Type<?> type = n != -1 ? object2.apply(n) : this.getTemplate(string).apply((TypeFamily)object2).apply(-1);
            hashMap.put(string, type);
        }
        return hashMap;
    }

    public Set<String> types() {
        return this.types.keySet();
    }

    public Type<?> getTypeRaw(DSL.TypeReference typeReference) {
        String string = typeReference.typeName();
        return this.types.computeIfAbsent(string, string2 -> {
            throw new IllegalArgumentException("Unknown type: " + string);
        });
    }

    public Type<?> getType(DSL.TypeReference typeReference) {
        String string = typeReference.typeName();
        Type type = this.types.computeIfAbsent(string, string2 -> {
            throw new IllegalArgumentException("Unknown type: " + string);
        });
        if (type instanceof RecursivePoint.RecursivePointType) {
            return type.findCheckedType(-1).orElseThrow(() -> new IllegalStateException("Could not find choice type in the recursive type"));
        }
        return type;
    }

    public TypeTemplate resolveTemplate(String string) {
        return this.typeTemplates.getOrDefault(string, () -> {
            throw new IllegalArgumentException("Unknown type: " + string);
        }).get();
    }

    public TypeTemplate id(String string) {
        int n = this.recursiveTypes.getOrDefault((Object)string, -1);
        if (n != -1) {
            return DSL.id(n);
        }
        return this.getTemplate(string);
    }

    protected TypeTemplate getTemplate(String string) {
        return DSL.named(string, this.resolveTemplate(string));
    }

    public Type<?> getChoiceType(DSL.TypeReference typeReference, String string) {
        TaggedChoice.TaggedChoiceType<?> taggedChoiceType = this.findChoiceType(typeReference);
        if (!taggedChoiceType.types().containsKey(string)) {
            throw new IllegalArgumentException("Data fixer not registered for: " + string + " in " + typeReference.typeName());
        }
        return taggedChoiceType.types().get(string);
    }

    public TaggedChoice.TaggedChoiceType<?> findChoiceType(DSL.TypeReference typeReference) {
        return this.getType(typeReference).findChoiceType("id", -1).orElseThrow(() -> new IllegalArgumentException("Not a choice type"));
    }

    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        this.parent.registerTypes(schema, map, map2);
    }

    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        return this.parent.registerEntities(schema);
    }

    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        return this.parent.registerBlockEntities(schema);
    }

    public void registerSimple(Map<String, Supplier<TypeTemplate>> map, String string) {
        this.register(map, string, DSL::remainder);
    }

    public void register(Map<String, Supplier<TypeTemplate>> map, String string, Function<String, TypeTemplate> function) {
        this.register(map, string, () -> (TypeTemplate)function.apply(string));
    }

    public void register(Map<String, Supplier<TypeTemplate>> map, String string, Supplier<TypeTemplate> supplier) {
        map.put(string, supplier);
    }

    public void registerType(boolean bl, DSL.TypeReference typeReference, Supplier<TypeTemplate> supplier) {
        this.typeTemplates.put(typeReference.typeName(), supplier);
        if (bl && !this.recursiveTypes.containsKey((Object)typeReference.typeName())) {
            this.recursiveTypes.put((Object)typeReference.typeName(), this.recursiveTypes.size());
        }
    }

    public int getVersionKey() {
        return this.versionKey;
    }

    public Schema getParent() {
        return this.parent;
    }
}

