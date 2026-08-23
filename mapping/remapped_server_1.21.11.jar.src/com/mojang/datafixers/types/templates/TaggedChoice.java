/*     */ package com.mojang.datafixers.types.templates;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.FamilyOptic;
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
/*     */ import com.mojang.datafixers.types.families.TypeFamily;
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
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public final class TaggedChoice<K> implements TypeTemplate {
/*     */   private final String name;
/*     */   private final Type<K> keyType;
/*     */   private final Object2ObjectMap<K, TypeTemplate> templates;
/*  56 */   private final Map<Pair<TypeFamily, Integer>, Type<?>> types = Maps.newConcurrentMap();
/*     */   private final int size;
/*     */   
/*     */   public TaggedChoice(String paramString, Type<K> paramType, Object2ObjectMap<K, TypeTemplate> paramObject2ObjectMap) {
/*  60 */     this.name = paramString;
/*  61 */     this.keyType = paramType;
/*  62 */     this.templates = paramObject2ObjectMap;
/*  63 */     this.size = paramObject2ObjectMap.values().stream().mapToInt(TypeTemplate::size).max().orElse(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  68 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeFamily apply(TypeFamily paramTypeFamily) {
/*  73 */     return paramInt -> (Type)this.types.computeIfAbsent(Pair.of(paramTypeFamily, Integer.valueOf(paramInt)), ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A, B> FamilyOptic<A, B> applyO(FamilyOptic<A, B> paramFamilyOptic, Type<A> paramType, Type<B> paramType1) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A, B> Either<TypeTemplate, Type.FieldNotFoundException> findFieldOrType(int paramInt, @Nullable String paramString, Type<A> paramType, Type<B> paramType1) {
/*  89 */     return Either.right(new Type.FieldNotFoundException("Not implemented"));
/*     */   }
/*     */ 
/*     */   
/*     */   public IntFunction<RewriteResult<?, ?>> hmap(TypeFamily paramTypeFamily, IntFunction<RewriteResult<?, ?>> paramIntFunction) {
/*  94 */     return paramInt -> {
/*     */         RewriteResult rewriteResult = RewriteResult.nop(apply(paramTypeFamily).apply(paramInt));
/*     */         ObjectIterator<Map.Entry> objectIterator = this.templates.entrySet().iterator();
/*     */         while (objectIterator.hasNext()) {
/*     */           Map.Entry entry = objectIterator.next();
/*     */           RewriteResult<?, ?> rewriteResult1 = ((TypeTemplate)entry.getValue()).hmap(paramTypeFamily, paramIntFunction).apply(paramInt);
/*     */           rewriteResult = TaggedChoiceType.elementResult((K)entry.getKey(), (TaggedChoiceType<K>)rewriteResult.view().newType(), rewriteResult1).compose(rewriteResult);
/*     */         } 
/*     */         return rewriteResult;
/*     */       };
/*     */   }
/*     */   public boolean equals(Object paramObject) {
/* 106 */     if (this == paramObject) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (!(paramObject instanceof TaggedChoice)) {
/* 110 */       return false;
/*     */     }
/* 112 */     TaggedChoice taggedChoice = (TaggedChoice)paramObject;
/* 113 */     return (Objects.equals(this.name, taggedChoice.name) && Objects.equals(this.keyType, taggedChoice.keyType) && Objects.equals(this.templates, taggedChoice.templates));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     int i = this.name.hashCode();
/* 119 */     i = 31 * i + this.keyType.hashCode();
/* 120 */     i = 31 * i + this.templates.hashCode();
/* 121 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return "TaggedChoice[" + this.name + ", " + Joiner.on(", ").withKeyValueSeparator(" -> ").join((Map)this.templates) + "]";
/*     */   }
/*     */   
/*     */   public static final class TaggedChoiceType<K> extends Type<Pair<K, ?>> {
/*     */     private final String name;
/*     */     private final Type<K> keyType;
/*     */     protected final Object2ObjectMap<K, Type<?>> types;
/*     */     private final int hashCode;
/*     */     
/*     */     public TaggedChoiceType(String param1String, Type<K> param1Type, Object2ObjectMap<K, Type<?>> param1Object2ObjectMap) {
/* 136 */       this.name = param1String;
/* 137 */       this.keyType = param1Type;
/* 138 */       this.types = param1Object2ObjectMap;
/* 139 */       this.hashCode = Objects.hash(new Object[] { param1String, param1Type, param1Object2ObjectMap });
/*     */     }
/*     */ 
/*     */     
/*     */     public RewriteResult<Pair<K, ?>, ?> all(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 144 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap1 = new Object2ObjectOpenHashMap(this.types.size());
/* 145 */       for (ObjectIterator<Map.Entry> objectIterator1 = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator1.hasNext(); ) { Map.Entry entry = objectIterator1.next();
/* 146 */         Optional<RewriteResult> optional = param1TypeRewriteRule.rewrite((Type)entry.getValue());
/* 147 */         if (optional.isPresent() && !((RewriteResult)optional.get()).view().isNop()) {
/* 148 */           object2ObjectOpenHashMap1.put(entry.getKey(), optional.get());
/*     */         } }
/*     */ 
/*     */       
/* 152 */       if (object2ObjectOpenHashMap1.isEmpty())
/* 153 */         return RewriteResult.nop(this); 
/* 154 */       if (object2ObjectOpenHashMap1.size() == 1) {
/* 155 */         Map.Entry entry = (Map.Entry)object2ObjectOpenHashMap1.entrySet().iterator().next();
/* 156 */         return elementResult((K)entry.getKey(), this, (RewriteResult<?, ?>)entry.getValue());
/*     */       } 
/* 158 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap2 = new Object2ObjectOpenHashMap(this.types);
/* 159 */       BitSet bitSet = new BitSet();
/* 160 */       for (ObjectIterator<Map.Entry> objectIterator2 = Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap1).iterator(); objectIterator2.hasNext(); ) { Map.Entry entry = objectIterator2.next();
/* 161 */         object2ObjectOpenHashMap2.put(entry.getKey(), ((RewriteResult)entry.getValue()).view().newType());
/* 162 */         bitSet.or(((RewriteResult)entry.getValue()).recData()); }
/*     */       
/* 164 */       return RewriteResult.create(View.create(Functions.fun("TaggedChoiceTypeRewriteResult " + object2ObjectOpenHashMap1.size(), new RewriteFunc((Map<?, ? extends RewriteResult<?, ?>>)object2ObjectOpenHashMap1), this, DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap2))), bitSet);
/*     */     }
/*     */     
/*     */     public static <K, FT, FR> RewriteResult<Pair<K, ?>, Pair<K, ?>> elementResult(K param1K, TaggedChoiceType<K> param1TaggedChoiceType, RewriteResult<FT, FR> param1RewriteResult) {
/* 168 */       return opticView(param1TaggedChoiceType, param1RewriteResult, TypedOptic.tagged(param1TaggedChoiceType, param1K, param1RewriteResult.view().type(), param1RewriteResult.view().newType()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<RewriteResult<Pair<K, ?>, ?>> one(TypeRewriteRule param1TypeRewriteRule) {
/* 173 */       for (ObjectIterator<Map.Entry> objectIterator = this.types.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/* 174 */         Optional<RewriteResult<?, ?>> optional = param1TypeRewriteRule.rewrite((Type)entry.getValue());
/* 175 */         if (optional.isPresent()) {
/* 176 */           return Optional.of(elementResult((K)entry.getKey(), this, optional.get()));
/*     */         } }
/*     */       
/* 179 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Type<?> updateMu(RecursiveTypeFamily param1RecursiveTypeFamily) {
/* 184 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
/* 185 */       for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 186 */         object2ObjectOpenHashMap.put(entry.getKey(), ((Type)entry.getValue()).updateMu(param1RecursiveTypeFamily)); }
/*     */       
/* 188 */       return DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeTemplate buildTemplate() {
/* 193 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types.size());
/* 194 */       for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable(this.types).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 195 */         object2ObjectOpenHashMap.put(entry.getKey(), ((Type)entry.getValue()).template()); }
/*     */       
/* 197 */       return DSL.taggedChoice(this.name, this.keyType, (Map)object2ObjectOpenHashMap);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Codec<Pair<K, ?>> buildCodec() {
/* 202 */       return this.keyType.codec().partialDispatch(this.name, param1Pair -> DataResult.success(param1Pair.getFirst()), param1Object -> getMapCodec((K)param1Object).map(()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static <K, V> MapCodec<Pair<K, V>> asEntryPair(K param1K, MapCodec<V> param1MapCodec) {
/* 210 */       return param1MapCodec.xmap(param1Object2 -> Pair.of(param1Object1, param1Object2), Pair::getSecond);
/*     */     }
/*     */     
/*     */     private DataResult<? extends MapCodec<?>> getMapCodec(K param1K) {
/* 214 */       return Optional.<Type>ofNullable((Type)this.types.get(param1K))
/* 215 */         .map(param1Type -> DataResult.success(MapCodec.assumeMapUnsafe(param1Type.codec())))
/* 216 */         .orElseGet(() -> DataResult.error(()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findFieldTypeOpt(String param1String) {
/* 221 */       return this.types.values().stream().map(param1Type -> param1Type.findFieldTypeOpt(param1String)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Pair<K, ?>> point(DynamicOps<?> param1DynamicOps) {
/* 226 */       return this.types.entrySet().stream().map(param1Entry -> ((Type)param1Entry.getValue()).point(param1DynamicOps).map(())).filter(Optional::isPresent).findFirst().flatMap(Function.identity()).map(param1Pair -> param1Pair);
/*     */     }
/*     */     
/*     */     public Optional<Typed<Pair<K, ?>>> point(DynamicOps<?> param1DynamicOps, K param1K, Object param1Object) {
/* 230 */       if (!this.types.containsKey(param1K)) {
/* 231 */         return Optional.empty();
/*     */       }
/* 233 */       return Optional.of(new Typed(this, param1DynamicOps, Pair.of(param1K, param1Object)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <FT, FR> Either<TypedOptic<Pair<K, ?>, ?, FT, FR>, Type.FieldNotFoundException> findTypeInChildren(Type<FT> param1Type, Type<FR> param1Type1, Type.TypeMatcher<FT, FR> param1TypeMatcher, boolean param1Boolean) {
/*     */       Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR> traversal;
/*     */       TypeToken typeToken;
/* 242 */       final Map optics = (Map)this.types.entrySet().stream().map(param1Entry -> Pair.of(param1Entry.getKey(), ((Type)param1Entry.getValue()).findType(param1Type1, param1Type2, param1TypeMatcher, param1Boolean))).filter(param1Pair -> ((Either)param1Pair.getSecond()).left().isPresent()).map(param1Pair -> param1Pair.mapSecond(())).collect(Pair.toMap());
/*     */ 
/*     */       
/* 245 */       if (map.isEmpty())
/* 246 */         return Either.right(new Type.FieldNotFoundException("Not found in any choices")); 
/* 247 */       if (map.size() == 1) {
/* 248 */         Map.Entry entry = map.entrySet().iterator().next();
/* 249 */         return Either.left(cap(this, entry.getKey(), (TypedOptic<?, ?, ?, ?>)entry.getValue()));
/*     */       } 
/* 251 */       HashSet hashSet = Sets.newHashSet();
/* 252 */       map.values().forEach(param1TypedOptic -> param1Set.addAll(param1TypedOptic.bounds()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 259 */       if (TypedOptic.instanceOf(hashSet, Cartesian.Mu.TYPE_TOKEN) && map.size() == this.types.size()) {
/* 260 */         typeToken = Cartesian.Mu.TYPE_TOKEN;
/*     */         
/* 262 */         Lens<Pair<K, ?>, Pair<K, ?>, FT, FR> lens = new Lens<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public FT view(Pair<K, ?> param2Pair) {
/* 265 */               TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 266 */               return (FT)capView(param2Pair, typedOptic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> FT capView(Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 271 */               return (FT)Optics.toLens((Optic)param2TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).view(param2Pair.getSecond());
/*     */             }
/*     */ 
/*     */             
/*     */             public Pair<K, ?> update(FR param2FR, Pair<K, ?> param2Pair) {
/* 276 */               TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 277 */               return capUpdate(param2FR, param2Pair, typedOptic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Pair<K, ?> capUpdate(FR param2FR, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 282 */               return Pair.of(param2Pair.getFirst(), Optics.toLens((Optic)param2TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).update(param2FR, param2Pair.getSecond()));
/*     */             }
/*     */           };
/* 285 */       } else if (TypedOptic.instanceOf(hashSet, AffineP.Mu.TYPE_TOKEN)) {
/* 286 */         typeToken = AffineP.Mu.TYPE_TOKEN;
/*     */         
/* 288 */         Affine<Pair<K, ?>, Pair<K, ?>, FT, FR> affine = new Affine<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public Either<Pair<K, ?>, FT> preview(Pair<K, ?> param2Pair) {
/* 291 */               if (!optics.containsKey(param2Pair.getFirst())) {
/* 292 */                 return Either.left(param2Pair);
/*     */               }
/* 294 */               TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 295 */               return capPreview(param2Pair, typedOptic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Either<Pair<K, ?>, FT> capPreview(Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 300 */               return Optics.toAffine((Optic)param2TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).preview(param2Pair.getSecond()).mapLeft(param2Object -> Pair.of(param2Pair.getFirst(), param2Object));
/*     */             }
/*     */ 
/*     */             
/*     */             public Pair<K, ?> set(FR param2FR, Pair<K, ?> param2Pair) {
/* 305 */               if (!optics.containsKey(param2Pair.getFirst())) {
/* 306 */                 return param2Pair;
/*     */               }
/* 308 */               TypedOptic typedOptic = (TypedOptic)optics.get(param2Pair.getFirst());
/* 309 */               return capSet(param2FR, param2Pair, typedOptic);
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T> Pair<K, ?> capSet(FR param2FR, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 314 */               return Pair.of(param2Pair.getFirst(), Optics.toAffine((Optic)param2TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).set(param2FR, param2Pair.getSecond()));
/*     */             }
/*     */           };
/* 317 */       } else if (TypedOptic.instanceOf(hashSet, TraversalP.Mu.TYPE_TOKEN)) {
/* 318 */         typeToken = TraversalP.Mu.TYPE_TOKEN;
/*     */         
/* 320 */         traversal = new Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR>()
/*     */           {
/*     */             public <F extends K1> FunctionType<Pair<K, ?>, App<F, Pair<K, ?>>> wander(Applicative<F, ?> param2Applicative, FunctionType<FT, App<F, FR>> param2FunctionType) {
/* 323 */               return param2Pair -> {
/*     */                   if (!param2Map.containsKey(param2Pair.getFirst())) {
/*     */                     return param2Applicative.point(param2Pair);
/*     */                   }
/*     */                   TypedOptic typedOptic = (TypedOptic)param2Map.get(param2Pair.getFirst());
/*     */                   return capTraversal(param2Applicative, param2FunctionType, param2Pair, typedOptic);
/*     */                 };
/*     */             }
/*     */ 
/*     */             
/*     */             private <S, T, F extends K1> App<F, Pair<K, ?>> capTraversal(Applicative<F, ?> param2Applicative, FunctionType<FT, App<F, FR>> param2FunctionType, Pair<K, ?> param2Pair, TypedOptic<S, T, FT, FR> param2TypedOptic) {
/* 334 */               Traversal traversal = Optics.toTraversal((Optic)param2TypedOptic.upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 335 */               return param2Applicative.ap(param2Object -> Pair.of(param2Pair.getFirst(), param2Object), (App)traversal.wander(param2Applicative, param2FunctionType).apply(param2Pair.getSecond()));
/*     */             }
/*     */           };
/*     */       } else {
/* 339 */         throw new IllegalStateException("Could not merge TaggedChoiceType optics, unknown bound: " + Arrays.toString(hashSet.toArray()));
/*     */       } 
/*     */       
/* 342 */       Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap(this.types);
/* 343 */       for (ObjectIterator<Object2ObjectMap.Entry> objectIterator = Object2ObjectMaps.fastIterable((Object2ObjectMap)object2ObjectOpenHashMap).iterator(); objectIterator.hasNext(); ) { Object2ObjectMap.Entry entry = objectIterator.next();
/* 344 */         TypedOptic typedOptic = (TypedOptic)map.get(entry.getKey());
/* 345 */         if (typedOptic != null) {
/* 346 */           entry.setValue(typedOptic.tType());
/*     */         } }
/*     */ 
/*     */       
/* 350 */       return Either.left(new TypedOptic(typeToken, this, 
/*     */ 
/*     */             
/* 353 */             DSL.taggedChoiceType(this.name, this.keyType, (Map)object2ObjectOpenHashMap), param1Type, param1Type1, (Optic)traversal));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private <S, T, FT, FR> TypedOptic<Pair<K, ?>, Pair<K, ?>, FT, FR> cap(TaggedChoiceType<K> param1TaggedChoiceType, K param1K, TypedOptic<S, T, FT, FR> param1TypedOptic) {
/* 362 */       return TypedOptic.tagged(param1TaggedChoiceType, param1K, param1TypedOptic.sType(), param1TypedOptic.tType()).compose(param1TypedOptic);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<TaggedChoiceType<?>> findChoiceType(String param1String, int param1Int) {
/* 367 */       if (Objects.equals(param1String, this.name)) {
/* 368 */         return Optional.of(this);
/*     */       }
/* 370 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Type<?>> findCheckedType(int param1Int) {
/* 375 */       return this.types.values().stream().map(param1Type -> param1Type.findCheckedType(param1Int)).filter(Optional::isPresent).findFirst().flatMap(Function.identity());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object, boolean param1Boolean1, boolean param1Boolean2) {
/* 380 */       if (this == param1Object) {
/* 381 */         return true;
/*     */       }
/* 383 */       if (!(param1Object instanceof TaggedChoiceType)) {
/* 384 */         return false;
/*     */       }
/* 386 */       TaggedChoiceType taggedChoiceType = (TaggedChoiceType)param1Object;
/* 387 */       if (!Objects.equals(this.name, taggedChoiceType.name)) {
/* 388 */         return false;
/*     */       }
/* 390 */       if (!this.keyType.equals(taggedChoiceType.keyType, param1Boolean1, param1Boolean2)) {
/* 391 */         return false;
/*     */       }
/* 393 */       if (this.types.size() != taggedChoiceType.types.size()) {
/* 394 */         return false;
/*     */       }
/* 396 */       for (ObjectIterator<Map.Entry> objectIterator = this.types.entrySet().iterator(); objectIterator.hasNext(); ) { Map.Entry entry = objectIterator.next();
/* 397 */         if (!((Type)entry.getValue()).equals(taggedChoiceType.types.get(entry.getKey()), param1Boolean1, param1Boolean2)) {
/* 398 */           return false;
/*     */         } }
/*     */       
/* 401 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 406 */       return this.hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 411 */       return "TaggedChoiceType[" + this.name + ", " + Joiner.on(", \n").withKeyValueSeparator(" -> ").join((Map)this.types) + "]\n";
/*     */     }
/*     */     
/*     */     public String getName() {
/* 415 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<K> getKeyType() {
/* 419 */       return this.keyType;
/*     */     }
/*     */     
/*     */     public boolean hasType(K param1K) {
/* 423 */       return this.types.containsKey(param1K);
/*     */     }
/*     */     
/*     */     public Map<K, Type<?>> types() {
/* 427 */       return (Map<K, Type<?>>)this.types;
/*     */     }
/*     */     
/*     */     private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> {
/*     */       private final Map<K, ? extends RewriteResult<?, ?>> results;
/*     */       
/*     */       public RewriteFunc(Map<K, ? extends RewriteResult<?, ?>> param2Map) {
/* 434 */         this.results = param2Map;
/*     */       }
/*     */ 
/*     */       
/*     */       public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(DynamicOps<?> param2DynamicOps) {
/* 439 */         return param2Pair -> {
/*     */             RewriteResult<?, ?> rewriteResult = this.results.get(param2Pair.getFirst());
/*     */             return (rewriteResult == null) ? param2Pair : capRuleApply(param2DynamicOps, param2Pair, rewriteResult);
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private <A, B> Pair<K, B> capRuleApply(DynamicOps<?> param2DynamicOps, Pair<K, ?> param2Pair, RewriteResult<A, B> param2RewriteResult) {
/* 450 */         return param2Pair.mapSecond(param2Object -> ((Function)param2RewriteResult.view().function().evalCached().apply(param2DynamicOps)).apply(param2Object));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(Object param2Object) {
/* 455 */         if (this == param2Object) {
/* 456 */           return true;
/*     */         }
/* 458 */         if (param2Object == null || getClass() != param2Object.getClass()) {
/* 459 */           return false;
/*     */         }
/* 461 */         RewriteFunc rewriteFunc = (RewriteFunc)param2Object;
/* 462 */         return Objects.equals(this.results, rewriteFunc.results);
/*     */       }
/*     */       
/*     */       public int hashCode()
/*     */       {
/* 467 */         return this.results.hashCode(); } } } class null implements Lens<Pair<K, ?>, Pair<K, ?>, FT, FR> { public FT view(Pair<K, ?> param1Pair) { TypedOptic typedOptic = (TypedOptic)optics.get(param1Pair.getFirst()); return (FT)capView(param1Pair, typedOptic); } private <S, T> FT capView(Pair<K, ?> param1Pair, TypedOptic<S, T, FT, FR> param1TypedOptic) { return (FT)Optics.toLens((Optic)param1TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).view(param1Pair.getSecond()); } public Pair<K, ?> update(FR param1FR, Pair<K, ?> param1Pair) { TypedOptic typedOptic = (TypedOptic)optics.get(param1Pair.getFirst()); return capUpdate(param1FR, param1Pair, typedOptic); } private <S, T> Pair<K, ?> capUpdate(FR param1FR, Pair<K, ?> param1Pair, TypedOptic<S, T, FT, FR> param1TypedOptic) { return Pair.of(param1Pair.getFirst(), Optics.toLens((Optic)param1TypedOptic.upCast(Cartesian.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).update(param1FR, param1Pair.getSecond())); } } class null implements Affine<Pair<K, ?>, Pair<K, ?>, FT, FR> { public Either<Pair<K, ?>, FT> preview(Pair<K, ?> param1Pair) { if (!optics.containsKey(param1Pair.getFirst())) return Either.left(param1Pair);  TypedOptic typedOptic = (TypedOptic)optics.get(param1Pair.getFirst()); return capPreview(param1Pair, typedOptic); } private <S, T> Either<Pair<K, ?>, FT> capPreview(Pair<K, ?> param1Pair, TypedOptic<S, T, FT, FR> param1TypedOptic) { return Optics.toAffine((Optic)param1TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).preview(param1Pair.getSecond()).mapLeft(param1Object -> Pair.of(param1Pair.getFirst(), param1Object)); } public Pair<K, ?> set(FR param1FR, Pair<K, ?> param1Pair) { if (!optics.containsKey(param1Pair.getFirst())) return param1Pair;  TypedOptic typedOptic = (TypedOptic)optics.get(param1Pair.getFirst()); return capSet(param1FR, param1Pair, typedOptic); } private <S, T> Pair<K, ?> capSet(FR param1FR, Pair<K, ?> param1Pair, TypedOptic<S, T, FT, FR> param1TypedOptic) { return Pair.of(param1Pair.getFirst(), Optics.toAffine((Optic)param1TypedOptic.upCast(AffineP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)).set(param1FR, param1Pair.getSecond())); } } class null implements Traversal<Pair<K, ?>, Pair<K, ?>, FT, FR> { public <F extends K1> FunctionType<Pair<K, ?>, App<F, Pair<K, ?>>> wander(Applicative<F, ?> param1Applicative, FunctionType<FT, App<F, FR>> param1FunctionType) { return param1Pair -> { if (!param1Map.containsKey(param1Pair.getFirst())) return param1Applicative.point(param1Pair);  TypedOptic typedOptic = (TypedOptic)param1Map.get(param1Pair.getFirst()); return capTraversal(param1Applicative, param1FunctionType, param1Pair, typedOptic); }; } private <S, T, F extends K1> App<F, Pair<K, ?>> capTraversal(Applicative<F, ?> param1Applicative, FunctionType<FT, App<F, FR>> param1FunctionType, Pair<K, ?> param1Pair, TypedOptic<S, T, FT, FR> param1TypedOptic) { Traversal traversal = Optics.toTraversal((Optic)param1TypedOptic.upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new)); return param1Applicative.ap(param1Object -> Pair.of(param1Pair.getFirst(), param1Object), (App)traversal.wander(param1Applicative, param1FunctionType).apply(param1Pair.getSecond())); } } private static final class RewriteFunc<K> implements Function<DynamicOps<?>, Function<Pair<K, ?>, Pair<K, ?>>> { public int hashCode() { return this.results.hashCode(); }
/*     */ 
/*     */     
/*     */     private final Map<K, ? extends RewriteResult<?, ?>> results;
/*     */     
/*     */     public RewriteFunc(Map<K, ? extends RewriteResult<?, ?>> param1Map) {
/*     */       this.results = param1Map;
/*     */     }
/*     */     
/*     */     public FunctionType<Pair<K, ?>, Pair<K, ?>> apply(DynamicOps<?> param1DynamicOps) {
/*     */       return param1Pair -> {
/*     */           RewriteResult<?, ?> rewriteResult = this.results.get(param1Pair.getFirst());
/*     */           return (rewriteResult == null) ? param1Pair : capRuleApply(param1DynamicOps, param1Pair, rewriteResult);
/*     */         };
/*     */     }
/*     */     
/*     */     private <A, B> Pair<K, B> capRuleApply(DynamicOps<?> param1DynamicOps, Pair<K, ?> param1Pair, RewriteResult<A, B> param1RewriteResult) {
/*     */       return param1Pair.mapSecond(param1Object -> ((Function)param1RewriteResult.view().function().evalCached().apply(param1DynamicOps)).apply(param1Object));
/*     */     }
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*     */       if (this == param1Object)
/*     */         return true; 
/*     */       if (param1Object == null || getClass() != param1Object.getClass())
/*     */         return false; 
/*     */       RewriteFunc rewriteFunc = (RewriteFunc)param1Object;
/*     */       return Objects.equals(this.results, rewriteFunc.results);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\TaggedChoice.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */