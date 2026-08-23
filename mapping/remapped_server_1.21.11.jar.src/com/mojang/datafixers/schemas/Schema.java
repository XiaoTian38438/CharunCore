/*     */ package com.mojang.datafixers.schemas;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ public class Schema
/*     */ {
/*  25 */   private final Object2IntMap<String> recursiveTypes = (Object2IntMap<String>)new Object2IntOpenHashMap();
/*  26 */   private final Map<String, Supplier<TypeTemplate>> typeTemplates = Maps.newHashMap();
/*     */   private final Map<String, Type<?>> types;
/*     */   private final int versionKey;
/*     */   private final String name;
/*     */   private final Schema parent;
/*     */   
/*     */   public Schema(int paramInt, Schema paramSchema) {
/*  33 */     this.versionKey = paramInt;
/*  34 */     int i = DataFixUtils.getSubVersion(paramInt);
/*  35 */     this.name = "V" + DataFixUtils.getVersion(paramInt) + ((i == 0) ? "" : ("." + i));
/*  36 */     this.parent = paramSchema;
/*  37 */     registerTypes(this, registerEntities(this), registerBlockEntities(this));
/*  38 */     this.types = buildTypes();
/*     */   }
/*     */   
/*     */   protected Map<String, Type<?>> buildTypes() {
/*  42 */     HashMap<String, Type> hashMap = Maps.newHashMap();
/*     */     
/*  44 */     ArrayList<TypeTemplate> arrayList = Lists.newArrayList();
/*     */     
/*  46 */     for (ObjectIterator<Object2IntMap.Entry> objectIterator = this.recursiveTypes.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry entry = objectIterator.next();
/*  47 */       arrayList.add(DSL.check((String)entry.getKey(), entry.getIntValue(), getTemplate((String)entry.getKey()))); }
/*     */ 
/*     */     
/*  50 */     TypeTemplate typeTemplate = arrayList.stream().reduce(DSL::or).get();
/*  51 */     RecursiveTypeFamily recursiveTypeFamily = new RecursiveTypeFamily(this.name, typeTemplate);
/*     */     
/*  53 */     for (String str : this.typeTemplates.keySet()) {
/*     */       Type type;
/*  55 */       int i = this.recursiveTypes.getOrDefault(str, -1);
/*  56 */       if (i != -1) {
/*  57 */         type = recursiveTypeFamily.apply(i);
/*     */       } else {
/*  59 */         type = getTemplate(str).apply((TypeFamily)recursiveTypeFamily).apply(-1);
/*     */       } 
/*  61 */       hashMap.put(str, type);
/*     */     } 
/*  63 */     return (Map)hashMap;
/*     */   }
/*     */   
/*     */   public Set<String> types() {
/*  67 */     return this.types.keySet();
/*     */   }
/*     */   
/*     */   public Type<?> getTypeRaw(DSL.TypeReference paramTypeReference) {
/*  71 */     String str = paramTypeReference.typeName();
/*  72 */     return this.types.computeIfAbsent(str, paramString2 -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + paramString1);
/*     */         });
/*     */   }
/*     */   
/*     */   public Type<?> getType(DSL.TypeReference paramTypeReference) {
/*  78 */     String str = paramTypeReference.typeName();
/*  79 */     Type<?> type = this.types.computeIfAbsent(str, paramString2 -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + paramString1);
/*     */         });
/*  82 */     if (type instanceof com.mojang.datafixers.types.templates.RecursivePoint.RecursivePointType) {
/*  83 */       return (Type)type.findCheckedType(-1).orElseThrow(() -> new IllegalStateException("Could not find choice type in the recursive type"));
/*     */     }
/*  85 */     return type;
/*     */   }
/*     */   
/*     */   public TypeTemplate resolveTemplate(String paramString) {
/*  89 */     return ((Supplier<TypeTemplate>)this.typeTemplates.getOrDefault(paramString, () -> {
/*     */           throw new IllegalArgumentException("Unknown type: " + paramString);
/*  91 */         })).get();
/*     */   }
/*     */   
/*     */   public TypeTemplate id(String paramString) {
/*  95 */     int i = this.recursiveTypes.getOrDefault(paramString, -1);
/*  96 */     if (i != -1) {
/*  97 */       return DSL.id(i);
/*     */     }
/*  99 */     return getTemplate(paramString);
/*     */   }
/*     */   
/*     */   protected TypeTemplate getTemplate(String paramString) {
/* 103 */     return DSL.named(paramString, resolveTemplate(paramString));
/*     */   }
/*     */   
/*     */   public Type<?> getChoiceType(DSL.TypeReference paramTypeReference, String paramString) {
/* 107 */     TaggedChoice.TaggedChoiceType<?> taggedChoiceType = findChoiceType(paramTypeReference);
/* 108 */     if (!taggedChoiceType.types().containsKey(paramString)) {
/* 109 */       throw new IllegalArgumentException("Data fixer not registered for: " + paramString + " in " + paramTypeReference.typeName());
/*     */     }
/* 111 */     return (Type)taggedChoiceType.types().get(paramString);
/*     */   }
/*     */   
/*     */   public TaggedChoice.TaggedChoiceType<?> findChoiceType(DSL.TypeReference paramTypeReference) {
/* 115 */     return (TaggedChoice.TaggedChoiceType)getType(paramTypeReference).findChoiceType("id", -1).orElseThrow(() -> new IllegalArgumentException("Not a choice type"));
/*     */   }
/*     */   
/*     */   public void registerTypes(Schema paramSchema, Map<String, Supplier<TypeTemplate>> paramMap1, Map<String, Supplier<TypeTemplate>> paramMap2) {
/* 119 */     this.parent.registerTypes(paramSchema, paramMap1, paramMap2);
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema paramSchema) {
/* 123 */     return this.parent.registerEntities(paramSchema);
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema paramSchema) {
/* 127 */     return this.parent.registerBlockEntities(paramSchema);
/*     */   }
/*     */   
/*     */   public void registerSimple(Map<String, Supplier<TypeTemplate>> paramMap, String paramString) {
/* 131 */     register(paramMap, paramString, DSL::remainder);
/*     */   }
/*     */   
/*     */   public void register(Map<String, Supplier<TypeTemplate>> paramMap, String paramString, Function<String, TypeTemplate> paramFunction) {
/* 135 */     register(paramMap, paramString, () -> (TypeTemplate)paramFunction.apply(paramString));
/*     */   }
/*     */   
/*     */   public void register(Map<String, Supplier<TypeTemplate>> paramMap, String paramString, Supplier<TypeTemplate> paramSupplier) {
/* 139 */     paramMap.put(paramString, paramSupplier);
/*     */   }
/*     */   
/*     */   public void registerType(boolean paramBoolean, DSL.TypeReference paramTypeReference, Supplier<TypeTemplate> paramSupplier) {
/* 143 */     this.typeTemplates.put(paramTypeReference.typeName(), paramSupplier);
/*     */     
/* 145 */     if (paramBoolean && !this.recursiveTypes.containsKey(paramTypeReference.typeName())) {
/* 146 */       this.recursiveTypes.put(paramTypeReference.typeName(), this.recursiveTypes.size());
/*     */     }
/*     */   }
/*     */   
/*     */   public int getVersionKey() {
/* 151 */     return this.versionKey;
/*     */   }
/*     */   
/*     */   public Schema getParent() {
/* 155 */     return this.parent;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\schemas\Schema.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */