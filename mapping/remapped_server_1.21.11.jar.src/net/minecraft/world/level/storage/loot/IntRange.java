/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class IntRange {
/*     */   static {
/*  21 */     RECORD_CODEC = RecordCodecBuilder.create(paramInstance -> paramInstance.group((App)NumberProviders.CODEC.optionalFieldOf("min").forGetter(()), (App)NumberProviders.CODEC.optionalFieldOf("max").forGetter(())).apply((Applicative)paramInstance, IntRange::new));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  26 */     CODEC = Codec.either((Codec)Codec.INT, RECORD_CODEC).xmap(paramEither -> (IntRange)paramEither.map(IntRange::exact, Function.identity()), paramIntRange -> {
/*     */           OptionalInt optionalInt = paramIntRange.unpackExact();
/*     */           return optionalInt.isPresent() ? Either.left(Integer.valueOf(optionalInt.getAsInt())) : Either.right(paramIntRange);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Codec<IntRange> RECORD_CODEC;
/*     */ 
/*     */   
/*     */   public static final Codec<IntRange> CODEC;
/*     */ 
/*     */   
/*     */   private final NumberProvider min;
/*     */ 
/*     */   
/*     */   private final NumberProvider max;
/*     */ 
/*     */   
/*     */   private final IntLimiter limiter;
/*     */ 
/*     */   
/*     */   private final IntChecker predicate;
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  55 */     ImmutableSet.Builder builder = ImmutableSet.builder();
/*  56 */     if (this.min != null) {
/*  57 */       builder.addAll(this.min.getReferencedContextParams());
/*     */     }
/*  59 */     if (this.max != null) {
/*  60 */       builder.addAll(this.max.getReferencedContextParams());
/*     */     }
/*  62 */     return (Set<ContextKey<?>>)builder.build();
/*     */   }
/*     */   
/*     */   private IntRange(Optional<NumberProvider> paramOptional1, Optional<NumberProvider> paramOptional2) {
/*  66 */     this(paramOptional1.orElse(null), paramOptional2.orElse(null));
/*     */   }
/*     */   
/*     */   private IntRange(NumberProvider paramNumberProvider1, NumberProvider paramNumberProvider2) {
/*  70 */     this.min = paramNumberProvider1;
/*  71 */     this.max = paramNumberProvider2;
/*     */     
/*  73 */     if (paramNumberProvider1 == null) {
/*  74 */       if (paramNumberProvider2 == null) {
/*  75 */         this.limiter = ((paramLootContext, paramInt) -> paramInt);
/*  76 */         this.predicate = ((paramLootContext, paramInt) -> true);
/*     */       } else {
/*  78 */         this.limiter = ((paramLootContext, paramInt) -> Math.min(paramNumberProvider.getInt(paramLootContext), paramInt));
/*  79 */         this.predicate = ((paramLootContext, paramInt) -> (paramInt <= paramNumberProvider.getInt(paramLootContext)));
/*     */       }
/*     */     
/*  82 */     } else if (paramNumberProvider2 == null) {
/*  83 */       this.limiter = ((paramLootContext, paramInt) -> Math.max(paramNumberProvider.getInt(paramLootContext), paramInt));
/*  84 */       this.predicate = ((paramLootContext, paramInt) -> (paramInt >= paramNumberProvider.getInt(paramLootContext)));
/*     */     } else {
/*  86 */       this.limiter = ((paramLootContext, paramInt) -> Mth.clamp(paramInt, paramNumberProvider1.getInt(paramLootContext), paramNumberProvider2.getInt(paramLootContext)));
/*  87 */       this.predicate = ((paramLootContext, paramInt) -> (paramInt >= paramNumberProvider1.getInt(paramLootContext) && paramInt <= paramNumberProvider2.getInt(paramLootContext)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static IntRange exact(int paramInt) {
/*  93 */     ConstantValue constantValue = ConstantValue.exactly(paramInt);
/*  94 */     return new IntRange((Optional)Optional.of(constantValue), (Optional)Optional.of(constantValue));
/*     */   }
/*     */   
/*     */   public static IntRange range(int paramInt1, int paramInt2) {
/*  98 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(paramInt1)), (Optional)Optional.of(ConstantValue.exactly(paramInt2)));
/*     */   }
/*     */   
/*     */   public static IntRange lowerBound(int paramInt) {
/* 102 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(paramInt)), Optional.empty());
/*     */   }
/*     */   
/*     */   public static IntRange upperBound(int paramInt) {
/* 106 */     return new IntRange(Optional.empty(), (Optional)Optional.of(ConstantValue.exactly(paramInt)));
/*     */   }
/*     */   
/*     */   public int clamp(LootContext paramLootContext, int paramInt) {
/* 110 */     return this.limiter.apply(paramLootContext, paramInt);
/*     */   }
/*     */   
/*     */   public boolean test(LootContext paramLootContext, int paramInt) {
/* 114 */     return this.predicate.test(paramLootContext, paramInt);
/*     */   }
/*     */   
/*     */   private OptionalInt unpackExact() {
/* 118 */     if (Objects.equals(this.min, this.max)) { NumberProvider numberProvider = this.min; if (numberProvider instanceof ConstantValue) { ConstantValue constantValue = (ConstantValue)numberProvider;
/* 119 */         if (Math.floor(constantValue.value()) == constantValue.value())
/* 120 */           return OptionalInt.of((int)constantValue.value());  }
/*     */        }
/*     */     
/* 123 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntLimiter {
/*     */     int apply(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntChecker {
/*     */     boolean test(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\storage\loot\IntRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */