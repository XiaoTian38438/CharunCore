/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Func;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.constant.EmptyPart;
/*     */ import com.mojang.datafixers.types.constant.EmptyPartPassthrough;
/*     */ import com.mojang.datafixers.types.templates.Check;
/*     */ import com.mojang.datafixers.types.templates.CompoundList;
/*     */ import com.mojang.datafixers.types.templates.Const;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.types.templates.Named;
/*     */ import com.mojang.datafixers.types.templates.Product;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.types.templates.Sum;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public interface DSL
/*     */ {
/*     */   public static interface TypeReference {
/*     */     String typeName();
/*     */     
/*     */     default TypeTemplate in(Schema param1Schema) {
/*  42 */       return param1Schema.id(typeName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Type<Boolean> bool() {
/*  49 */     return Instances.BOOL_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Integer> intType() {
/*  53 */     return Instances.INT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Long> longType() {
/*  57 */     return Instances.LONG_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Byte> byteType() {
/*  61 */     return Instances.BYTE_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Short> shortType() {
/*  65 */     return Instances.SHORT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Float> floatType() {
/*  69 */     return Instances.FLOAT_TYPE;
/*     */   }
/*     */   
/*     */   static Type<Double> doubleType() {
/*  73 */     return Instances.DOUBLE_TYPE;
/*     */   }
/*     */   
/*     */   static Type<String> string() {
/*  77 */     return Instances.STRING_TYPE;
/*     */   }
/*     */   
/*     */   static TypeTemplate emptyPart() {
/*  81 */     return constType(Instances.EMPTY_PART);
/*     */   }
/*     */   
/*     */   static Type<Unit> emptyPartType() {
/*  85 */     return Instances.EMPTY_PART;
/*     */   }
/*     */   
/*     */   static TypeTemplate remainder() {
/*  89 */     return constType(Instances.EMPTY_PASSTHROUGH);
/*     */   }
/*     */   
/*     */   static Type<Dynamic<?>> remainderType() {
/*  93 */     return Instances.EMPTY_PASSTHROUGH;
/*     */   }
/*     */   
/*     */   static TypeTemplate check(String paramString, int paramInt, TypeTemplate paramTypeTemplate) {
/*  97 */     return (TypeTemplate)new Check(paramString, paramInt, paramTypeTemplate);
/*     */   }
/*     */   
/*     */   static TypeTemplate compoundList(TypeTemplate paramTypeTemplate) {
/* 101 */     return compoundList(constType(string()), paramTypeTemplate);
/*     */   }
/*     */   
/*     */   static <V> CompoundList.CompoundListType<String, V> compoundList(Type<V> paramType) {
/* 105 */     return compoundList(string(), paramType);
/*     */   }
/*     */   
/*     */   static TypeTemplate compoundList(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) {
/* 109 */     return and((TypeTemplate)new CompoundList(paramTypeTemplate1, paramTypeTemplate2), remainder());
/*     */   }
/*     */   
/*     */   static <K, V> CompoundList.CompoundListType<K, V> compoundList(Type<K> paramType, Type<V> paramType1) {
/* 113 */     return new CompoundList.CompoundListType(paramType, paramType1);
/*     */   }
/*     */   
/*     */   static TypeTemplate constType(Type<?> paramType) {
/* 117 */     return (TypeTemplate)new Const(paramType);
/*     */   }
/*     */   
/*     */   static TypeTemplate hook(TypeTemplate paramTypeTemplate, Hook.HookFunction paramHookFunction1, Hook.HookFunction paramHookFunction2) {
/* 121 */     return (TypeTemplate)new Hook(paramTypeTemplate, paramHookFunction1, paramHookFunction2);
/*     */   }
/*     */   
/*     */   static <A> Type<A> hook(Type<A> paramType, Hook.HookFunction paramHookFunction1, Hook.HookFunction paramHookFunction2) {
/* 125 */     return (Type<A>)new Hook.HookType(paramType, paramHookFunction1, paramHookFunction2);
/*     */   }
/*     */   
/*     */   static TypeTemplate list(TypeTemplate paramTypeTemplate) {
/* 129 */     return (TypeTemplate)new List(paramTypeTemplate);
/*     */   }
/*     */   
/*     */   static <A> List.ListType<A> list(Type<A> paramType) {
/* 133 */     return new List.ListType(paramType);
/*     */   }
/*     */   
/*     */   static TypeTemplate named(String paramString, TypeTemplate paramTypeTemplate) {
/* 137 */     return (TypeTemplate)new Named(paramString, paramTypeTemplate);
/*     */   }
/*     */   
/*     */   static <A> Type<Pair<String, A>> named(String paramString, Type<A> paramType) {
/* 141 */     return (Type<Pair<String, A>>)new Named.NamedType(paramString, paramType);
/*     */   }
/*     */   
/*     */   static TypeTemplate and(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) {
/* 145 */     return (TypeTemplate)new Product(paramTypeTemplate1, paramTypeTemplate2);
/*     */   }
/*     */   
/*     */   static TypeTemplate and(TypeTemplate paramTypeTemplate, TypeTemplate... paramVarArgs) {
/* 149 */     if (paramVarArgs.length == 0) {
/* 150 */       return paramTypeTemplate;
/*     */     }
/* 152 */     TypeTemplate typeTemplate = paramVarArgs[paramVarArgs.length - 1];
/* 153 */     for (int i = paramVarArgs.length - 2; i >= 0; i--) {
/* 154 */       typeTemplate = and(paramVarArgs[i], typeTemplate);
/*     */     }
/* 156 */     return and(paramTypeTemplate, typeTemplate);
/*     */   }
/*     */   
/*     */   static TypeTemplate and(List<TypeTemplate> paramList) {
/* 160 */     switch (paramList.size()) { case 0:
/* 161 */         throw new IllegalArgumentException("Must have at least one type");
/*     */       case 1:
/* 163 */        }  return and(paramList
/* 164 */         .get(0), (TypeTemplate[])paramList
/* 165 */         .subList(1, paramList.size()).toArray(paramInt -> new TypeTemplate[paramInt]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate allWithRemainder(TypeTemplate paramTypeTemplate, TypeTemplate... paramVarArgs) {
/* 171 */     return and(paramTypeTemplate, (TypeTemplate[])ObjectArrays.concat((Object[])paramVarArgs, remainder()));
/*     */   }
/*     */   
/*     */   static <F, G> Type<Pair<F, G>> and(Type<F> paramType, Type<G> paramType1) {
/* 175 */     return (Type<Pair<F, G>>)new Product.ProductType(paramType, paramType1);
/*     */   }
/*     */   
/*     */   static <F, G, H> Type<Pair<F, Pair<G, H>>> and(Type<F> paramType, Type<G> paramType1, Type<H> paramType2) {
/* 179 */     return and(paramType, and(paramType1, paramType2));
/*     */   }
/*     */   
/*     */   static <F, G, H, I> Type<Pair<F, Pair<G, Pair<H, I>>>> and(Type<F> paramType, Type<G> paramType1, Type<H> paramType2, Type<I> paramType3) {
/* 183 */     return and(paramType, and(paramType1, and(paramType2, paramType3)));
/*     */   }
/*     */   
/*     */   static TypeTemplate id(int paramInt) {
/* 187 */     return (TypeTemplate)new RecursivePoint(paramInt);
/*     */   }
/*     */   
/*     */   static TypeTemplate or(TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) {
/* 191 */     return (TypeTemplate)new Sum(paramTypeTemplate1, paramTypeTemplate2);
/*     */   }
/*     */   
/*     */   static <F, G> Type<Either<F, G>> or(Type<F> paramType, Type<G> paramType1) {
/* 195 */     return (Type<Either<F, G>>)new Sum.SumType(paramType, paramType1);
/*     */   }
/*     */   
/*     */   static TypeTemplate field(String paramString, TypeTemplate paramTypeTemplate) {
/* 199 */     return (TypeTemplate)new Tag(paramString, paramTypeTemplate);
/*     */   }
/*     */   
/*     */   static <A> Tag.TagType<A> field(String paramString, Type<A> paramType) {
/* 203 */     return new Tag.TagType(paramString, paramType);
/*     */   }
/*     */   
/*     */   static <K> TaggedChoice<K> taggedChoice(String paramString, Type<K> paramType, Map<K, TypeTemplate> paramMap) {
/* 207 */     return new TaggedChoice(paramString, paramType, (Object2ObjectMap)new Object2ObjectOpenHashMap(paramMap));
/*     */   }
/*     */   
/*     */   static <K> TaggedChoice<K> taggedChoiceLazy(String paramString, Type<K> paramType, Map<K, Supplier<TypeTemplate>> paramMap) {
/* 211 */     return taggedChoice(paramString, paramType, (Map<K, TypeTemplate>)paramMap.entrySet().stream().map(paramEntry -> Pair.of(paramEntry.getKey(), ((Supplier<TypeTemplate>)paramEntry.getValue()).get())).collect(Pair.toMap()));
/*     */   }
/*     */ 
/*     */   
/*     */   static <K> Type<Pair<K, ?>> taggedChoiceType(String paramString, Type<K> paramType, Map<K, ? extends Type<?>> paramMap) {
/* 216 */     return (Type<Pair<K, ?>>)Instances.TAGGED_CHOICE_TYPE_CACHE.computeIfAbsent(new Instances.TaggedChoiceCacheKey(paramString, paramType, paramMap), Instances.TaggedChoiceCacheKey::build);
/*     */   }
/*     */   
/*     */   static <A, B> Type<Function<A, B>> func(Type<A> paramType, Type<B> paramType1) {
/* 220 */     return (Type<Function<A, B>>)new Func(paramType, paramType1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <A> Type<Either<A, Unit>> optional(Type<A> paramType) {
/* 226 */     return or(paramType, emptyPartType());
/*     */   }
/*     */   
/*     */   static TypeTemplate optional(TypeTemplate paramTypeTemplate) {
/* 230 */     return or(paramTypeTemplate, emptyPart());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString, TypeTemplate paramTypeTemplate) {
/* 236 */     return allWithRemainder(
/* 237 */         field(paramString, paramTypeTemplate), new TypeTemplate[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2) {
/* 245 */     return allWithRemainder(
/* 246 */         field(paramString1, paramTypeTemplate1), new TypeTemplate[] {
/* 247 */           field(paramString2, paramTypeTemplate2)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3) {
/* 256 */     return allWithRemainder(
/* 257 */         field(paramString1, paramTypeTemplate1), new TypeTemplate[] {
/* 258 */           field(paramString2, paramTypeTemplate2), 
/* 259 */           field(paramString3, paramTypeTemplate3)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString, TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) {
/* 267 */     return and(
/* 268 */         field(paramString, paramTypeTemplate1), paramTypeTemplate2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, TypeTemplate paramTypeTemplate3) {
/* 278 */     return and(
/* 279 */         field(paramString1, paramTypeTemplate1), new TypeTemplate[] {
/* 280 */           field(paramString2, paramTypeTemplate2), paramTypeTemplate3
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate fields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, TypeTemplate paramTypeTemplate4) {
/* 291 */     return and(
/* 292 */         field(paramString1, paramTypeTemplate1), new TypeTemplate[] {
/* 293 */           field(paramString2, paramTypeTemplate2), 
/* 294 */           field(paramString3, paramTypeTemplate3), paramTypeTemplate4
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString, TypeTemplate paramTypeTemplate) {
/* 300 */     return allWithRemainder(
/* 301 */         optional(field(paramString, paramTypeTemplate)), new TypeTemplate[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2) {
/* 309 */     return allWithRemainder(
/* 310 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 311 */           optional(field(paramString2, paramTypeTemplate2))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3) {
/* 320 */     return allWithRemainder(
/* 321 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 322 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 323 */           optional(field(paramString3, paramTypeTemplate3))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, String paramString4, TypeTemplate paramTypeTemplate4) {
/* 333 */     return allWithRemainder(
/* 334 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 335 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 336 */           optional(field(paramString3, paramTypeTemplate3)), 
/* 337 */           optional(field(paramString4, paramTypeTemplate4))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, String paramString4, TypeTemplate paramTypeTemplate4, String paramString5, TypeTemplate paramTypeTemplate5) {
/* 348 */     return allWithRemainder(
/* 349 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 350 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 351 */           optional(field(paramString3, paramTypeTemplate3)), 
/* 352 */           optional(field(paramString4, paramTypeTemplate4)), 
/* 353 */           optional(field(paramString5, paramTypeTemplate5))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString, TypeTemplate paramTypeTemplate1, TypeTemplate paramTypeTemplate2) {
/* 361 */     return and(
/* 362 */         optional(field(paramString, paramTypeTemplate1)), paramTypeTemplate2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, TypeTemplate paramTypeTemplate3) {
/* 372 */     return and(
/* 373 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 374 */           optional(field(paramString2, paramTypeTemplate2)), paramTypeTemplate3
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, TypeTemplate paramTypeTemplate4) {
/* 385 */     return and(
/* 386 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 387 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 388 */           optional(field(paramString3, paramTypeTemplate3)), paramTypeTemplate4
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, String paramString4, TypeTemplate paramTypeTemplate4, TypeTemplate paramTypeTemplate5) {
/* 400 */     return and(
/* 401 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 402 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 403 */           optional(field(paramString3, paramTypeTemplate3)), 
/* 404 */           optional(field(paramString4, paramTypeTemplate4)), paramTypeTemplate5
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeTemplate optionalFields(String paramString1, TypeTemplate paramTypeTemplate1, String paramString2, TypeTemplate paramTypeTemplate2, String paramString3, TypeTemplate paramTypeTemplate3, String paramString4, TypeTemplate paramTypeTemplate4, String paramString5, TypeTemplate paramTypeTemplate5, TypeTemplate paramTypeTemplate6) {
/* 417 */     return and(
/* 418 */         optional(field(paramString1, paramTypeTemplate1)), new TypeTemplate[] {
/* 419 */           optional(field(paramString2, paramTypeTemplate2)), 
/* 420 */           optional(field(paramString3, paramTypeTemplate3)), 
/* 421 */           optional(field(paramString4, paramTypeTemplate4)), 
/* 422 */           optional(field(paramString5, paramTypeTemplate5)), paramTypeTemplate6
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   static TypeTemplate optionalFields(Pair<String, TypeTemplate>... paramVarArgs) {
/* 429 */     return and(Stream.<TypeTemplate>concat(
/* 430 */           Arrays.<Pair<String, TypeTemplate>>stream(paramVarArgs).map(paramPair -> optional(field((String)paramPair.getFirst(), (TypeTemplate)paramPair.getSecond()))), 
/* 431 */           Stream.of(remainder()))
/* 432 */         .toList());
/*     */   }
/*     */   
/*     */   static TypeTemplate optionalFieldsLazy(Map<String, Supplier<TypeTemplate>> paramMap) {
/* 436 */     return and(Stream.<TypeTemplate>concat(paramMap
/* 437 */           .entrySet().stream().map(paramEntry -> optional(field((String)paramEntry.getKey(), ((Supplier<TypeTemplate>)paramEntry.getValue()).get()))), 
/* 438 */           Stream.of(remainder()))
/* 439 */         .toList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static OpticFinder<Dynamic<?>> remainderFinder() {
/* 445 */     return Instances.REMAINDER_FINDER;
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> typeFinder(Type<FT> paramType) {
/* 449 */     return new FieldFinder<>(null, paramType);
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> fieldFinder(String paramString, Type<FT> paramType) {
/* 453 */     return new FieldFinder<>(paramString, paramType);
/*     */   }
/*     */   
/*     */   static <FT> OpticFinder<FT> namedChoice(String paramString, Type<FT> paramType) {
/* 457 */     return new NamedChoiceFinder<>(paramString, paramType);
/*     */   }
/*     */   
/*     */   static Unit unit() {
/* 461 */     return Unit.INSTANCE;
/*     */   }
/*     */   
/*     */   public static final class Instances {
/* 465 */     private static final Type<Boolean> BOOL_TYPE = (Type<Boolean>)new Const.PrimitiveType((Codec)Codec.BOOL);
/* 466 */     private static final Type<Integer> INT_TYPE = (Type<Integer>)new Const.PrimitiveType((Codec)Codec.INT);
/* 467 */     private static final Type<Long> LONG_TYPE = (Type<Long>)new Const.PrimitiveType((Codec)Codec.LONG);
/* 468 */     private static final Type<Byte> BYTE_TYPE = (Type<Byte>)new Const.PrimitiveType((Codec)Codec.BYTE);
/* 469 */     private static final Type<Short> SHORT_TYPE = (Type<Short>)new Const.PrimitiveType((Codec)Codec.SHORT);
/* 470 */     private static final Type<Float> FLOAT_TYPE = (Type<Float>)new Const.PrimitiveType((Codec)Codec.FLOAT);
/* 471 */     private static final Type<Double> DOUBLE_TYPE = (Type<Double>)new Const.PrimitiveType((Codec)Codec.DOUBLE);
/* 472 */     private static final Type<String> STRING_TYPE = (Type<String>)new Const.PrimitiveType((Codec)Codec.STRING);
/* 473 */     private static final Type<Unit> EMPTY_PART = (Type<Unit>)new EmptyPart();
/* 474 */     private static final Type<Dynamic<?>> EMPTY_PASSTHROUGH = (Type<Dynamic<?>>)new EmptyPartPassthrough();
/*     */     
/* 476 */     private static final OpticFinder<Dynamic<?>> REMAINDER_FINDER = DSL.remainderType().finder();
/*     */     
/* 478 */     private static final Map<TaggedChoiceCacheKey<?>, Type<? extends Pair<?, ?>>> TAGGED_CHOICE_TYPE_CACHE = Maps.newConcurrentMap();
/*     */     public static final class TaggedChoiceCacheKey<K> extends Record { private final String name; private final Type<K> keyType; private final Map<K, ? extends Type<?>> types;
/* 480 */       public TaggedChoiceCacheKey(String param2String, Type<K> param2Type, Map<K, ? extends Type<?>> param2Map) { this.name = param2String; this.keyType = param2Type; this.types = param2Map; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/* 480 */         //   #480	-> 0 } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #480	-> 0 } public final boolean equals(Object param2Object) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/* 480 */         //   #480	-> 0 } public Type<K> keyType() { return this.keyType; } public Map<K, ? extends Type<?>> types() { return this.types; }
/*     */       
/* 482 */       public TaggedChoice.TaggedChoiceType<K> build() { return new TaggedChoice.TaggedChoiceType(this.name, this.keyType, (Object2ObjectMap)new Object2ObjectOpenHashMap(this.types)); } } } public static final class TaggedChoiceCacheKey<K> extends Record { private final String name; private final Type<K> keyType; public TaggedChoice.TaggedChoiceType<K> build() { return new TaggedChoice.TaggedChoiceType(this.name, this.keyType, (Object2ObjectMap)new Object2ObjectOpenHashMap(this.types)); }
/*     */ 
/*     */     
/*     */     private final Map<K, ? extends Type<?>> types;
/*     */     
/*     */     public TaggedChoiceCacheKey(String param1String, Type<K> param1Type, Map<K, ? extends Type<?>> param1Map) {
/*     */       this.name = param1String;
/*     */       this.keyType = param1Type;
/*     */       this.types = param1Map;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #480	-> 0
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #480	-> 0
/*     */     }
/*     */     
/*     */     public final boolean equals(Object param1Object) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/DSL$Instances$TaggedChoiceCacheKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #480	-> 0
/*     */     }
/*     */     
/*     */     public String name() {
/*     */       return this.name;
/*     */     }
/*     */     
/*     */     public Type<K> keyType() {
/*     */       return this.keyType;
/*     */     }
/*     */     
/*     */     public Map<K, ? extends Type<?>> types() {
/*     */       return this.types;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DSL.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */