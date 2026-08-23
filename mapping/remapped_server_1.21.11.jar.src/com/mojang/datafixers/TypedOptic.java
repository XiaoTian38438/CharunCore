/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public final class TypedOptic<S, T, A, B> extends Record {
/*     */   private final Set<TypeToken<? extends K1>> bounds;
/*     */   private final List<? extends Element<?, ?, ?, ?>> elements;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/TypedOptic;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */   }
/*     */   
/*  34 */   public TypedOptic(Set<TypeToken<? extends K1>> paramSet, List<? extends Element<?, ?, ?, ?>> paramList) { this.bounds = paramSet; this.elements = paramList; } public final boolean equals(Object paramObject) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/TypedOptic;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*  34 */     //   #34	-> 0 } public Set<TypeToken<? extends K1>> bounds() { return this.bounds; } public List<? extends Element<?, ?, ?, ?>> elements() { return this.elements; }
/*     */    public TypedOptic(TypeToken<? extends K1> paramTypeToken, Type<S> paramType, Type<T> paramType1, Type<A> paramType2, Type<B> paramType3, Optic<?, S, T, A, B> paramOptic) {
/*  36 */     this((Set<TypeToken<? extends K1>>)ImmutableSet.of(paramTypeToken), paramType, paramType1, paramType2, paramType3, paramOptic);
/*     */   }
/*     */   
/*     */   public TypedOptic(Set<TypeToken<? extends K1>> paramSet, Type<S> paramType, Type<T> paramType1, Type<A> paramType2, Type<B> paramType3, Optic<?, S, T, A, B> paramOptic) {
/*  40 */     this(paramSet, List.of(new Element<>(paramType, paramType1, paramType2, paramType3, paramOptic)));
/*     */   }
/*     */   
/*     */   public <P extends com.mojang.datafixers.kinds.K2, Proof2 extends K1> App2<P, S, T> apply(TypeToken<Proof2> paramTypeToken, App<Proof2, P> paramApp, App2<P, A, B> paramApp2) {
/*  44 */     return ((Optic)upCast(paramTypeToken)
/*  45 */       .orElseThrow(() -> new IllegalArgumentException("Couldn't upcast")))
/*     */ 
/*     */       
/*  48 */       .eval(paramApp)
/*  49 */       .apply(paramApp2);
/*     */   }
/*     */   
/*     */   public Optic<?, S, T, ?, ?> outermost() {
/*  53 */     return outermostElement().optic();
/*     */   }
/*     */   
/*     */   public Optic<?, ?, ?, A, B> innermost() {
/*  57 */     return innermostElement().optic();
/*     */   }
/*     */ 
/*     */   
/*     */   private Element<S, T, ?, ?> outermostElement() {
/*  62 */     return (Element<S, T, ?, ?>)this.elements.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private Element<?, ?, A, B> innermostElement() {
/*  67 */     return (Element<?, ?, A, B>)this.elements.get(this.elements.size() - 1);
/*     */   }
/*     */   
/*     */   public Type<S> sType() {
/*  71 */     return outermostElement().sType();
/*     */   }
/*     */   
/*     */   public Type<T> tType() {
/*  75 */     return outermostElement().tType();
/*     */   }
/*     */   
/*     */   public Type<A> aType() {
/*  79 */     return innermostElement().aType();
/*     */   }
/*     */   
/*     */   public Type<B> bType() {
/*  83 */     return innermostElement().bType();
/*     */   }
/*     */   
/*     */   public <A1, B1> TypedOptic<S, T, A1, B1> compose(TypedOptic<A, B, A1, B1> paramTypedOptic) {
/*  87 */     ImmutableSet.Builder builder = ImmutableSet.builder();
/*  88 */     builder.addAll(this.bounds);
/*  89 */     builder.addAll(paramTypedOptic.bounds);
/*  90 */     ImmutableList.Builder builder1 = ImmutableList.builderWithExpectedSize(elements().size() + paramTypedOptic.elements().size());
/*  91 */     builder1.addAll(elements());
/*  92 */     builder1.addAll(paramTypedOptic.elements());
/*  93 */     return new TypedOptic((Set<TypeToken<? extends K1>>)builder.build(), (List<? extends Element<?, ?, ?, ?>>)builder1.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(TypeToken<Proof2> paramTypeToken) {
/*  98 */     if (instanceOf(this.bounds, paramTypeToken)) {
/*  99 */       if (this.elements.size() == 1) {
/* 100 */         return Optional.of((Optic)((Element<S, T, A, B>)this.elements.get(0)).optic());
/*     */       }
/* 102 */       List list = (List)this.elements.stream().map(paramElement -> paramElement.optic()).collect(Collectors.toList());
/* 103 */       return (Optional)Optional.of(new Optic.CompositionOptic(list));
/*     */     } 
/* 105 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static <Proof2 extends K1> boolean instanceOf(Collection<TypeToken<? extends K1>> paramCollection, TypeToken<Proof2> paramTypeToken) {
/* 109 */     return paramCollection.stream().allMatch(paramTypeToken2 -> paramTypeToken2.isSupertypeOf(paramTypeToken1));
/*     */   }
/*     */   
/*     */   public static <S, T> TypedOptic<S, T, S, T> adapter(Type<S> paramType, Type<T> paramType1) {
/* 113 */     return new TypedOptic<>(Profunctor.Mu.TYPE_TOKEN, paramType, paramType1, paramType, paramType1, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 119 */         (Optic<?, S, T, S, T>)Optics.id());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, F2> TypedOptic<Pair<F, G>, Pair<F2, G>, F, F2> proj1(Type<F> paramType, Type<G> paramType1, Type<F2> paramType2) {
/* 124 */     return new TypedOptic<>(Cartesian.Mu.TYPE_TOKEN, 
/*     */         
/* 126 */         DSL.and(paramType, paramType1), 
/* 127 */         DSL.and(paramType2, paramType1), paramType, paramType2, 
/*     */ 
/*     */         
/* 130 */         (Optic<?, Pair<F, G>, Pair<F2, G>, F, F2>)Optics.proj1());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, G2> TypedOptic<Pair<F, G>, Pair<F, G2>, G, G2> proj2(Type<F> paramType, Type<G> paramType1, Type<G2> paramType2) {
/* 135 */     return new TypedOptic<>(Cartesian.Mu.TYPE_TOKEN, 
/*     */         
/* 137 */         DSL.and(paramType, paramType1), 
/* 138 */         DSL.and(paramType, paramType2), paramType1, paramType2, 
/*     */ 
/*     */         
/* 141 */         (Optic<?, Pair<F, G>, Pair<F, G2>, G, G2>)Optics.proj2());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, F2> TypedOptic<Either<F, G>, Either<F2, G>, F, F2> inj1(Type<F> paramType, Type<G> paramType1, Type<F2> paramType2) {
/* 146 */     return new TypedOptic<>(Cocartesian.Mu.TYPE_TOKEN, 
/*     */         
/* 148 */         DSL.or(paramType, paramType1), 
/* 149 */         DSL.or(paramType2, paramType1), paramType, paramType2, 
/*     */ 
/*     */         
/* 152 */         (Optic<?, Either<F, G>, Either<F2, G>, F, F2>)Optics.inj1());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <F, G, G2> TypedOptic<Either<F, G>, Either<F, G2>, G, G2> inj2(Type<F> paramType, Type<G> paramType1, Type<G2> paramType2) {
/* 157 */     return new TypedOptic<>(Cocartesian.Mu.TYPE_TOKEN, 
/*     */         
/* 159 */         DSL.or(paramType, paramType1), 
/* 160 */         DSL.or(paramType, paramType2), paramType1, paramType2, 
/*     */ 
/*     */         
/* 163 */         (Optic<?, Either<F, G>, Either<F, G2>, G, G2>)Optics.inj2());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V, K2> TypedOptic<List<Pair<K, V>>, List<Pair<K2, V>>, K, K2> compoundListKeys(Type<K> paramType, Type<K2> paramType1, Type<V> paramType2) {
/* 168 */     return (new TypedOptic<>(TraversalP.Mu.TYPE_TOKEN, 
/*     */         
/* 170 */         (Type)DSL.compoundList(paramType, paramType2), 
/* 171 */         (Type)DSL.compoundList(paramType1, paramType2), 
/* 172 */         DSL.and(paramType, paramType2), 
/* 173 */         DSL.and(paramType1, paramType2), 
/* 174 */         (Optic<?, List<Pair<K, V>>, List<Pair<K2, V>>, ?, ?>)Optics.listTraversal()))
/* 175 */       .compose(new TypedOptic<>(TraversalP.Mu.TYPE_TOKEN, 
/*     */           
/* 177 */           DSL.and(paramType, paramType2), 
/* 178 */           DSL.and(paramType1, paramType2), paramType, paramType1, 
/*     */ 
/*     */           
/* 181 */           (Optic<?, ?, ?, K, K2>)Optics.proj1()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, V, V2> TypedOptic<List<Pair<K, V>>, List<Pair<K, V2>>, V, V2> compoundListElements(Type<K> paramType, Type<V> paramType1, Type<V2> paramType2) {
/* 186 */     return (new TypedOptic<>(TraversalP.Mu.TYPE_TOKEN, 
/*     */         
/* 188 */         (Type)DSL.compoundList(paramType, paramType1), 
/* 189 */         (Type)DSL.compoundList(paramType, paramType2), 
/* 190 */         DSL.and(paramType, paramType1), 
/* 191 */         DSL.and(paramType, paramType2), 
/* 192 */         (Optic<?, List<Pair<K, V>>, List<Pair<K, V2>>, ?, ?>)Optics.listTraversal()))
/* 193 */       .compose(new TypedOptic<>(TraversalP.Mu.TYPE_TOKEN, 
/*     */           
/* 195 */           DSL.and(paramType, paramType1), 
/* 196 */           DSL.and(paramType, paramType2), paramType1, paramType2, 
/*     */ 
/*     */           
/* 199 */           (Optic<?, ?, ?, V, V2>)Optics.proj2()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A, B> TypedOptic<List<A>, List<B>, A, B> list(Type<A> paramType, Type<B> paramType1) {
/* 204 */     return new TypedOptic<>(TraversalP.Mu.TYPE_TOKEN, 
/*     */         
/* 206 */         (Type)DSL.list(paramType), 
/* 207 */         (Type)DSL.list(paramType1), paramType, paramType1, 
/*     */ 
/*     */         
/* 210 */         (Optic<?, List<A>, List<B>, A, B>)Optics.listTraversal());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <K, A, B> TypedOptic<Pair<K, ?>, Pair<K, ?>, A, B> tagged(TaggedChoice.TaggedChoiceType<K> paramTaggedChoiceType, K paramK, Type<A> paramType, Type<B> paramType1) {
/* 215 */     return new TypedOptic<>(Cocartesian.Mu.TYPE_TOKEN, (Type)paramTaggedChoiceType, 
/*     */ 
/*     */         
/* 218 */         replaceTagged(paramTaggedChoiceType, paramK, paramType, paramType1), paramType, paramType1, (Optic<?, Pair<K, ?>, Pair<K, ?>, A, B>)new InjTagged(paramK));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, A, B> Type<Pair<K, ?>> replaceTagged(TaggedChoice.TaggedChoiceType<K> paramTaggedChoiceType, K paramK, Type<A> paramType, Type<B> paramType1) {
/* 226 */     if (Objects.equals(paramType, paramType1)) {
/* 227 */       return (Type)paramTaggedChoiceType;
/*     */     }
/* 229 */     if (!Objects.equals(paramTaggedChoiceType.types().get(paramK), paramType)) {
/* 230 */       throw new IllegalArgumentException("Focused type doesn't match.");
/*     */     }
/* 232 */     HashMap<K, Type<B>> hashMap = Maps.newHashMap(paramTaggedChoiceType.types());
/* 233 */     hashMap.put(paramK, paramType1);
/* 234 */     return DSL.taggedChoiceType(paramTaggedChoiceType.getName(), paramTaggedChoiceType.getKeyType(), hashMap);
/*     */   }
/*     */   
/*     */   public TypedOptic<S, T, A, B> castOuter(Type<S> paramType, Type<T> paramType1) {
/* 238 */     return castOuterUnchecked(paramType, paramType1);
/*     */   }
/*     */   
/*     */   public <S2, T2> TypedOptic<S2, T2, A, B> castOuterUnchecked(Type<S2> paramType, Type<T2> paramType1) {
/* 242 */     ArrayList<Element<?, ?, ?, ?>> arrayList = new ArrayList<>(this.elements);
/* 243 */     arrayList.set(0, ((Element)arrayList.get(0)).castOuterUnchecked(paramType, paramType1));
/* 244 */     return new TypedOptic(this.bounds, arrayList);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 249 */     return "(" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(" ◦ ")) + ")";
/*     */   }
/*     */   public static final class Element<S, T, A, B> extends Record { private final Type<S> sType; private final Type<T> tType; private final Type<A> aType; private final Type<B> bType; private final Optic<?, S, T, A, B> optic;
/* 252 */     public Element(Type<S> param1Type, Type<T> param1Type1, Type<A> param1Type2, Type<B> param1Type3, Optic<?, S, T, A, B> param1Optic) { this.sType = param1Type; this.tType = param1Type1; this.aType = param1Type2; this.bType = param1Type3; this.optic = param1Optic; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/TypedOptic$Element;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #252	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/TypedOptic$Element;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 252 */       //   #252	-> 0 } public Type<S> sType() { return this.sType; } public Type<T> tType() { return this.tType; } public Type<A> aType() { return this.aType; } public Type<B> bType() { return this.bType; } public Optic<?, S, T, A, B> optic() { return this.optic; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <S2, T2> Element<S2, T2, A, B> castOuterUnchecked(Type<S2> param1Type, Type<T2> param1Type1) {
/* 261 */       return new Element(param1Type, param1Type1, this.aType, this.bType, this.optic);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 266 */       return this.optic.toString();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypedOptic.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */