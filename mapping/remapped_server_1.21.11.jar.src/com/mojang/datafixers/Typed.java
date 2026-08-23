/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.App2;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.Const;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.kinds.K2;
/*     */ import com.mojang.datafixers.kinds.Monoid;
/*     */ import com.mojang.datafixers.optics.Forget;
/*     */ import com.mojang.datafixers.optics.ForgetOpt;
/*     */ import com.mojang.datafixers.optics.Optic;
/*     */ import com.mojang.datafixers.optics.Optics;
/*     */ import com.mojang.datafixers.optics.ReForgetC;
/*     */ import com.mojang.datafixers.optics.Traversal;
/*     */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public final class Typed<A> {
/*     */   protected final Type<A> type;
/*     */   
/*     */   public Typed(Type<A> paramType, DynamicOps<?> paramDynamicOps, A paramA) {
/*  37 */     this.type = paramType;
/*  38 */     this.ops = paramDynamicOps;
/*  39 */     this.value = paramA;
/*     */   }
/*     */   protected final DynamicOps<?> ops; protected final A value;
/*     */   
/*     */   public String toString() {
/*  44 */     return "Typed[" + String.valueOf(this.value) + "]";
/*     */   }
/*     */   
/*     */   public <FT> FT get(OpticFinder<FT> paramOpticFinder) {
/*  48 */     return (FT)Forget.unbox(((TypedOptic)paramOpticFinder.<A>findType(this.type, false).orThrow()).apply(new TypeToken<Forget.Instance.Mu<FT>>() {  }, (App)new Forget.Instance(), 
/*     */ 
/*     */           
/*  51 */           (App2)Optics.forget(Function.identity())))
/*  52 */       .run(this.value);
/*     */   }
/*     */   
/*     */   public <FT> Typed<FT> getTyped(OpticFinder<FT> paramOpticFinder) {
/*  56 */     TypedOptic typedOptic = (TypedOptic)paramOpticFinder.<A>findType(this.type, false).orThrow();
/*  57 */     return new Typed(typedOptic.aType(), this.ops, (A)Forget.unbox(typedOptic.apply(new TypeToken<Forget.Instance.Mu<FT>>() {  }, (App)new Forget.Instance(), 
/*     */ 
/*     */             
/*  60 */             (App2)Optics.forget(Function.identity())))
/*  61 */         .run(this.value));
/*     */   }
/*     */   
/*     */   public <FT> Optional<FT> getOptional(OpticFinder<FT> paramOpticFinder) {
/*  65 */     TypedOptic typedOptic = (TypedOptic)paramOpticFinder.<A>findType(this.type, false).orThrow();
/*  66 */     return ForgetOpt.unbox(typedOptic.apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  69 */           (App2)Optics.forgetOpt(Optional::of)))
/*  70 */       .run(this.value);
/*     */   }
/*     */   
/*     */   public <FT> FT getOrCreate(OpticFinder<FT> paramOpticFinder) {
/*  74 */     return (FT)DataFixUtils.or(getOptional(paramOpticFinder), () -> paramOpticFinder.type().point(this.ops)).orElseThrow(() -> new IllegalStateException("Could not create default value for type: " + String.valueOf(paramOpticFinder.type())));
/*     */   }
/*     */   
/*     */   public <FT> FT getOrDefault(OpticFinder<FT> paramOpticFinder, FT paramFT) {
/*  78 */     return ForgetOpt.unbox(((TypedOptic)paramOpticFinder.<A>findType(this.type, false).orThrow()).apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  81 */           (App2)Optics.forgetOpt(Optional::of)))
/*  82 */       .run(this.value).orElse(paramFT);
/*     */   }
/*     */   
/*     */   public <FT> Optional<Typed<FT>> getOptionalTyped(OpticFinder<FT> paramOpticFinder) {
/*  86 */     TypedOptic typedOptic = (TypedOptic)paramOpticFinder.<A>findType(this.type, false).orThrow();
/*  87 */     return ForgetOpt.unbox(typedOptic.apply(new TypeToken<ForgetOpt.Instance.Mu<FT>>() {  }, (App)new ForgetOpt.Instance(), 
/*     */ 
/*     */           
/*  90 */           (App2)Optics.forgetOpt(Optional::of)))
/*  91 */       .run(this.value).map(paramObject -> new Typed(paramTypedOptic.aType(), this.ops, (A)paramObject));
/*     */   }
/*     */   
/*     */   public <FT> Typed<FT> getOrCreateTyped(OpticFinder<FT> paramOpticFinder) {
/*  95 */     return (Typed<FT>)DataFixUtils.or(getOptionalTyped(paramOpticFinder), () -> paramOpticFinder.type().pointTyped(this.ops)).orElseThrow(() -> new IllegalStateException("Could not create default value for type: " + String.valueOf(paramOpticFinder.type())));
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> set(OpticFinder<FT> paramOpticFinder, FT paramFT) {
/*  99 */     return set(paramOpticFinder, new Typed(paramOpticFinder.type(), this.ops, (A)paramFT));
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> set(OpticFinder<FT> paramOpticFinder, Type<FR> paramType, FR paramFR) {
/* 103 */     return set(paramOpticFinder, new Typed(paramType, this.ops, (A)paramFR));
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> set(OpticFinder<FT> paramOpticFinder, Typed<FR> paramTyped) {
/* 107 */     TypedOptic<?, ?, ?, FR> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramTyped.type, false).orThrow();
/* 108 */     return setCap(typedOptic, paramTyped);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <B, FT, FR> Typed<B> setCap(TypedOptic<A, B, FT, FR> paramTypedOptic, Typed<FR> paramTyped) {
/* 116 */     Object object = ReForgetC.unbox(paramTypedOptic.apply(new TypeToken<ReForgetC.Instance.Mu<FR>>() {  }, (App<ReForgetC.Instance.Mu<FR>, K2>)new ReForgetC.Instance(), (App2<K2, FT, FR>)Optics.reForgetC("set", Either.left(Function.identity())))).run(this.value, paramTyped.value);
/* 117 */     return new Typed(paramTypedOptic.tType(), this.ops, (A)object);
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateTyped(OpticFinder<FT> paramOpticFinder, Function<Typed<?>, Typed<?>> paramFunction) {
/* 121 */     return updateTyped(paramOpticFinder, paramOpticFinder.type(), paramFunction);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateTyped(OpticFinder<FT> paramOpticFinder, Type<FR> paramType, Function<Typed<?>, Typed<?>> paramFunction) {
/* 125 */     TypedOptic<?, ?, ?, ?> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramType, false).orThrow();
/* 126 */     return updateCap(typedOptic, paramObject -> {
/*     */           Typed typed = paramFunction.apply(new Typed(paramOpticFinder.type(), this.ops, (A)paramObject));
/*     */           return paramTypedOptic.bType().ifSame(typed).orElseThrow(());
/*     */         });
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> update(OpticFinder<FT> paramOpticFinder, Function<FT, FT> paramFunction) {
/* 133 */     return update(paramOpticFinder, paramOpticFinder.type(), paramFunction);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> update(OpticFinder<FT> paramOpticFinder, Type<FR> paramType, Function<FT, FR> paramFunction) {
/* 137 */     TypedOptic<?, ?, FT, FR> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramType, false).orThrow();
/* 138 */     return updateCap(typedOptic, paramFunction);
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateRecursiveTyped(OpticFinder<FT> paramOpticFinder, Function<Typed<?>, Typed<?>> paramFunction) {
/* 142 */     return updateRecursiveTyped(paramOpticFinder, paramOpticFinder.type(), paramFunction);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateRecursiveTyped(OpticFinder<FT> paramOpticFinder, Type<FR> paramType, Function<Typed<?>, Typed<?>> paramFunction) {
/* 146 */     TypedOptic<?, ?, ?, ?> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramType, true).orThrow();
/* 147 */     return updateCap(typedOptic, paramObject -> {
/*     */           Typed typed = paramFunction.apply(new Typed(paramOpticFinder.type(), this.ops, (A)paramObject));
/*     */           return paramTypedOptic.bType().ifSame(typed).orElseThrow(());
/*     */         });
/*     */   }
/*     */   
/*     */   public <FT> Typed<?> updateRecursive(OpticFinder<FT> paramOpticFinder, Function<FT, FT> paramFunction) {
/* 154 */     return updateRecursive(paramOpticFinder, paramOpticFinder.type(), paramFunction);
/*     */   }
/*     */   
/*     */   public <FT, FR> Typed<?> updateRecursive(OpticFinder<FT> paramOpticFinder, Type<FR> paramType, Function<FT, FR> paramFunction) {
/* 158 */     TypedOptic<?, ?, FT, FR> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramType, true).orThrow();
/* 159 */     return updateCap(typedOptic, paramFunction);
/*     */   }
/*     */   
/*     */   private <B, FT, FR> Typed<B> updateCap(TypedOptic<A, B, FT, FR> paramTypedOptic, Function<FT, FR> paramFunction) {
/* 163 */     Traversal traversal = Optics.toTraversal((Optic)paramTypedOptic.<K1>upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 164 */     Object object = IdF.get(traversal.wander((Applicative)IdF.Instance.INSTANCE, paramObject -> IdF.create(paramFunction.apply(paramObject))).apply(this.value));
/* 165 */     return new Typed(paramTypedOptic.tType(), this.ops, (A)object);
/*     */   }
/*     */   
/*     */   public <FT> List<Typed<FT>> getAllTyped(OpticFinder<FT> paramOpticFinder) {
/* 169 */     TypedOptic<?, ?, ?, ?> typedOptic = (TypedOptic)paramOpticFinder.<A, FR>findType(this.type, paramOpticFinder.type(), false).orThrow();
/* 170 */     return (List<Typed<FT>>)getAll(typedOptic).stream().map(paramObject -> new Typed(paramOpticFinder.type(), this.ops, (A)paramObject)).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public <FT> List<FT> getAll(TypedOptic<A, ?, FT, ?> paramTypedOptic) {
/* 174 */     Traversal traversal = Optics.toTraversal((Optic)paramTypedOptic.<K1>upCast(TraversalP.Mu.TYPE_TOKEN).orElseThrow(IllegalArgumentException::new));
/* 175 */     return (List<FT>)Const.unbox(traversal.wander((Applicative)new Const.Instance(Monoid.listMonoid()), paramObject -> Const.create(ImmutableList.of(paramObject))).apply(this.value));
/*     */   }
/*     */   
/*     */   public Typed<A> out() {
/* 179 */     if (!(this.type instanceof RecursivePoint.RecursivePointType)) {
/* 180 */       throw new IllegalArgumentException("Not recursive");
/*     */     }
/* 182 */     Type<A> type = ((RecursivePoint.RecursivePointType)this.type).unfold();
/* 183 */     return new Typed(type, this.ops, this.value);
/*     */   }
/*     */   
/*     */   public <B> Typed<Either<A, B>> inj1(Type<B> paramType) {
/* 187 */     return new Typed((Type)DSL.or(this.type, paramType), this.ops, (A)Optics.inj1().build(this.value));
/*     */   }
/*     */   
/*     */   public <B> Typed<Either<B, A>> inj2(Type<B> paramType) {
/* 191 */     return new Typed((Type)DSL.or(paramType, this.type), this.ops, (A)Optics.inj2().build(this.value));
/*     */   }
/*     */   
/*     */   public static <A, B> Typed<Pair<A, B>> pair(Typed<A> paramTyped, Typed<B> paramTyped1) {
/* 195 */     return new Typed<>(DSL.and(paramTyped.type, paramTyped1.type), paramTyped.ops, Pair.of(paramTyped.value, paramTyped1.value));
/*     */   }
/*     */   
/*     */   public Type<A> getType() {
/* 199 */     return this.type;
/*     */   }
/*     */   
/*     */   public DynamicOps<?> getOps() {
/* 203 */     return this.ops;
/*     */   }
/*     */   
/*     */   public A getValue() {
/* 207 */     return this.value;
/*     */   }
/*     */   
/*     */   public DataResult<? extends Dynamic<?>> write() {
/* 211 */     return this.type.writeDynamic(this.ops, this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Typed.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */