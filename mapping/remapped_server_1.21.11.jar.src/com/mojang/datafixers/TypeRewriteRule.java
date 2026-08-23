/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.functions.PointFreeRule;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface TypeRewriteRule
/*     */ {
/*     */   static TypeRewriteRule nop() {
/*  19 */     return Nop.INSTANCE;
/*     */   }
/*     */   
/*     */   public enum Nop implements TypeRewriteRule, Supplier<TypeRewriteRule> {
/*  23 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/*  27 */       return Optional.of(RewriteResult.nop(param1Type));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypeRewriteRule get() {
/*  32 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(List<TypeRewriteRule> paramList) {
/*  37 */     return new Seq(paramList);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(TypeRewriteRule paramTypeRewriteRule1, TypeRewriteRule paramTypeRewriteRule2) {
/*  41 */     if (Objects.equals(paramTypeRewriteRule1, nop())) {
/*  42 */       return paramTypeRewriteRule2;
/*     */     }
/*  44 */     if (Objects.equals(paramTypeRewriteRule2, nop())) {
/*  45 */       return paramTypeRewriteRule1;
/*     */     }
/*  47 */     return seq((List<TypeRewriteRule>)ImmutableList.of(paramTypeRewriteRule1, paramTypeRewriteRule2));
/*     */   }
/*     */   
/*     */   static TypeRewriteRule seq(TypeRewriteRule paramTypeRewriteRule, TypeRewriteRule... paramVarArgs) {
/*  51 */     if (paramVarArgs.length == 0) {
/*  52 */       return paramTypeRewriteRule;
/*     */     }
/*  54 */     int i = paramVarArgs.length - 1;
/*  55 */     TypeRewriteRule typeRewriteRule = paramVarArgs[i];
/*  56 */     while (i > 0) {
/*  57 */       i--;
/*  58 */       typeRewriteRule = seq(paramVarArgs[i], typeRewriteRule);
/*     */     } 
/*  60 */     return seq(paramTypeRewriteRule, typeRewriteRule);
/*     */   }
/*     */   
/*     */   public static final class Seq implements TypeRewriteRule {
/*     */     protected final List<TypeRewriteRule> rules;
/*     */     private final int hashCode;
/*     */     
/*     */     public Seq(List<TypeRewriteRule> param1List) {
/*  68 */       this.rules = (List<TypeRewriteRule>)ImmutableList.copyOf(param1List);
/*  69 */       this.hashCode = this.rules.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/*  74 */       RewriteResult<A, A> rewriteResult = RewriteResult.nop(param1Type);
/*  75 */       for (TypeRewriteRule typeRewriteRule : this.rules) {
/*  76 */         Optional<RewriteResult<A, ?>> optional = cap1(typeRewriteRule, rewriteResult);
/*  77 */         if (!optional.isPresent()) {
/*  78 */           return Optional.empty();
/*     */         }
/*  80 */         rewriteResult = (RewriteResult<A, A>)optional.get();
/*     */       } 
/*  82 */       return Optional.of(rewriteResult);
/*     */     }
/*     */     
/*     */     protected <A, B> Optional<RewriteResult<A, ?>> cap1(TypeRewriteRule param1TypeRewriteRule, RewriteResult<A, B> param1RewriteResult) {
/*  86 */       return param1TypeRewriteRule.rewrite(param1RewriteResult.view().newType()).map(param1RewriteResult2 -> param1RewriteResult2.compose(param1RewriteResult1));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/*  91 */       if (param1Object == this) {
/*  92 */         return true;
/*     */       }
/*  94 */       if (!(param1Object instanceof Seq)) {
/*  95 */         return false;
/*     */       }
/*  97 */       Seq seq = (Seq)param1Object;
/*  98 */       return Objects.equals(this.rules, seq.rules);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 103 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule orElse(TypeRewriteRule paramTypeRewriteRule1, TypeRewriteRule paramTypeRewriteRule2) {
/* 108 */     return orElse(paramTypeRewriteRule1, () -> paramTypeRewriteRule);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule orElse(TypeRewriteRule paramTypeRewriteRule, Supplier<TypeRewriteRule> paramSupplier) {
/* 112 */     return new OrElse(paramTypeRewriteRule, paramSupplier);
/*     */   }
/*     */   
/*     */   public static final class OrElse implements TypeRewriteRule {
/*     */     protected final TypeRewriteRule first;
/*     */     protected final Supplier<TypeRewriteRule> second;
/*     */     private final int hashCode;
/*     */     
/*     */     public OrElse(TypeRewriteRule param1TypeRewriteRule, Supplier<TypeRewriteRule> param1Supplier) {
/* 121 */       this.first = param1TypeRewriteRule;
/* 122 */       this.second = param1Supplier;
/* 123 */       this.hashCode = Objects.hash(new Object[] { param1TypeRewriteRule, param1Supplier });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 128 */       Optional<RewriteResult<A, ?>> optional = this.first.rewrite(param1Type);
/* 129 */       if (optional.isPresent()) {
/* 130 */         return optional;
/*     */       }
/* 132 */       return ((TypeRewriteRule)this.second.get()).rewrite(param1Type);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 137 */       if (param1Object == this) {
/* 138 */         return true;
/*     */       }
/* 140 */       if (!(param1Object instanceof OrElse)) {
/* 141 */         return false;
/*     */       }
/* 143 */       OrElse orElse = (OrElse)param1Object;
/* 144 */       return (Objects.equals(this.first, orElse.first) && Objects.equals(this.second, orElse.second));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 149 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   static TypeRewriteRule all(TypeRewriteRule paramTypeRewriteRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 154 */     return new All(paramTypeRewriteRule, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule one(TypeRewriteRule paramTypeRewriteRule) {
/* 158 */     return new One(paramTypeRewriteRule);
/*     */   }
/*     */   
/*     */   static TypeRewriteRule once(TypeRewriteRule paramTypeRewriteRule) {
/* 162 */     return orElse(paramTypeRewriteRule, () -> one(once(paramTypeRewriteRule)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeRewriteRule checkOnce(TypeRewriteRule paramTypeRewriteRule, Consumer<Type<?>> paramConsumer) {
/* 168 */     return paramTypeRewriteRule;
/*     */   }
/*     */   
/*     */   static TypeRewriteRule everywhere(TypeRewriteRule paramTypeRewriteRule, PointFreeRule paramPointFreeRule, boolean paramBoolean1, boolean paramBoolean2) {
/* 172 */     return new Everywhere(paramTypeRewriteRule, paramPointFreeRule, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   
/*     */   static <B> TypeRewriteRule ifSame(Type<B> paramType, RewriteResult<B, ?> paramRewriteResult) {
/* 176 */     return new IfSame<>(paramType, paramRewriteResult);
/*     */   }
/*     */   <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> paramType);
/*     */   
/*     */   public static class All implements TypeRewriteRule { private final TypeRewriteRule rule;
/*     */     private final boolean recurse;
/*     */     private final boolean checkIndex;
/*     */     private final int hashCode;
/*     */     
/*     */     public All(TypeRewriteRule param1TypeRewriteRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 186 */       this.rule = param1TypeRewriteRule;
/* 187 */       this.recurse = param1Boolean1;
/* 188 */       this.checkIndex = param1Boolean2;
/* 189 */       this.hashCode = Objects.hash(new Object[] { param1TypeRewriteRule, Boolean.valueOf(param1Boolean1), Boolean.valueOf(param1Boolean2) });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 194 */       return Optional.of(param1Type.all(this.rule, this.recurse, this.checkIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 199 */       if (param1Object == this) {
/* 200 */         return true;
/*     */       }
/* 202 */       if (!(param1Object instanceof All)) {
/* 203 */         return false;
/*     */       }
/* 205 */       All all = (All)param1Object;
/* 206 */       return (Objects.equals(this.rule, all.rule) && this.recurse == all.recurse && this.checkIndex == all.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 211 */       return this.hashCode;
/*     */     } }
/*     */   public static final class One extends Record implements TypeRewriteRule { private final TypeRewriteRule rule;
/*     */     
/* 215 */     public One(TypeRewriteRule param1TypeRewriteRule) { this.rule = param1TypeRewriteRule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/TypeRewriteRule$One;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 215 */       //   #215	-> 0 } public TypeRewriteRule rule() { return this.rule; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/TypeRewriteRule$One;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0 }
/*     */     public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/TypeRewriteRule$One;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #215	-> 0 } public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 218 */       return param1Type.one(this.rule);
/*     */     } }
/*     */   public static final class CheckOnce extends Record implements TypeRewriteRule { private final TypeRewriteRule rule; private final Consumer<Type<?>> onFail;
/*     */     
/* 222 */     public CheckOnce(TypeRewriteRule param1TypeRewriteRule, Consumer<Type<?>> param1Consumer) { this.rule = param1TypeRewriteRule; this.onFail = param1Consumer; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/datafixers/TypeRewriteRule$CheckOnce;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #222	-> 0 } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/datafixers/TypeRewriteRule$CheckOnce;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #222	-> 0 } public final boolean equals(Object param1Object) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/datafixers/TypeRewriteRule$CheckOnce;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/* 222 */       //   #222	-> 0 } public TypeRewriteRule rule() { return this.rule; } public Consumer<Type<?>> onFail() { return this.onFail; }
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 225 */       Optional<RewriteResult<A, ?>> optional = this.rule.rewrite(param1Type);
/* 226 */       if (!optional.isPresent() || ((RewriteResult)optional.get()).view().isNop()) {
/* 227 */         this.onFail.accept(param1Type);
/*     */       }
/* 229 */       return optional;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Everywhere implements TypeRewriteRule {
/*     */     protected final TypeRewriteRule rule;
/*     */     protected final PointFreeRule optimizationRule;
/*     */     protected final boolean recurse;
/*     */     private final boolean checkIndex;
/*     */     private final int hashCode;
/*     */     
/*     */     public Everywhere(TypeRewriteRule param1TypeRewriteRule, PointFreeRule param1PointFreeRule, boolean param1Boolean1, boolean param1Boolean2) {
/* 241 */       this.rule = param1TypeRewriteRule;
/* 242 */       this.optimizationRule = param1PointFreeRule;
/* 243 */       this.recurse = param1Boolean1;
/* 244 */       this.checkIndex = param1Boolean2;
/* 245 */       this.hashCode = Objects.hash(new Object[] { param1TypeRewriteRule, param1PointFreeRule, Boolean.valueOf(param1Boolean1), Boolean.valueOf(param1Boolean2) });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 250 */       return param1Type.everywhere(this.rule, this.optimizationRule, this.recurse, this.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 255 */       if (param1Object == this) {
/* 256 */         return true;
/*     */       }
/* 258 */       if (!(param1Object instanceof Everywhere)) {
/* 259 */         return false;
/*     */       }
/* 261 */       Everywhere everywhere = (Everywhere)param1Object;
/* 262 */       return (Objects.equals(this.rule, everywhere.rule) && Objects.equals(this.optimizationRule, everywhere.optimizationRule) && this.recurse == everywhere.recurse && this.checkIndex == everywhere.checkIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 267 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IfSame<B> implements TypeRewriteRule {
/*     */     private final Type<B> targetType;
/*     */     private final RewriteResult<B, ?> value;
/*     */     private final int hashCode;
/*     */     
/*     */     public IfSame(Type<B> param1Type, RewriteResult<B, ?> param1RewriteResult) {
/* 277 */       this.targetType = param1Type;
/* 278 */       this.value = param1RewriteResult;
/* 279 */       this.hashCode = Objects.hash(new Object[] { param1Type, param1RewriteResult });
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> Optional<RewriteResult<A, ?>> rewrite(Type<A> param1Type) {
/* 284 */       return param1Type.ifSame(this.targetType, this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object param1Object) {
/* 289 */       if (param1Object == this) {
/* 290 */         return true;
/*     */       }
/* 292 */       if (!(param1Object instanceof IfSame)) {
/* 293 */         return false;
/*     */       }
/* 295 */       IfSame ifSame = (IfSame)param1Object;
/* 296 */       return (Objects.equals(this.targetType, ifSame.targetType) && Objects.equals(this.value, ifSame.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 301 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\TypeRewriteRule.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */