/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FunctionType;
/*     */ import com.mojang.datafixers.RewriteResult;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.TypedOptic;
/*     */ import com.mojang.datafixers.View;
/*     */ import com.mojang.datafixers.functions.Functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.Affine;
/*     */ import com.mojang.datafixers.optics.Lens;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.families.RecursiveTypeFamily;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TaggedChoiceType<K>
/*     */   extends Type<Pair<K, ?>>
/*     */ {
/*     */   private final String name;
/*     */   private final Type<K> keyType;
/*     */   protected final Object2ObjectMap<K, Type<?>> types;
/*     */   private final int hashCode;
/*     */   
/*     */   public TaggedChoiceType(String paramString, Type<K> paramType, Object2ObjectMap<K, Type<?>> paramObject2ObjectMap) {
/* 136 */     this.name = paramString;
/* 137 */     this.keyType = paramType;
/* 138 */     this.types = paramObject2ObjectMap;
/* 139 */     this.hashCode = Objects.hash(new Object[] { paramString, paramType, paramObject2ObjectMap });
/*     */   }
/*     */ 
/*     */   
/*     */   public RewriteResult<Pair<K, ?>, ?> all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 144 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap1 = new Object2ObjectOpenHashMap(this.types.size());
/* 145 */     for (ObjectIterator<Map.Entry> objectIterator1 = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator1.hasNext(); ) { Map.Entry entry = objectIterator1.next();
/* 146 */       Optional<RewriteResult> optional = paramTypeRewriteRule.rewrite((Type)entry.getValue());
/* 147 */       if (optional.isPresent() && !((RewriteResult)optional.get()).view().isNop()) {
/* 148 */         object2ObjectOpenHashMap1.put(entry.getKey(), optional.get());
/*     */       } }
/*     */ 
/*     */     
/* 152 */     if (object2ObjectOpenHashMap1.isEmpty())
/* 153 */       return RewriteResult.nop(this); 
/* 154 */     if (object2ObjectOpenHashMap1.size() == 1) {
/* 155 */       Map.Entry entry = (Map.Entry)object2ObjectOpenHashMap1.entrySet().iterator().next();
/* 156 */       return elementResult((K)entry.getKey(), this, (RewriteResult<?, ?>)entry.getValue());
/*     */     } 
/* 158 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap2 = new Object2ObjectOpenHashMap(this.types);
/* 159 */     BitSet bitSet = new BitSet();
/* 160 */     for (ObjectIterator<Map.Entry> objectIterator2 = Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap1).iterator(); objectIterator2.hasNext(); ) { Map.Entry entry = objectIterator2.next();
/* 161 */       object2ObjectOpenHashMap2.put(entry.getKey(), ((RewriteResult)entry.getValue()).view().newType());
/* 162 */       bitSet.or(((RewriteResult)entry.getValue()).recData()); }
/*     */     
/* 164 */     return RewriteResult.create(View.create(Functions.fun("TaggedChoiceTypeRewriteResult " + object2ObjectOpenHashMap1.size(), new RewriteFunc((Map<?, ? extends RewriteResult<?, ?>>)object2ObjectOpenHashMap1), this, DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap2))), bitSet);
/*     */   }
/*     */   
/*     */   public static <K, FT, FR> RewriteResult<Pair<K, ?>, Pair<K, ?>> elementResult(K paramK, TaggedChoiceType<K> paramTaggedChoiceType, RewriteResult<FT, FR> paramRewriteResult) {
/* 168 */     return opticView(paramTaggedChoiceType, paramRewriteResult, TypedOptic.tagged(paramTaggedChoiceType, paramK, paramRewriteResult.view().type(), paramRewriteResult.view().newType()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<RewriteResult<Pair<K, ?>, ?>> one(TypeRewriteRule paramTypeRewriteRule) {
/* 173 */     for (ObjectIterator<Map.Entry> objectIterator = this.types.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/* 174 */       Optional<RewriteResult<?, ?>> optional = paramTypeRewriteRule.rewrite((Type)entry.getValue());
/* 175 */       if (optional.isPresent()) {
/* 176 */         return Optional.of(elementResult((K)entry.getKey(), this, optional.get()));
/*     */       } }
/*     */     
/* 179 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Type<?> updateMu(RecursiveTypeFamily paramRecursiveTypeFamily) {
/* 184 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
/* 185 */     for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 186 */       object2ObjectOpenHashMap.put(entry.getKey(), ((Type)entry.getValue()).updateMu(paramRecursiveTypeFamily)); }
/*     */     
/* 188 */     return DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeTemplate buildTemplate() {
/* 193 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
/* 194 */     for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 195 */       object2ObjectOpenHashMap.put(entry.getKey(), ((Type)entry.getValue()).template()); }
/*     */     
/* 197 */     return DSL.taggedChoice(this.name, this.keyType, (Map)object2ObjectOpenHashMap);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Codec<Pair<K, ?>> buildCodec() {
/* 202 */     return this.keyType.codec().partialDispatch(this.name, paramPair -> DataResult.success(paramPair.getFirst()), paramObject -> getMapCodec((K)paramObject).map(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> MapCodec<Pair<K, V>> asEntryPair(K paramK, MapCodec<V> paramMapCodec) {
/* 210 */     return paramMapCodec.xmap(paramObject2 -> Pair.of(paramObject1, paramObject2), Pair::getSecond);
/*     */   }
/*     */   
/*     */   private DataResult<? extends MapCodec<?>> getMapCodec(K paramK) {
/* 214 */     return Optional.<Type>ofNullable((Type)this.types.get(paramK))
/* 215 */       .map(paramType -> DataResult.success(MapCodec.assumeMapUnsafe(paramType.codec())))
/* 216 */       .orElseGet(() -> DataResult.error(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findFieldTypeOpt(String paramString) {
/* 221 */     return this.types.values().stream().map(paramType -> paramType.findFieldTypeOpt(paramString)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Pair<K, ?>> point(DynamicOps<?> paramDynamicOps) {
/* 226 */     return this.types.entrySet().stream().map(paramEntry -> ((Type)paramEntry.getValue()).point(paramDynamicOps).map(())).filter(Optional::isPresent).findFirst().flatMap(Function.identity()).map(paramPair -> paramPair);
/*     */   }
/*     */   
/*     */   public Optional<Typed<Pair<K, ?>>> point(DynamicOps<?> paramDynamicOps, K paramK, Object paramObject) {
/* 230 */     if (!this.types.containsKey(paramK)) {
/* 231 */       return Optional.empty();
/*     */     }
/* 233 */     return Optional.of(new Typed(this, paramDynamicOps, Pair.of(paramK, paramObject)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <FT, FR> Either<TypedOptic<Pair<K, ?>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> paramType, Type<FR> paramType1, Type.TypeMatcher<FT, FR> paramTypeMatcher, boolean paramBoolean) {
/*     */     Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR> traversal;
/*     */     TypeToken typeToken;
/* 242 */     final Map optics = (Map)this.types.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), ((Type)paramEntry.getValue()).findType(paramType1, paramType2, paramTypeMatcher, paramBoolean))).filter(paramPair -> ((Either)paramPair.getSecond()).left().isPresent()).map(paramPair -> paramPair.mapSecond(())).collect(Pair.toMap());
/*     */ 
/*     */     
/* 245 */     if (map.isEmpty())
/* 246 */       return Either.right(new Type.FieldNotFoundException("Not found in any choices")); 
/* 247 */     if (map.size() == 1) {
/* 248 */       Map.Entry entry = map.entrySet().iterator().next();
/* 249 */       return Either.left(cap(this, entry.getKey(), (TypedOptic<?, ?, ?, ?>)entry.getValue()));
/*     */     } 
/* 251 */     HashSet hashSet = Sets.newHashSet();
/* 252 */     map.values().forEach(paramTypedOptic -> paramSet.addAll(paramTypedOptic.bounds()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     if (TypedOptic.instanceOf(hashSet, Cartesian.Mu.TYPE_TOKEN) && map.size() == this.types.size()) {
/* 260 */       typeToken = Cartesian.Mu.TYPE_TOKEN;
/*     */       
/* 262 */       Lens<Pair<K, ?>, Pair<K, ?>, FT, FR> lens = new Lens<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */         {
/*     */           public FT view(Pair<K, ?> param2Pair) {
/* 265 */             TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 266 */             return (FT)capView(param2Pair, typedOptic);
/*     */           }
/*     */ 
/*     */           
/*     */           private <S, T> FT capView(Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 271 */             return (FT)Optics.toLens((Optic)param2TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).view(param2Pair.getSecond());
/*     */           }
/*     */ 
/*     */           
/*     */           public Pair<K, ?> update(FR param2FR, Pair<K, ?> param2Pair) {
/* 276 */             TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 277 */             return capUpdate(param2FR, param2Pair, typedOptic);
/*     */           }
/*     */ 
/*     */           
/*     */           private <S, T> Pair<K, ?> capUpdate(FR param2FR, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 282 */             return Pair.of(param2Pair.getFirst(), Optics.toLens((Optic)param2TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).update(param2FR, param2Pair.getSecond()));
/*     */           }
/*     */         };
/* 285 */     } else if (TypedOptic.instanceOf(hashSet, AffineP.Mu.TYPE_TOKEN)) {
/* 286 */       typeToken = AffineP.Mu.TYPE_TOKEN;
/*     */       
/* 288 */       Affine<Pair<K, ?>, Pair<K, ?>, FT, FR> affine = new Affine<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */         {
/*     */           public Either<Pair<K, ?>, FT> preview(Pair<K, ?> param2Pair) {
/* 291 */             if (!optics.containsKey(param2Pair.getFirst())) {
/* 292 */               return Either.left(param2Pair);
/*     */             }
/* 294 */             TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 295 */             return capPreview(param2Pair, typedOptic);
/*     */           }
/*     */ 
/*     */           
/*     */           private <S, T> Either<Pair<K, ?>, FT> capPreview(Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 300 */             return Optics.toAffine((Optic)param2TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).preview(param2Pair.getSecond()).mapLeft(param2Object -> Pair.of(param2Pair.getFirst(), param2Object));
/*     */           }
/*     */ 
/*     */           
/*     */           public Pair<K, ?> set(FR param2FR, Pair<K, ?> param2Pair) {
/* 305 */             if (!optics.containsKey(param2Pair.getFirst())) {
/* 306 */               return param2Pair;
/*     */             }
/* 308 */             TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 309 */             return capSet(param2FR, param2Pair, typedOptic);
/*     */           }
/*     */ 
/*     */           
/*     */           private <S, T> Pair<K, ?> capSet(FR param2FR, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 314 */             return Pair.of(param2Pair.getFirst(), Optics.toAffine((Optic)param2TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).set(param2FR, param2Pair.getSecond()));
/*     */           }
/*     */         };
/* 317 */     } else if (TypedOptic.instanceOf(hashSet, TraversalP.Mu.TYPE_TOKEN)) {
/* 318 */       typeToken = TraversalP.Mu.TYPE_TOKEN;
/*     */       
/* 320 */       traversal = new Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */         {
/*     */           public <F extends K1> FunctionType<Pair<K, ?>, App<F, Pair<K, ?>>> wander(Applicative<F, ?> param2Applicative, FunctionType<FT, App<F, FR>> param2FunctionType) {
/* 323 */             return param2Pair -> {
/*     */                 if (!param2Map.containsKey(param2Pair.getFirst())) {
/*     */                   return param2Applicative.point(param2Pair);
/*     */                 }
/*     */                 TypedOptic typedOptic = (TypedOptic)param2Map.get(param2Pair.getFirst());
/*     */                 return capTraversal(param2Applicative, param2FunctionType, param2Pair, typedOptic);
/*     */               };
/*     */           }
/*     */ 
/*     */           
/*     */           private <S, T, F extends K1> App<F, Pair<K, ?>> capTraversal(Applicative<F, ?> param2Applicative, FunctionType<FT, App<F, FR>> param2FunctionType, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 334 */             Traversal traversal = Optics.toTraversal((Optic)param2TypedOptic.upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 335 */             return param2Applicative.ap(param2Object -> Pair.of(param2Pair.getFirst(), param2Object), (App)traversal.wander(param2Applicative, param2FunctionType).apply(param2Pair.getSecond()));
/*     */           }
/*     */         };
/*     */     } else {
/* 339 */       throw new IllegalStateException("Could not merge TaggedChoiceType optics, unknown bound: " + Arrays.toString(hashSet.toArray()));
/*     */     } 
/*     */     
/* 342 */     Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types);
/* 343 */     for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 344 */       TypedOptic typedOptic = (TypedOptic)map.get(entry.getKey());
/* 345 */       if (typedOptic != null) {
/* 346 */         entry.setValue(typedOptic.tType());
/*     */       } }
/*     */ 
/*     */     
/* 350 */     return Either.left(new TypedOptic(typeToken, this, 
/*     */ 
/*     */           
/* 353 */           DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap), paramType, paramType1, (Optic)traversal));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <S, T, FT, FR> TypedOptic<Pair<K, ?>, Pair<K, ?>, FT, FR> cap(TaggedChoiceType<K> paramTaggedChoiceType, K paramK, TypedOptic<S, T, FT, FR> paramTypedOptic) {
/* 362 */     return TypedOptic.tagged(paramTaggedChoiceType, paramK, paramTypedOptic.sType(), paramTypedOptic.tType()).compose(paramTypedOptic);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<TaggedChoiceType<?>> findChoiceType(String paramString, int paramInt) {
/* 367 */     if (Objects.equals(paramString, this.name)) {
/* 368 */       return Optional.of(this);
/*     */     }
/* 370 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Type<?>> findCheckedType(int paramInt) {
/* 375 */     return this.types.values().stream().map(paramType -> paramType.findCheckedType(paramInt)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject, boolean paramBoolean1, boolean paramBoolean2) {
/* 380 */     if (this == paramObject) {
/* 381 */       return true;
/*     */     }
/* 383 */     if (!(paramObject instanceof TaggedChoiceType)) {
/* 384 */       return false;
/*     */     }
/* 386 */     TaggedChoiceType taggedChoiceType = (TaggedChoiceType)paramObject;
/* 387 */     if (!Objects.equals(this.name, taggedChoiceType.name)) {
/* 388 */       return false;
/*     */     }
/* 390 */     if (!this.keyType.equals(taggedChoiceType.keyType, paramBoolean1, paramBoolean2)) {
/* 391 */       return false;
/*     */     }
/* 393 */     if (this.types.size() != taggedChoiceType.types.size()) {
/* 394 */       return false;
/*     */     }
/* 396 */     for (ObjectIterator<Map.Entry> objectIterator = this.types.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/* 397 */       if (!((Type)entry.getValue()).equals(taggedChoiceType.types.get(entry.getKey()), paramBoolean1, paramBoolean2)) {
/* 398 */         return false;
/*     */       } }
/*     */     
/* 401 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 406 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 411 */     return "TaggedChoiceType[" + this.name + ", " + Joiner.on(", \n").withKeyValueSeparator(" -> ").join((Map)this.types) + "]\n";
/*     */   }
/*     */   
/*     */   public String getName() {
/* 415 */     return this.name;
/*     */   }
/*     */   
/*     */   public Type<K> getKeyType() {
/* 419 */     return this.keyType;
/*     */   }
/*     */   
/*     */   public boolean hasType(K paramK) {
/* 423 */     return this.types.containsKey(paramK);
/*     */   }
/*     */   
/*     */   public Map<K, Type<?>> types() {
/* 427 */     return (Map<K, Type<?>>)this.types;
/*     */   }
/*     */   
/*     */   private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
/*     */     private final Map<K, ? extends RewriteResult<?, ?>> results;
/*     */     
/*     */     public RewriteFunc(Map<K, ? extends RewriteResult<?, ?>> param2Map) {
/* 434 */       this.results = param2Map;
/*     */     }
/*     */ 
/*     */     
/*     */     public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(DynamicOps<?> param2DynamicOps) {
/* 439 */       return param2Pair -> {
/*     */           RewriteResult<?, ?> rewriteResult = this.results.get(param2Pair.getFirst());
/*     */           return (rewriteResult == null) ? param2Pair : capRuleApply(param2DynamicOps, param2Pair, rewriteResult);
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <A, B> Pair<K, B> capRuleApply(DynamicOps<?> param2DynamicOps, Pair<K, ?> param2Pair, RewriteResult<A, B> param2RewriteResult) {
/* 450 */       return param2Pair.mapSecond(param2Object -> ((Function)param2RewriteResult.view().function().evalCached().apply(param2DynamicOps)).apply(param2Object));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param2Object) {
/* 455 */       if (this == param2Object) {
/* 456 */         return true;
/*     */       }
/* 458 */       if (param2Object == null || getClass() != param2Object.getClass()) {
/* 459 */         return false;
/*     */       }
/* 461 */       RewriteFunc rewriteFunc = (RewriteFunc)param2Object;
/* 462 */       return Objects.equals(this.results, rewriteFunc.results);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 467 */       return this.results.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\TaggedChoice$TaggedChoiceType.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */