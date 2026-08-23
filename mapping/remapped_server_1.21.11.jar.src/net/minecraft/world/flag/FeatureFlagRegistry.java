/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FeatureFlagRegistry
/*     */ {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final FeatureFlagUniverse universe;
/*     */   private final Map<Identifier, FeatureFlag> names;
/*     */   private final FeatureFlagSet allFlags;
/*     */   
/*     */   FeatureFlagRegistry(FeatureFlagUniverse paramFeatureFlagUniverse, FeatureFlagSet paramFeatureFlagSet, Map<Identifier, FeatureFlag> paramMap) {
/*  27 */     this.universe = paramFeatureFlagUniverse;
/*  28 */     this.names = paramMap;
/*  29 */     this.allFlags = paramFeatureFlagSet;
/*     */   }
/*     */   
/*     */   public boolean isSubset(FeatureFlagSet paramFeatureFlagSet) {
/*  33 */     return paramFeatureFlagSet.isSubsetOf(this.allFlags);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet allFlags() {
/*  37 */     return this.allFlags;
/*     */   }
/*     */   
/*     */   public FeatureFlagSet fromNames(Iterable<Identifier> paramIterable) {
/*  41 */     return fromNames(paramIterable, paramIdentifier -> LOGGER.warn("Unknown feature flag: {}", paramIdentifier));
/*     */   }
/*     */   
/*     */   public FeatureFlagSet subset(FeatureFlag... paramVarArgs) {
/*  45 */     return FeatureFlagSet.create(this.universe, Arrays.asList(paramVarArgs));
/*     */   }
/*     */   
/*     */   public FeatureFlagSet fromNames(Iterable<Identifier> paramIterable, Consumer<Identifier> paramConsumer) {
/*  49 */     Set<FeatureFlag> set = Sets.newIdentityHashSet();
/*  50 */     for (Identifier identifier : paramIterable) {
/*  51 */       FeatureFlag featureFlag = this.names.get(identifier);
/*  52 */       if (featureFlag == null) {
/*  53 */         paramConsumer.accept(identifier); continue;
/*     */       } 
/*  55 */       set.add(featureFlag);
/*     */     } 
/*     */     
/*  58 */     return FeatureFlagSet.create(this.universe, set);
/*     */   }
/*     */   
/*     */   public Set<Identifier> toNames(FeatureFlagSet paramFeatureFlagSet) {
/*  62 */     HashSet<Identifier> hashSet = new HashSet();
/*     */     
/*  64 */     this.names.forEach((paramIdentifier, paramFeatureFlag) -> {
/*     */           if (paramFeatureFlagSet.contains(paramFeatureFlag)) {
/*     */             paramSet.add(paramIdentifier);
/*     */           }
/*     */         });
/*  69 */     return hashSet;
/*     */   }
/*     */   
/*     */   public Codec<FeatureFlagSet> codec() {
/*  73 */     return Identifier.CODEC.listOf().comapFlatMap(paramList -> {
/*     */           HashSet hashSet = new HashSet();
/*     */           Objects.requireNonNull(hashSet);
/*     */           FeatureFlagSet featureFlagSet = fromNames(paramList, hashSet::add);
/*     */           return !hashSet.isEmpty() ? DataResult.error((), featureFlagSet) : DataResult.success(featureFlagSet);
/*     */         }paramFeatureFlagSet -> List.copyOf(toNames(paramFeatureFlagSet)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final FeatureFlagUniverse universe;
/*     */     
/*     */     private int id;
/*     */     
/*  88 */     private final Map<Identifier, FeatureFlag> flags = new LinkedHashMap<>();
/*     */     
/*     */     public Builder(String param1String) {
/*  91 */       this.universe = new FeatureFlagUniverse(param1String);
/*     */     }
/*     */     
/*     */     public FeatureFlag createVanilla(String param1String) {
/*  95 */       return create(Identifier.withDefaultNamespace(param1String));
/*     */     }
/*     */     
/*     */     public FeatureFlag create(Identifier param1Identifier) {
/*  99 */       if (this.id >= 64)
/*     */       {
/* 101 */         throw new IllegalStateException("Too many feature flags");
/*     */       }
/* 103 */       FeatureFlag featureFlag1 = new FeatureFlag(this.universe, this.id++);
/* 104 */       FeatureFlag featureFlag2 = this.flags.put(param1Identifier, featureFlag1);
/* 105 */       if (featureFlag2 != null) {
/* 106 */         throw new IllegalStateException("Duplicate feature flag " + String.valueOf(param1Identifier));
/*     */       }
/* 108 */       return featureFlag1;
/*     */     }
/*     */     
/*     */     public FeatureFlagRegistry build() {
/* 112 */       FeatureFlagSet featureFlagSet = FeatureFlagSet.create(this.universe, this.flags.values());
/* 113 */       return new FeatureFlagRegistry(this.universe, featureFlagSet, Map.copyOf(this.flags));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\flag\FeatureFlagRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */