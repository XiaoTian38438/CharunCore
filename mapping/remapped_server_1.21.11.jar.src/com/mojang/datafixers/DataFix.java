/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.BitSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.Nullable;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DataFix
/*     */ {
/*  21 */   private static final Logger LOGGER = LoggerFactory.getLogger(DataFix.class);
/*     */   
/*     */   private final Schema outputSchema;
/*     */   private final boolean changesType;
/*     */   @Nullable
/*     */   private TypeRewriteRule rule;
/*     */   
/*     */   public DataFix(Schema paramSchema, boolean paramBoolean) {
/*  29 */     this.outputSchema = paramSchema;
/*  30 */     this.changesType = paramBoolean;
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhere(String paramString, Type<A> paramType, Function<DynamicOps<?>, Function<A, A>> paramFunction) {
/*  34 */     return fixTypeEverywhere(paramString, paramType, paramType, paramFunction, new BitSet());
/*     */   }
/*     */ 
/*     */   
/*     */   protected <A, B> TypeRewriteRule convertUnchecked(String paramString, Type<A> paramType, Type<B> paramType1) {
/*  39 */     return fixTypeEverywhere(paramString, paramType, paramType1, paramDynamicOps -> Function.identity(), new BitSet());
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule writeAndRead(String paramString, Type<?> paramType1, Type<?> paramType2) {
/*  43 */     return writeFixAndRead(paramString, paramType1, paramType2, Function.identity());
/*     */   }
/*     */ 
/*     */   
/*     */   protected <A, B> TypeRewriteRule writeFixAndRead(String paramString, Type<A> paramType, Type<B> paramType1, Function<Dynamic<?>, Dynamic<?>> paramFunction) {
/*  48 */     AtomicReference atomicReference = new AtomicReference();
/*  49 */     RewriteResult<A, B> rewriteResult = unchecked(paramString, paramType, paramType1, paramDynamicOps -> (), new BitSet());
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
/*  61 */     TypeRewriteRule typeRewriteRule = fixTypeEverywhere(paramType, rewriteResult);
/*     */     
/*  63 */     atomicReference.setPlain(paramType.all(typeRewriteRule, true, false).view().newType());
/*  64 */     return typeRewriteRule;
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(String paramString, Type<A> paramType, Type<B> paramType1, Function<DynamicOps<?>, Function<A, B>> paramFunction) {
/*  68 */     return fixTypeEverywhere(paramString, paramType, paramType1, paramFunction, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(String paramString, Type<A> paramType, Type<B> paramType1, Function<DynamicOps<?>, Function<A, B>> paramFunction, BitSet paramBitSet) {
/*  72 */     return fixTypeEverywhere(paramType, unchecked(paramString, paramType, paramType1, paramFunction, paramBitSet));
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhereTyped(String paramString, Type<A> paramType, Function<Typed<?>, Typed<?>> paramFunction) {
/*  76 */     return fixTypeEverywhereTyped(paramString, paramType, paramFunction, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A> TypeRewriteRule fixTypeEverywhereTyped(String paramString, Type<A> paramType, Function<Typed<?>, Typed<?>> paramFunction, BitSet paramBitSet) {
/*  80 */     return fixTypeEverywhereTyped(paramString, paramType, paramType, paramFunction, paramBitSet);
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String paramString, Type<A> paramType, Type<B> paramType1, Function<Typed<?>, Typed<?>> paramFunction) {
/*  84 */     return fixTypeEverywhereTyped(paramString, paramType, paramType1, paramFunction, new BitSet());
/*     */   }
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(String paramString, Type<A> paramType, Type<B> paramType1, Function<Typed<?>, Typed<?>> paramFunction, BitSet paramBitSet) {
/*  88 */     return fixTypeEverywhere(paramType, checked(paramString, paramType, paramType1, paramFunction, paramBitSet));
/*     */   }
/*     */   
/*     */   private static <A, B> RewriteResult<A, B> unchecked(String paramString, Type<A> paramType, Type<B> paramType1, Function<DynamicOps<?>, Function<A, B>> paramFunction, BitSet paramBitSet) {
/*  92 */     return RewriteResult.create(View.create(paramString, paramType, paramType1, new NamedFunctionWrapper<>(paramString, paramFunction)), paramBitSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A, B> RewriteResult<A, B> checked(String paramString, Type<A> paramType, Type<B> paramType1, Function<Typed<?>, Typed<?>> paramFunction, BitSet paramBitSet) {
/*  97 */     return RewriteResult.create(View.create(paramString, paramType, paramType1, new NamedFunctionWrapper<>(paramString, paramDynamicOps -> ())), paramBitSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A, B> TypeRewriteRule fixTypeEverywhere(Type<A> paramType, RewriteResult<A, B> paramRewriteResult) {
/* 107 */     return TypeRewriteRule.checkOnce(TypeRewriteRule.everywhere(TypeRewriteRule.ifSame(paramType, paramRewriteResult), DataFixerUpper.OPTIMIZATION_RULE, true, true), this::onFail);
/*     */   }
/*     */   
/*     */   protected void onFail(Type<?> paramType) {
/* 111 */     LOGGER.info("Not matched: " + String.valueOf(this) + " " + String.valueOf(paramType));
/*     */   }
/*     */   
/*     */   public final int getVersionKey() {
/* 115 */     return getOutputSchema().getVersionKey();
/*     */   }
/*     */   
/*     */   public TypeRewriteRule getRule() {
/* 119 */     if (this.rule == null) {
/* 120 */       this.rule = makeRule();
/*     */     }
/* 122 */     return this.rule;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Schema getInputSchema() {
/* 128 */     if (this.changesType) {
/* 129 */       return this.outputSchema.getParent();
/*     */     }
/* 131 */     return getOutputSchema();
/*     */   }
/*     */   
/*     */   protected Schema getOutputSchema() {
/* 135 */     return this.outputSchema;
/*     */   }
/*     */   
/*     */   protected abstract TypeRewriteRule makeRule();
/*     */   
/*     */   private static final class NamedFunctionWrapper<A, B> implements Function<DynamicOps<?>, Function<A, B>> { private final String name;
/*     */     
/*     */     public NamedFunctionWrapper(String param1String, Function<DynamicOps<?>, Function<A, B>> param1Function) {
/* 143 */       this.name = param1String;
/* 144 */       this.delegate = param1Function;
/*     */     }
/*     */     private final Function<DynamicOps<?>, Function<A, B>> delegate;
/*     */     
/*     */     public Function<A, B> apply(DynamicOps<?> param1DynamicOps) {
/* 149 */       return this.delegate.apply(param1DynamicOps);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 154 */       if (this == param1Object) {
/* 155 */         return true;
/*     */       }
/* 157 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/* 158 */         return false;
/*     */       }
/* 160 */       NamedFunctionWrapper namedFunctionWrapper = (NamedFunctionWrapper)param1Object;
/* 161 */       return Objects.equals(this.name, namedFunctionWrapper.name);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 166 */       return this.name.hashCode();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFix.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */